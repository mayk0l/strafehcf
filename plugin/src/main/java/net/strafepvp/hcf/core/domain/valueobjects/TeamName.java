package net.strafepvp.hcf.core.domain.valueobjects;

/**
 * Value Object que representa el nombre de un equipo
 * 
 * @param value El nombre del equipo
 */
public record TeamName(String value) {
    
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 16;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9_]+$";
    
    public TeamName {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        
        String trimmed = value.trim();
        
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Team name must be at least " + MIN_LENGTH + " characters");
        }
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Team name cannot exceed " + MAX_LENGTH + " characters");
        }
        
        if (!trimmed.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException("Team name can only contain letters, numbers, and underscores");
        }
        
        // Reassign the trimmed value
        value = trimmed;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
