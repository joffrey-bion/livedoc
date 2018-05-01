package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.messages.ApiMessageChannel;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

public class MessagesDocReader {

    public List<AsyncMessageDoc> readAsyncMessages(Method method, TypeReferenceProvider typeReferenceProvider) {
        ApiMessageChannel[] annotations = method.getAnnotationsByType(ApiMessageChannel.class);
        return Arrays.stream(annotations)
                     .map(a -> createMessageDoc(a, method, typeReferenceProvider))
                     .collect(Collectors.toList());
    }

    private AsyncMessageDoc createMessageDoc(ApiMessageChannel a, Method method,
            TypeReferenceProvider typeReferenceProvider) {
        AsyncMessageDoc doc = new AsyncMessageDoc();
        doc.setName(a.name());
        doc.setSummary(a.summary());
        doc.setDescription(a.description());
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
