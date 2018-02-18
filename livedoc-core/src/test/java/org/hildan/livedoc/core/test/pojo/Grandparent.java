package org.hildan.livedoc.core.test.pojo;

import java.util.Date;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiType(name = "grandparent", show = false)
public class Grandparent {

    @ApiTypeProperty(description = "the test surname")
    @ApiVersion(since = "1.0")
    private String surname;

    @ApiTypeProperty(description = "the date of birth", format = "yyyy-MM-dd HH:mm:ss")
    private Date dob;

}
