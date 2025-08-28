package net.strafepvp.hcf.adapters.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.strafepvp.hcf.StrafeHCFPlugin;
import net.strafepvp.hcf.application.config.ConfigManager.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * Gestor de base de datos para StrafeHCF
 * 
 * Maneja la conexión y configuración de la base de datos utilizando
 * HikariCP para pooling de conexiones.
 * 
 * @author mayk0l
 */
public class DatabaseManager {
    
    private final StrafeHCFPlugin plugin;
    private final DatabaseConfig config;
    
    private HikariDataSource dataSource;
    private boolean initialized = false;
    
    public DatabaseManager(StrafeHCFPlugin plugin, DatabaseConfig config) {
        this.plugin = plugin;
        this.config = config;
    }
    
    /**
     * Inicializa la conexión a la base de datos
     */
    public void initialize() {
        plugin.getLogger().info("Inicializando conexión a la base de datos...");
        
        try {
            // Create HikariCP configuration
            HikariConfig hikariConfig = createHikariConfig();
            
            // Create data source
            this.dataSource = new HikariDataSource(hikariConfig);
            
            // Test connection
            testConnection();
            
            // Initialize database schema
            initializeSchema();
            
            this.initialized = true;
            
            plugin.getLogger().info(String.format(
                "Base de datos inicializada correctamente (Tipo: %s, Pool: %d/%d)",
                config.type(),
                config.minimumIdle(),
                config.maximumPoolSize()
            ));
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error al inicializar la base de datos", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    /**
     * Crea la configuración de HikariCP
     */
    private HikariConfig createHikariConfig() {
        HikariConfig config = new HikariConfig();
        
        // Database connection settings
        String jdbcUrl = buildJdbcUrl();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(this.config.username());
        config.setPassword(this.config.password());
        
        // Set driver class name based on database type
        String driverClassName = getDriverClassName();
        if (driverClassName != null) {
            config.setDriverClassName(driverClassName);
        }
        
        // Connection pool settings
        config.setMaximumPoolSize(this.config.maximumPoolSize());
        config.setMinimumIdle(this.config.minimumIdle());
        config.setConnectionTimeout(this.config.connectionTimeout());
        config.setIdleTimeout(this.config.idleTimeout());
        config.setMaxLifetime(this.config.maxLifetime());
        
        // Performance settings
        config.setLeakDetectionThreshold(60000);
        config.setConnectionTestQuery("SELECT 1");
        
        // Pool name for monitoring
        config.setPoolName("StrafeHCF-Pool");
        
        return config;
    }
    
    /**
     * Obtiene el nombre de clase del driver según el tipo de base de datos
     */
    private String getDriverClassName() {
        return switch (config.type().toUpperCase()) {
            case "POSTGRESQL" -> "org.postgresql.Driver";
            case "MYSQL" -> "com.mysql.cj.jdbc.Driver";
            case "H2" -> "net.strafepvp.hcf.lib.h2.Driver"; // Driver H2 relocacionado
            default -> null;
        };
    }
    
    /**
     * Construye la URL JDBC basada en la configuración
     */
    private String buildJdbcUrl() {
        return switch (config.type().toUpperCase()) {
            case "POSTGRESQL" -> String.format(
                "jdbc:postgresql://%s:%d/%s",
                config.host(),
                config.port(),
                config.name()
            );
            case "MYSQL" -> String.format(
                "jdbc:mysql://%s:%d/%s",
                config.host(),
                config.port(),
                config.name()
            );
            case "H2" -> String.format(
                "jdbc:h2:%s/data/%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                plugin.getDataFolder().getAbsolutePath(),
                config.name()
            );
            default -> throw new IllegalArgumentException("Unsupported database type: " + config.type());
        };
    }
    
    /**
     * Prueba la conexión a la base de datos
     */
    private void testConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                plugin.getLogger().info("Conexión a la base de datos establecida correctamente");
            } else {
                throw new SQLException("Database connection is not valid");
            }
        }
    }
    
    /**
     * Inicializa el esquema de la base de datos
     */
    private void initializeSchema() {
        plugin.getLogger().info("Inicializando esquema de base de datos...");
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            // Create teams table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS teams (
                    id VARCHAR(36) PRIMARY KEY,
                    name VARCHAR(16) NOT NULL UNIQUE,
                    leader_id VARCHAR(36) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    dtr DECIMAL(4,2) NOT NULL DEFAULT 1.01,
                    is_raidable BOOLEAN DEFAULT FALSE,
                    last_death_time TIMESTAMP NULL
                )
                """);
            
            // Create players table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS players (
                    id VARCHAR(36) PRIMARY KEY,
                    minecraft_uuid VARCHAR(36) NOT NULL UNIQUE,
                    username VARCHAR(16) NOT NULL,
                    team_id VARCHAR(36) NULL,
                    team_role VARCHAR(20) DEFAULT 'MEMBER',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    pvp_protection_until TIMESTAMP NULL,
                    deathban_until TIMESTAMP NULL,
                    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL
                )
                """);
            
            // Create claims table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS claims (
                    id VARCHAR(36) PRIMARY KEY,
                    team_id VARCHAR(36) NOT NULL,
                    world_name VARCHAR(50) NOT NULL,
                    min_x INT NOT NULL,
                    min_z INT NOT NULL,
                    max_x INT NOT NULL,
                    max_z INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
                )
                """);
            
            // Create koth_events table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS koth_events (
                    id VARCHAR(36) PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    world_name VARCHAR(50) NOT NULL,
                    center_x INT NOT NULL,
                    center_z INT NOT NULL,
                    radius INT NOT NULL,
                    capture_time INT NOT NULL,
                    is_active BOOLEAN DEFAULT FALSE,
                    current_controller VARCHAR(36) NULL,
                    control_start_time TIMESTAMP NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """);
            
            plugin.getLogger().info("Esquema de base de datos inicializado correctamente");
            
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Error al inicializar esquema de base de datos", e);
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
    
    /**
     * Obtiene una conexión de la base de datos
     * 
     * @return Conexión a la base de datos
     * @throws SQLException Si hay error al obtener la conexión
     */
    public Connection getConnection() throws SQLException {
        if (!initialized || dataSource == null) {
            throw new IllegalStateException("Database manager not initialized");
        }
        
        return dataSource.getConnection();
    }
    
    /**
     * Obtiene el data source
     * 
     * @return DataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * Verifica si la base de datos está saludable
     * 
     * @return true si la conexión es válida
     */
    public boolean isHealthy() {
        if (!initialized || dataSource == null) {
            return false;
        }
        
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Database health check failed", e);
            return false;
        }
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void shutdown() {
        plugin.getLogger().info("Cerrando conexiones de base de datos...");
        
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().info("Base de datos cerrada correctamente");
        }
        
        this.initialized = false;
    }
    
    /**
     * Obtiene información sobre el estado del pool de conexiones
     * 
     * @return Información del pool
     */
    public String getPoolStatus() {
        if (dataSource == null) {
            return "DataSource not initialized";
        }
        
        return String.format(
            "Pool Status - Active: %d, Idle: %d, Waiting: %d, Total: %d",
            dataSource.getHikariPoolMXBean().getActiveConnections(),
            dataSource.getHikariPoolMXBean().getIdleConnections(),
            dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection(),
            dataSource.getHikariPoolMXBean().getTotalConnections()
        );
    }
    
    /**
     * Obtiene estadísticas del pool de conexiones
     * 
     * @return Estadísticas del pool
     */
    public String getPoolStats() {
        return getPoolStatus();
    }
}
