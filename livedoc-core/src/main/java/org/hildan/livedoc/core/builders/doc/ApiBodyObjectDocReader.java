package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.model.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.model.doc.ApiRequestBodyDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.util.LivedocUtils;

public class ApiBodyObjectDocReader {

    public static ApiRequestBodyDoc read(Method method, TemplateProvider templateProvider) {
        Type responseType = getBodyType(method);
        if (responseType == null) {
            return null;
        }
        LivedocType livedocType = LivedocTypeBuilder.build(responseType);
        Object template = templateProvider.getTemplate(responseType);
        return new ApiRequestBodyDoc(livedocType, template);
    }

    private static Type getBodyType(Method method) {
        if (method.isAnnotationPresent(ApiRequestBodyType.class)) {
            ApiRequestBodyType annotation = method.getAnnotation(ApiRequestBodyType.class);
            return annotation.value();
        }

        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiRequestBodyType.class);
        if (index != -1) {
            Type type = method.getGenericParameterTypes()[index];
            return type;
        }

        return null;
    }

}
