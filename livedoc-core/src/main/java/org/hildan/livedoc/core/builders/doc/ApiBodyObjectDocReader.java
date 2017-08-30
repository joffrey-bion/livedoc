package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotations.ApiBodyObject;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiBodyObjectDoc;
import org.hildan.livedoc.core.util.LivedocUtils;

public class ApiBodyObjectDocReader {

    public static ApiBodyObjectDoc read(Method method) {
        if (method.isAnnotationPresent(ApiBodyObject.class)) {
            ApiBodyObjectDoc apiBodyObjectDoc = new ApiBodyObjectDoc(
                    LivedocTypeBuilder.build(new LivedocType(), method.getAnnotation(ApiBodyObject.class).clazz(),
                            method.getAnnotation(ApiBodyObject.class).clazz()));
            return apiBodyObjectDoc;
        }

        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiBodyObject.class);
        if (index != -1) {
            ApiBodyObjectDoc apiBodyObjectDoc = new ApiBodyObjectDoc(
                    LivedocTypeBuilder.build(new LivedocType(), method.getParameterTypes()[index],
                            method.getGenericParameterTypes()[index]));
            return apiBodyObjectDoc;
        }

        return null;
    }

}
