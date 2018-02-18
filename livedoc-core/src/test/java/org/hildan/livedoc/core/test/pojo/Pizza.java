package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@ApiType(name = "customPizzaObject", group = "Restaurant")
public class Pizza extends Parent {

    @ApiTypeProperty(description = "the cost of this pizza")
    private Float price;

    @ApiTypeProperty(description = "the topping of this pizza")
    private String[] topping;

}
