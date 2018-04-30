package org.hildan.livedoc.core.model.doc.types;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hildan.livedoc.core.model.doc.AbstractDoc;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.combined.DocMerger;
import org.hildan.livedoc.core.readers.combined.Mergeable;
import org.hildan.livedoc.core.readers.combined.SpecialDefaultIntValue;

import com.google.common.base.Joiner;

public class PropertyDoc extends AbstractDoc implements Comparable<PropertyDoc>, Versioned, Mergeable<PropertyDoc> {

    public final String livedocId = UUID.randomUUID().toString();

    private LivedocType type;

    private String name;

    private String description;

    private Set<String> format;

    private String[] allowedValues;

    private String required;

    private VersionDoc supportedVersions;

    @SpecialDefaultIntValue(Integer.MAX_VALUE)
    private Integer order;

    public PropertyDoc() {
        this.format = new LinkedHashSet<>();
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    public Set<String> getFormat() {
        return format;
    }

    public String getDisplayedFormat() {
        return Joiner.on(", ").join(format);
    }

    public void setFormat(Set<String> format) {
        this.format = format;
    }

    public void addFormat(String format) {
        this.format.add(format);
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

    public VersionDoc getSupportedVersions() {
        return supportedVersions;
    }

    public void setSupportedVersions(VersionDoc supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

    public LivedocType getType() {
        return type;
    }

    public void setType(LivedocType type) {
        this.type = type;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    @Override
    public PropertyDoc merge(PropertyDoc override, DocMerger merger) {
        return merger.mergeProperties(this, override, new PropertyDoc());
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
        PropertyDoc other = (PropertyDoc) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * This comparison is the same as the one in PropertyDoc class
     */
    @Override
    public int compareTo(PropertyDoc o) {
        if (this.getOrder().equals(o.getOrder())) {
            return this.getName().compareTo(o.getName());
        } else {
            return this.getOrder() - o.getOrder();
        }
    }

}
