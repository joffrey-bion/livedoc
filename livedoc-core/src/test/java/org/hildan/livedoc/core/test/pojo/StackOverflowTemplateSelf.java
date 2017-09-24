package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectProperty;

public class StackOverflowTemplateSelf {

    @ApiObjectProperty
    private Integer id;

    @ApiObjectProperty
    private StackOverflowTemplateSelf ooo;

}
