package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiResponseObjectDoc;
import org.springframework.http.ResponseEntity;

public class SpringResponseBuilder {

    /**
     * Builds the ApiResponseObjectDoc from the method's return type and checks if the first type corresponds to a
     * ResponseEntity class. In that case removes the "responseentity" string from the final list because it's not
     * important to the documentation user.
     *
     * @param method
     *         the method to create the response object for
     *
     * @return the created {@link ApiResponseObjectDoc}
     */
    public static ApiResponseObjectDoc buildResponse(Method method) {
        Type returnType = getActualReturnType(method);
        LivedocType livedocType = LivedocTypeBuilder.build(returnType);
        return new ApiResponseObjectDoc(livedocType);
    }

    private static Type getActualReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (ResponseEntity.class.equals(method.getReturnType())) {
            returnType = ((ParameterizedType)returnType).getActualTypeArguments()[0];
        }
        return returnType;
    }
}
