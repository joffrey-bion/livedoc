package org.hildan.livedoc.springmvc.readers;

import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SpringResponseStatusBuilder {

    public static String buildResponseStatusCode(Method method) {
        ResponseStatus responseStatus = method.getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value().toString() + " - " + responseStatus.value().getReasonPhrase();
        }
        return HttpStatus.OK.toString() + " - " + "OK";
    }

}
