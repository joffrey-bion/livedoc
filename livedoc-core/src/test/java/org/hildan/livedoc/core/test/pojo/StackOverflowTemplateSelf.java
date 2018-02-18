package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

public class StackOverflowTemplateSelf {

    @ApiTypeProperty
    private Integer id;

    @ApiTypeProperty
    private StackOverflowTemplateSelf ooo;

}
