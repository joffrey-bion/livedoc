package org.hildan.livedoc.core.util.pojo;

import java.util.List;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiVersion;
import org.hildan.livedoc.core.annotation.ApiObjectField;

@ApiObject(name = "parent")
public class Parent extends Grandparent {

	@ApiObjectField(description = "the test name")
	private String name;

	@ApiObjectField(description = "the test name")
	@ApiVersion(since = "1.0", until = "2.12")
	private List<Child> children;
}
