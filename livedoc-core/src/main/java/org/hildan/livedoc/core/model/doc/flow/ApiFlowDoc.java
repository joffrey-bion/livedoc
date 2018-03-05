package org.hildan.livedoc.core.model.doc.flow;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.groups.Groupable;

public class ApiFlowDoc implements Comparable<ApiFlowDoc>, Groupable {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private List<String> preconditions;

    private List<ApiFlowStepDoc> steps;

    private List<ApiOperationDoc> operations;

    private String group;

    public ApiFlowDoc() {
        this.preconditions = new LinkedList<>();
        this.steps = new LinkedList<>();
        this.operations = new LinkedList<>();
    }

    public static ApiFlowDoc buildFromAnnotation(ApiFlow annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        ApiFlowDoc apiFlowDoc = new ApiFlowDoc();
        apiFlowDoc.setDescription(annotation.description());
        apiFlowDoc.setName(annotation.name());
        apiFlowDoc.setGroup(annotation.group());
        for (String precondition : annotation.preconditions()) {
            apiFlowDoc.addPrecondition(precondition);
        }
        for (ApiFlowStep apiFlowStep : annotation.steps()) {
            ApiFlowStepDoc apiFlowStepDoc = ApiFlowStepDoc.buildFromAnnotation(apiFlowStep, apiOperationDocsById);
            apiFlowDoc.addStep(apiFlowStepDoc);
            apiFlowDoc.addMethod(apiFlowStepDoc.getApiOperationDoc());
        }
        return apiFlowDoc;
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

    public List<ApiOperationDoc> getOperations() {
        return operations;
    }

    public void setOperations(List<ApiOperationDoc> operations) {
        this.operations = operations;
    }

    public void addMethod(ApiOperationDoc method) {
        this.operations.add(method);
    }

    @Override
    public int compareTo(ApiFlowDoc o) {
        return name.compareTo(o.getName());
    }
}
