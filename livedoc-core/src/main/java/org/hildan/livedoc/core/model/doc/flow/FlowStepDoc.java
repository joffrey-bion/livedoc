package org.hildan.livedoc.core.model.doc.flow;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;

public class FlowStepDoc {

    private static final String ERROR_MISSING_METHOD_ID = "No method found with id: %s";

    private static final String HINT_MISSING_METHOD_ID = "Add the same id to both ApiOperation and ApiFlowStep";

    public final String livedocId = UUID.randomUUID().toString();

    private String apiOperationId;

    private ApiOperationDoc apiOperationDoc;

    static FlowStepDoc buildFromAnnotation(ApiFlowStep annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        FlowStepDoc flowStepDoc = new FlowStepDoc();
        flowStepDoc.setApiOperationId(annotation.apiOperationId());
        flowStepDoc.setApiOperationDoc(getApiOperationDoc(annotation, apiOperationDocsById));
        return flowStepDoc;
    }

    private static ApiOperationDoc getApiOperationDoc(ApiFlowStep annotation,
            Map<String, ? extends ApiOperationDoc> apiOperationDocsById) {
        ApiOperationDoc apiOperationDoc = apiOperationDocsById.get(annotation.apiOperationId());
        if (apiOperationDoc == null) {
            apiOperationDoc = new ApiOperationDoc();
            String missingIdMsg = String.format(ERROR_MISSING_METHOD_ID, annotation.apiOperationId());
            apiOperationDoc.setPaths(Collections.singletonList(missingIdMsg));
            apiOperationDoc.addJsondocError(missingIdMsg);
            apiOperationDoc.addJsondocHint(HINT_MISSING_METHOD_ID);
        }
        return apiOperationDoc;
    }

    public String getApiOperationId() {
        return apiOperationId;
    }

    public void setApiOperationId(String apiOperationId) {
        this.apiOperationId = apiOperationId;
    }

    public ApiOperationDoc getApiOperationDoc() {
        return apiOperationDoc;
    }

    public void setApiOperationDoc(ApiOperationDoc apiOperationDoc) {
        this.apiOperationDoc = apiOperationDoc;
    }

}
