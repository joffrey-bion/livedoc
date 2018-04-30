package org.hildan.livedoc.core.readers.combined;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link TypeDocReader} that merges the result of multiple {@code TypeDocReader}s. This allows for
 * easier processing of the readers' output, because it encapsulates the fact that there are multiple readers involved.
 */
public class CombinedTypeDocReader implements TypeDocReader {

    private final List<TypeDocReader> typeDocReaders;

    private final DocMerger docMerger;

    /**
     * Creates a new {@link CombinedDocReader} combining the given {@link TypeDocReader}s.
     *
     * @param typeDocReaders
     *         the {@link TypeDocReader}s to use to create the combined documentation of the types mentioned in the API.
     *         They are called in the given order, and the last reader's output takes precedence over previous ones
     *         during the merge.
     */
    public CombinedTypeDocReader(List<TypeDocReader> typeDocReaders) {
        this.typeDocReaders = typeDocReaders;
        this.docMerger = new DocMerger();
    }

    @NotNull
    @Override
    public Optional<TypeDoc> buildTypeDocBase(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        return readFromAllReadersAndMerge(r -> r.buildTypeDocBase(clazz, typeReferenceProvider, templateProvider));
    }

    @NotNull
    @Override
    public Optional<PropertyDoc> buildPropertyDoc(Property property, TypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        return readFromAllReadersAndMerge(r -> r.buildPropertyDoc(property, parentDoc, typeReferenceProvider));
    }

    private <D> Optional<D> readFromAllReadersAndMerge(Function<TypeDocReader, Optional<D>> buildDoc) {
        return typeDocReaders.stream()
                             .map(buildDoc)
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .reduce(docMerger::merge);
    }
}
