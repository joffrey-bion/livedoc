package org.hildan.livedoc.core.readers.javadoc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.types.references.DefaultTypeReferenceProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JavadocTypeDocReaderTest {

    private TypeReferenceProvider typeReferenceProvider;

    private JavadocTypeDocReader reader;

    /**
     * Represents a person.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    private static class Person {

        /**
         * The name of this person.
         */
        private String name;

        /**
         * The age of this person.
         */
        private Integer age;

        /**
         * Gets this person's name.
         *
         * @return this person's name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets this person's age.
         */
        public Integer getAge() {
            return age;
        }
    }

    @Before
    public void setUp() {
        typeReferenceProvider = new DefaultTypeReferenceProvider(c -> true);
        reader = new JavadocTypeDocReader();
    }

    @Test
    public void buildTypeDocBase() {
        Optional<ApiTypeDoc> doc = reader.buildTypeDocBase(Person.class, typeReferenceProvider, c -> null);
        assertTrue(doc.isPresent());
        ApiTypeDoc apiTypeDoc = doc.get();
        assertEquals("Person", apiTypeDoc.getName());
        assertEquals("Represents a person.", apiTypeDoc.getDescription());
    }

    @Test
    public void buildPropertyDoc() throws NoSuchMethodException, NoSuchFieldException {
        Field nameField = Person.class.getDeclaredField("name");
        Field ageField = Person.class.getDeclaredField("age");

        Method getName = Person.class.getDeclaredMethod("getName");
        Method getAge = Person.class.getDeclaredMethod("getAge");

        Property nameProp = new Property("name", String.class, getName);
        nameProp.setField(nameField);
        nameProp.setGetter(getName);

        Property ageProp = new Property("age", Integer.class, getAge);
        ageProp.setField(ageField);
        ageProp.setGetter(getAge);

        Optional<ApiTypeDoc> doc = reader.buildTypeDocBase(Person.class, typeReferenceProvider, c -> null);
        assertTrue(doc.isPresent());

        ApiTypeDoc apiTypeDoc = doc.get();
        Optional<ApiPropertyDoc> maybeNameDoc = reader.buildPropertyDoc(nameProp, apiTypeDoc, typeReferenceProvider);
        Optional<ApiPropertyDoc> maybeAgeDoc = reader.buildPropertyDoc(ageProp, apiTypeDoc, typeReferenceProvider);
        assertTrue(maybeNameDoc.isPresent());
        assertTrue(maybeAgeDoc.isPresent());

        ApiPropertyDoc nameDoc = maybeNameDoc.get();
        ApiPropertyDoc ageDoc = maybeAgeDoc.get();
        assertEquals("name", nameDoc.getName());
        assertEquals("age", ageDoc.getName());
        assertEquals("this person's name", nameDoc.getDescription());
        assertEquals("Gets this person's age.", ageDoc.getDescription());
    }
}
