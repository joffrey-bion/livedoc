package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectProperty;

public class StackOverflowTemplateObjectTwo {

    @ApiObjectProperty(name = "id", description = "The group identifier")
    private Long id;

    @ApiObjectProperty(description = "The group name")
    private String name;

    @ApiObjectProperty(name = "owner", description = "The group owner", required = true)
    private StackOverflowTemplateObjectOne owner;

}
