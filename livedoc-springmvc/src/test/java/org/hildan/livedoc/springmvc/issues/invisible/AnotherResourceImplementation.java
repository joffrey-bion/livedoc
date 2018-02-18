package org.hildan.livedoc.springmvc.issues.invisible;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@ApiType(name = "another resource implementation")
public class AnotherResourceImplementation implements ResourceInterface {

    @ApiTypeProperty(name = "resource id")
    private String id;

    @Override
    public String getId() {
        return this.id;
    }

}
