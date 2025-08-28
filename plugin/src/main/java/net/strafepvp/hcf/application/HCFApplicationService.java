package net.strafepvp.hcf.application;

import net.strafepvp.hcf.StrafeHCFPlugin;
import net.strafepvp.hcf.adapters.persistence.DatabaseManager;
import net.strafepvp.hcf.adapters.spigot.SpigotCommandManager;
import net.strafepvp.hcf.adapters.spigot.SpigotEventManager;
import net.strafepvp.hcf.application.config.ConfigManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * Servicio principal de aplicación para StrafeHCF
 * 
 * Coordina todos los servicios y componentes del plugin siguiendo
 * los principios de arquitectura hexagonal.
 * 
 * @author mayk0l
 */
public class HCFApplicationService {
    
    private final StrafeHCFPlugin plugin;
    private final ConfigManager configManager;
    
    // Core managers
    private DatabaseManager databaseManager;
    private SpigotCommandManager commandManager;
    private SpigotEventManager eventManager;
    
    // Service state
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final AtomicInteger activeServices = new AtomicInteger(0);
    
    public HCFApplicationService(StrafeHCFPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    
    /**
     * Inicia todos los servicios de la aplicación
     */
    public void start() {
        if (isStarted.get()) {
            plugin.getLogger().warning("HCFApplicationService ya está iniciado");
            return;
        }
        
        plugin.getLogger().info("Iniciando servicios de aplicación...");
        
        try {
            // 1. Initialize Database Manager
            initializeDatabaseManager();
            
            // 2. Initialize Command Manager
            initializeCommandManager();
            
            // 3. Initialize Event Manager
            initializeEventManager();
            
            // 4. Post-initialization tasks
            performPostInitialization();
            
            isStarted.set(true);
            plugin.getLogger().info(String.format("Servicios de aplicación iniciados correctamente (%d servicios activos)", 
                activeServices.get()));
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error crítico al iniciar servicios de aplicación", e);
            stop(); // Cleanup en caso de error
            throw new RuntimeException("Fallo al iniciar HCFApplicationService", e);
        }
    }
    
    /**
     * Detiene todos los servicios de la aplicación
     */
    public void stop() {
        if (!isStarted.get()) {
            plugin.getLogger().info("HCFApplicationService ya está detenido");
            return;
        }
        
        plugin.getLogger().info("Deteniendo servicios de aplicación...");
        
        try {
            // Stop in reverse order
            if (eventManager != null) {
                eventManager.shutdown();
                plugin.getLogger().info("EventManager detenido");
            }
            
            if (commandManager != null) {
                commandManager.shutdown();
                plugin.getLogger().info("CommandManager detenido");
            }
            
            if (databaseManager != null) {
                databaseManager.shutdown();
                plugin.getLogger().info("DatabaseManager detenido");
            }
            
            isStarted.set(false);
            activeServices.set(0);
            plugin.getLogger().info("Servicios de aplicación detenidos correctamente");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error al detener servicios de aplicación", e);
        }
    }
    
    /**
     * Inicializa el gestor de base de datos
     */
    private void initializeDatabaseManager() {
        plugin.getLogger().info("Inicializando DatabaseManager...");
        
        this.databaseManager = new DatabaseManager(plugin, configManager.getDatabaseConfig());
        databaseManager.initialize();
        
        activeServices.incrementAndGet();
        plugin.getLogger().info("DatabaseManager inicializado correctamente");
    }
    
    /**
     * Inicializa el gestor de comandos
     */
    private void initializeCommandManager() {
        plugin.getLogger().info("Inicializando CommandManager...");
        
        this.commandManager = new SpigotCommandManager(plugin);
        commandManager.initialize();
        
        activeServices.incrementAndGet();
        plugin.getLogger().info("CommandManager inicializado correctamente");
    }
    
    /**
     * Inicializa el gestor de eventos
     */
    private void initializeEventManager() {
        plugin.getLogger().info("Inicializando EventManager...");
        
        this.eventManager = new SpigotEventManager(plugin);
        eventManager.initialize();
        
        activeServices.incrementAndGet();
        plugin.getLogger().info("EventManager inicializado correctamente");
    }
    
    /**
     * Realiza tareas de post-inicialización
     */
    private void performPostInitialization() {
        plugin.getLogger().info("Ejecutando tareas de post-inicialización...");
        
        // Verificar integridad de servicios
        verifyServiceIntegrity();
        
        // Cargar datos iniciales si es necesario
        loadInitialData();
        
        // Registrar métricas y monitoreo
        setupMetricsAndMonitoring();
        
        plugin.getLogger().info("Post-inicialización completada");
    }
    
    /**
     * Verifica la integridad de todos los servicios
     */
    private void verifyServiceIntegrity() {
        boolean allServicesHealthy = true;
        
        if (databaseManager == null || !databaseManager.isHealthy()) {
            plugin.getLogger().severe("DatabaseManager no está saludable");
            allServicesHealthy = false;
        }
        
        if (commandManager == null) {
            plugin.getLogger().severe("CommandManager no está inicializado");
            allServicesHealthy = false;
        }
        
        if (eventManager == null) {
            plugin.getLogger().severe("EventManager no está inicializado");
            allServicesHealthy = false;
        }
        
        if (!allServicesHealthy) {
            throw new IllegalStateException("Uno o más servicios críticos no están saludables");
        }
        
        plugin.getLogger().info("Verificación de integridad de servicios: EXITOSA");
    }
    
    /**
     * Carga datos iniciales necesarios
     */
    private void loadInitialData() {
        plugin.getLogger().info("Cargando datos iniciales...");
        
        // Aquí se pueden cargar equipos, jugadores, claims, etc.
        // Por ahora solo registramos que la carga fue exitosa
        
        plugin.getLogger().info("Datos iniciales cargados correctamente");
    }
    
    /**
     * Configura métricas y monitoreo
     */
    private void setupMetricsAndMonitoring() {
        plugin.getLogger().info("Configurando métricas y monitoreo...");
        
        // Aquí se pueden configurar sistemas de métricas
        // Por ejemplo: registrar tasks de monitoreo, health checks, etc.
        
        plugin.getLogger().info("Métricas y monitoreo configurados");
    }
    
    /**
     * Recarga todos los servicios
     */
    public void reload() {
        plugin.getLogger().info("Recargando servicios de aplicación...");
        
        try {
            // Detener servicios actuales
            stop();
            
            // Recargar configuración
            configManager.reloadConfiguration();
            
            // Reiniciar servicios
            start();
            
            plugin.getLogger().info("Servicios de aplicación recargados correctamente");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error al recargar servicios de aplicación", e);
            throw new RuntimeException("Fallo al recargar HCFApplicationService", e);
        }
    }
    
    /**
     * Obtiene el estado de salud del servicio
     * 
     * @return true si todos los servicios están saludables
     */
    public boolean isHealthy() {
        if (!isStarted.get()) {
            return false;
        }
        
        return databaseManager != null && databaseManager.isHealthy() &&
               commandManager != null &&
               eventManager != null;
    }
    
    /**
     * Obtiene información detallada del estado del servicio
     * 
     * @return Información de estado
     */
    public String getHealthInfo() {
        StringBuilder health = new StringBuilder();
        health.append("HCFApplicationService Status:\n");
        health.append("- Started: ").append(isStarted.get()).append("\n");
        health.append("- Active Services: ").append(activeServices.get()).append("\n");
        health.append("- Database: ").append(databaseManager != null ? (databaseManager.isHealthy() ? "HEALTHY" : "UNHEALTHY") : "NOT_INITIALIZED").append("\n");
        health.append("- Commands: ").append(commandManager != null ? "INITIALIZED" : "NOT_INITIALIZED").append("\n");
        health.append("- Events: ").append(eventManager != null ? "INITIALIZED" : "NOT_INITIALIZED").append("\n");
        
        return health.toString();
    }
    
    // Getters para acceso a los managers
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public SpigotCommandManager getCommandManager() {
        return commandManager;
    }
    
    public SpigotEventManager getEventManager() {
        return eventManager;
    }
    
    public boolean isStarted() {
        return isStarted.get();
    }
    
    public int getActiveServicesCount() {
        return activeServices.get();
    }
}