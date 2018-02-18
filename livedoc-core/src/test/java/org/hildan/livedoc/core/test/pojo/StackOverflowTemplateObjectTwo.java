package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

public class StackOverflowTemplateObjectTwo {

    @ApiTypeProperty(name = "id", description = "The group identifier")
    private Long id;

    @ApiTypeProperty(description = "The group name")
    private String name;

    @ApiTypeProperty(name = "owner", description = "The group owner", required = true)
    private StackOverflowTemplateObjectOne owner;

}
