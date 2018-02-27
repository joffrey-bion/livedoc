package org.hildan.livedoc.core.builders.doc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiStage;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.annotations.ApiVisibility;
import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.Stage;
import org.hildan.livedoc.core.model.doc.Visibility;
import org.hildan.livedoc.core.model.doc.types.ApiFieldDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.types.references.DefaultTypeReferenceProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.test.pojo.HibernateValidatorPojo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ApiTypeReaderTest {

    private ApiTypeDocReader reader;

    private TypeReferenceProvider typeReferenceProvider;

    @Before
    public void setUp() {
        reader = new ApiTypeDocReader(new FieldPropertyScanner());
        typeReferenceProvider = new DefaultTypeReferenceProvider(c -> true);
    }

    @SuppressWarnings({"unused", "DefaultAnnotationParam"})
    @ApiType(name = "test-object")
    @ApiVisibility(Visibility.PUBLIC)
    @ApiStage(Stage.PRE_ALPHA)
    @ApiVersion(since = "1.0", until = "2.12")
    private class TestObject {

        @ApiTypeProperty(description = "the test name", required = true)
        private String name;

        @ApiTypeProperty(description = "the test age", required = false)
        private Integer age;

        @ApiTypeProperty(description = "the test avg")
        private Long avg;

        @ApiTypeProperty(description = "the test map")
        private Map<String, Integer> map;

        @SuppressWarnings("rawtypes")
        @ApiTypeProperty(
                description = "an unparametrized list to test https://github.com/fabiomaffioletti/jsondoc/issues/5")
        private List unparametrizedList;

        @ApiTypeProperty(description = "a parametrized list")
        private List<String> parametrizedList;

        @ApiTypeProperty(description = "a wildcard parametrized list to test https://github"
                + ".com/fabiomaffioletti/jsondoc/issues/5")
        private List<?> wildcardParametrized;

        @ApiTypeProperty(description = "a Long array to test https://github.com/fabiomaffioletti/jsondoc/issues/27")
        private Long[] longObjArray;

        @ApiTypeProperty(description = "a long array to test https://github.com/fabiomaffioletti/jsondoc/issues/27")
        private long[] longArray;

        @ApiTypeProperty(name = "foo_bar",
                description = "a property to test https://github.com/fabiomaffioletti/jsondoc/pull/31", required = true)
        private String fooBar;

        @ApiTypeProperty(name = "version", description = "a property to test version since and until", required = true)
        @ApiVersion(since = "1.0", until = "2.12")
        private String version;

        @ApiTypeProperty(name = "test-enum", description = "a test enum")
        private TestEnum testEnum;

        @ApiTypeProperty(name = "test-enum-with-allowed-values", description = "a test enum with allowed values",
                allowedValues = {"A", "B", "C"})
        private TestEnum testEnumWithAllowedValues;

        @ApiTypeProperty(name = "orderedProperty", order = 1)
        private String orderedProperty;
    }

    @Test
    public void testApiObjectDoc() {
        ApiTypeDoc doc = reader.read(TestObject.class, typeReferenceProvider);
        assertEquals("test-object", doc.getName());
        assertEquals(14, doc.getFields().size());
        assertEquals("1.0", doc.getSupportedVersions().getSince());
        assertEquals("2.12", doc.getSupportedVersions().getUntil());
        assertEquals(Visibility.PUBLIC, doc.getVisibility());
        assertEquals(Stage.PRE_ALPHA, doc.getStage());

        for (ApiFieldDoc fieldDoc : doc.getFields()) {
            if (fieldDoc.getName().equals("wildcardParametrized")) {
                assertEquals("List<?>", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("unparametrizedList")) {
                assertEquals("List", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("parametrizedList")) {
                assertEquals("List<String>", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("name")) {
                assertEquals("String", fieldDoc.getType().getOneLineText());
                assertEquals("name", fieldDoc.getName());
                assertEquals("true", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("age")) {
                assertEquals("Integer", fieldDoc.getType().getOneLineText());
                assertEquals("age", fieldDoc.getName());
                assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("avg")) {
                assertEquals("Long", fieldDoc.getType().getOneLineText());
                assertEquals("avg", fieldDoc.getName());
                assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("map")) {
                assertEquals("Map<String, Integer>", fieldDoc.getType().getOneLineText());
            }

            if (fieldDoc.getName().equals("longObjArray")) {
                assertEquals("Long[]", fieldDoc.getType().getOneLineText());
                assertEquals("longObjArray", fieldDoc.getName());
                assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("longArray")) {
                assertEquals("long[]", fieldDoc.getType().getOneLineText());
                assertEquals("longArray", fieldDoc.getName());
                assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("fooBar")) {
                assertEquals("String", fieldDoc.getType().getOneLineText());
                assertEquals("foo_bar", fieldDoc.getName());
                assertEquals("false", fieldDoc.getRequired());
            }

            if (fieldDoc.getName().equals("version")) {
                assertEquals("String", fieldDoc.getType().getOneLineText());
                assertEquals("1.0", fieldDoc.getSupportedVersions().getSince());
                assertEquals("2.12", fieldDoc.getSupportedVersions().getUntil());
            }

            if (fieldDoc.getName().equals("test-enum")) {
                assertEquals("test-enum", fieldDoc.getName());
                assertEquals(TestEnum.TESTENUM1.name(), fieldDoc.getAllowedValues()[0]);
                assertEquals(TestEnum.TESTENUM2.name(), fieldDoc.getAllowedValues()[1]);
                assertEquals(TestEnum.TESTENUM3.name(), fieldDoc.getAllowedValues()[2]);
            }

            if (fieldDoc.getName().equals("test-enum-with-allowed-values")) {
                assertEquals("A", fieldDoc.getAllowedValues()[0]);
                assertEquals("B", fieldDoc.getAllowedValues()[1]);
                assertEquals("C", fieldDoc.getAllowedValues()[2]);
            }

            if (fieldDoc.getName().equals("orderedProperty")) {
                assertEquals("orderedProperty", fieldDoc.getName());
                assertEquals(1, fieldDoc.getOrder().intValue());
            } else {
                assertEquals(Integer.MAX_VALUE, fieldDoc.getOrder().intValue());
            }

        }
    }

    @ApiType(name = "test-enum")
    private enum TestEnum {
        TESTENUM1,
        TESTENUM2,
        TESTENUM3
    }

    @Test
    public void testEnumObjectDoc() {
        ApiTypeDoc doc = reader.read(TestEnum.class, typeReferenceProvider);
        assertEquals("test-enum", doc.getName());
        assertEquals(0, doc.getFields().size());
        assertEquals(TestEnum.TESTENUM1.name(), doc.getAllowedValues()[0]);
        assertEquals(TestEnum.TESTENUM2.name(), doc.getAllowedValues()[1]);
        assertEquals(TestEnum.TESTENUM3.name(), doc.getAllowedValues()[2]);
    }

    @SuppressWarnings("unused")
    @ApiType
    private class NoNameApiObject {
        @ApiTypeProperty
        private Long id;
    }

    @Test
    public void testNoNameApiObjectDoc() {
        ApiTypeDoc doc = reader.read(NoNameApiObject.class, typeReferenceProvider);
        assertEquals("NoNameApiObject", doc.getName());
        assertEquals("id", doc.getFields().iterator().next().getName());
    }

    @SuppressWarnings("unused")
    @ApiType
    private class TemplateApiObject {
        @ApiTypeProperty
        private Long id;

        @ApiTypeProperty
        private String name;
    }

    @Test
    public void testTemplateApiObjectDoc() {
        ApiTypeDoc doc = reader.read(TemplateApiObject.class, typeReferenceProvider);
        assertEquals("TemplateApiObject", doc.getName());
        Iterator<ApiFieldDoc> iterator = doc.getFields().iterator();
        assertEquals("id", iterator.next().getName());
        assertEquals("name", iterator.next().getName());
    }

    @ApiType
    private class UndefinedVisibilityAndStage {}

    @Test
    public void testUndefinedVisibilityAndStageDoc() {
        ApiTypeDoc doc = reader.read(UndefinedVisibilityAndStage.class, typeReferenceProvider);
        assertEquals("UndefinedVisibilityAndStage", doc.getName());
        assertNull(doc.getVisibility());
        assertNull(doc.getStage());
    }

    @Test
    public void testApiObjectDocWithHibernateValidator() {
        ApiTypeDoc doc = reader.read(HibernateValidatorPojo.class, typeReferenceProvider);
        Set<ApiFieldDoc> fields = doc.getFields();
        for (ApiFieldDoc fieldDoc : fields) {
            if (fieldDoc.getName().equals("id")) {
                Iterator<String> formats = fieldDoc.getFormat().iterator();
                assertEquals("a not empty id", formats.next());
                assertEquals("length must be between 2 and 2147483647", formats.next());
                assertEquals("must be less than or equal to 9", formats.next());
            }
        }
    }

}
