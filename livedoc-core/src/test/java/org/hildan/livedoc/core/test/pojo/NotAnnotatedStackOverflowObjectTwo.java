package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObjectProperty;

public class NotAnnotatedStackOverflowObjectTwo {

    private Long id;

    private String name;

    @ApiObjectProperty(processtemplate = false)
    private NotAnnotatedStackOverflowObjectOne typeOne;

}
