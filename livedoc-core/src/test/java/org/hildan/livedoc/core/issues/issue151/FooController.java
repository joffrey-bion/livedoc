package org.hildan.livedoc.core.issues.issue151;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;

@Api(name = "Foo Services", description = "bla, bla, bla ...", group = "foogroup")
public class FooController {

    @ApiMethod(path = {"/api/foo"}, description = "Main foo service")
    @ApiResponseBodyType
    public FooWrapper<BarPojo> getBar() {
        return null;
    }

    @ApiMethod(path = {"/api/foo-wildcard"}, description = "Main foo service with wildcard")
    @ApiResponseBodyType
    public FooWrapper<?> wildcard() {
        return null;
    }

}
