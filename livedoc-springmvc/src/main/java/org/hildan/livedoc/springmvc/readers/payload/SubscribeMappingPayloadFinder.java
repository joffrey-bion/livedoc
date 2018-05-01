package org.hildan.livedoc.springmvc.readers.payload;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.springmvc.utils.ClasspathUtils;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;

public class SubscribeMappingPayloadFinder {

    public static LivedocType getPayloadType(Method method, TypeReferenceProvider typeReferenceProvider) {
        if (responseSentToOtherDestination(method)) {
            // here we just can't know what the payload type of this subscription is
            // because the return value is not sent on this subscription
            return null;
        }
        Type type = SpringReturnTypeReader.getActualReturnType(method);
        return typeReferenceProvider.getReference(type);
    }

    private static boolean responseSentToOtherDestination(Method method) {
        if (ClasspathUtils.isSendToOnClasspath() && method.isAnnotationPresent(SendTo.class)) {
            return true;
        }
        if (ClasspathUtils.isSendToUserOnClasspath() && method.isAnnotationPresent(SendToUser.class)) {
            return true;
        }
        return false;
    }
}
