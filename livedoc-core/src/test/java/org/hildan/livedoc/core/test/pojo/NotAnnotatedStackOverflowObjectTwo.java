package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

public class NotAnnotatedStackOverflowObjectTwo {

    private Long id;

    private String name;

    @ApiTypeProperty
    private NotAnnotatedStackOverflowObjectOne typeOne;

}
