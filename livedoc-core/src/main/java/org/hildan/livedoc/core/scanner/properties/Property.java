package org.hildan.livedoc.core.scanner.properties;

import java.lang.reflect.Type;
import java.util.Objects;

public class Property {

    private final String name;

    private final Type type;

    private final Boolean required;

    public Property(String name, Type type) {
        this(name, type, null);
    }

    public Property(String name, Type type, Boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Boolean isRequired() {
        return required;
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
