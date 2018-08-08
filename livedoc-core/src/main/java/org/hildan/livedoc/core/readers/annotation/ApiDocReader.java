package org.hildan.livedoc.core.readers.annotation;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.readers.LivedocIdGenerator;
import org.jetbrains.annotations.Nullable;

public class ApiDocReader {

    public static ApiDoc read(Class<?> controller) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setLivedocId(LivedocIdGenerator.defaultApiId(controller));
        apiDoc.setName(controller.getSimpleName());
        apiDoc.setSupportedVersions(ApiVersionDocReader.read(controller));
        apiDoc.setAuth(ApiAuthDocReader.readController(controller));
        apiDoc.setStage(ApiStageReader.read(controller));

        Api api = controller.getAnnotation(Api.class);
        if (api != null) {
            String id = nullifyIfEmpty(api.id());
            if (id != null) {
                apiDoc.setLivedocId(id);
            }
            String name = nullifyIfEmpty(api.name());
            if (name != null) {
                apiDoc.setName(name);
            }
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
