package org.hildan.livedoc.core.util.pojo;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;

@ApiObject(name = "customPizzaObject", group = "Restaurant")
public class Pizza extends Parent {

    @ApiObjectField(description = "the cost of this pizza")
    private Float price;

    @ApiObjectField(description = "the topping of this pizza")
    private String[] topping;

}
