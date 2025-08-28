package net.strafepvp.hcf.adapters.spigot.commands;

import net.strafepvp.hcf.StrafeHCFPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Comando de equipos/facciones
 * 
 * Maneja todos los comandos relacionados con los equipos HCF.
 * 
 * @author mayk0l
 */
public class TeamCommand extends Command {
    
    private final StrafeHCFPlugin plugin;
    
    public TeamCommand(StrafeHCFPlugin plugin) {
        super("team");
        this.plugin = plugin;
        
        setDescription("Comando principal de equipos");
        setUsage("/team [subcomando] [argumentos]");
        setPermission("strafehcf.team");
        setAliases(Arrays.asList("t", "faction", "f"));
    }
    
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        // Only players can use team commands
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser usado por jugadores.");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check basic permission
        if (!player.hasPermission("strafehcf.team")) {
            player.sendMessage(ChatColor.RED + "No tienes permisos para usar comandos de equipo.");
            return true;
        }
        
        // No arguments - show team info
        if (args.length == 0) {
            showTeamInfo(player);
            return true;
        }
        
        // Handle subcommands
        String subcommand = args[0].toLowerCase();
        
        switch (subcommand) {
            case "create" -> handleCreate(player, args);
            case "disband" -> handleDisband(player);
            case "invite" -> handleInvite(player, args);
            case "kick" -> handleKick(player, args);
            case "leave" -> handleLeave(player);
            case "accept" -> handleAccept(player);
            case "deny" -> handleDeny(player);
            case "info" -> handleInfo(player, args);
            case "list" -> handleList(player);
            case "chat", "c" -> handleChat(player, args);
            case "help" -> showHelp(player);
            default -> {
                player.sendMessage(ChatColor.RED + "Subcomando desconocido: " + subcommand);
                showHelp(player);
            }
        }
        
        return true;
    }
    
    /**
     * Muestra información del equipo del jugador
     */
    private void showTeamInfo(Player player) {
        // TODO: Implement team info display
        player.sendMessage(ChatColor.YELLOW + "Mostrando información de tu equipo...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja la creación de equipos
     */
    private void handleCreate(Player player, String[] args) {
        if (!player.hasPermission("strafehcf.team.create")) {
            player.sendMessage(ChatColor.RED + "No tienes permisos para crear equipos.");
            return;
        }
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Uso: /team create <nombre>");
            return;
        }
        
        String teamName = args[1];
        
        // TODO: Implement team creation logic
        player.sendMessage(ChatColor.YELLOW + "Creando equipo: " + teamName);
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja la disolución de equipos
     */
    private void handleDisband(Player player) {
        // TODO: Implement team disbanding
        player.sendMessage(ChatColor.YELLOW + "Disolviendo equipo...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja las invitaciones a equipos
     */
    private void handleInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Uso: /team invite <jugador>");
            return;
        }
        
        String targetPlayer = args[1];
        
        // TODO: Implement team invitation logic
        player.sendMessage(ChatColor.YELLOW + "Invitando a " + targetPlayer + " al equipo...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja expulsar miembros del equipo
     */
    private void handleKick(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Uso: /team kick <jugador>");
            return;
        }
        
        String targetPlayer = args[1];
        
        // TODO: Implement team kick logic
        player.sendMessage(ChatColor.YELLOW + "Expulsando a " + targetPlayer + " del equipo...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja salir del equipo
     */
    private void handleLeave(Player player) {
        // TODO: Implement leave team logic
        player.sendMessage(ChatColor.YELLOW + "Saliendo del equipo...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja aceptar invitaciones
     */
    private void handleAccept(Player player) {
        // TODO: Implement accept invitation logic
        player.sendMessage(ChatColor.YELLOW + "Aceptando invitación...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja rechazar invitaciones
     */
    private void handleDeny(Player player) {
        // TODO: Implement deny invitation logic
        player.sendMessage(ChatColor.YELLOW + "Rechazando invitación...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja información de otros equipos
     */
    private void handleInfo(Player player, String[] args) {
        String teamName = args.length > 1 ? args[1] : null;
        
        // TODO: Implement team info logic
        player.sendMessage(ChatColor.YELLOW + "Información del equipo" + 
            (teamName != null ? " " + teamName : "") + "...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja listar equipos
     */
    private void handleList(Player player) {
        // TODO: Implement team list logic
        player.sendMessage(ChatColor.YELLOW + "Listando equipos...");
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Maneja el chat de equipo
     */
    private void handleChat(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Uso: /team chat <mensaje>");
            return;
        }
        
        // Build message from arguments
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) message.append(" ");
            message.append(args[i]);
        }
        
        // TODO: Implement team chat logic
        player.sendMessage(ChatColor.YELLOW + "Enviando mensaje al chat del equipo: " + message);
        player.sendMessage(ChatColor.GRAY + "Funcionalidad en desarrollo.");
    }
    
    /**
     * Muestra la ayuda del comando
     */
    private void showHelp(Player player) {
        player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------");
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Comandos de Equipo");
        player.sendMessage(ChatColor.GRAY + "/team " + ChatColor.WHITE + "- Ver información de tu equipo");
        player.sendMessage(ChatColor.GRAY + "/team create <nombre> " + ChatColor.WHITE + "- Crear equipo");
        player.sendMessage(ChatColor.GRAY + "/team disband " + ChatColor.WHITE + "- Disolver equipo");
        player.sendMessage(ChatColor.GRAY + "/team invite <jugador> " + ChatColor.WHITE + "- Invitar jugador");
        player.sendMessage(ChatColor.GRAY + "/team kick <jugador> " + ChatColor.WHITE + "- Expulsar jugador");
        player.sendMessage(ChatColor.GRAY + "/team leave " + ChatColor.WHITE + "- Salir del equipo");
        player.sendMessage(ChatColor.GRAY + "/team accept " + ChatColor.WHITE + "- Aceptar invitación");
        player.sendMessage(ChatColor.GRAY + "/team deny " + ChatColor.WHITE + "- Rechazar invitación");
        player.sendMessage(ChatColor.GRAY + "/team info [equipo] " + ChatColor.WHITE + "- Ver información");
        player.sendMessage(ChatColor.GRAY + "/team list " + ChatColor.WHITE + "- Listar equipos");
        player.sendMessage(ChatColor.GRAY + "/team chat <mensaje> " + ChatColor.WHITE + "- Chat de equipo");
        player.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------");
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("strafehcf.team")) {
            return Collections.emptyList();
        }
        
        if (args.length == 1) {
            return Arrays.asList("create", "disband", "invite", "kick", "leave", 
                                "accept", "deny", "info", "list", "chat", "help");
        }
        
        return Collections.emptyList();
    }
}
