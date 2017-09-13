package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.annotations.ApiBodyObject;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiBodyObjectDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.util.LivedocUtils;

public class ApiBodyObjectDocReader {

    public static ApiBodyObjectDoc read(Method method, TemplateProvider templateProvider) {
        LivedocType responseType = getBodyType(method);
        if (responseType == null) {
            return null;
        }
        Class<?> baseType = responseType.getComposingTypes().get(0);
        Object template = templateProvider.getTemplate(baseType);
        return new ApiBodyObjectDoc(responseType, template);
    }

    private static LivedocType getBodyType(Method method) {
        if (method.isAnnotationPresent(ApiBodyObject.class)) {
            ApiBodyObject annotation = method.getAnnotation(ApiBodyObject.class);
            return LivedocTypeBuilder.build(annotation.clazz());
        }

        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiBodyObject.class);
        if (index != -1) {
            Type type = method.getGenericParameterTypes()[index];
            return LivedocTypeBuilder.build(type);
        }

        return null;
    }

}
