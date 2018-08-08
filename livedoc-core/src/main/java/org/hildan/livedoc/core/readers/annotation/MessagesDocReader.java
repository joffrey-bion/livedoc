package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.messages.ApiMessageChannel;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class MessagesDocReader {

    public static List<AsyncMessageDoc> readAsyncMessages(Method method, TypeReferenceProvider typeReferenceProvider) {
        ApiMessageChannel[] annotations = method.getAnnotationsByType(ApiMessageChannel.class);
        return Arrays.stream(annotations)
                     .map(a -> createMessageDoc(a, method, typeReferenceProvider))
                     .collect(Collectors.toList());
    }

    private static AsyncMessageDoc createMessageDoc(ApiMessageChannel a, Method method,
            TypeReferenceProvider typeReferenceProvider) {
        AsyncMessageDoc doc = new AsyncMessageDoc();
        doc.setLivedocId(nullifyIfEmpty(a.id()));
        doc.setName(nullifyIfEmpty(a.name()));
        doc.setSummary(nullifyIfEmpty(a.summary()));
        doc.setDescription(nullifyIfEmpty(a.description()));
        doc.setCommand(a.command());
        doc.setDestinations(Arrays.asList(a.destinations()));
        doc.setDestinationVariables(ApiPathParamDocReader.read(a.destinationVariables(), typeReferenceProvider));
        doc.setHeaders(ApiHeaderDocReader.buildHeaderDocs(a.headers()));
        doc.setPayloadType(typeReferenceProvider.getReference(a.payloadType()));
        doc.setTriggeredMessages(Collections.emptyList());

        // TODO
        //        doc.setAuth();
        //        doc.setStage();
        //        doc.setSupportedVersions();
        return doc;
    }
}
