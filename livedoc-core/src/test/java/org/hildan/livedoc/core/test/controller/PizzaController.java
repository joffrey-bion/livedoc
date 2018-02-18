package org.hildan.livedoc.core.test.controller;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.test.pojo.Pizza;

@Api(description = "Annotations put on an interface instead of on a concrete class", name = "interface services")
public interface PizzaController {

    @ApiMethod(path = "/pizzas/pizza/{id}", verbs = ApiVerb.GET, produces = "application/json")
    @ApiResponseBodyType
    Pizza get(@ApiPathParam(name = "id") Long id);

}
