package net.strafepvp.hcf.application.lifecycle;

import net.strafepvp.hcf.StrafeHCFPlugin;
import net.strafepvp.hcf.application.HCFApplicationService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Gestor del ciclo de vida del plugin StrafeHCF
 * 
 * Maneja tareas periódicas, limpieza de recursos y monitoreo
 * del estado del plugin durante su ejecución.
 * 
 * @author mayk0l
 */
public class PluginLifecycleManager {
    
    private final StrafeHCFPlugin plugin;
    private final HCFApplicationService applicationService;
    
    private ScheduledExecutorService scheduler;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    
    public PluginLifecycleManager(StrafeHCFPlugin plugin, HCFApplicationService applicationService) {
        this.plugin = plugin;
        this.applicationService = applicationService;
    }
    
    /**
     * Inicia el gestor del ciclo de vida
     */
    public void start() {
        if (isStarted.get()) {
            plugin.getLogger().warning("PluginLifecycleManager ya está iniciado");
            return;
        }
        
        plugin.getLogger().info("Iniciando gestor del ciclo de vida...");
        
        // Crear el scheduler para tareas periódicas
        this.scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread thread = new Thread(r, "StrafeHCF-Lifecycle");
            thread.setDaemon(true);
            return thread;
        });
        
        // Programar tareas periódicas
        schedulePeriodicTasks();
        
        // Registrar shutdown hook
        registerShutdownHook();
        
        isStarted.set(true);
        plugin.getLogger().info("Gestor del ciclo de vida iniciado correctamente");
    }
    
    /**
     * Programa tareas periódicas del plugin
     */
    private void schedulePeriodicTasks() {
        // Health check cada 5 minutos
        scheduler.scheduleAtFixedRate(this::performHealthCheck, 5, 5, TimeUnit.MINUTES);
        
        // Limpieza de datos temporales cada hora
        scheduler.scheduleAtFixedRate(this::performCleanupTasks, 1, 1, TimeUnit.HOURS);
        
        // Estadísticas cada 30 minutos
        scheduler.scheduleAtFixedRate(this::logStatistics, 30, 30, TimeUnit.MINUTES);
        
        plugin.getLogger().info("Tareas periódicas programadas");
    }
    
    /**
     * Registra un shutdown hook para limpieza en caso de cierre abrupto
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            plugin.getLogger().info("Ejecutando shutdown hook de emergencia...");
            shutdown();
        }, "StrafeHCF-ShutdownHook"));
    }
    
    /**
     * Realiza un health check del sistema
     */
    private void performHealthCheck() {
        try {
            boolean isHealthy = applicationService.isHealthy();
            
            if (!isHealthy) {
                plugin.getLogger().warning("Health check falló - algunos servicios no están saludables");
                plugin.getLogger().info(applicationService.getHealthInfo());
            } else {
                plugin.getLogger().info("Health check exitoso - todos los servicios están saludables");
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error durante health check: " + e.getMessage());
        }
    }
    
    /**
     * Realiza tareas de limpieza periódicas
     */
    private void performCleanupTasks() {
        try {
            plugin.getLogger().info("Ejecutando tareas de limpieza...");
            
            // Limpiar invitaciones expiradas
            cleanExpiredInvitations();
            
            // Limpiar jugadores desconectados hace tiempo
            cleanDisconnectedPlayers();
            
            // Limpiar cache obsoleto
            cleanObsoleteCache();
            
            plugin.getLogger().info("Tareas de limpieza completadas");
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error durante tareas de limpieza: " + e.getMessage());
        }
    }
    
    /**
     * Registra estadísticas del sistema
     */
    private void logStatistics() {
        try {
            // Memory stats
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory() / 1024 / 1024;
            long freeMemory = runtime.freeMemory() / 1024 / 1024;
            long usedMemory = totalMemory - freeMemory;
            
            plugin.getLogger().info(String.format("Estadísticas - Memoria: %dMB usado / %dMB total", 
                usedMemory, totalMemory));
            
            // Database stats
            if (applicationService.getDatabaseManager() != null) {
                plugin.getLogger().info("Base de datos: " + applicationService.getDatabaseManager().getPoolStats());
            }
            
            // Application stats
            plugin.getLogger().info(applicationService.getHealthInfo());
            
        } catch (Exception e) {
            plugin.getLogger().warning("Error al registrar estadísticas: " + e.getMessage());
        }
    }
    
    /**
     * Limpia invitaciones de equipo expiradas
     */
    private void cleanExpiredInvitations() {
        // TODO: Implementar limpieza de invitaciones expiradas
        plugin.getLogger().fine("Limpiando invitaciones expiradas...");
    }
    
    /**
     * Limpia datos de jugadores desconectados hace mucho tiempo
     */
    private void cleanDisconnectedPlayers() {
        // TODO: Implementar limpieza de jugadores inactivos
        plugin.getLogger().fine("Limpiando datos de jugadores inactivos...");
    }
    
    /**
     * Limpia cache obsoleto
     */
    private void cleanObsoleteCache() {
        // TODO: Implementar limpieza de cache
        plugin.getLogger().fine("Limpiando cache obsoleto...");
        
        // Ejecutar garbage collector si es necesario
        Runtime runtime = Runtime.getRuntime();
        long freeMemoryBefore = runtime.freeMemory();
        runtime.gc();
        long freeMemoryAfter = runtime.freeMemory();
        
        if (freeMemoryAfter > freeMemoryBefore) {
            long freed = (freeMemoryAfter - freeMemoryBefore) / 1024 / 1024;
            plugin.getLogger().fine(String.format("Garbage collector liberó %dMB de memoria", freed));
        }
    }
    
    /**
     * Detiene el gestor del ciclo de vida y limpia recursos
     */
    public void shutdown() {
        if (!isStarted.get()) {
            return;
        }
        
        plugin.getLogger().info("Deteniendo gestor del ciclo de vida...");
        
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            
            try {
                // Esperar hasta 10 segundos para que terminen las tareas
                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    plugin.getLogger().warning("Algunas tareas no terminaron en tiempo, forzando cierre...");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                plugin.getLogger().warning("Interrupción durante el cierre del scheduler");
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        isStarted.set(false);
        plugin.getLogger().info("Gestor del ciclo de vida detenido correctamente");
    }
    
    /**
     * Verifica si el gestor está iniciado
     */
    public boolean isStarted() {
        return isStarted.get();
    }
    
    /**
     * Ejecuta una tarea asíncrona
     */
    public void runAsync(Runnable task) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.execute(task);
        } else {
            plugin.getLogger().warning("Scheduler no disponible para ejecutar tarea asíncrona");
        }
    }
    
    /**
     * Programa una tarea para ejecutar después de un delay
     */
    public void runLater(Runnable task, long delay, TimeUnit unit) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.schedule(task, delay, unit);
        } else {
            plugin.getLogger().warning("Scheduler no disponible para programar tarea");
        }
    }
}