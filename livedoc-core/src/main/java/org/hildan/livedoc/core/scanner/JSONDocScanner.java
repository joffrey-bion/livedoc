package org.hildan.livedoc.core.scanner;

import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.JSONDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.pojo.flow.ApiFlowDoc;
import org.hildan.livedoc.core.pojo.global.ApiGlobalDoc;

public interface JSONDocScanner {

    JSONDoc getJSONDoc(String version, String basePath, List<String> packages, boolean playgroundEnabled,
            MethodDisplay methodDisplay);

    Set<ApiDoc> getApiDocs(Set<Class<?>> classes, MethodDisplay displayMethodAs);

    Set<ApiObjectDoc> getApiObjectDocs(Set<Class<?>> classes);

    Set<ApiFlowDoc> getApiFlowDocs(Set<Class<?>> classes, List<ApiMethodDoc> apiMethodDocs);

    ApiGlobalDoc getApiGlobalDoc(Set<Class<?>> global, Set<Class<?>> changelogs, Set<Class<?>> migrations);

}
