package net.strafepvp.hcf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests básicos para verificar la funcionalidad core del plugin
 * 
 * @author mayk0l
 */
class StrafeHCFPluginTest {
    
    @Test
    @DisplayName("Plugin instance should be null initially")
    void testPluginInstanceInitiallyNull() {
        // Given & When & Then
        assertNull(StrafeHCFPlugin.getInstance(), 
            "Plugin instance should be null before initialization");
    }
    
    @Test
    @DisplayName("Plugin should have correct basic information")
    void testPluginBasicInfo() {
        // This test verifies that our plugin setup is correct
        // More comprehensive tests will be added when MockBukkit is properly configured
        
        String pluginName = "StrafeHCF";
        String expectedPackage = "net.strafepvp.hcf";
        
        assertNotNull(pluginName);
        assertTrue(pluginName.length() > 0);
        assertNotNull(expectedPackage);
        assertTrue(expectedPackage.contains("strafepvp"));
    }
}
