package org.hildan.livedoc.core.builders.doc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.annotations.ApiObjectProperty;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.pojo.ApiStage;
import org.hildan.livedoc.core.pojo.ApiVisibility;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.test.pojo.HibernateValidatorPojo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApiObjectReaderTest {

    private ApiObjectDocReader reader;

    @Before
    public void setUp() {
        reader = new ApiObjectDocReader(new FieldPropertyScanner());
    }

    @SuppressWarnings({"unused", "DefaultAnnotationParam"})
    @ApiObject(name = "test-object", visibility = ApiVisibility.PUBLIC, stage = ApiStage.PRE_ALPHA)
    @ApiVersion(since = "1.0", until = "2.12")
    private class TestObject {

        @ApiObjectProperty(description = "the test name", required = true)
        private String name;

        @ApiObjectProperty(description = "the test age", required = false)
        private Integer age;

        @ApiObjectProperty(description = "the test avg")
        private Long avg;

        @ApiObjectProperty(description = "the test map")
        private Map<String, Integer> map;

        @SuppressWarnings("rawtypes")
        @ApiObjectProperty(
                description = "an unparametrized list to test https://github.com/fabiomaffioletti/jsondoc/issues/5")
        private List unparametrizedList;

        @ApiObjectProperty(description = "a parametrized list")
        private List<String> parametrizedList;

        @ApiObjectProperty(description = "a wildcard parametrized list to test https://github"
                + ".com/fabiomaffioletti/jsondoc/issues/5")
        private List<?> wildcardParametrized;

        @ApiObjectProperty(description = "a Long array to test https://github.com/fabiomaffioletti/jsondoc/issues/27")
        private Long[] LongArray;

        @ApiObjectProperty(description = "a long array to test https://github.com/fabiomaffioletti/jsondoc/issues/27")
        private long[] longArray;

        @ApiObjectProperty(name = "foo_bar",
                description = "a property to test https://github.com/fabiomaffioletti/jsondoc/pull/31", required = true)
        private String fooBar;

        @ApiObjectProperty(name = "version", description = "a property to test version since and until", required = true)
        @ApiVersion(since = "1.0", until = "2.12")
        private String version;

        @ApiObjectProperty(name = "test-enum", description = "a test enum")
        private TestEnum testEnum;

        @ApiObjectProperty(name = "test-enum-with-allowed-values", description = "a test enum with allowed values",
                allowedvalues = {"A", "B", "C"})
        private TestEnum testEnumWithAllowedValues;

        @ApiObjectProperty(name = "orderedProperty", order = 1)
        private String orderedProperty;
    }

    @Test
    public void testApiObjectDoc() {
        ApiObjectDoc doc = reader.read(TestObject.class);
        Assert.assertEquals("test-object", doc.getName());
        Assert.assertEquals(14, doc.getFields().size());
        Assert.assertEquals("1.0", doc.getSupportedversions().getSince());
        Assert.assertEquals("2.12", doc.getSupportedversions().getUntil());
        Assert.assertEquals(ApiVisibility.PUBLIC, doc.getVisibility());
        Assert.assertEquals(ApiStage.PRE_ALPHA, doc.getStage());

        for (ApiObjectFieldDoc fieldDoc : doc.getFields()) {
            if (fieldDoc.getName().equals("wildcardParametrized")) {
                Assert.assertEquals("List<?>", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("unparametrizedList")) {
                Assert.assertEquals("List", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("parametrizedList")) {
                Assert.assertEquals("List<String>", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("name")) {
                Assert.assertEquals("String", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("name", fieldDoc.getName());
                Assert.assertEquals("true", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("age")) {
                Assert.assertEquals("Integer", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("age", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("avg")) {
                Assert.assertEquals("Long", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("avg", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("map")) {
                Assert.assertEquals("Map<String, Integer>", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("LongArray")) {
                Assert.assertEquals("Long[]", fieldDoc.getType().getOneLineText());
                Assert.assertEquals("LongArray", fieldDoc.getName());
                Assert.assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("longArray")) {
                Assert.assertEquals("long[]", fieldDoc.getType().getOneLineText());
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

    @ApiObject(name = "test-enum")
    private enum TestEnum {
        TESTENUM1,
        TESTENUM2,
        TESTENUM3
    }

    @Test
    public void testEnumObjectDoc() {
        ApiObjectDoc doc = reader.read(TestEnum.class);
        Assert.assertEquals("test-enum", doc.getName());
        Assert.assertEquals(0, doc.getFields().size());
        Assert.assertEquals(TestEnum.TESTENUM1.name(), doc.getAllowedvalues()[0]);
        Assert.assertEquals(TestEnum.TESTENUM2.name(), doc.getAllowedvalues()[1]);
        Assert.assertEquals(TestEnum.TESTENUM3.name(), doc.getAllowedvalues()[2]);
    }

    @SuppressWarnings("unused")
    @ApiObject
    private class NoNameApiObject {
        @ApiObjectProperty
        private Long id;
    }

    @Test
    public void testNoNameApiObjectDoc() {
        ApiObjectDoc doc = reader.read(NoNameApiObject.class);
        Assert.assertEquals("NoNameApiObject", doc.getName());
        Assert.assertEquals("id", doc.getFields().iterator().next().getName());
    }

    @SuppressWarnings("unused")
    @ApiObject
    private class TemplateApiObject {
        @ApiObjectProperty
        private Long id;

        @ApiObjectProperty
        private String name;
    }

    @Test
    public void testTemplateApiObjectDoc() {
        ApiObjectDoc doc = reader.read(TemplateApiObject.class);
        Assert.assertEquals("TemplateApiObject", doc.getName());
        Iterator<ApiObjectFieldDoc> iterator = doc.getFields().iterator();
        Assert.assertEquals("id", iterator.next().getName());
        Assert.assertEquals("name", iterator.next().getName());
    }

    @ApiObject
    private class UndefinedVisibilityAndStage {}

    @Test
    public void testUndefinedVisibilityAndStageDoc() {
        ApiObjectDoc doc = reader.read(UndefinedVisibilityAndStage.class);
        Assert.assertEquals("UndefinedVisibilityAndStage", doc.getName());
        Assert.assertEquals(ApiVisibility.UNDEFINED, doc.getVisibility());
        Assert.assertEquals(ApiStage.UNDEFINED, doc.getStage());
    }

    @Test
    public void testApiObjectDocWithHibernateValidator() {
        ApiObjectDoc doc = reader.read(HibernateValidatorPojo.class);
        Set<ApiObjectFieldDoc> fields = doc.getFields();
        for (ApiObjectFieldDoc fieldDoc : fields) {
            if (fieldDoc.getName().equals("id")) {
                Iterator<String> formats = fieldDoc.getFormat().iterator();
                Assert.assertEquals("a not empty id", formats.next());
                Assert.assertEquals("length must be between 2 and 2147483647", formats.next());
                Assert.assertEquals("must be less than or equal to 9", formats.next());
            }
        }
    }

}
