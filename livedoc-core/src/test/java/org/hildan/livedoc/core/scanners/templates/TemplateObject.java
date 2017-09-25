package org.hildan.livedoc.core.scanners.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.test.pojo.Gender;

public class TemplateObject {

    @ApiObjectProperty(name = "my_id")
    private Integer id;

    @ApiObjectProperty
    private String name;

    @ApiObjectProperty
    private Gender gender;

    @ApiObjectProperty
    private boolean bool;

    @ApiObjectProperty
    private int idint;

    @ApiObjectProperty
    private long idlong;

    @ApiObjectProperty
    private char namechar;

    @ApiObjectProperty
    private String[] stringarr;

    @ApiObjectProperty
    private Integer[] integerarr;

    @ApiObjectProperty
    private List untypedlist;

    @ApiObjectProperty
    private List<?> wildcardlist;

    @ApiObjectProperty
    private List<Long> longlist;

    @ApiObjectProperty
    private List<String> stringlist;

    @ApiObjectProperty(name = "sub_obj")
    private TemplateSubObject subObj = new TemplateSubObject();

    @ApiObjectProperty
    private List<TemplateSubObject> subobjlist = new ArrayList<>();

    @ApiObjectProperty
    private int[] intarr;

    @ApiObjectProperty
    private int[][] intarrarr;

    @ApiObjectProperty
    private String[][] stringarrarr;

    @ApiObjectProperty
    private TemplateSubSubObject[] subsubobjarr;

    @ApiObjectProperty
    private Map map;

    @ApiObjectProperty
    private Map<String, Integer> mapstringinteger;

    @ApiObjectProperty
    private Map<TemplateSubObject, Integer> mapsubobjinteger;

    @ApiObjectProperty
    private Map<String, TemplateSubObject> mapintegersubobj;

    @ApiObjectProperty
    private Map<String, List<TemplateSubObject>> mapintegerlistsubsubobj;

}
