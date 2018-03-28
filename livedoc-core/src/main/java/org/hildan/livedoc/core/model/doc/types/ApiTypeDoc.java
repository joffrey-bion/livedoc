package org.hildan.livedoc.core.model.doc.types;

import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.model.doc.AbstractDoc;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.model.doc.Stage;
import org.hildan.livedoc.core.model.doc.Staged;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.doc.version.Versioned;
import org.hildan.livedoc.core.util.LivedocUtils;

public class ApiTypeDoc extends AbstractDoc implements Comparable<ApiTypeDoc>, Groupable, Staged, Versioned {

    private final String livedocId;

    private String name;

    private String description;

    private Set<ApiPropertyDoc> fields;

    private String[] allowedValues;

    private String group;

    private Stage stage;

    private ApiVersionDoc supportedVersions;

    private Object template;

    private boolean show;

    public ApiTypeDoc(Class<?> clazz) {
        this.livedocId = LivedocUtils.getLivedocId(clazz);
        this.name = null;
        this.description = null;
        this.supportedVersions = null;
        this.allowedValues = new String[] {};
        this.fields = new TreeSet<>();
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

    public Set<ApiPropertyDoc> getFields() {
        return fields;
    }

    public void setFields(Set<ApiPropertyDoc> fields) {
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
    public ApiVersionDoc getSupportedVersions() {
        return supportedVersions;
    }

    @Override
    public void setSupportedVersions(ApiVersionDoc supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

    @Override
    public int compareTo(ApiTypeDoc o) {
        return name.compareTo(o.getName());
    }

}
