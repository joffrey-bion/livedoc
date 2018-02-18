package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

public class StackOverflowTemplateObjectOne {

    @ApiTypeProperty(description = "The user id")
    private Long id;

    @ApiTypeProperty(description = "The user firstname")
    private String firstname;

    @ApiTypeProperty(description = "The user's default group")
    private StackOverflowTemplateObjectTwo defaultgroup;

}
