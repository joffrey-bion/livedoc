package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;

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
        ApiResponseObjectDoc apiResponseObjectDoc = new ApiResponseObjectDoc(
                LivedocTypeBuilder.build(new LivedocType(), method.getReturnType(), method.getGenericReturnType()));

        if (method.getReturnType().isAssignableFrom(ResponseEntity.class)) {
            apiResponseObjectDoc.getLivedocType().getType().remove(0);
        }

        return apiResponseObjectDoc;
    }
}
