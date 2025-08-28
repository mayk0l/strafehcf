package net.strafepvp.hcf.application.config;

import net.strafepvp.hcf.StrafeHCFPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * Gestor centralizado de configuración para StrafeHCF
 * 
 * Maneja la carga, validación y acceso a la configuración del plugin.
 * Proporciona una interfaz tipada para acceder a los valores de configuración.
 * 
 * @author mayk0l
 */
public class ConfigManager {
    
    private final StrafeHCFPlugin plugin;
    private FileConfiguration config;
    
    // Database Configuration
    private DatabaseConfig databaseConfig;
    private TeamConfig teamConfig;
    private CombatConfig combatConfig;
    private DeathbanConfig deathbanConfig;
    private ClaimsConfig claimsConfig;
    private KothConfig kothConfig;
    private ClassesConfig classesConfig;
    
    public ConfigManager(StrafeHCFPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Carga la configuración desde el archivo config.yml
     */
    public void loadConfiguration() {
        plugin.getLogger().info("Cargando configuración desde config.yml");
        
        this.config = plugin.getConfig();
        
        // Load individual config sections
        loadDatabaseConfig();
        loadTeamConfig();
        loadCombatConfig();
        loadDeathbanConfig();
        loadClaimsConfig();
        loadKothConfig();
        loadClassesConfig();
        
        // Validate configuration
        validateConfiguration();
        
        plugin.getLogger().info("Configuración cargada y validada correctamente");
    }
    
    /**
     * Recarga la configuración
     */
    public void reloadConfiguration() {
        plugin.reloadConfig();
        loadConfiguration();
    }
    
    /**
     * Carga la configuración de base de datos
     */
    private void loadDatabaseConfig() {
        ConfigurationSection dbSection = config.getConfigurationSection("database");
        if (dbSection == null) {
            plugin.getLogger().warning("Sección 'database' no encontrada en config.yml, usando valores por defecto");
            return;
        }
        
        this.databaseConfig = new DatabaseConfig(
            dbSection.getString("type", "H2"),
            dbSection.getString("host", "localhost"),
            dbSection.getInt("port", 5432),
            dbSection.getString("name", "strafehcf"),
            dbSection.getString("username", "strafehcf"),
            dbSection.getString("password", "changeme"),
            dbSection.getInt("pool.maximum_pool_size", 10),
            dbSection.getInt("pool.minimum_idle", 5),
            dbSection.getLong("pool.connection_timeout", 30000),
            dbSection.getLong("pool.idle_timeout", 600000),
            dbSection.getLong("pool.max_lifetime", 1800000)
        );
    }
    
    /**
     * Carga la configuración de equipos
     */
    private void loadTeamConfig() {
        ConfigurationSection teamSection = config.getConfigurationSection("teams");
        if (teamSection == null) {
            plugin.getLogger().warning("Sección 'teams' no encontrada en config.yml");
            return;
        }
        
        this.teamConfig = new TeamConfig(
            teamSection.getInt("max_members", 15),
            teamSection.getInt("max_claims", 1),
            teamSection.getInt("min_members_to_claim", 3),
            teamSection.getBoolean("leader_only_disband", true),
            teamSection.getInt("ally_limit", 3),
            teamSection.getDouble("dtr.starting_dtr", 1.01),
            teamSection.getDouble("dtr.max_dtr", 7.2),
            teamSection.getDouble("dtr.loss_per_death", 1.0),
            teamSection.getDouble("dtr.regen_rate", 0.1),
            teamSection.getInt("dtr.regen_interval", 30),
            teamSection.getInt("dtr.freeze_time", 45)
        );
    }
    
    /**
     * Carga la configuración de combate
     */
    private void loadCombatConfig() {
        ConfigurationSection combatSection = config.getConfigurationSection("combat");
        if (combatSection == null) {
            plugin.getLogger().warning("Sección 'combat' no encontrada en config.yml");
            return;
        }
        
        this.combatConfig = new CombatConfig(
            combatSection.getInt("tag_duration", 45),
            combatSection.getInt("logout_timer", 10),
            combatSection.getBoolean("pvp_protection.enabled", true),
            combatSection.getInt("pvp_protection.duration", 30),
            combatSection.getInt("cooldowns.enderpearl", 16),
            combatSection.getInt("cooldowns.golden_apple", 45),
            combatSection.getInt("cooldowns.enchanted_golden_apple", 10800),
            combatSection.getBoolean("strength_nerf.enabled", true),
            combatSection.getDouble("strength_nerf.nerf_percentage", 33.5)
        );
    }
    
    /**
     * Carga la configuración de deathban
     */
    private void loadDeathbanConfig() {
        ConfigurationSection deathbanSection = config.getConfigurationSection("deathban");
        if (deathbanSection == null) {
            plugin.getLogger().warning("Sección 'deathban' no encontrada en config.yml");
            return;
        }
        
        this.deathbanConfig = new DeathbanConfig(
            deathbanSection.getInt("default_time", 45),
            deathbanSection.getBoolean("arena.enabled", true),
            deathbanSection.getString("arena.world", "world"),
            deathbanSection.getDouble("arena.spawn_location.x", 0),
            deathbanSection.getDouble("arena.spawn_location.y", 100),
            deathbanSection.getDouble("arena.spawn_location.z", 0),
            deathbanSection.getInt("arena.kill_reduction_time", 5)
        );
    }
    
    /**
     * Carga la configuración de claims
     */
    private void loadClaimsConfig() {
        ConfigurationSection claimsSection = config.getConfigurationSection("claims");
        if (claimsSection == null) {
            plugin.getLogger().warning("Sección 'claims' no encontrada en config.yml");
            return;
        }
        
        this.claimsConfig = new ClaimsConfig(
            claimsSection.getString("tool", "GOLDEN_HOE"),
            claimsSection.getDouble("cost_per_block", 0.5),
            claimsSection.getInt("min_size", 10),
            claimsSection.getInt("max_size", 50),
            claimsSection.getInt("buffer_distance", 5),
            claimsSection.getBoolean("overclaim.enabled", true),
            claimsSection.getBoolean("overclaim.only_when_raidable", true),
            claimsSection.getStringList("allowed_worlds")
        );
    }
    
    /**
     * Carga la configuración de KOTH
     */
    private void loadKothConfig() {
        ConfigurationSection kothSection = config.getConfigurationSection("koth");
        if (kothSection == null) {
            plugin.getLogger().warning("Sección 'koth' no encontrada en config.yml");
            return;
        }
        
        this.kothConfig = new KothConfig(
            kothSection.getInt("capture_time", 900),
            kothSection.getInt("contest_radius", 10),
            kothSection.getInt("max_simultaneous", 1),
            kothSection.getStringList("rewards"),
            kothSection.getBoolean("schedule.enabled", false),
            kothSection.getStringList("schedule.times")
        );
    }
    
    /**
     * Carga la configuración de clases
     */
    private void loadClassesConfig() {
        ConfigurationSection classesSection = config.getConfigurationSection("classes");
        if (classesSection == null) {
            plugin.getLogger().warning("Sección 'classes' no encontrada en config.yml");
            return;
        }
        
        this.classesConfig = new ClassesConfig(
            classesSection.getBoolean("diamond.enabled", true),
            classesSection.getBoolean("archer.enabled", true),
            classesSection.getDouble("archer.abilities.bow_damage_multiplier", 1.25),
            classesSection.getInt("archer.abilities.speed_bonus", 1),
            classesSection.getInt("archer.abilities.tag_duration", 10),
            classesSection.getBoolean("bard.enabled", true),
            classesSection.getInt("bard.abilities.energy", 120)
        );
    }
    
    /**
     * Valida la configuración cargada
     */
    private void validateConfiguration() {
        plugin.getLogger().info("Validando configuración...");
        
        boolean hasErrors = false;
        
        // Validar database config
        if (databaseConfig == null) {
            plugin.getLogger().severe("Configuración de base de datos inválida");
            hasErrors = true;
        }
        
        // Validar team config
        if (teamConfig != null && teamConfig.maxMembers <= 0) {
            plugin.getLogger().severe("teams.max_members debe ser mayor a 0");
            hasErrors = true;
        }
        
        // Validar combat config
        if (combatConfig != null && combatConfig.tagDuration <= 0) {
            plugin.getLogger().severe("combat.tag_duration debe ser mayor a 0");
            hasErrors = true;
        }
        
        if (hasErrors) {
            throw new IllegalStateException("Configuración inválida detectada. Revisa los logs para más detalles.");
        }
        
        plugin.getLogger().info("Configuración validada correctamente");
    }
    
    // Getters for configuration objects
    
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
    
    public TeamConfig getTeamConfig() {
        return teamConfig;
    }
    
    public CombatConfig getCombatConfig() {
        return combatConfig;
    }
    
    public DeathbanConfig getDeathbanConfig() {
        return deathbanConfig;
    }
    
    public ClaimsConfig getClaimsConfig() {
        return claimsConfig;
    }
    
    public KothConfig getKothConfig() {
        return kothConfig;
    }
    
    public ClassesConfig getClassesConfig() {
        return classesConfig;
    }
    
    // Configuration record classes
    
    public record DatabaseConfig(
        String type,
        String host,
        int port,
        String name,
        String username,
        String password,
        int maximumPoolSize,
        int minimumIdle,
        long connectionTimeout,
        long idleTimeout,
        long maxLifetime
    ) {}
    
    public record TeamConfig(
        int maxMembers,
        int maxClaims,
        int minMembersToClaim,
        boolean leaderOnlyDisband,
        int allyLimit,
        double startingDtr,
        double maxDtr,
        double lossPerDeath,
        double regenRate,
        int regenInterval,
        int freezeTime
    ) {}
    
    public record CombatConfig(
        int tagDuration,
        int logoutTimer,
        boolean pvpProtectionEnabled,
        int pvpProtectionDuration,
        int enderpearlCooldown,
        int goldenAppleCooldown,
        int enchantedGoldenAppleCooldown,
        boolean strengthNerfEnabled,
        double strengthNerfPercentage
    ) {}
    
    public record DeathbanConfig(
        int defaultTime,
        boolean arenaEnabled,
        String arenaWorld,
        double arenaSpawnX,
        double arenaSpawnY,
        double arenaSpawnZ,
        int killReductionTime
    ) {}
    
    public record ClaimsConfig(
        String tool,
        double costPerBlock,
        int minSize,
        int maxSize,
        int bufferDistance,
        boolean overclaimEnabled,
        boolean overclaimOnlyWhenRaidable,
        List<String> allowedWorlds
    ) {}
    
    public record KothConfig(
        int captureTime,
        int contestRadius,
        int maxSimultaneous,
        List<String> rewards,
        boolean scheduleEnabled,
        List<String> scheduleTimes
    ) {}
    
    public record ClassesConfig(
        boolean diamondEnabled,
        boolean archerEnabled,
        double archerBowDamageMultiplier,
        int archerSpeedBonus,
        int archerTagDuration,
        boolean bardEnabled,
        int bardEnergy
    ) {}
}