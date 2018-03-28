package org.hildan.livedoc.core.readers.annotation;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.util.BeanUtils;
import org.jetbrains.annotations.Nullable;

public class ApiDocReader {

    public static ApiDoc read(Class<?> controller) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setName(controller.getSimpleName());
        apiDoc.setSupportedVersions(ApiVersionDocReader.read(controller));
        apiDoc.setAuth(ApiAuthDocReader.readController(controller));
        apiDoc.setStage(ApiStageReader.read(controller));

        Api api = controller.getAnnotation(Api.class);
        if (api != null) {
            apiDoc.setName(BeanUtils.maybeOverridden(nullifyIfEmpty(api.name()), apiDoc.getName()));
            apiDoc.setDescription(nullifyIfEmpty(api.description()));
            apiDoc.setGroup(api.group());
        }
        return apiDoc;
    }

    @Nullable
    public static String nullifyIfEmpty(String value) {
        return value.isEmpty() ? null : value;
    }
}
