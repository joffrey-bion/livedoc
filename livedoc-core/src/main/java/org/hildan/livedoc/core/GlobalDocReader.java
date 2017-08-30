package org.hildan.livedoc.core;

import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.flow.ApiFlowDoc;
import org.hildan.livedoc.core.pojo.global.ApiGlobalDoc;

public interface GlobalDocReader {

    Set<ApiFlowDoc> getApiFlowDocs(Map<String, ? extends ApiMethodDoc> apiMethodDocsById);

    ApiGlobalDoc getApiGlobalDoc();
}
