package org.hildan.livedoc.springmvc.issues.issue151;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;

@ApiObject(group = "foogroup", description = "Foo description")
public class FooWrapper<T> {

    @ApiObjectProperty(description = "The wrapper's content")
    private T content;

}
