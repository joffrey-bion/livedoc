package org.hildan.livedoc.core.test.pojo;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;

@ApiObject
public interface PojoInterface {

    @ApiObjectProperty
    Integer id = 0;

}
