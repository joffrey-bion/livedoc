package org.hildan.livedoc.core.issues.issue151;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;

@ApiObject(group = "foogroup", description = "Foo description")
public class FooWrapper<T> {

    @ApiObjectField(description = "The wrapper's content")
    private T content;

}
