package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.model.doc.ApiRequestBodyDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.scanner.builder.body.MessageMappingBodyFinder;
import org.hildan.livedoc.springmvc.scanner.builder.body.RequestMappingBodyFinder;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringRequestBodyBuilder {

    public static ApiRequestBodyDoc buildRequestBody(Method method, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        int index = getIndexOfBodyParam(method);
        if (index < 0) {
            return null;
        }
        Type bodyParamType = method.getGenericParameterTypes()[index];
        Object template = templateProvider.getTemplate(bodyParamType);
        LivedocType livedocType = typeReferenceProvider.getReference(bodyParamType);

        return new ApiRequestBodyDoc(livedocType, template);
    }

    private static int getIndexOfBodyParam(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            return RequestMappingBodyFinder.getBodyParamIndex(method);
        }
        if (ClasspathUtils.isMessageMappingOnClasspath() && method.isAnnotationPresent(MessageMapping.class)) {
            return MessageMappingBodyFinder.getBodyParamIndex(method);
        }
        return -1;
    }
}
