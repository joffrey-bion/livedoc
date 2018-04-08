package org.hildan.livedoc.core.readers;

import java.util.Optional;

import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

public interface TypeDocReader {

    @NotNull
    Optional<ApiTypeDoc> buildTypeDocBase(@NotNull Class<?> clazz, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider);

    @NotNull
    Optional<ApiPropertyDoc> buildPropertyDoc(Property property, ApiTypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider);
}
