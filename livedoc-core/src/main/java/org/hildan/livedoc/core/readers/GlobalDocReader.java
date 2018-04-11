package org.hildan.livedoc.core.readers;

import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.flow.ApiFlowDoc;
import org.hildan.livedoc.core.model.doc.global.ApiGlobalDoc;
import org.jetbrains.annotations.NotNull;

/**
 * A component able to build the global documentation of an API (general information, flows, migrations).
 */
public interface GlobalDocReader {

    @NotNull
    ApiGlobalDoc getApiGlobalDoc();

    @NotNull
    Set<ApiFlowDoc> getApiFlowDocs(Map<String, ? extends ApiOperationDoc> apiOperationDocsById);
}
