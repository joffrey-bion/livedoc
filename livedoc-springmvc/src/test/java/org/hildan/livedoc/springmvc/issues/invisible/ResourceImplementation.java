package org.hildan.livedoc.springmvc.issues.invisible;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;

@ApiObject(name = "resource implementation")
public class ResourceImplementation implements ResourceInterface {

    @ApiObjectField(name = "resource id")
    private String id;

    @Override
    public String getId() {
        return this.id;
    }

}
