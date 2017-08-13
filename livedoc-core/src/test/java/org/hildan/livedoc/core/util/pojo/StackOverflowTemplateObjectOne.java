package org.hildan.livedoc.core.util.pojo;

import org.hildan.livedoc.core.annotation.ApiObjectField;

public class StackOverflowTemplateObjectOne {

    @ApiObjectField(description = "The user id")
    private Long id;

    @ApiObjectField(description = "The user firstname")
    private String firstname;

    @ApiObjectField(description = "The user's default group", processtemplate = false)
    private StackOverflowTemplateObjectTwo defaultgroup;

}
