package org.hildan.livedoc.core.builders.doc;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.util.BeanUtils;

public class ApiDocReader {

    public static ApiDoc read(Class<?> controller) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setName(controller.getSimpleName());
        apiDoc.setSupportedversions(ApiVersionDocReader.read(controller));
        apiDoc.setAuth(ApiAuthDocReader.read(controller));

        Api api = controller.getAnnotation(Api.class);
        if (api != null) {
            apiDoc.setName(BeanUtils.maybeOverridden(api.name(), apiDoc.getName()));
            apiDoc.setDescription(api.description());
            apiDoc.setGroup(api.group());
            apiDoc.setVisibility(api.visibility());
            apiDoc.setStage(api.stage());
        }
        return apiDoc;
    }

}
