package org.hildan.livedoc.springmvc.scanner;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.scanner.builder.JSONDocApiMethodDocBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class Spring4JSONDocScanner extends AbstractSpringJSONDocScanner {

    @Override
    public Set<Class<?>> jsondocControllers() {
        Set<Class<?>> jsondocControllers = reflections.getTypesAnnotatedWith(Controller.class, true);
        jsondocControllers.addAll(reflections.getTypesAnnotatedWith(RestController.class, true));

        try {
            Class.forName("org.springframework.data.rest.webmvc.RepositoryRestController");
            jsondocControllers.addAll(reflections.getTypesAnnotatedWith(RepositoryRestController.class, true));

        } catch (ClassNotFoundException e) {
            log.debug(e.getMessage() + ".class not found");
        }

        return jsondocControllers;
    }

    @Override
    public Set<Method> jsondocMethods(Class<?> controller) {
        Set<Method> annotatedMethods = new LinkedHashSet<>();
        for (Method method : controller.getDeclaredMethods()) {
            if (shouldDocument(method)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private boolean shouldDocument(Method method) {
        return method.isAnnotationPresent(RequestMapping.class) || method.isAnnotationPresent(MessageMapping.class)
                || method.isAnnotationPresent(SubscribeMapping.class);
    }
}
