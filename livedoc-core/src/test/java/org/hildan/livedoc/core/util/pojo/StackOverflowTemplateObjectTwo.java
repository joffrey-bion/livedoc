package org.hildan.livedoc.core.util.pojo;

import org.hildan.livedoc.core.annotation.ApiObjectField;

public class StackOverflowTemplateObjectTwo {

	@ApiObjectField(name = "id", description = "The group identifier")
	private Long id;

	@ApiObjectField(description = "The group name")
	private String name;
	
	@ApiObjectField(name = "owner", description = "The group owner", required = true)
	private StackOverflowTemplateObjectOne owner;

}
