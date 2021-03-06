package org.hildan.livedoc.springmvc.readers.payload;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class MessageMappingPayloadFinder {

    private static final List<Class<?>> NON_BODY_PARAM_TYPES = Arrays.asList(MessageHeaders.class,
            MessageHeaderAccessor.class, Principal.class);

    private static final List<Class<? extends Annotation>> NON_BODY_PARAM_ANNOTATIONS = Arrays.asList(Header.class,
            Headers.class, DestinationVariable.class);

    public static LivedocType getPayloadType(Method method, TypeReferenceProvider typeReferenceProvider) {
        int index = getBodyParamIndex(method);
        if (index < 0) {
            if (hasWayOfAccessingMessagePayload(method)) {
                // we don't know the payload type, but maybe this method expects one anyway
                return null;
            } else {
                // this method doesn't have access to the message payload, so we don't really expect any
                return typeReferenceProvider.getReference(void.class);
            }
        }
        Type bodyParamType = method.getGenericParameterTypes()[index];
        return typeReferenceProvider.getReference(bodyParamType);
    }

    private static int getBodyParamIndex(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramTypes.length; i++) {
            if (isBodyParam(paramTypes[i], paramAnnotations[i])) {
                return i;
            }
        }
        // no body param found
        return -1;
    }

    private static boolean isBodyParam(Class<?> paramType, Annotation[] paramAnnotations) {
        if (paramType.equals(Message.class)) {
            // it is too generic to be useful in the API doc even if it is indeed the body param
            return false;
        }
        if (NON_BODY_PARAM_TYPES.contains(paramType)) {
            return false;
        }
        for (Annotation paramAnnotation : paramAnnotations) {
            // guarantees this param is the payload
            if (paramAnnotation instanceof Payload) {
                return true;
            }
            // guarantees this param is NOT the payload by definition of the annotation
            if (isNonBodyParamAnnotation(paramAnnotation)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNonBodyParamAnnotation(Annotation paramAnnotation) {
        for (Class<? extends Annotation> annotation : NON_BODY_PARAM_ANNOTATIONS) {
            if (annotation.isInstance(paramAnnotation)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasWayOfAccessingMessagePayload(Method method) {
        return Arrays.stream(method.getParameterTypes()).anyMatch(Message.class::equals);
    }
}
