package org.hildan.livedoc.springmvc.issues.issue151;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;

@ApiObject(group = "bargroup", description = "Bar description")
public class BarPojo {

    @ApiObjectField(description = "Bar description")
    private String barField;

}
