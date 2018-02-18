package org.hildan.livedoc.springmvc.issues.invisible;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@ApiType(name = "resource implementation")
public class ResourceImplementation implements ResourceInterface {

    @ApiTypeProperty(name = "resource id")
    private String id;

    @Override
    public String getId() {
        return this.id;
    }

}
