package org.hildan.livedoc.springmvc.scanner;

import java.util.Set;

import org.springframework.stereotype.Controller;

public class Spring3DocAnnotationScanner extends AbstractSpringDocAnnotationScanner {

    @Override
    public Set<Class<?>> jsondocControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class, true);
    }

}
