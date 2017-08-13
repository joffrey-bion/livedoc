package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotation.ApiResponseObject;
import org.hildan.livedoc.core.pojo.ApiResponseObjectDoc;
import org.hildan.livedoc.core.util.LivedocDefaultType;
import org.hildan.livedoc.core.util.LivedocType;
import org.hildan.livedoc.core.util.LivedocTypeBuilder;

public class ApiResponseObjectDocReader {

    public static ApiResponseObjectDoc build(Method method) {
        if (method.isAnnotationPresent(ApiResponseObject.class)) {
            ApiResponseObject annotation = method.getAnnotation(ApiResponseObject.class);

            if (annotation.clazz().isAssignableFrom(LivedocDefaultType.class)) {
                return new ApiResponseObjectDoc(LivedocTypeBuilder.build(new LivedocType(), method.getReturnType(),
                        method.getGenericReturnType()));
            } else {
                return new ApiResponseObjectDoc(
                        LivedocTypeBuilder.build(new LivedocType(), annotation.clazz(), annotation.clazz()));
            }
        }

        return null;
    }

}
