package org.hildan.livedoc.core.test.pojo;

import java.util.Map;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiType(name = "child")
public class Child extends Parent {

    @ApiTypeProperty(description = "the test age")
    @ApiVersion(since = "1.0")
    private Integer age;

    @ApiTypeProperty(description = "the test games")
    private Long[] games;

    @ApiTypeProperty(description = "the scores for each game")
    private Map<String, Integer> scores;

    @ApiTypeProperty(name = "gender", description = "the gender of this person")
    @ApiVersion(since = "1.2")
    private Gender gender;

}
