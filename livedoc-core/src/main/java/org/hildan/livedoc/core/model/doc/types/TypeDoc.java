package org.hildan.livedoc.core.model.doc.types;

import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.model.doc.AbstractDoc;
import org.hildan.livedoc.core.model.doc.Stage;
import org.hildan.livedoc.core.model.doc.Staged;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.readers.LivedocIdGenerator;

public class TypeDoc extends AbstractDoc implements Comparable<TypeDoc>, Groupable, Staged, Versioned {

    private final String livedocId;

    private String name;

    private String description;

    private List<PropertyDoc> fields;

    private String[] allowedValues;

    private String group;

    private Stage stage;

    private VersionDoc supportedVersions;

    private Object template;

    private boolean show;

    public TypeDoc(Class<?> clazz) {
        this.livedocId = LivedocIdGenerator.getTypeId(clazz);
        this.name = null;
        this.description = null;
        this.supportedVersions = null;
        this.allowedValues = new String[] {};
        this.fields = new ArrayList<>();
        this.group = "";
        this.stage = null;
        this.show = true;
    }

    public String getLivedocId() {
        return livedocId;
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

    public List<PropertyDoc> getFields() {
        return fields;
    }

    public void setFields(List<PropertyDoc> fields) {
        this.fields = fields;
    }

    public String[] getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(String[] allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Object getTemplate() {
        return template;
    }

    public void setTemplate(Object template) {
        this.template = template;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public VersionDoc getSupportedVersions() {
        return supportedVersions;
    }

    @Override
    public void setSupportedVersions(VersionDoc supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

    @Override
    public int compareTo(TypeDoc o) {
        return name.compareTo(o.getName());
    }

}
