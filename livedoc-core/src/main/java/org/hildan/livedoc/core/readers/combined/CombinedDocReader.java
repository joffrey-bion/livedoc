package org.hildan.livedoc.core.readers.combined;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.LivedocReaderBuilder;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.readers.annotation.LivedocAnnotationDocReader;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of {@link DocReader} that merges the result of multiple {@code DocReader}s. This allows for easier
 * processing of the readers' output, because it encapsulates the fact that there are multiple readers involved.
 */
public class CombinedDocReader implements DocReader {

    private final List<DocReader> docReaders;

    private final DocMerger docMerger;

    /**
     * Creates a new {@link CombinedDocReader} combining the given {@link DocReader}s.
     *
     * @param docReaders
     *         the {@link DocReader}s to use to create the combined documentation. They are called in the given order,
     *         and the last reader's output takes precedence over previous ones during the merge. This is why, in most
     *         cases, the {@link LivedocAnnotationDocReader} should be put last, so that the user is always able to
     *         override the final result using Livedoc annotations. The {@link LivedocReaderBuilder} does this by
     *         default.
     */
    public CombinedDocReader(List<DocReader> docReaders) {
        this.docReaders = docReaders;
        this.docMerger = new DocMerger();
    }

    @NotNull
    @Override
    public Collection<Class<?>> findControllerTypes() {
        return docReaders.stream()
                         .map(DocReader::findControllerTypes)
                         .flatMap(Collection::stream)
                         .collect(Collectors.toSet());
    }

    @NotNull
    @Override
    public Optional<ApiDoc> buildApiDocBase(@NotNull Class<?> controllerType) {
        return readFromAllReadersAndMerge(r -> r.buildApiDocBase(controllerType));
    }

    @Override
    public boolean isApiOperation(@NotNull Method method, @NotNull Class<?> controller) {
        return docReaders.stream().anyMatch(r -> r.isApiOperation(method, controller));
    }

    @NotNull
    @Override
    public Optional<ApiOperationDoc> buildApiOperationDoc(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        return readFromAllReadersAndMerge(
                r -> r.buildApiOperationDoc(method, controller, parentApiDoc, typeReferenceProvider, templateProvider));
    }

    @Override
    public boolean usesAsyncMessages(@NotNull Method method, @NotNull Class<?> controller) {
        return docReaders.stream().anyMatch(r -> r.usesAsyncMessages(method, controller));
    }

    @NotNull
    @Override
    public List<AsyncMessageDoc> buildAsyncMessageDocs(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull ApiDoc parentApiDoc, @NotNull TypeReferenceProvider typeReferenceProvider,
            @NotNull TemplateProvider templateProvider) {
        return readFromAllReadersAndMergeLists(
                r -> r.buildAsyncMessageDocs(method, controller, parentApiDoc, typeReferenceProvider, templateProvider),
                AsyncMessageDoc::getDestinations);
    }

    private <D> Optional<D> readFromAllReadersAndMerge(Function<DocReader, Optional<D>> buildDoc) {
        return docReaders.stream()
                         .map(buildDoc)
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .reduce(docMerger::merge);
    }

    private <D> List<D> readFromAllReadersAndMergeLists(Function<DocReader, List<D>> buildDoc,
            Function<D, ?> keyExtractor) {
        return docReaders.stream()
                         .map(buildDoc)
                         .filter(l -> !l.isEmpty())
                         .reduce((l1, l2) -> docMerger.mergeList(l1, l2, keyExtractor))
                         .orElse(Collections.emptyList());
    }
}
