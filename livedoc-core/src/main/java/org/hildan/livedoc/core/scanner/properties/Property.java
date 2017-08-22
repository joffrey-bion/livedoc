package org.hildan.livedoc.core.scanner.properties;

import java.lang.reflect.Type;
import java.util.Objects;

public class Property {

    private final String name;

    private final Type type;

    public Property(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Property property = (Property) o;
        return Objects.equals(name, property.name) && Objects.equals(type, property.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + " (" + type + ')';
    }
}
