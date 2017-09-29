package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiBodyObjectDoc;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.springmvc.scanner.builder.body.MessageMappingBodyFinder;
import org.hildan.livedoc.springmvc.scanner.builder.body.RequestMappingBodyFinder;
import org.hildan.livedoc.springmvc.scanner.utils.ClasspathUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpringRequestBodyBuilder {

    public static ApiBodyObjectDoc buildRequestBody(Method method, TemplateProvider templateProvider) {
        int index = getIndexOfBodyParam(method);
        if (index < 0) {
            return null;
        }
        Class<?> bodyParamBaseType = method.getParameterTypes()[index];
        Object template = templateProvider.getTemplate(bodyParamBaseType);

        Type bodyParamType = method.getGenericParameterTypes()[index];
        LivedocType livedocType = LivedocTypeBuilder.build(bodyParamType);

        return new ApiBodyObjectDoc(livedocType, template);
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
