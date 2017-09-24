package org.hildan.livedoc.core.test.pojo;

import java.util.Map;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;

@ApiObject(name = "child")
public class Child extends Parent {

    @ApiObjectProperty(description = "the test age")
    @ApiVersion(since = "1.0")
    private Integer age;

    @ApiObjectProperty(description = "the test games")
    private Long[] games;

    @ApiObjectProperty(description = "the scores for each game")
    private Map<String, Integer> scores;

    @ApiObjectProperty(name = "gender", description = "the gender of this person")
    @ApiVersion(since = "1.2")
    private Gender gender;

}
