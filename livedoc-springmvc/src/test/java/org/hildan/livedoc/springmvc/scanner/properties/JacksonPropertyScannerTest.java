package org.hildan.livedoc.springmvc.scanner.properties;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.reflect.TypeToken;

import static org.junit.Assert.assertEquals;

public class JacksonPropertyScannerTest {

    private PropertyScanner scanner;

    private interface MyInterface {}

    private static class MyImpl implements MyInterface {}

    @Before
    public void setUp() throws Exception {
        scanner = new JacksonPropertyScanner(new ObjectMapper());
    }

    @JsonIgnoreProperties({"ignoredOnClass", "ignoredOnClass2"})
    private static class MyDto {

        @JsonProperty("publicFieldRenamed")
        public String publicField;

        private int fieldWithGetter;

        private List<Double> generic;

        @JsonSerialize(as = MyImpl.class)
        private MyInterface interfaceField;

        private String writeOnly;

        @JsonIgnore
        private long ignoredOnField;

        private long ignoredOnGetter;

        private long ignoredOnClass;

        public long ignoredOnClass2;

        public int getFieldWithGetter() {
            return fieldWithGetter;
        }

        public List<Double> getGeneric() {
            return generic;
        }

        public String getComputedValue() {
            return publicField + fieldWithGetter;
        }

        public void setWriteOnly(String value) {
            this.writeOnly = value;
        }

        public long getIgnoredOnField() {
            return ignoredOnField;
        }

        @JsonIgnore
        public long getIgnoredOnGetter() {
            return ignoredOnGetter;
        }

        public long getIgnoredOnClass() {
            return ignoredOnClass;
        }
    }

    @Test
    public void getProperties() throws Exception {
        List<Property> properties = scanner.getProperties(MyDto.class);

        Type listDouble = new TypeToken<List<Double>>() {}.getType();
        List<Property> expectedProps = Arrays.asList(
                new Property("fieldWithGetter", int.class, MyDto.class.getDeclaredMethod("getFieldWithGetter")),
                new Property("generic", List.class, listDouble, MyDto.class.getDeclaredMethod("getGeneric")),
                new Property("interfaceField", MyImpl.class, MyDto.class.getDeclaredField("interfaceField")),
                new Property("computedValue", String.class, MyDto.class.getDeclaredMethod("getComputedValue")),
                new Property("publicFieldRenamed", String.class, MyDto.class.getDeclaredField("publicField")));
        assertEquals(expectedProps, properties);
    }
}
