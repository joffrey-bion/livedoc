package org.hildan.livedoc.core.builders.doc;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.util.BeanUtils;

public class ApiDocReader {

    public static ApiDoc read(Class<?> controller) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setName(controller.getSimpleName());
        apiDoc.setSupportedVersions(ApiVersionDocReader.read(controller));
        apiDoc.setAuth(ApiAuthDocReader.read(controller));
        apiDoc.setStage(ApiStageReader.read(controller));
        apiDoc.setVisibility(ApiVisibilityReader.read(controller));

        Api api = controller.getAnnotation(Api.class);
        if (api != null) {
            apiDoc.setName(BeanUtils.maybeOverridden(api.name(), apiDoc.getName()));
            apiDoc.setDescription(api.description());
            apiDoc.setGroup(api.group());
        }
        return apiDoc;
    }
}
