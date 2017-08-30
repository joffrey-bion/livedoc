package org.hildan.livedoc.springmvc.scanner.properties;

import java.util.ArrayList;
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
import static org.junit.Assert.assertEquals;

public class JacksonPropertyScannerTest {

    private PropertyScanner scanner;

    private interface MyInterface {}
    private static class MyImpl implements MyInterface {}

    @Before
    public void setUp() throws Exception {
        scanner = new JacksonPropertyScanner(new ObjectMapper());
    }

    @JsonIgnoreProperties(value = {"ignoredOnClass", "ignoredOnClass2"})
    private static class MyDto {

        @JsonProperty("publicFieldRenamed")
        public String publicField;

        private int fieldWithGetter;

        @JsonSerialize(as=MyImpl.class)
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
        List<Property> expectedProps = new ArrayList<>();
        expectedProps.add(new Property("fieldWithGetter", int.class, MyDto.class.getDeclaredMethod("getFieldWithGetter")));
        expectedProps.add(new Property("interfaceField", MyImpl.class, MyDto.class.getDeclaredField("interfaceField")));
        expectedProps.add(new Property("computedValue", String.class, MyDto.class.getDeclaredMethod("getComputedValue")));
        expectedProps.add(new Property("publicFieldRenamed", String.class, MyDto.class.getDeclaredField("publicField")));
        assertEquals(expectedProps, properties);
    }
}
