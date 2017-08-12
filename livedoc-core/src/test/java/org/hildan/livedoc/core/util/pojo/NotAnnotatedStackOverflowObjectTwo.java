package org.hildan.livedoc.core.util.pojo;

import org.hildan.livedoc.core.annotation.ApiObjectField;


public class NotAnnotatedStackOverflowObjectTwo {

	private Long id;

	private String name;
	
	@ApiObjectField(processtemplate = false)
	private NotAnnotatedStackOverflowObjectOne typeOne;

}
