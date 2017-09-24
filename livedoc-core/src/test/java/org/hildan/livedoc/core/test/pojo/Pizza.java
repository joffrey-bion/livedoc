package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;

@ApiObject(name = "customPizzaObject", group = "Restaurant")
public class Pizza extends Parent {

    @ApiObjectProperty(description = "the cost of this pizza")
    private Float price;

    @ApiObjectProperty(description = "the topping of this pizza")
    private String[] topping;

}
