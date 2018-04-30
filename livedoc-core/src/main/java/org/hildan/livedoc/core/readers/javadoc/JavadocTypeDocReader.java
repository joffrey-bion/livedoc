package org.hildan.livedoc.core.readers.javadoc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link TypeDocReader} that reads the Javadoc to build the documentation of types.
 */
public class JavadocTypeDocReader implements TypeDocReader {

    @NotNull
    @Override
    public Optional<TypeDoc> buildTypeDocBase(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        TypeDoc doc = new TypeDoc(clazz);
        doc.setName(clazz.getSimpleName());
        doc.setDescription(JavadocHelper.getJavadocDescription(clazz).orElse(null));
        return Optional.of(doc);
    }

    @NotNull
    @Override
    public Optional<PropertyDoc> buildPropertyDoc(Property property, TypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        PropertyDoc doc = new PropertyDoc();
        doc.setName(property.getName());
        doc.setDescription(getPropertyDescription(property));
        return Optional.of(doc);
    }

    @Nullable
    private static String getPropertyDescription(Property property) {
        Field field = property.getField();
        if (field != null) {
            Optional<String> description = JavadocHelper.getJavadocDescription(field);
            if (description.isPresent()) {
                return description.get();
            }
        }
        Method getter = property.getGetter();
        if (getter != null) {
            Optional<String> returnDesc = JavadocHelper.getReturnDescription(getter);
            return returnDesc.orElse(JavadocHelper.getJavadocDescription(getter).orElse(null));
        }
        return null;
    }
}
