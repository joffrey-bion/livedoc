package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.model.doc.RequestBodyDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.LivedocUtils;

public class ApiRequestBodyTypeDocReader {

    public static RequestBodyDoc read(Method method, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        Type responseType = getBodyType(method);
        if (responseType == null) {
            return null;
        }
        LivedocType livedocType = typeReferenceProvider.getReference(responseType);
        Object template = templateProvider.getTemplate(responseType);
        return new RequestBodyDoc(livedocType, template);
    }

    private static Type getBodyType(Method method) {
        ApiRequestBodyType annotation = method.getAnnotation(ApiRequestBodyType.class);
        if (annotation != null) {
            return annotation.value();
        }

        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiRequestBodyType.class);
        if (index != -1) {
            return method.getGenericParameterTypes()[index];
        }
        return null;
    }

}
