package org.hildan.livedoc.core.pojo.flow;

import java.util.Map;
import java.util.UUID;

import org.hildan.livedoc.core.annotations.flow.ApiFlowStep;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;

import com.google.common.collect.Sets;

public class ApiFlowStepDoc {

    private static final String ERROR_MISSING_METHOD_ID = "No method found with id: %s";

    private static final String HINT_MISSING_METHOD_ID = "Add the same id to both ApiMethod and ApiFlowStep";

    public final String livedocId = UUID.randomUUID().toString();

    private String apimethodid;

    private ApiMethodDoc apimethoddoc;

    static ApiFlowStepDoc buildFromAnnotation(ApiFlowStep annotation,
            Map<String, ? extends ApiMethodDoc> apiMethodDocsById) {
        ApiFlowStepDoc apiFlowStepDoc = new ApiFlowStepDoc();
        apiFlowStepDoc.setApimethodid(annotation.apimethodid());
        apiFlowStepDoc.setApimethoddoc(getApiMethodDoc(annotation, apiMethodDocsById));
        return apiFlowStepDoc;
    }

    private static ApiMethodDoc getApiMethodDoc(ApiFlowStep annotation,
            Map<String, ? extends ApiMethodDoc> apiMethodDocsById) {
        ApiMethodDoc apiMethodDoc = apiMethodDocsById.get(annotation.apimethodid());
        if (apiMethodDoc == null) {
            apiMethodDoc = new ApiMethodDoc();
            apiMethodDoc.setPath(Sets.newHashSet(String.format(ERROR_MISSING_METHOD_ID, annotation.apimethodid())));
            apiMethodDoc.addJsondocerror(String.format(ERROR_MISSING_METHOD_ID, annotation.apimethodid()));
            apiMethodDoc.addJsondochint(HINT_MISSING_METHOD_ID);
        }
        return apiMethodDoc;
    }

    public String getApimethodid() {
        return apimethodid;
    }

    public void setApimethodid(String apimethodid) {
        this.apimethodid = apimethodid;
    }

    public ApiMethodDoc getApimethoddoc() {
        return apimethoddoc;
    }

    public void setApimethoddoc(ApiMethodDoc apimethoddoc) {
        this.apimethoddoc = apimethoddoc;
    }

}
