package net.strafepvp.hcf.adapters.spigot.commands;

import net.strafepvp.hcf.StrafeHCFPlugin;
import net.strafepvp.hcf.adapters.spigot.SpigotCommandManager.BaseCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Comando principal del plugin HCF
 * 
 * Maneja comandos administrativos y de información general.
 * 
 * @author mayk0l
 */
public class HCFCommand extends BaseCommand {
    
    public HCFCommand(StrafeHCFPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public boolean execute(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendMainHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        
        switch (subCommand) {
            case "help":
                sendMainHelp(sender);
                break;
            
            case "version":
            case "ver":
                sendVersionInfo(sender);
                break;
            
            case "reload":
                handleReload(sender);
                break;
            
            case "status":
                handleStatus(sender);
                break;
            
            case "debug":
                handleDebug(sender, subArgs);
                break;
            
            default:
                sender.sendMessage("§cSubcomando desconocido: " + subCommand);
                sendMainHelp(sender);
                break;
        }
        
        return true;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("help", "version", "status");
            
            if (sender.hasPermission("strafehcf.admin")) {
                subCommands = Arrays.asList("help", "version", "reload", "status", "debug");
            }
            
            return filterStartingWith(args[0], subCommands);
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
            return filterStartingWith(args[1], Arrays.asList("database", "services", "config"));
        }
        
        return super.tabComplete(sender, command, alias, args);
    }
    
    @Override
    public boolean hasPermission(CommandSender sender) {
        // Comando básico disponible para todos, pero algunos subcomandos requieren permisos
        return true;
    }
    
    /**
     * Envía la ayuda principal del comando
     */
    private void sendMainHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== StrafeHCF ===");
        sender.sendMessage("§6/hcf help §8- §fMuestra esta ayuda");
        sender.sendMessage("§6/hcf version §8- §fMuestra información de versión");
        sender.sendMessage("§6/hcf status §8- §fMuestra el estado del servidor HCF");
        
        if (sender.hasPermission("strafehcf.admin")) {
            sender.sendMessage("§c§lComandos de Administrador:");
            sender.sendMessage("§c/hcf reload §8- §fRecarga la configuración");
            sender.sendMessage("§c/hcf debug <tipo> §8- §fInformación de debug");
        }
        
        sender.sendMessage("§6Para comandos de equipo usa: §f/team help");
    }
    
    /**
     * Envía información de versión
     */
    private void sendVersionInfo(CommandSender sender) {
        sender.sendMessage("§6§l=== Información de Versión ===");
        sender.sendMessage("§6Plugin: §fStrafeHCF v" + plugin.getDescription().getVersion());
        sender.sendMessage("§6Autor: §f" + String.join(", ", plugin.getDescription().getAuthors()));
        sender.sendMessage("§6Servidor: §f" + plugin.getServer().getName() + " " + plugin.getServer().getVersion());
        sender.sendMessage("§6Jugadores: §f" + plugin.getServer().getOnlinePlayers().size() + "/" + plugin.getServer().getMaxPlayers());
    }
    
    /**
     * Maneja el comando de recarga
     */
    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("strafehcf.admin.reload")) {
            sender.sendMessage("§cNo tienes permisos para recargar el plugin.");
            return;
        }
        
        sender.sendMessage("§eRecargando StrafeHCF...");
        
        try {
            // Recargar el servicio de aplicación
            if (plugin.getApplicationService() != null) {
                plugin.getApplicationService().reload();
                sender.sendMessage("§aStrafeHCF recargado correctamente.");
            } else {
                sender.sendMessage("§cEl servicio de aplicación no está disponible.");
            }
            
        } catch (Exception e) {
            sender.sendMessage("§cError al recargar el plugin: " + e.getMessage());
            plugin.getLogger().severe("Error en comando reload: " + e.getMessage());
        }
    }
    
    /**
     * Maneja el comando de estado
     */
    private void handleStatus(CommandSender sender) {
        sender.sendMessage("§6§l=== Estado del Servidor HCF ===");
        
        // Información básica
        sender.sendMessage("§6Estado del Plugin: §f" + (plugin.isEnabled() ? "§aActivo" : "§cInactivo"));
        
        // Estado del servicio de aplicación
        if (plugin.getApplicationService() != null) {
            boolean healthy = plugin.getApplicationService().isHealthy();
            sender.sendMessage("§6Servicios: §f" + (healthy ? "§aSaludables" : "§cProblemas detectados"));
            sender.sendMessage("§6Servicios Activos: §f" + plugin.getApplicationService().getActiveServicesCount());
        } else {
            sender.sendMessage("§6Servicios: §cNo inicializados");
        }
        
        // Información de la base de datos
        if (plugin.getApplicationService() != null && plugin.getApplicationService().getDatabaseManager() != null) {
            boolean dbHealthy = plugin.getApplicationService().getDatabaseManager().isHealthy();
            sender.sendMessage("§6Base de Datos: §f" + (dbHealthy ? "§aConectada" : "§cDesconectada"));
        } else {
            sender.sendMessage("§6Base de Datos: §cNo disponible");
        }
        
        // Información del servidor
        sender.sendMessage("§6TPS: §f" + String.format("%.2f", plugin.getServer().getTPS()[0]));
        sender.sendMessage("§6Memoria: §f" + getMemoryUsage());
    }
    
    /**
     * Maneja comandos de debug
     */
    private void handleDebug(CommandSender sender, String[] args) {
        if (!sender.hasPermission("strafehcf.admin.debug")) {
            sender.sendMessage("§cNo tienes permisos para usar comandos de debug.");
            return;
        }
        
        if (args.length == 0) {
            sender.sendMessage("§cUso: /hcf debug <database|services|config>");
            return;
        }
        
        String debugType = args[0].toLowerCase();
        
        switch (debugType) {
            case "database":
                debugDatabase(sender);
                break;
            
            case "services":
                debugServices(sender);
                break;
            
            case "config":
                debugConfig(sender);
                break;
            
            default:
                sender.sendMessage("§cTipo de debug desconocido: " + debugType);
                sender.sendMessage("§cUso: /hcf debug <database|services|config>");
                break;
        }
    }
    
    /**
     * Debug de base de datos
     */
    private void debugDatabase(CommandSender sender) {
        sender.sendMessage("§6§l=== Debug Base de Datos ===");
        
        if (plugin.getApplicationService() != null && plugin.getApplicationService().getDatabaseManager() != null) {
            var dbManager = plugin.getApplicationService().getDatabaseManager();
            sender.sendMessage("§6Estado: §f" + (dbManager.isHealthy() ? "§aSaludable" : "§cProblemas"));
            sender.sendMessage("§6Pool: §f" + dbManager.getPoolStatus());
        } else {
            sender.sendMessage("§cDatabaseManager no disponible");
        }
    }
    
    /**
     * Debug de servicios
     */
    private void debugServices(CommandSender sender) {
        sender.sendMessage("§6§l=== Debug Servicios ===");
        
        if (plugin.getApplicationService() != null) {
            sender.sendMessage(plugin.getApplicationService().getHealthInfo());
        } else {
            sender.sendMessage("§cApplicationService no disponible");
        }
    }
    
    /**
     * Debug de configuración
     */
    private void debugConfig(CommandSender sender) {
        sender.sendMessage("§6§l=== Debug Configuración ===");
        
        if (plugin.getConfigManager() != null) {
            var config = plugin.getConfigManager();
            var dbConfig = config.getDatabaseConfig();
            
            sender.sendMessage("§6Tipo BD: §f" + dbConfig.type());
            sender.sendMessage("§6Host: §f" + dbConfig.host() + ":" + dbConfig.port());
            sender.sendMessage("§6Base: §f" + dbConfig.name());
            sender.sendMessage("§6Pool Max: §f" + dbConfig.maximumPoolSize());
        } else {
            sender.sendMessage("§cConfigManager no disponible");
        }
    }
    
    /**
     * Obtiene el uso de memoria formateado
     */
    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        return String.format("%dMB / %dMB (%.1f%%)", 
            usedMemory, maxMemory, (double) usedMemory / maxMemory * 100);
    }
}
