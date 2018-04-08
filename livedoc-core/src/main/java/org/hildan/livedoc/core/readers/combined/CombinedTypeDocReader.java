package org.hildan.livedoc.core.readers.combined;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hildan.livedoc.core.merger.DocMerger;
import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.scanners.properties.FieldPropertyScanner;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

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
        this.docMerger = new DocMerger(new FieldPropertyScanner());
    }

    @NotNull
    @Override
    public Optional<ApiTypeDoc> buildTypeDocBase(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        return readFromAllReadersAndMerge(r -> r.buildTypeDocBase(clazz, typeReferenceProvider, templateProvider));
    }

    @NotNull
    @Override
    public Optional<ApiPropertyDoc> buildPropertyDoc(Property property, ApiTypeDoc parentDoc,
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
