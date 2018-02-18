package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.hildan.livedoc.core.model.doc.auth.ApiAuthDoc;
import org.hildan.livedoc.core.model.doc.ApiAuthType;

public class ApiAuthDocReader {

    public static final String ANONYMOUS = "anonymous";

    public static ApiAuthDoc read(Class<?> controller) {
        if (controller.isAnnotationPresent(ApiAuthNone.class)) {
            return readFromApiAuthNoneAnnotation(controller.getAnnotation(ApiAuthNone.class));
        }
        if (controller.isAnnotationPresent(ApiAuthBasic.class)) {
            return readFromApiAuthBasicAnnotation(controller.getAnnotation(ApiAuthBasic.class));
        }
        if (controller.isAnnotationPresent(ApiAuthToken.class)) {
            return readFromApiAuthTokenAnnotation(controller.getAnnotation(ApiAuthToken.class));
        }
        return null;
    }

    public static ApiAuthDoc read(Method method) {
        if (method.isAnnotationPresent(ApiAuthNone.class)) {
            return readFromApiAuthNoneAnnotation(method.getAnnotation(ApiAuthNone.class));
        }
        if (method.isAnnotationPresent(ApiAuthBasic.class)) {
            return readFromApiAuthBasicAnnotation(method.getAnnotation(ApiAuthBasic.class));
        }
        if (method.isAnnotationPresent(ApiAuthToken.class)) {
            return readFromApiAuthTokenAnnotation(method.getAnnotation(ApiAuthToken.class));
        }
        return read(method.getDeclaringClass());
    }

    private static ApiAuthDoc readFromApiAuthNoneAnnotation(ApiAuthNone annotation) {
        ApiAuthDoc apiAuthDoc = new ApiAuthDoc();
        apiAuthDoc.setType(ApiAuthType.NONE);
        apiAuthDoc.addRole(ANONYMOUS);
        return apiAuthDoc;
    }

    private static ApiAuthDoc readFromApiAuthBasicAnnotation(ApiAuthBasic annotation) {
        ApiAuthDoc apiAuthDoc = new ApiAuthDoc();
        apiAuthDoc.setType(ApiAuthType.BASIC_AUTH);
        apiAuthDoc.setRoles(Arrays.asList(annotation.roles()));
        for (ApiAuthBasicUser testuser : annotation.testUsers()) {
            apiAuthDoc.addTestUser(testuser.username(), testuser.password());
        }
        return apiAuthDoc;
    }

    private static ApiAuthDoc readFromApiAuthTokenAnnotation(ApiAuthToken annotation) {
        ApiAuthDoc apiAuthDoc = new ApiAuthDoc();
        apiAuthDoc.setType(ApiAuthType.TOKEN);
        apiAuthDoc.setScheme(annotation.scheme());
        apiAuthDoc.setRoles(Arrays.asList(annotation.roles()));
        for (String testtoken : annotation.testTokens()) {
            apiAuthDoc.addTestToken(testtoken);
        }
        return apiAuthDoc;
    }
}
