package org.hildan.livedoc.springmvc.issues.issue151;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;

@ApiObject(group = "foogroup", description = "Foo description")
public class FooWrapper<T> {

    @ApiObjectField(description = "The wrapper's content")
    private T content;

}
