package org.hildan.livedoc.core.test.pojo;

import java.util.List;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiObject(name = "parent")
public class Parent extends Grandparent {

    @ApiObjectProperty(description = "the test name")
    private String name;

    @ApiObjectProperty(description = "the test name")
    @ApiVersion(since = "1.0", until = "2.12")
    private List<Child> children;
}
