package net.strafepvp.hcf.core.domain.valueobjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para TeamName value object
 * 
 * @author mayk0l
 */
class TeamNameTest {
    
    @Test
    @DisplayName("Should create valid team name")
    void testValidTeamName() {
        // Given
        String validName = "TestTeam";
        
        // When
        TeamName teamName = new TeamName(validName);
        
        // Then
        assertEquals(validName, teamName.value());
        assertEquals(validName, teamName.toString());
    }
    
    @Test
    @DisplayName("Should trim whitespace from team name")
    void testTeamNameTrimming() {
        // Given
        String nameWithWhitespace = "  TestTeam  ";
        String expectedName = "TestTeam";
        
        // When
        TeamName teamName = new TeamName(nameWithWhitespace);
        
        // Then
        assertEquals(expectedName, teamName.value());
    }
    
    @Test
    @DisplayName("Should reject null team name")
    void testNullTeamName() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> new TeamName(null));
    }
    
    @Test
    @DisplayName("Should reject empty team name")
    void testEmptyTeamName() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> new TeamName(""));
        assertThrows(IllegalArgumentException.class, () -> new TeamName("   "));
    }
    
    @Test
    @DisplayName("Should reject team name that is too short")
    void testTeamNameTooShort() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> new TeamName("AB"));
    }
    
    @Test
    @DisplayName("Should reject team name that is too long")
    void testTeamNameTooLong() {
        // Given
        String longName = "ThisTeamNameIsTooLongForMinecraft";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new TeamName(longName));
    }
    
    @Test
    @DisplayName("Should reject team name with invalid characters")
    void testTeamNameInvalidCharacters() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> new TeamName("Team Name")); // space
        assertThrows(IllegalArgumentException.class, () -> new TeamName("Team-Name")); // hyphen
        assertThrows(IllegalArgumentException.class, () -> new TeamName("Team@Name")); // special char
        assertThrows(IllegalArgumentException.class, () -> new TeamName("Team.Name")); // dot
    }
    
    @Test
    @DisplayName("Should accept team name with valid characters")
    void testTeamNameValidCharacters() {
        // Given & When & Then
        assertDoesNotThrow(() -> new TeamName("TeamName123"));
        assertDoesNotThrow(() -> new TeamName("Team_Name"));
        assertDoesNotThrow(() -> new TeamName("UPPERCASE"));
        assertDoesNotThrow(() -> new TeamName("lowercase"));
        assertDoesNotThrow(() -> new TeamName("MiXeDcAsE"));
        assertDoesNotThrow(() -> new TeamName("Team123"));
        assertDoesNotThrow(() -> new TeamName("123Team"));
    }
}
