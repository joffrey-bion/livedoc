package org.hildan.livedoc.core.util.pojo;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;

@ApiObject(name = "customPizzaObject", group = "Restaurant")
public class Pizza extends Parent {

	@ApiObjectField(description = "the cost of this pizza")
	private Float price;

	@ApiObjectField(description = "the topping of this pizza")
	private String[] topping;

}
