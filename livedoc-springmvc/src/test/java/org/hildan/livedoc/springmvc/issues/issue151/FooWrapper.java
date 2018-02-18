package org.hildan.livedoc.springmvc.issues.issue151;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@ApiType(group = "foogroup", description = "Foo description")
public class FooWrapper<T> {

    @ApiTypeProperty(description = "The wrapper's content")
    private T content;

}
