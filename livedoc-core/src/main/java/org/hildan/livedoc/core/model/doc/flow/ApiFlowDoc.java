package org.hildan.livedoc.core.model.doc.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.readers.combined.SpecialDefaultStringValue;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.groups.Groupable;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiFlowDoc implements Comparable<ApiFlowDoc>, Groupable {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private List<String> preconditions = new ArrayList<>();

    private List<ApiFlowStepDoc> steps = new ArrayList<>();

    private List<ApiOperationDoc> operations = new ArrayList<>();

    @SpecialDefaultStringValue("")
    private String group = "";

    public static ApiFlowDoc buildFromAnnotation(ApiFlow annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        ApiFlowDoc apiFlowDoc = new ApiFlowDoc();
        apiFlowDoc.setName(annotation.name());
        apiFlowDoc.setDescription(nullifyIfEmpty(annotation.description()));
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
