package net.strafepvp.hcf.domain.value;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa el nombre de un equipo
 * 
 * Encapsula el nombre del equipo con validaciones de negocio apropiadas,
 * incluyendo longitud, caracteres permitidos y formato.
 * 
 * @author mayk0l
 */
public final class TeamName {
    
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 16;
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    
    private final String name;
    
    /**
     * Constructor privado para crear un TeamName
     * 
     * @param name El nombre del equipo, debe cumplir las reglas de validación
     * @throws IllegalArgumentException si el nombre no es válido
     */
    private TeamName(String name) {
        this.name = validateAndNormalize(name);
    }
    
    /**
     * Crea un TeamName desde un String
     * 
     * @param name El nombre del equipo
     * @return Una nueva instancia de TeamName
     * @throws IllegalArgumentException si el nombre no es válido
     */
    public static TeamName of(String name) {
        return new TeamName(name);
    }
    
    /**
     * Valida y normaliza el nombre del equipo
     * 
     * @param name El nombre a validar
     * @return El nombre normalizado
     * @throws IllegalArgumentException si el nombre no es válido
     */
    private String validateAndNormalize(String name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre del equipo no puede ser null");
        }
        
        String trimmed = name.trim();
        
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El nombre del equipo debe tener al menos %d caracteres", MIN_LENGTH)
            );
        }
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("El nombre del equipo no puede tener más de %d caracteres", MAX_LENGTH)
            );
        }
        
        if (!VALID_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                "El nombre del equipo solo puede contener letras, números y guiones bajos"
            );
        }
        
        // Verificar que no comience ni termine con guión bajo
        if (trimmed.startsWith("_") || trimmed.endsWith("_")) {
            throw new IllegalArgumentException(
                "El nombre del equipo no puede comenzar ni terminar con guión bajo"
            );
        }
        
        return trimmed;
    }
    
    /**
     * Verifica si un nombre es válido sin crear la instancia
     * 
     * @param name El nombre a verificar
     * @return true si el nombre es válido, false en caso contrario
     */
    public static boolean isValid(String name) {
        try {
            new TeamName(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Obtiene el nombre del equipo
     * 
     * @return El nombre del equipo
     */
    public String value() {
        return name;
    }
    
    /**
     * Obtiene el nombre del equipo en minúsculas
     * 
     * @return El nombre del equipo en minúsculas
     */
    public String toLowerCase() {
        return name.toLowerCase();
    }
    
    /**
     * Obtiene el nombre del equipo en mayúsculas
     * 
     * @return El nombre del equipo en mayúsculas
     */
    public String toUpperCase() {
        return name.toUpperCase();
    }
    
    /**
     * Obtiene la longitud del nombre
     * 
     * @return La longitud del nombre
     */
    public int length() {
        return name.length();
    }
    
    /**
     * Verifica si el nombre comienza con el prefijo dado
     * 
     * @param prefix El prefijo a verificar
     * @return true si el nombre comienza con el prefijo
     */
    public boolean startsWith(String prefix) {
        return name.startsWith(prefix);
    }
    
    /**
     * Verifica si el nombre termina con el sufijo dado
     * 
     * @param suffix El sufijo a verificar
     * @return true si el nombre termina con el sufijo
     */
    public boolean endsWith(String suffix) {
        return name.endsWith(suffix);
    }
    
    /**
     * Verifica si el nombre contiene el texto dado (ignorando mayúsculas/minúsculas)
     * 
     * @param text El texto a buscar
     * @return true si el nombre contiene el texto
     */
    public boolean containsIgnoreCase(String text) {
        return name.toLowerCase().contains(text.toLowerCase());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamName teamName = (TeamName) o;
        return Objects.equals(name, teamName.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
