package org.hildan.livedoc.core.readers.javadoc;

import java.lang.reflect.Method;
import java.util.Optional;

import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link TypeDocReader} that reads the Javadoc to build the documentation of types.
 */
public class JavadocTypeDocReader implements TypeDocReader {

    @NotNull
    @Override
    public Optional<ApiTypeDoc> buildTypeDocBase(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        ApiTypeDoc doc = new ApiTypeDoc(clazz);
        doc.setName(clazz.getSimpleName());
        doc.setDescription(JavadocHelper.getJavadocDescription(clazz).orElse(null));
        return Optional.of(doc);
    }

    @NotNull
    @Override
    public Optional<ApiPropertyDoc> buildPropertyDoc(Property property, ApiTypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        ApiPropertyDoc doc = new ApiPropertyDoc();
        doc.setName(property.getName());
        Method getter = property.getGetter();
        if (getter != null) {
            doc.setDescription(JavadocHelper.getReturnDescription(getter).orElse(null));
        }
        return Optional.of(doc);
    }
}
