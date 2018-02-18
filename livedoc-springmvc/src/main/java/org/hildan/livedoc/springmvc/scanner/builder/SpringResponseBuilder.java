package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.model.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.springframework.http.ResponseEntity;

public class SpringResponseBuilder {

    /**
     * Builds the response body type from the method's return type.
     * <p>
     * This method handles Spring's {@link ResponseEntity} wrappers by unwrapping them, because they don't matter to the
     * user of the doc.
     *
     * @param method
     *         the method to create the response object for
     *
     * @return the created {@link LivedocType}
     */
    public static LivedocType buildResponse(Method method) {
        Type returnType = getActualReturnType(method);
        return LivedocTypeBuilder.build(returnType);
    }

    private static Type getActualReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (ResponseEntity.class.equals(method.getReturnType())) {
            returnType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
        }
        return returnType;
    }
}
