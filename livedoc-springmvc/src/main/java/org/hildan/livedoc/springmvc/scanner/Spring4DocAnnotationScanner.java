package org.hildan.livedoc.springmvc.scanner;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class Spring4DocAnnotationScanner extends AbstractSpringDocAnnotationScanner {

    @Override
    public Set<Class<?>> jsondocControllers() {
        Set<Class<?>> jsondocControllers = reflections.getTypesAnnotatedWith(Controller.class, true);
        jsondocControllers.addAll(reflections.getTypesAnnotatedWith(RestController.class, true));
        if (isSpringDataRestOnClassPath()) {
            jsondocControllers.addAll(reflections.getTypesAnnotatedWith(RepositoryRestController.class, true));
        }
        return jsondocControllers;
    }

    private static boolean isSpringDataRestOnClassPath() {
        try {
            Class.forName("org.springframework.data.rest.webmvc.RepositoryRestController");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    protected boolean shouldDocumentMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class) || method.isAnnotationPresent(MessageMapping.class)
                || method.isAnnotationPresent(SubscribeMapping.class);
    }
}
