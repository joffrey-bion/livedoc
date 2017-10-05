package org.hildan.livedoc.springmvc.scanner.builder.body;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.util.LivedocUtils;
import org.springframework.web.bind.annotation.RequestBody;

public class RequestMappingBodyFinder {

    public static int getBodyParamIndex(Method method) {
        return LivedocUtils.getIndexOfParameterWithAnnotation(method, RequestBody.class);
    }
}
