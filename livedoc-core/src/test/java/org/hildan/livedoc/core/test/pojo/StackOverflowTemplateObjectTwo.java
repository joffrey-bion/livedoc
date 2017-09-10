package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectField;

public class StackOverflowTemplateObjectTwo {

    @ApiObjectField(name = "id", description = "The group identifier")
    private Long id;

    @ApiObjectField(description = "The group name")
    private String name;

    @ApiObjectField(name = "owner", description = "The group owner", required = true)
    private StackOverflowTemplateObjectOne owner;

}
