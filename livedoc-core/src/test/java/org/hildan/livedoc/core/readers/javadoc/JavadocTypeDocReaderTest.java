package org.hildan.livedoc.core.readers.javadoc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
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
         * The name of this {@link Person}.
         */
        private String name;

        private Integer age;

        private String phone;

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
         *
         * @return this person's age
         */
        public Integer getAge() {
            return age;
        }

        /**
         * Gets this person's {@code phone number}.
         */
        public String getPhone() {
            return phone;
        }
    }

    @Before
    public void setUp() {
        typeReferenceProvider = new DefaultTypeReferenceProvider(c -> true);
        reader = new JavadocTypeDocReader();
    }

    @Test
    public void buildTypeDocBase() {
        Optional<TypeDoc> doc = reader.buildTypeDocBase(Person.class, typeReferenceProvider, c -> null);
        assertTrue(doc.isPresent());
        TypeDoc typeDoc = doc.get();
        assertEquals("Person", typeDoc.getName());
        assertEquals("Represents a person.", typeDoc.getDescription());
    }

    @Test
    public void buildPropertyDoc() throws NoSuchMethodException, NoSuchFieldException {
        Field nameField = Person.class.getDeclaredField("name");
        Field ageField = Person.class.getDeclaredField("age");
        Field phoneField = Person.class.getDeclaredField("phone");

        Method getName = Person.class.getDeclaredMethod("getName");
        Method getAge = Person.class.getDeclaredMethod("getAge");
        Method getPhone = Person.class.getDeclaredMethod("getPhone");

        Property nameProp = new Property("name", String.class, getName);
        nameProp.setField(nameField);
        nameProp.setGetter(getName);

        Property ageProp = new Property("age", Integer.class, getAge);
        ageProp.setField(ageField);
        ageProp.setGetter(getAge);

        Property phoneProp = new Property("phone", String.class, getPhone);
        phoneProp.setField(phoneField);
        phoneProp.setGetter(getPhone);

        Optional<TypeDoc> doc = reader.buildTypeDocBase(Person.class, typeReferenceProvider, c -> null);
        assertTrue(doc.isPresent());

        TypeDoc typeDoc = doc.get();
        Optional<PropertyDoc> maybeNameDoc = reader.buildPropertyDoc(nameProp, typeDoc, typeReferenceProvider);
        Optional<PropertyDoc> maybeAgeDoc = reader.buildPropertyDoc(ageProp, typeDoc, typeReferenceProvider);
        Optional<PropertyDoc> maybePhoneDoc = reader.buildPropertyDoc(phoneProp, typeDoc, typeReferenceProvider);
        assertTrue(maybeNameDoc.isPresent());
        assertTrue(maybeAgeDoc.isPresent());
        assertTrue(maybePhoneDoc.isPresent());

        PropertyDoc nameDoc = maybeNameDoc.get();
        PropertyDoc ageDoc = maybeAgeDoc.get();
        PropertyDoc phoneDoc = maybePhoneDoc.get();
        assertEquals("name", nameDoc.getName());
        assertEquals("age", ageDoc.getName());
        assertEquals("phone", phoneDoc.getName());
        // takes field doc if present
        assertEquals("The name of this <code>Person</code>.", nameDoc.getDescription());
        // takes the return description if no field doc
        assertEquals("this person's age", ageDoc.getDescription());
        // takes the method description if neither field doc nor return description
        assertEquals("Gets this person's <code>phone number</code>.", phoneDoc.getDescription());
    }
}
