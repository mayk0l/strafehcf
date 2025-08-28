package net.strafepvp.hcf.domain.value;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa la identificación única de un equipo
 * 
 * Encapsula el UUID del equipo y proporciona una representación de dominio
 * para la identificación de equipos en el sistema HCF.
 * 
 * @author mayk0l
 */
public final class TeamId {
    
    private final UUID uuid;
    
    /**
     * Constructor privado para crear un TeamId desde un UUID
     * 
     * @param uuid El UUID del equipo, no puede ser null
     * @throws IllegalArgumentException si el UUID es null
     */
    private TeamId(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "UUID no puede ser null");
    }
    
    /**
     * Crea un TeamId desde un UUID
     * 
     * @param uuid El UUID del equipo
     * @return Una nueva instancia de TeamId
     * @throws IllegalArgumentException si el UUID es null
     */
    public static TeamId of(UUID uuid) {
        return new TeamId(uuid);
    }
    
    /**
     * Crea un TeamId desde una representación String del UUID
     * 
     * @param uuidString La representación String del UUID
     * @return Una nueva instancia de TeamId
     * @throws IllegalArgumentException si el String es null, vacío o no es un UUID válido
     */
    public static TeamId fromString(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID string no puede ser null o vacío");
        }
        
        try {
            UUID uuid = UUID.fromString(uuidString.trim());
            return new TeamId(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UUID string inválido: " + uuidString, e);
        }
    }
    
    /**
     * Genera un TeamId aleatorio
     * 
     * @return Una nueva instancia de TeamId con UUID aleatorio
     */
    public static TeamId random() {
        return new TeamId(UUID.randomUUID());
    }
    
    /**
     * Obtiene el UUID del equipo
     * 
     * @return El UUID del equipo
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
        TeamId teamId = (TeamId) o;
        return Objects.equals(uuid, teamId.uuid);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
    
    @Override
    public String toString() {
        return "TeamId{" + uuid + "}";
    }
}
