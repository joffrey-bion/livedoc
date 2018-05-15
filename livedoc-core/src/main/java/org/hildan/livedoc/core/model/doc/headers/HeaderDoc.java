package org.hildan.livedoc.core.model.doc.headers;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;

public class HeaderDoc implements Mergeable<HeaderDoc> {

    private String name;

    private String description;

    private HeaderFilterType type;

    private List<String> values;

    private String defaultValue;

    private HeaderDoc() {
    }

    private HeaderDoc(String name, String description, HeaderFilterType type, List<String> values,
            String defaultValue) {
        this.name = name;
        this.description = description;
        this.values = values;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public static HeaderDoc required(String name, String description) {
        return new HeaderDoc(name, description, HeaderFilterType.REQUIRED_MATCHING, Collections.singletonList("*"),
                null);
    }

    public static HeaderDoc optional(String name, String description, String defaultValue) {
        return new HeaderDoc(name, description, HeaderFilterType.OPTIONAL, Collections.emptyList(), defaultValue);
    }

    public static HeaderDoc forbidden(String name, String description) {
        return new HeaderDoc(name, description, HeaderFilterType.FORBIDDEN, Collections.emptyList(), null);
    }

    public static HeaderDoc differentFrom(String name, String description, String forbiddenValue) {
        return new HeaderDoc(name, description, HeaderFilterType.DIFFERENT, Collections.singletonList(forbiddenValue),
                null);
    }

    public static HeaderDoc matching(String name, String description, String format) {
        return new HeaderDoc(name, description, HeaderFilterType.REQUIRED_MATCHING, Collections.singletonList(format),
                null);
    }

    public static HeaderDoc oneOf(String name, String description, List<String> allowedValues) {
        return new HeaderDoc(name, description, HeaderFilterType.REQUIRED_MATCHING, allowedValues, null);
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

        HeaderDoc that = (HeaderDoc) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public HeaderDoc merge(HeaderDoc override, DocMerger merger) {
        HeaderDoc merged = merger.mergeProperties(this, override, new HeaderDoc());
        merged.values = merger.mergeAndSort(this.values, override.values, s -> s);
        return merged;
    }
}
