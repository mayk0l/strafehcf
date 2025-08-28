package net.strafepvp.hcf.core.domain.valueobjects;

import java.util.UUID;

/**
 * Value Object que representa un ID único de equipo
 * 
 * @param value El UUID del equipo
 */
public record TeamId(UUID value) {
    
    public TeamId {
        if (value == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
    }
    
    /**
     * Crea un nuevo TeamId
     */
    public static TeamId generate() {
        return new TeamId(UUID.randomUUID());
    }
    
    /**
     * Crea un TeamId desde un string
     */
    public static TeamId fromString(String id) {
        return new TeamId(UUID.fromString(id));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}
