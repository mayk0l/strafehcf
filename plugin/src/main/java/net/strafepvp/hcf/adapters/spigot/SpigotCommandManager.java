package net.strafepvp.hcf.adapters.spigot;

import net.strafepvp.hcf.StrafeHCFPlugin;
import net.strafepvp.hcf.adapters.spigot.commands.HCFCommand;
import net.strafepvp.hcf.adapters.spigot.commands.TeamCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

/**
 * Gestor de comandos para Spigot/Paper
 * 
 * Maneja el registro y ejecución de todos los comandos del plugin.
 * Actúa como un adaptador entre el sistema de comandos de Spigot
 * y la lógica de dominio del plugin.
 * 
 * @author mayk0l
 */
public class SpigotCommandManager implements CommandExecutor, TabCompleter {
    
    private final StrafeHCFPlugin plugin;
    private final Map<String, BaseCommand> commands;
    private boolean initialized = false;
    
    public SpigotCommandManager(StrafeHCFPlugin plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
    }
    
    /**
     * Inicializa el gestor de comandos
     */
    public void initialize() {
        if (initialized) {
            plugin.getLogger().warning("SpigotCommandManager ya está inicializado");
            return;
        }
        
        plugin.getLogger().info("Inicializando gestor de comandos...");
        
        try {
            // Registrar comandos principales
            registerCommands();
            
            // Registrar executor y tab completer
            setupCommandHandlers();
            
            this.initialized = true;
            
            plugin.getLogger().info(String.format(
                "Gestor de comandos inicializado correctamente (%d comandos registrados)",
                commands.size()
            ));
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error al inicializar gestor de comandos", e);
            throw new RuntimeException("Failed to initialize command manager", e);
        }
    }
    
    /**
     * Registra todos los comandos del plugin
     */
    private void registerCommands() {
        plugin.getLogger().info("Registrando comandos...");
        
        // Comando principal HCF
        HCFCommand hcfCommand = new HCFCommand(plugin);
        registerCommand("hcf", hcfCommand);
        registerCommand("strafehcf", hcfCommand); // Alias
        
        // TODO: Comandos de equipo - temporalmente deshabilitados hasta arreglar herencia
        // TeamCommand teamCommand = new TeamCommand(plugin);
        // registerCommand("team", teamCommand);
        // registerCommand("t", teamCommand); // Alias
        // registerCommand("faction", teamCommand); // Alias común
        // registerCommand("f", teamCommand); // Alias común
        
        plugin.getLogger().info("Comandos registrados correctamente");
    }
    
    /**
     * Registra un comando individual
     * 
     * @param name Nombre del comando
     * @param command Instancia del comando
     */
    private void registerCommand(String name, BaseCommand command) {
        commands.put(name.toLowerCase(), command);
        plugin.getLogger().fine("Comando registrado: " + name);
    }
    
    /**
     * Configura los handlers de comandos en Bukkit
     */
    private void setupCommandHandlers() {
        for (String commandName : commands.keySet()) {
            org.bukkit.command.PluginCommand bukkitCommand = plugin.getCommand(commandName);
            if (bukkitCommand != null) {
                bukkitCommand.setExecutor(this);
                bukkitCommand.setTabCompleter(this);
            } else {
                plugin.getLogger().warning("No se pudo encontrar el comando en plugin.yml: " + commandName);
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            String commandName = command.getName().toLowerCase();
            BaseCommand baseCommand = commands.get(commandName);
            
            if (baseCommand == null) {
                plugin.getLogger().warning("Comando no encontrado: " + commandName);
                return false;
            }
            
            // Verificar permisos básicos
            if (!baseCommand.hasPermission(sender)) {
                sender.sendMessage("§cNo tienes permisos para usar este comando.");
                return true;
            }
            
            // Ejecutar comando
            return baseCommand.execute(sender, command, label, args);
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, 
                String.format("Error ejecutando comando '%s' para %s", command.getName(), sender.getName()), e);
            
            sender.sendMessage("§cError interno al ejecutar el comando. Revisa la consola para más detalles.");
            return true;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        try {
            String commandName = command.getName().toLowerCase();
            BaseCommand baseCommand = commands.get(commandName);
            
            if (baseCommand == null) {
                return Collections.emptyList();
            }
            
            // Verificar permisos para tab completion
            if (!baseCommand.hasPermission(sender)) {
                return Collections.emptyList();
            }
            
            return baseCommand.tabComplete(sender, command, alias, args);
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, 
                "Error en tab completion para comando: " + command.getName(), e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Detiene el gestor de comandos
     */
    public void shutdown() {
        plugin.getLogger().info("Deteniendo gestor de comandos...");
        
        // Limpiar comandos registrados
        commands.clear();
        
        this.initialized = false;
        
        plugin.getLogger().info("Gestor de comandos detenido correctamente");
    }
    
    /**
     * Obtiene un comando por nombre
     * 
     * @param name Nombre del comando
     * @return Comando o null si no existe
     */
    public BaseCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }
    
    /**
     * Obtiene todos los comandos registrados
     * 
     * @return Mapa de comandos
     */
    public Map<String, BaseCommand> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
    
    /**
     * Verifica si está inicializado
     * 
     * @return true si está inicializado
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Clase base abstracta para comandos
     */
    public static abstract class BaseCommand {
        
        protected final StrafeHCFPlugin plugin;
        
        public BaseCommand(StrafeHCFPlugin plugin) {
            this.plugin = plugin;
        }
        
        /**
         * Ejecuta el comando
         * 
         * @param sender Quien ejecuta el comando
         * @param command Comando
         * @param label Label usado
         * @param args Argumentos
         * @return true si se ejecutó correctamente
         */
        public abstract boolean execute(CommandSender sender, Command command, String label, String[] args);
        
        /**
         * Proporciona tab completion
         * 
         * @param sender Quien ejecuta el comando
         * @param command Comando
         * @param alias Alias usado
         * @param args Argumentos
         * @return Lista de sugerencias
         */
        public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return Collections.emptyList();
        }
        
        /**
         * Verifica si el sender tiene permisos para el comando
         * 
         * @param sender Quien ejecuta el comando
         * @return true si tiene permisos
         */
        public abstract boolean hasPermission(CommandSender sender);
        
        /**
         * Verifica si el sender es un jugador
         * 
         * @param sender Sender a verificar
         * @return true si es jugador
         */
        protected boolean isPlayer(CommandSender sender) {
            return sender instanceof Player;
        }
        
        /**
         * Obtiene el Player del sender si es válido
         * 
         * @param sender Sender
         * @return Player o null
         */
        protected Player getPlayer(CommandSender sender) {
            return isPlayer(sender) ? (Player) sender : null;
        }
        
        /**
         * Envía un mensaje de ayuda
         * 
         * @param sender A quien enviar
         * @param usage Uso del comando
         * @param description Descripción
         */
        protected void sendHelp(CommandSender sender, String usage, String description) {
            sender.sendMessage("§6Uso: §f" + usage);
            sender.sendMessage("§6Descripción: §f" + description);
        }
        
        /**
         * Filtra una lista basada en el input parcial
         * 
         * @param input Input parcial
         * @param options Opciones disponibles
         * @return Lista filtrada
         */
        protected List<String> filterStartingWith(String input, List<String> options) {
            if (input.isEmpty()) {
                return options;
            }
            
            return options.stream()
                .filter(option -> option.toLowerCase().startsWith(input.toLowerCase()))
                .sorted()
                .toList();
        }
        
        /**
         * Filtra nombres de jugadores online
         * 
         * @param input Input parcial
         * @return Lista de nombres filtrada
         */
        protected List<String> filterPlayerNames(String input) {
            List<String> playerNames = plugin.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
            
            return filterStartingWith(input, playerNames);
        }
    }
}