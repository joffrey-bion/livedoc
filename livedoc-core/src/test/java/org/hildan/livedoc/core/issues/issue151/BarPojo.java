package org.hildan.livedoc.core.issues.issue151;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@ApiType(group = "bargroup", description = "Bar description")
public class BarPojo {

    @ApiTypeProperty(description = "Bar description")
    private String barField;

}
