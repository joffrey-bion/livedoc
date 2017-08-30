package org.hildan.livedoc.core.pojo.flow;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Groupable;

public class ApiFlowDoc implements Comparable<ApiFlowDoc>, Groupable {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private List<String> preconditions;

    private List<ApiFlowStepDoc> steps;

    private List<ApiMethodDoc> methods;

    private String group;

    public static ApiFlowDoc buildFromAnnotation(ApiFlow annotation,
            Map<String, ? extends ApiMethodDoc> apiMethodDocsById) {
        ApiFlowDoc apiFlowDoc = new ApiFlowDoc();
        apiFlowDoc.setDescription(annotation.description());
        apiFlowDoc.setName(annotation.name());
        apiFlowDoc.setGroup(annotation.group());
        for (String precondition : annotation.preconditions()) {
            apiFlowDoc.addPrecondition(precondition);
        }
        for (ApiFlowStep apiFlowStep : annotation.steps()) {
            ApiFlowStepDoc apiFlowStepDoc = ApiFlowStepDoc.buildFromAnnotation(apiFlowStep, apiMethodDocsById);
            apiFlowDoc.addStep(apiFlowStepDoc);
            apiFlowDoc.addMethod(apiFlowStepDoc.getApimethoddoc());
        }
        return apiFlowDoc;
    }

    public ApiFlowDoc() {
        this.preconditions = new LinkedList<>();
        this.steps = new LinkedList<>();
        this.methods = new LinkedList<>();
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

    @Override
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void addStep(ApiFlowStepDoc apiFlowStepDoc) {
        this.steps.add(apiFlowStepDoc);
    }

    public List<ApiFlowStepDoc> getSteps() {
        return steps;
    }

    public void setSteps(List<ApiFlowStepDoc> steps) {
        this.steps = steps;
    }

    public List<String> getPreconditions() {
        return preconditions;
    }

    public void setPreconditions(List<String> preconditions) {
        this.preconditions = preconditions;
    }

    public void addPrecondition(String precondition) {
        this.preconditions.add(precondition);
    }

    public List<ApiMethodDoc> getMethods() {
        return methods;
    }

    public void setMethods(List<ApiMethodDoc> methods) {
        this.methods = methods;
    }

    public void addMethod(ApiMethodDoc method) {
        this.methods.add(method);
    }

    @Override
    public int compareTo(ApiFlowDoc o) {
        return name.compareTo(o.getName());
    }
}
