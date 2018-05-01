package org.hildan.livedoc.springmvc.readers.messages;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.annotations.messages.StompCommand;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.readers.mappings.MappingsResolver;
import org.hildan.livedoc.springmvc.readers.mappings.PathVariableReader;
import org.hildan.livedoc.springmvc.readers.payload.MessageMappingPayloadFinder;
import org.hildan.livedoc.springmvc.readers.payload.SubscribeMappingPayloadFinder;
import org.hildan.livedoc.springmvc.utils.ClasspathUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

public class MessageHandlerReader {

    public static List<AsyncMessageDoc> read(@NotNull Method method, @NotNull Class<?> controller,
            @NotNull TypeReferenceProvider typeReferenceProvider) {
        if (ClasspathUtils.isMessageMappingOnClasspath() && method.isAnnotationPresent(MessageMapping.class)) {
            return buildAsyncSendDoc(method, controller, typeReferenceProvider);
        }
        if (ClasspathUtils.isSubscribeMappingOnClasspath() && method.isAnnotationPresent(SubscribeMapping.class)) {
            return buildAsyncSubscribeDoc(method, controller, typeReferenceProvider);
        }
        return Collections.emptyList();
    }

    private static List<AsyncMessageDoc> buildAsyncSendDoc(Method method, Class<?> controller,
            TypeReferenceProvider typeReferenceProvider) {
        StompCommand command = StompCommand.SEND;
        List<String> destinations = MappingsResolver.getMessageMappings(method, controller);
        LivedocType payloadType = MessageMappingPayloadFinder.getPayloadType(method, typeReferenceProvider);
        return buildAsyncMessageDoc(method, typeReferenceProvider, command, destinations, payloadType);
    }

    private static List<AsyncMessageDoc> buildAsyncSubscribeDoc(Method method, Class<?> controller,
            TypeReferenceProvider typeReferenceProvider) {
        StompCommand command = StompCommand.SUBSCRIBE;
        List<String> destinations = MappingsResolver.getSubscribeMappings(method, controller);
        LivedocType payloadType = SubscribeMappingPayloadFinder.getPayloadType(method, typeReferenceProvider);
        return buildAsyncMessageDoc(method, typeReferenceProvider, command, destinations, payloadType);
    }

    @NotNull
    private static List<AsyncMessageDoc> buildAsyncMessageDoc(Method method,
            TypeReferenceProvider typeReferenceProvider, StompCommand command, List<String> destinations,
            LivedocType payloadType) {
        AsyncMessageDoc doc = new AsyncMessageDoc();
        doc.setName(method.getName());
        doc.setDescription(JavadocHelper.getJavadocDescription(method).orElse(null));
        doc.setCommand(command);
        doc.setDestinations(destinations);
        doc.setDestinationVariables(PathVariableReader.buildDestinationVariableDocs(method, typeReferenceProvider));
        doc.setPayloadType(payloadType);
        List<AsyncMessageDoc> triggeredMessages = TriggeredMessagesReader.buildTriggeredMessagesDocs(method,
                typeReferenceProvider);
        doc.setTriggeredMessages(triggeredMessages);

        List<AsyncMessageDoc> docs = new ArrayList<>();
        docs.add(doc);
        docs.addAll(triggeredMessages);
        return docs;
    }

}
