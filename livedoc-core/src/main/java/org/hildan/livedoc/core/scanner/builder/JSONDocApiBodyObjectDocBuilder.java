package org.hildan.livedoc.core.scanner.builder;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotation.ApiBodyObject;
import org.hildan.livedoc.core.pojo.ApiBodyObjectDoc;
import org.hildan.livedoc.core.util.JSONDocType;
import org.hildan.livedoc.core.util.JSONDocTypeBuilder;
import org.hildan.livedoc.core.util.JSONDocUtils;

public class JSONDocApiBodyObjectDocBuilder {

    public static ApiBodyObjectDoc build(Method method) {
        if (method.isAnnotationPresent(ApiBodyObject.class)) {
            ApiBodyObjectDoc apiBodyObjectDoc = new ApiBodyObjectDoc(
                    JSONDocTypeBuilder.build(new JSONDocType(), method.getAnnotation(ApiBodyObject.class).clazz(),
                            method.getAnnotation(ApiBodyObject.class).clazz()));
            return apiBodyObjectDoc;
        }

        Integer index = JSONDocUtils.getIndexOfParameterWithAnnotation(method, ApiBodyObject.class);
        if (index != -1) {
            ApiBodyObjectDoc apiBodyObjectDoc = new ApiBodyObjectDoc(
                    JSONDocTypeBuilder.build(new JSONDocType(), method.getParameterTypes()[index],
                            method.getGenericParameterTypes()[index]));
            return apiBodyObjectDoc;
        }

        return null;
    }

}
