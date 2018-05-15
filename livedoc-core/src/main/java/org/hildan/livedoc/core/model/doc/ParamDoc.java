package org.hildan.livedoc.core.model.doc;

import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;

public class ParamDoc extends AbstractDoc implements Comparable<ParamDoc>, Mergeable<ParamDoc> {

    private LivedocType type;

    private String name;

    private String description;

    private String required;

    private String[] allowedValues;

    private String format;

    private String defaultValue;

    private ParamDoc() {
    }

    public ParamDoc(String name, String description, LivedocType type, String required, String[] allowedValues,
            String format, String defaultValue) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.allowedValues = allowedValues;
        this.format = format;
        this.defaultValue = defaultValue;
    }

    public LivedocType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public ParamDoc merge(ParamDoc override, DocMerger merger) {
        return merger.mergeProperties(this, override, new ParamDoc());
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
        ParamDoc other = (ParamDoc) obj;
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
    public int compareTo(ParamDoc o) {
        return this.name.compareTo(o.getName());
    }

}
