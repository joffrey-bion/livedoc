package org.hildan.livedoc.core.util.controller;

import org.hildan.livedoc.core.util.pojo.Pizza;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

public class PizzaControllerImpl implements PizzaController {

    @Override
    public @ResponseBody
    Pizza get(@PathVariable Long id) {
        return null;
    }

}
