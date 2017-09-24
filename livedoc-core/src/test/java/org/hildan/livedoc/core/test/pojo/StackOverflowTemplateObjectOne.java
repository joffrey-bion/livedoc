package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectProperty;

public class StackOverflowTemplateObjectOne {

    @ApiObjectProperty(description = "The user id")
    private Long id;

    @ApiObjectProperty(description = "The user firstname")
    private String firstname;

    @ApiObjectProperty(description = "The user's default group", processtemplate = false)
    private StackOverflowTemplateObjectTwo defaultgroup;

}
