package org.hildan.livedoc.springmvc.issues.issue151;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;

@ApiObject(group = "bargroup", description = "Bar description")
public class BarPojo {

    @ApiObjectProperty(description = "Bar description")
    private String barField;

}
