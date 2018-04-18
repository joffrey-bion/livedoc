package org.hildan.livedoc.core.model.doc.headers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiHeaderDoc that = (ApiHeaderDoc) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public ApiHeaderDoc merge(ApiHeaderDoc override, DocMerger merger) {
        ApiHeaderDoc merged = merger.mergeProperties(this, override, new ApiHeaderDoc());
        merged.values = merger.mergeAndSort(this.values, override.values, s -> s);
        return merged;
    }
}
