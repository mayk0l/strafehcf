package net.strafepvp.hcf;

import net.strafepvp.hcf.application.HCFApplicationService;
import net.strafepvp.hcf.application.config.ConfigManager;
import net.strafepvp.hcf.application.lifecycle.PluginLifecycleManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Plugin principal de StrafeHCF
 * 
 * Plugin HCF moderno y optimizado para Minecraft 1.21.8
 * Desarrollado específicamente para el servidor StrafePVP
 * 
 * Arquitectura: Hexagonal (Ports and Adapters)
 * Principios: SOLID, DDD, Clean Architecture
 * 
 * @author mayk0l
 * @version 1.0.0-SNAPSHOT
 */
public final class StrafeHCFPlugin extends JavaPlugin {
    
    private static StrafeHCFPlugin instance;
    
    private ConfigManager configManager;
    private HCFApplicationService applicationService;
    private PluginLifecycleManager lifecycleManager;
    
    @Override
    public void onLoad() {
        instance = this;
        
        getLogger().info("Cargando StrafeHCF v" + getPluginMeta().getVersion());
        getLogger().info("Desarrollado para StrafePVP por mayk0l");
    }
    
    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. Initialize Configuration
            initializeConfiguration();
            
            // 2. Initialize Application Service
            initializeApplicationService();
            
            // 3. Initialize Plugin Lifecycle
            initializeLifecycle();
            
            // 4. Start Application
            applicationService.start();
            
            long loadTime = System.currentTimeMillis() - startTime;
            getLogger().info(String.format("StrafeHCF habilitado correctamente en %dms", loadTime));
            getLogger().info("Estado: ACTIVO | Arquitectura: Hexagonal | Java: " + System.getProperty("java.version"));
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error crítico al habilitar StrafeHCF", e);
            getLogger().severe("El plugin será deshabilitado por seguridad");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Deshabilitando StrafeHCF...");
        
        try {
            if (applicationService != null) {
                applicationService.stop();
            }
            
            if (lifecycleManager != null) {
                lifecycleManager.shutdown();
            }
            
            getLogger().info("StrafeHCF deshabilitado correctamente");
            
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Error durante la deshabilitación del plugin", e);
        } finally {
            instance = null;
        }
    }
    
    /**
     * Inicializa el sistema de configuración
     */
    private void initializeConfiguration() {
        getLogger().info("Inicializando configuración...");
        
        // Crear directorio de configuración si no existe
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Guardar configuración por defecto
        saveDefaultConfig();
        
        // Inicializar ConfigManager
        this.configManager = new ConfigManager(this);
        configManager.loadConfiguration();
        
        getLogger().info("Configuración cargada correctamente");
    }
    
    /**
     * Inicializa el servicio de aplicación principal
     */
    private void initializeApplicationService() {
        getLogger().info("Inicializando servicio de aplicación...");
        
        this.applicationService = new HCFApplicationService(this, configManager);
        
        getLogger().info("Servicio de aplicación inicializado");
    }
    
    /**
     * Inicializa el gestor del ciclo de vida del plugin
     */
    private void initializeLifecycle() {
        getLogger().info("Inicializando ciclo de vida del plugin...");
        
        this.lifecycleManager = new PluginLifecycleManager(this, applicationService);
        
        getLogger().info("Ciclo de vida del plugin inicializado");
    }
    
    /**
     * Obtiene la instancia del plugin
     * 
     * @return Instancia del plugin
     */
    public static StrafeHCFPlugin getInstance() {
        return instance;
    }
    
    /**
     * Obtiene el gestor de configuración
     * 
     * @return ConfigManager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Obtiene el servicio de aplicación
     * 
     * @return HCFApplicationService
     */
    public HCFApplicationService getApplicationService() {
        return applicationService;
    }
    
    /**
     * Obtiene el gestor del ciclo de vida
     * 
     * @return PluginLifecycleManager
     */
    public PluginLifecycleManager getPluginLifecycleManager() {
        return lifecycleManager;
    }
    
    /**
     * Recarga el plugin completamente
     */
    public void reload() {
        getLogger().info("Recargando plugin...");
        
        try {
            // Reload configuration
            configManager.reloadConfiguration();
            
            // Restart application service if needed
            if (applicationService != null && applicationService.isStarted()) {
                applicationService.reload();
            }
            
            getLogger().info("Plugin recargado correctamente");
            
        } catch (Exception e) {
            getLogger().severe("Error al recargar el plugin: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene información sobre el estado del plugin
     * 
     * @return Información de estado
     */
    public String getStatusInfo() {
        StringBuilder status = new StringBuilder();
        status.append("StrafeHCF v").append(getPluginMeta().getVersion()).append("\n");
        status.append("Estado: ").append(isEnabled() ? "ACTIVO" : "INACTIVO").append("\n");
        status.append("Arquitectura: Hexagonal\n");
        status.append("Java: ").append(System.getProperty("java.version")).append("\n");
        status.append("Servidor: ").append(getServer().getName()).append(" ").append(getServer().getVersion()).append("\n");
        
        if (applicationService != null) {
            status.append("Servicios: ").append(applicationService.getActiveServicesCount()).append(" activos\n");
        }
        
        return status.toString();
    }
}