package net.strafepvp.hcf.core.domain.valueobjects;

import java.util.UUID;

/**
 * Value Object que representa un ID único de jugador
 * 
 * @param value El UUID del jugador de Minecraft
 */
public record PlayerId(UUID value) {
    
    public PlayerId {
        if (value == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
    }
    
    /**
     * Crea un PlayerId desde un string
     */
    public static PlayerId fromString(String id) {
        return new PlayerId(UUID.fromString(id));
    }
    
    /**
     * Crea un PlayerId desde el UUID de Minecraft
     */
    public static PlayerId fromMinecraftUUID(UUID minecraftUuid) {
        return new PlayerId(minecraftUuid);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
