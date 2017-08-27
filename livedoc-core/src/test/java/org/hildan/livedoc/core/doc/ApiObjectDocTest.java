package org.hildan.livedoc.core.doc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.annotation.ApiVersion;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVisibility;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;

public class ApiObjectDocTest {

    private DocAnnotationScanner scanner = new DefaultDocAnnotationScanner();

    @ApiObject(name = "test-object", visibility = ApiVisibility.PUBLIC, stage = ApiStage.PRE_ALPHA)
    @ApiVersion(since = "1.0", until = "2.12")
    private class TestObject {

        @ApiObjectField(description = "the test name", required = true)
        private String name;

        @ApiObjectField(description = "the test age", required = false)
        private Integer age;

        @ApiObjectField(description = "the test avg")
        private Long avg;

        @ApiObjectField(description = "the test map")
        private Map<String, Integer> map;

        @SuppressWarnings("rawtypes")
        @ApiObjectField(
                description = "an unparametrized list to test https://github.com/fabiomaffioletti/jsondoc/issues/5")
        private List unparametrizedList;

        @ApiObjectField(description = "a parametrized list")
        private List<String> parametrizedList;

        @ApiObjectField(
                description = "a wildcard parametrized list to test https://github"
                        + ".com/fabiomaffioletti/jsondoc/issues/5")
        private List<?> wildcardParametrized;

        @ApiObjectField(description = "a Long array to test https://github.com/fabiomaffioletti/jsondoc/issues/27")
        private Long[] LongArray;

        @ApiObjectField(description = "a long array to test https://github.com/fabiomaffioletti/jsondoc/issues/27")
        private long[] longArray;

        @ApiObjectField(name = "foo_bar",
                description = "a property to test https://github.com/fabiomaffioletti/jsondoc/pull/31", required = true)
        private String fooBar;

        @ApiObjectField(name = "version", description = "a property to test version since and until", required = true)
        @ApiVersion(since = "1.0", until = "2.12")
        private String version;

        @ApiObjectField(name = "test-enum", description = "a test enum")
        private TestEnum testEnum;

        @ApiObjectField(name = "test-enum-with-allowed-values", description = "a test enum with allowed values",
                allowedvalues = {"A", "B", "C"})
        private TestEnum testEnumWithAllowedValues;

        @ApiObjectField(name = "orderedProperty", order = 1)
        private String orderedProperty;
    }

    @ApiObject(name = "test-enum")
    private enum TestEnum {
        TESTENUM1,
        TESTENUM2,
        TESTENUM3;
    }

    @ApiObject
    private class NoNameApiObject {
        @ApiObjectField
        private Long id;
    }

    @ApiObject
    private class TemplateApiObject {
        @ApiObjectField
        private Long id;

        @ApiObjectField
        private String name;
    }

    @ApiObject
    private class UndefinedVisibilityAndStage {
    }

    @Test
    public void testUndefinedVisibilityAndStageDoc() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(UndefinedVisibilityAndStage.class);
        ApiObjectDoc apiObjectDoc = scanner.getApiObjectDocs(classes).iterator().next();
        Assert.assertEquals("UndefinedVisibilityAndStage", apiObjectDoc.getName());
        Assert.assertEquals(ApiVisibility.UNDEFINED, apiObjectDoc.getVisibility());
        Assert.assertEquals(ApiStage.UNDEFINED, apiObjectDoc.getStage());
    }

    @Test
    public void testTemplateApiObjectDoc() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TemplateApiObject.class);
        ApiObjectDoc apiObjectDoc = scanner.getApiObjectDocs(classes).iterator().next();
        Assert.assertEquals("TemplateApiObject", apiObjectDoc.getName());
        Iterator<ApiObjectFieldDoc> iterator = apiObjectDoc.getFields().iterator();
        Assert.assertEquals("id", iterator.next().getName());
        Assert.assertEquals("name", iterator.next().getName());
    }

    @Test
    public void testNoNameApiObjectDoc() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(NoNameApiObject.class);
        ApiObjectDoc apiObjectDoc = scanner.getApiObjectDocs(classes).iterator().next();
        Assert.assertEquals("NoNameApiObject", apiObjectDoc.getName());
        Assert.assertEquals("id", apiObjectDoc.getFields().iterator().next().getName());
        Assert.assertEquals(1, apiObjectDoc.getJsondochints().size());
    }

    @Test
    public void testEnumObjectDoc() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestEnum.class);
        ApiObjectDoc childDoc = scanner.getApiObjectDocs(classes).iterator().next();
        Assert.assertEquals("test-enum", childDoc.getName());
        Assert.assertEquals(0, childDoc.getFields().size());
        Assert.assertEquals(TestEnum.TESTENUM1.name(), childDoc.getAllowedvalues()[0]);
        Assert.assertEquals(TestEnum.TESTENUM2.name(), childDoc.getAllowedvalues()[1]);
        Assert.assertEquals(TestEnum.TESTENUM3.name(), childDoc.getAllowedvalues()[2]);
    }

    @Test
    public void testApiObjectDoc() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestObject.class);
        ApiObjectDoc childDoc = scanner.getApiObjectDocs(classes).iterator().next();
        Assert.assertEquals("test-object", childDoc.getName());
        Assert.assertEquals(14, childDoc.getFields().size());
        Assert.assertEquals("1.0", childDoc.getSupportedversions().getSince());
        Assert.assertEquals("2.12", childDoc.getSupportedversions().getUntil());
        Assert.assertEquals(ApiVisibility.PUBLIC, childDoc.getVisibility());
        Assert.assertEquals(ApiStage.PRE_ALPHA, childDoc.getStage());

        for (ApiObjectFieldDoc fieldDoc : childDoc.getFields()) {
            if (fieldDoc.getName().equals("wildcardParametrized")) {
                Assert.assertEquals("List", fieldDoc.getType().getType().get(0));
            }

            if (fieldDoc.getName().equals("unparametrizedList")) {
                Assert.assertEquals("List", fieldDoc.getType().getType().get(0));
            }

            if (fieldDoc.getName().equals("parametrizedList")) {
                Assert.assertEquals("List of String", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("name")) {
                Assert.assertEquals("String", fieldDoc.getType().getType().get(0));
                Assert.assertEquals("name", fieldDoc.getName());
                Assert.assertEquals("true", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("age")) {
                Assert.assertEquals("Integer", fieldDoc.getType().getType().get(0));
                Assert.assertEquals("age", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("avg")) {
                Assert.assertEquals("Long", fieldDoc.getType().getType().get(0));
                Assert.assertEquals("avg", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("map")) {
                Assert.assertEquals("Map", fieldDoc.getType().getType().get(0));
                Assert.assertEquals("String", fieldDoc.getType().getMapKey().getType().get(0));
                Assert.assertEquals("Integer", fieldDoc.getType().getMapValue().getType().get(0));
            }

            if (fieldDoc.getName().equals("LongArray")) {
                Assert.assertEquals("array of Long", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("LongArray", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("longArray")) {
                Assert.assertEquals("array of long", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("longArray", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("fooBar")) {
                Assert.assertEquals("String", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("foo_bar", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("version")) {
                Assert.assertEquals("String", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("1.0", fieldDoc.getSupportedversions().getSince());
                Assert.assertEquals("2.12", fieldDoc.getSupportedversions().getUntil());
            }

            if (fieldDoc.getName().equals("test-enum")) {
                Assert.assertEquals("test-enum", fieldDoc.getName());
                Assert.assertEquals(TestEnum.TESTENUM1.name(), fieldDoc.getAllowedvalues()[0]);
                Assert.assertEquals(TestEnum.TESTENUM2.name(), fieldDoc.getAllowedvalues()[1]);
                Assert.assertEquals(TestEnum.TESTENUM3.name(), fieldDoc.getAllowedvalues()[2]);
            }

            if (fieldDoc.getName().equals("test-enum-with-allowed-values")) {
                Assert.assertEquals("A", fieldDoc.getAllowedvalues()[0]);
                Assert.assertEquals("B", fieldDoc.getAllowedvalues()[1]);
                Assert.assertEquals("C", fieldDoc.getAllowedvalues()[2]);
            }

            if (fieldDoc.getName().equals("orderedProperty")) {
                Assert.assertEquals("orderedProperty", fieldDoc.getName());
                Assert.assertEquals(1, fieldDoc.getOrder().intValue());
            } else {
                Assert.assertEquals(Integer.MAX_VALUE, fieldDoc.getOrder().intValue());
            }

        }
    }

}
