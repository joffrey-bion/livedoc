package org.hildan.livedoc.core.util.controller;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiResponseObject;
import org.hildan.livedoc.core.pojo.ApiVerb;
import org.hildan.livedoc.core.util.pojo.Pizza;

@Api(description = "Annotations put on an interface instead of on a concrete class", name = "interface services")
public interface PizzaController {

    @ApiMethod(path = "/pizzas/pizza/{id}", verb = ApiVerb.GET, produces = "application/json")
    @ApiResponseObject
    Pizza get(@ApiPathParam(name = "id") Long id);

}
