package org.hildan.livedoc.core.util.pojo;

import java.util.Date;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectField;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiObject(name = "grandparent", show = false)
public class Grandparent {

    @ApiObjectField(description = "the test surname")
    @ApiVersion(since = "1.0")
    private String surname;

    @ApiObjectField(description = "the date of birth", format = "yyyy-MM-dd HH:mm:ss")
    private Date dob;

}
