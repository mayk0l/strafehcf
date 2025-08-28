package net.strafepvp.hcf.adapters.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Gestiona eventos de Spigot/Paper y los traduce al dominio de HCF
 */
public class SpigotEventManager implements Listener {
    
    private final Plugin plugin;
    private final Logger logger;
    private final ConcurrentHashMap<UUID, String> playerMapping;
    
    public SpigotEventManager(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.playerMapping = new ConcurrentHashMap<>();
        
        // Registrar este listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
        logger.info("SpigotEventManager registrado correctamente");
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        playerMapping.put(uuid, player.getName());
        
        logger.info(String.format("Jugador %s (%s) se unió al servidor", 
            player.getName(), uuid));
        
        // Aquí se pueden disparar eventos del dominio
        // Ejemplo: applicationService.handlePlayerJoin(playerId);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        String playerName = playerMapping.remove(uuid);
        
        if (playerName != null) {
            logger.info(String.format("Jugador %s (%s) salió del servidor", 
                player.getName(), uuid));
            
            // Aquí se pueden disparar eventos del dominio
            // Ejemplo: applicationService.handlePlayerLeave(playerId);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        String playerName = playerMapping.remove(uuid);
        
        if (playerName != null) {
            logger.info(String.format("Jugador %s (%s) fue expulsado: %s", 
                player.getName(), uuid, event.reason()));
            
            // Aquí se pueden disparar eventos del dominio
            // Ejemplo: applicationService.handlePlayerKick(playerId, event.getReason());
        }
    }
    
    /**
     * Inicializa el manager de eventos
     */
    public void initialize() {
        logger.info("SpigotEventManager inicializado");
    }
    
    /**
     * Cierra el manager de eventos
     */
    public void shutdown() {
        cleanup();
        logger.info("SpigotEventManager cerrado");
    }
    
    /**
     * Obtiene el nombre de un jugador por su UUID
     */
    public String getPlayerName(UUID uuid) {
        return playerMapping.get(uuid);
    }
    
    /**
     * Obtiene el nombre de un jugador por su instancia de Player
     */
    public String getPlayerName(Player player) {
        return getPlayerName(player.getUniqueId());
    }
    
    /**
     * Limpia todos los mapeos de jugadores (útil para reload/shutdown)
     */
    public void cleanup() {
        playerMapping.clear();
        logger.info("SpigotEventManager limpiado");
    }
    
    /**
     * Obtiene el número de jugadores actualmente mapeados
     */
    public int getPlayerCount() {
        return playerMapping.size();
    }
}