package org.hildan.livedoc.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultsTest {

    @Test
    public void defaultValueFor_primitives() {
        assertEquals(0, Defaults.defaultValueFor(byte.class));
        assertEquals(0, Defaults.defaultValueFor(short.class));
        assertEquals(0, Defaults.defaultValueFor(int.class));
        assertEquals(0L, Defaults.defaultValueFor(long.class));
        assertEquals(0f, Defaults.defaultValueFor(float.class));
        assertEquals(0.0, Defaults.defaultValueFor(double.class));
        assertEquals(false, Defaults.defaultValueFor(boolean.class));
        assertEquals('\0', Defaults.defaultValueFor(char.class));
    }

    @Test
    public void defaultValueFor_objects() {
        assertNull(Defaults.defaultValueFor(Integer.class));
        assertNull(Defaults.defaultValueFor(Double.class));
        assertNull(Defaults.defaultValueFor(Boolean.class));
        assertNull(Defaults.defaultValueFor(String.class));
    }
}
