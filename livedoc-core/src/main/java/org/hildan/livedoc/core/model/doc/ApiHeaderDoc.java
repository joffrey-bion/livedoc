package org.hildan.livedoc.core.model.doc;

import java.util.UUID;

public class ApiHeaderDoc {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private String[] allowedValues;

    public ApiHeaderDoc(String name, String description, String[] allowedValues) {
        this.name = name;
        this.description = description;
        this.allowedValues = allowedValues;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApiHeaderDoc other = (ApiHeaderDoc) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
