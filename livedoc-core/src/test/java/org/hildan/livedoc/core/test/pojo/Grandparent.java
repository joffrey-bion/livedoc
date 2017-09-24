package org.hildan.livedoc.core.test.pojo;

import java.util.Date;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiObject(name = "grandparent", show = false)
public class Grandparent {

    @ApiObjectProperty(description = "the test surname")
    @ApiVersion(since = "1.0")
    private String surname;

    @ApiObjectProperty(description = "the date of birth", format = "yyyy-MM-dd HH:mm:ss")
    private Date dob;

}
