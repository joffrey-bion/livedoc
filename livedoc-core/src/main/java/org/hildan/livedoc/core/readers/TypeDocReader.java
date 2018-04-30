package org.hildan.livedoc.core.readers;

import java.util.Optional;

import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Reads the documentation of types used throughout an API.
 */
public interface TypeDocReader {

    /**
     * Builds a base documentation for the given class, without property docs. The properties' doc will be later built
     * using {@link #buildPropertyDoc}, once the base doc is completed by taking into account all readers' output.
     *
     * @param clazz
     *         the class to document
     * @param typeReferenceProvider
     *         a {@link TypeReferenceProvider} to get {@link LivedocType}s for potentially mentioned types
     * @param templateProvider
     *         a {@link TemplateProvider} to generate templates for potentially mentioned types
     *
     * @return the created {@link TypeDoc}, or an empty optional if this reader cannot build a documentation for the
     * given type
     */
    @NotNull
    Optional<TypeDoc> buildTypeDocBase(@NotNull Class<?> clazz, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider);

    /**
     * Builds the documentation for the given property.
     *
     * @param property
     *         the property to document
     * @param parentDoc
     *         the base documentation of the type containing the given property
     * @param typeReferenceProvider
     *         a {@link TypeReferenceProvider} to get {@link LivedocType}s for potentially mentioned types
     *
     * @return the created {@link PropertyDoc}, or an empty optional if this reader cannot build a documentation for the
     * given property
     */
    @NotNull
    Optional<PropertyDoc> buildPropertyDoc(Property property, TypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider);
}
