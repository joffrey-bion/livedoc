package org.hildan.livedoc.core.model.doc.headers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ApiHeaderDoc {

    public final String livedocId = UUID.randomUUID().toString();

    private final String name;

    private final String description;

    private final HeaderFilterType type;

    private final List<String> values;

    private ApiHeaderDoc(String name, String description, List<String> values, HeaderFilterType type) {
        this.name = name;
        this.description = description;
        this.values = values;
        this.type = type;
    }

    public static ApiHeaderDoc required(String name, String description) {
        return new ApiHeaderDoc(name, description, Collections.singletonList("*"), HeaderFilterType.REQUIRED_MATCHING);
    }

    public static ApiHeaderDoc optional(String name, String description, String defaultValue) {
        return new ApiHeaderDoc(name, description, Collections.singletonList(defaultValue), HeaderFilterType.OPTIONAL);
    }

    public static ApiHeaderDoc forbidden(String name, String description) {
        return new ApiHeaderDoc(name, description, Collections.emptyList(), HeaderFilterType.FORBIDDEN);
    }

    public static ApiHeaderDoc differentFrom(String name, String description, String forbiddenValue) {
        return new ApiHeaderDoc(name, description, Collections.singletonList(forbiddenValue), HeaderFilterType.DIFFERENT);
    }

    public static ApiHeaderDoc matching(String name, String description, String format) {
        return new ApiHeaderDoc(name, description, Collections.singletonList(format), HeaderFilterType.REQUIRED_MATCHING);
    }

    public static ApiHeaderDoc oneOf(String name, String description, List<String> allowedValues) {
        return new ApiHeaderDoc(name, description, allowedValues, HeaderFilterType.REQUIRED_MATCHING);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HeaderFilterType getType() {
        return type;
    }

    public List<String> getValues() {
        return values;
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
