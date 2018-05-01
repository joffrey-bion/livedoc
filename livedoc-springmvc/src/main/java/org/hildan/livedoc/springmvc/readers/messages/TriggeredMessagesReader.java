package org.hildan.livedoc.springmvc.readers.messages;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.annotations.messages.StompCommand;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.readers.mappings.PathUtils;
import org.hildan.livedoc.springmvc.readers.payload.SpringReturnTypeReader;
import org.hildan.livedoc.springmvc.utils.ClasspathUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;

class TriggeredMessagesReader {

    static List<AsyncMessageDoc> buildTriggeredMessagesDocs(Method method,
            TypeReferenceProvider typeReferenceProvider) {
        List<AsyncMessageDoc> docs = new ArrayList<>();
        if (ClasspathUtils.isSendToOnClasspath() && method.isAnnotationPresent(SendTo.class)) {
            SendTo sendTo = method.getAnnotation(SendTo.class);
            docs.add(readSendToAnnotation(method, sendTo, typeReferenceProvider));
        }
        if (ClasspathUtils.isSendToUserOnClasspath() && method.isAnnotationPresent(SendToUser.class)) {
            SendToUser sendToUser = method.getAnnotation(SendToUser.class);
            docs.add(readSendToUserAnnotation(method, sendToUser, typeReferenceProvider));
        }
        return docs;
    }

    private static AsyncMessageDoc readSendToAnnotation(Method method, SendTo annotation,
            TypeReferenceProvider typeReferenceProvider) {
        return createTriggeredMessageDoc(method, typeReferenceProvider, Arrays.asList(annotation.value()));
    }

    private static AsyncMessageDoc readSendToUserAnnotation(Method method, SendToUser annotation,
            TypeReferenceProvider typeReferenceProvider) {
        List<String> destinations = Arrays.asList(annotation.value());
        // in reality, /user/{username} is prepended, but the broker will get rid of the username, and the actual path
        // that clients need to subscribe to is effectively only prepended with /user
        List<String> fullDestinations = PathUtils.joinAll(Collections.singletonList("/user"), destinations);
        return createTriggeredMessageDoc(method, typeReferenceProvider, fullDestinations);
    }

    @NotNull
    private static AsyncMessageDoc createTriggeredMessageDoc(Method method, TypeReferenceProvider typeReferenceProvider,
            List<String> destinations) {
        /*
        The current method does not define this message; it merely triggers it. This is why a lot of information is
        unavailable here: name, headers, destination variables, etc.
         */
        AsyncMessageDoc doc = new AsyncMessageDoc();
        doc.setCommand(StompCommand.SUBSCRIBE);
        doc.setDescription(JavadocHelper.getReturnDescription(method).orElse(null));
        doc.setDestinations(destinations);
        doc.setPayloadType(typeReferenceProvider.getReference(SpringReturnTypeReader.getActualReturnType(method)));
        return doc;
    }
}
