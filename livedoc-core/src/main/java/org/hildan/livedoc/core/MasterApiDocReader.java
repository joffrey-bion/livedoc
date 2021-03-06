package org.hildan.livedoc.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.exceptions.LivedocException;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.validators.ApiOperationDocDefaults;
import org.hildan.livedoc.core.validators.ApiOperationDocValidator;
import org.hildan.livedoc.core.validators.AsyncMessageDocDefaults;

public class MasterApiDocReader {

    private final DocReader docReader;

    public MasterApiDocReader(DocReader docReader) {
        this.docReader = docReader;
    }

    public List<ApiDoc> readApiDocs(TypeReferenceProvider typeReferenceProvider, TemplateProvider templateProvider) {
        return buildDocs(docReader.findControllerTypes(), c -> readApiDoc(c, typeReferenceProvider, templateProvider));
    }

    public Optional<ApiDoc> readApiDoc(Class<?> controller, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        Optional<ApiDoc> doc = docReader.buildApiDocBase(controller);
        doc.ifPresent(apiDoc -> {
            apiDoc.setOperations(readApiOperationDocs(controller, apiDoc, typeReferenceProvider, templateProvider));
            apiDoc.setMessages(readAsyncMessages(controller, apiDoc, typeReferenceProvider, templateProvider));
        });
        return doc;
    }

    private List<ApiOperationDoc> readApiOperationDocs(Class<?> controller, ApiDoc doc,
            TypeReferenceProvider typeReferenceProvider, TemplateProvider templateProvider) {
        List<ApiOperationDoc> docs = buildDocs(getApiOperationMethods(controller),
                m -> readApiOperationDoc(m, controller, doc, typeReferenceProvider, templateProvider));
        validateIds(docs);
        return docs;
    }

    private static void validateIds(List<ApiOperationDoc> docs) {
        Map<String, ApiOperationDoc> seenIds = new HashMap<>();
        for (ApiOperationDoc doc : docs) {
            String id = doc.getLivedocId();
            ApiOperationDoc other = seenIds.get(id);
            if (other != null) {
                String error = String.format("Duplicate ID \"%s\" for operations %s and %s. If these operations only "
                        + "differ by mime type, please customize at least one of their IDs using @ApiOperation(id = "
                        + "...)", id, doc, other);
                throw new LivedocException(error);
            }
            seenIds.put(id, doc);
        }
    }

    private List<Method> getApiOperationMethods(Class<?> controller) {
        return getAllMethods(controller).stream()
                                        .filter(m -> docReader.isApiOperation(m, controller))
                                        .collect(Collectors.toList());
    }

    private Optional<ApiOperationDoc> readApiOperationDoc(Method method, Class<?> controller, ApiDoc parentApiDoc,
            TypeReferenceProvider typeReferenceProvider, TemplateProvider templateProvider) {
        Optional<ApiOperationDoc> doc = docReader.buildApiOperationDoc(method, controller, parentApiDoc,
                typeReferenceProvider, templateProvider);
        doc.ifPresent(apiOperationDoc -> {
            ApiOperationDocDefaults.complete(apiOperationDoc);
            ApiOperationDocValidator.validate(apiOperationDoc);
        });
        return doc;
    }

    private List<AsyncMessageDoc> readAsyncMessages(Class<?> controller, ApiDoc doc,
            TypeReferenceProvider typeReferenceProvider, TemplateProvider templateProvider) {
        List<Method> methods = getMethodsUsingMessages(controller);
        return docReader.buildAsyncMessageDocs(methods, controller, doc, typeReferenceProvider, templateProvider)
                        .stream()
                        .peek(AsyncMessageDocDefaults::complete)
                        .sorted()
                        .collect(Collectors.toList());
    }

    private List<Method> getMethodsUsingMessages(Class<?> controller) {
        return getAllMethods(controller).stream()
                                        .filter(m -> docReader.usesAsyncMessages(m, controller))
                                        .collect(Collectors.toList());
    }

    private static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Method[] declaredMethods = currentClass.getDeclaredMethods();
            Collections.addAll(methods, declaredMethods);
            currentClass = currentClass.getSuperclass();
        }
        return methods;
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
