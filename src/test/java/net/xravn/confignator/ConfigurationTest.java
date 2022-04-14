package net.xravn.confignator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import net.xravn.confignator.exceptions.NodeNotFoundException;

public class ConfigurationTest {

    @Test
    public void testThrowNodeNotfound() {
        Configuration configuration = new Configuration("default");
        assertThrows(NodeNotFoundException.class, () -> configuration.set("keyNotExisting", "value"));
    }

}
