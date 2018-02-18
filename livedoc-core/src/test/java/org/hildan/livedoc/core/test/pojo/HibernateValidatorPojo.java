package org.hildan.livedoc.core.test.pojo;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.Length;
import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;

@ApiType
public class HibernateValidatorPojo {

    @ApiTypeProperty(format = "a not empty id")
    @Length(min = 2)
    @Max(9)
    private String id;

}
