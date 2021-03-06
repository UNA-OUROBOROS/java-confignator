package net.xravn.confignator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import net.xravn.confignator.exceptions.NodeNotFoundException;

public class ConfigurationTest {

    @Test
    public void testThrowNodeNotfound() {
        Configuration configuration = new Configuration("default");
        assertThrows(NodeNotFoundException.class, () -> configuration.set("keyNotExisting", "value"));
    }

    @Test
    void testCreateNewNode() {
        Configuration configuration = new Configuration("default");
        configuration.addValue("key", 2);
        int value = configuration.get("key");
        assertEquals(value, 2);
    }

    @Test
    void testCreateNewChildNode() {
        Configuration configuration = new Configuration("default");
        configuration.addValue("key.child", 3);
        int value = configuration.get("key.child");
        assertNull(configuration.get("key"));
        assertEquals(value, 3);
    }

}
