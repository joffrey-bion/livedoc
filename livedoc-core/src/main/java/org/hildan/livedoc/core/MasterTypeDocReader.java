package org.hildan.livedoc.core;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.validators.ApiTypeDocValidator;
import org.jetbrains.annotations.NotNull;

public class MasterTypeDocReader {

    private final TypeDocReader typeDocReader;

    private final PropertyScanner propertyScanner;

    public MasterTypeDocReader(TypeDocReader typeDocReader, PropertyScanner propertyScanner) {
        this.typeDocReader = typeDocReader;
        this.propertyScanner = propertyScanner;
    }

    @NotNull
    public Optional<ApiTypeDoc> buildTypeDoc(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        Optional<ApiTypeDoc> doc = typeDocReader.buildTypeDocBase(clazz, typeReferenceProvider, templateProvider);
        doc.ifPresent(typeDoc -> typeDoc.setFields(buildPropertyDocs(clazz, typeDoc, typeReferenceProvider)));
        doc.ifPresent(ApiTypeDocValidator::validate);
        return doc;
    }

    private List<ApiPropertyDoc> buildPropertyDocs(Class<?> clazz, ApiTypeDoc apiTypeDoc,
            TypeReferenceProvider typeRefProvider) {
        if (clazz.isEnum()) {
            return Collections.emptyList();
        }
        return propertyScanner.getProperties(clazz)
                              .stream()
                              .map(property -> typeDocReader.buildPropertyDoc(property, apiTypeDoc, typeRefProvider))
                              .filter(Optional::isPresent)
                              .map(Optional::get)
                              .sorted()
                              .collect(Collectors.toList());
    }
}
