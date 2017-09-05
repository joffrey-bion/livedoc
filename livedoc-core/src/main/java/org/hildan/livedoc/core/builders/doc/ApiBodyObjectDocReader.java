package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.annotations.ApiBodyObject;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiBodyObjectDoc;
import org.hildan.livedoc.core.util.LivedocUtils;

public class ApiBodyObjectDocReader {

    public static ApiBodyObjectDoc read(Method method) {
        if (method.isAnnotationPresent(ApiBodyObject.class)) {
            ApiBodyObject annotation = method.getAnnotation(ApiBodyObject.class);
            return new ApiBodyObjectDoc(LivedocTypeBuilder.build(annotation.clazz(), annotation.clazz()));
        }

        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiBodyObject.class);
        if (index != -1) {
            Class<?> clazz = method.getParameterTypes()[index];
            Type type = method.getGenericParameterTypes()[index];
            return new ApiBodyObjectDoc(LivedocTypeBuilder.build(clazz, type));
        }

        return null;
    }

}
