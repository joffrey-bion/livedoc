package org.hildan.livedoc.core;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.Property;
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

    List<TypeDoc> readTypeDocs(Collection<Class<?>> types, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        return buildDocs(types, t -> readTypeDoc(t, typeReferenceProvider, templateProvider));
    }

    @NotNull
    public Optional<TypeDoc> readTypeDoc(@NotNull Class<?> clazz, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        Optional<TypeDoc> doc = typeDocReader.buildTypeDocBase(clazz, typeReferenceProvider, templateProvider);
        doc.ifPresent(typeDoc -> typeDoc.setFields(buildPropertyDocs(clazz, typeDoc, typeReferenceProvider)));
        doc.ifPresent(ApiTypeDocValidator::validate);
        return doc;
    }

    private List<PropertyDoc> buildPropertyDocs(Class<?> clazz, TypeDoc typeDoc,
            TypeReferenceProvider typeRefProvider) {
        if (clazz.isEnum()) {
            return Collections.emptyList();
        }
        List<Property> props = propertyScanner.getProperties(clazz);
        return buildDocs(props, p -> typeDocReader.buildPropertyDoc(p, typeDoc, typeRefProvider));
    }

    private static <T, D extends Comparable<D>> List<D> buildDocs(Collection<T> objects,
            Function<T, Optional<D>> read) {
        return objects.stream()
                      .map(read)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .sorted()
                      .collect(Collectors.toList());
    }
}
