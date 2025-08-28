package net.strafepvp.hcf.domain.value;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa la identificación única de un jugador
 * 
 * Encapsula el UUID del jugador y proporciona una representación de dominio
 * para la identificación de jugadores en el sistema HCF.
 * 
 * @author mayk0l
 */
public final class PlayerId {
    
    private final UUID uuid;
    
    /**
     * Constructor privado para crear un PlayerId desde un UUID
     * 
     * @param uuid El UUID del jugador, no puede ser null
     * @throws IllegalArgumentException si el UUID es null
     */
    private PlayerId(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "UUID no puede ser null");
    }
    
    /**
     * Crea un PlayerId desde un UUID
     * 
     * @param uuid El UUID del jugador
     * @return Una nueva instancia de PlayerId
     * @throws IllegalArgumentException si el UUID es null
     */
    public static PlayerId of(UUID uuid) {
        return new PlayerId(uuid);
    }
    
    /**
     * Crea un PlayerId desde una representación String del UUID
     * 
     * @param uuidString La representación String del UUID
     * @return Una nueva instancia de PlayerId
     * @throws IllegalArgumentException si el String es null, vacío o no es un UUID válido
     */
    public static PlayerId fromString(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID string no puede ser null o vacío");
        }
        
        try {
            UUID uuid = UUID.fromString(uuidString.trim());
            return new PlayerId(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UUID string inválido: " + uuidString, e);
        }
    }
    
    /**
     * Genera un PlayerId aleatorio
     * 
     * @return Una nueva instancia de PlayerId con UUID aleatorio
     */
    public static PlayerId random() {
        return new PlayerId(UUID.randomUUID());
    }
    
    /**
     * Obtiene el UUID del jugador
     * 
     * @return El UUID del jugador
     */
    public UUID asUUID() {
        return uuid;
    }
    
    /**
     * Obtiene la representación String del UUID
     * 
     * @return El UUID como String
     */
    public String asString() {
        return uuid.toString();
    }
    
    /**
     * Obtiene una representación compacta del UUID (sin guiones)
     * 
     * @return El UUID como String sin guiones
     */
    public String asStringCompact() {
        return uuid.toString().replace("-", "");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerId playerId = (PlayerId) o;
        return Objects.equals(uuid, playerId.uuid);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
    
    @Override
    public String toString() {
        return "PlayerId{" + uuid + "}";
    }
}
