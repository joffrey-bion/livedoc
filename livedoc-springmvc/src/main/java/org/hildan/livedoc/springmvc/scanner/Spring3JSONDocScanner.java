package org.hildan.livedoc.springmvc.scanner;

import java.util.Set;

import org.springframework.stereotype.Controller;

public class Spring3JSONDocScanner extends AbstractSpringJSONDocScanner {

    @Override
    public Set<Class<?>> jsondocControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class, true);
    }

}
