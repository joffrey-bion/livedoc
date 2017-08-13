package org.hildan.livedoc.core.issues.issue151;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;

@ApiObject(group = "bargroup", description = "Bar description")
public class BarPojo {

    @ApiObjectField(description = "Bar description")
    private String barField;

}
