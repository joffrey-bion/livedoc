package org.hildan.livedoc.core.model.doc.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlow;
import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.groups.Groupable;
import org.hildan.livedoc.core.readers.combined.SpecialDefaultStringValue;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class FlowDoc implements Comparable<FlowDoc>, Groupable {

    public final String livedocId = UUID.randomUUID().toString();

    private String name;

    private String description;

    private List<String> preconditions = new ArrayList<>();

    private List<FlowStepDoc> steps = new ArrayList<>();

    private List<ApiOperationDoc> operations = new ArrayList<>();

    @SpecialDefaultStringValue("")
    private String group = "";

    public static FlowDoc buildFromAnnotation(ApiFlow annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        FlowDoc flowDoc = new FlowDoc();
        flowDoc.setName(annotation.name());
        flowDoc.setDescription(nullifyIfEmpty(annotation.description()));
        flowDoc.setGroup(annotation.group());
        for (String precondition : annotation.preconditions()) {
            flowDoc.addPrecondition(precondition);
        }
        for (ApiFlowStep apiFlowStep : annotation.steps()) {
            FlowStepDoc flowStepDoc = FlowStepDoc.buildFromAnnotation(apiFlowStep, apiOperationDocsById);
            flowDoc.addStep(flowStepDoc);
            flowDoc.addMethod(flowStepDoc.getApiOperationDoc());
        }
        return flowDoc;
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

    public void addStep(FlowStepDoc flowStepDoc) {
        this.steps.add(flowStepDoc);
    }

    public List<FlowStepDoc> getSteps() {
        return steps;
    }

    public void setSteps(List<FlowStepDoc> steps) {
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
    public int compareTo(FlowDoc o) {
        return name.compareTo(o.getName());
    }
}
