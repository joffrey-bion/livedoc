package org.hildan.livedoc.core.scanners.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.test.pojo.Gender;

public class TemplateObject {

    @ApiTypeProperty(name = "my_id")
    private Integer id;

    @ApiTypeProperty
    private String name;

    @ApiTypeProperty
    private Gender gender;

    @ApiTypeProperty
    private boolean bool;

    @ApiTypeProperty
    private int idint;

    @ApiTypeProperty
    private long idlong;

    @ApiTypeProperty
    private char namechar;

    @ApiTypeProperty
    private String[] stringarr;

    @ApiTypeProperty
    private Integer[] integerarr;

    @ApiTypeProperty
    private List rawlist;

    @ApiTypeProperty
    private List<?> wildcardlist;

    @ApiTypeProperty
    private List<Long> longlist;

    @ApiTypeProperty
    private List<String> stringlist;

    @ApiTypeProperty(name = "sub_obj")
    private TemplateSubObject subObj = new TemplateSubObject();

    @ApiTypeProperty
    private List<TemplateSubObject> subobjlist = new ArrayList<>();

    @ApiTypeProperty
    private int[] intarr;

    @ApiTypeProperty
    private int[][] intarrarr;

    @ApiTypeProperty
    private String[][] stringarrarr;

    @ApiTypeProperty
    private TemplateSubSubObject[] subsubobjarr;

    @ApiTypeProperty
    private Map map;

    @ApiTypeProperty
    private Map<String, Integer> mapstringinteger;

    @ApiTypeProperty
    private Map<TemplateSubObject, Integer> mapsubobjinteger;

    @ApiTypeProperty
    private Map<Integer, TemplateSubObject> mapintegersubobj;

    @ApiTypeProperty
    private Map<Integer, List<TemplateSubSubObject>> mapintegerlistsubsubobj;

}
