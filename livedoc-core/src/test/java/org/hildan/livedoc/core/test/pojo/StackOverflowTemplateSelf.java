package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectField;

public class StackOverflowTemplateSelf {

    @ApiObjectField
    private Integer id;

    @ApiObjectField
    private StackOverflowTemplateSelf ooo;

}
