package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.types.ApiType;

@ApiType(name = "gender", description = "An enum for the gender")
public enum Gender {

    MALE,
    FEMALE;

}
