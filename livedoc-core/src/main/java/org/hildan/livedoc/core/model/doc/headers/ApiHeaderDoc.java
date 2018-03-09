package org.hildan.livedoc.core.model.doc.headers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.hildan.livedoc.core.merger.DocMerger;
import org.hildan.livedoc.core.merger.Mergeable;

public class ApiHeaderDoc implements Mergeable<ApiHeaderDoc> {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private HeaderFilterType type;

    private List<String> values;

    private String defaultValue;

    private ApiHeaderDoc() {
    }

    private ApiHeaderDoc(String name, String description, HeaderFilterType type, List<String> values,
            String defaultValue) {
        this.name = name;
        this.description = description;
        this.values = values;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public static ApiHeaderDoc required(String name, String description) {
        return new ApiHeaderDoc(name, description, HeaderFilterType.REQUIRED_MATCHING, Collections.singletonList("*"),
                null);
    }

    public static ApiHeaderDoc optional(String name, String description, String defaultValue) {
        return new ApiHeaderDoc(name, description, HeaderFilterType.OPTIONAL, Collections.emptyList(), defaultValue);
    }

    public static ApiHeaderDoc forbidden(String name, String description) {
        return new ApiHeaderDoc(name, description, HeaderFilterType.FORBIDDEN, Collections.emptyList(), null);
    }

    public static ApiHeaderDoc differentFrom(String name, String description, String forbiddenValue) {
        return new ApiHeaderDoc(name, description, HeaderFilterType.DIFFERENT,
                Collections.singletonList(forbiddenValue), null);
    }

    public static ApiHeaderDoc matching(String name, String description, String format) {
        return new ApiHeaderDoc(name, description, HeaderFilterType.REQUIRED_MATCHING,
                Collections.singletonList(format), null);
    }

    public static ApiHeaderDoc oneOf(String name, String description, List<String> allowedValues) {
        return new ApiHeaderDoc(name, description, HeaderFilterType.REQUIRED_MATCHING, allowedValues, null);
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

    public String getDefaultValue() {
        return defaultValue;
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

    @Override
    public ApiHeaderDoc merge(ApiHeaderDoc override, DocMerger merger) {
        ApiHeaderDoc merged = merger.mergeProperties(this, override, new ApiHeaderDoc());
        merged.values = merger.mergeList(this.values, override.values, s -> s);
        return merged;
    }
}
