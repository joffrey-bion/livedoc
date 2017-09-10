package org.hildan.livedoc.core.test.pojo;

import java.util.List;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiObject(name = "parent")
public class Parent extends Grandparent {

    @ApiObjectField(description = "the test name")
    private String name;

    @ApiObjectField(description = "the test name")
    @ApiVersion(since = "1.0", until = "2.12")
    private List<Child> children;
}
