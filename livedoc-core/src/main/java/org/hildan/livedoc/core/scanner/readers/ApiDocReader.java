package org.hildan.livedoc.core.scanner.readers;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.pojo.ApiDoc;

public class ApiDocReader {

    public static ApiDoc read(Class<?> controller) {
        Api api = controller.getAnnotation(Api.class);
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setDescription(api.description());
        apiDoc.setName(api.name());
        apiDoc.setGroup(api.group());
        apiDoc.setVisibility(api.visibility());
        apiDoc.setStage(api.stage());
        return apiDoc;
    }

}
