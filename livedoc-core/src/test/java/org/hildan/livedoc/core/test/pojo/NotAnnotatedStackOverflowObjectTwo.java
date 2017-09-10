package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectField;

public class NotAnnotatedStackOverflowObjectTwo {

    private Long id;

    private String name;

    @ApiObjectField(processtemplate = false)
    private NotAnnotatedStackOverflowObjectOne typeOne;

}
