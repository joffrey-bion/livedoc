package org.hildan.livedoc.core.test.pojo;

import java.util.List;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiType(name = "parent")
public class Parent extends Grandparent {

    @ApiTypeProperty(description = "the test name")
    private String name;

    @ApiTypeProperty(description = "the test name")
    @ApiVersion(since = "1.0", until = "2.12")
    private List<Child> children;
}
