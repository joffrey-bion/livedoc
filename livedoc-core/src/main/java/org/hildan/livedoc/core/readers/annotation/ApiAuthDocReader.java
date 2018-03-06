package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.hildan.livedoc.core.model.doc.ApiAuthType;
import org.hildan.livedoc.core.model.doc.auth.ApiAuthDoc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ApiAuthDocReader {

    public static final String ANONYMOUS = "anonymous";

    public static ApiAuthDoc readController(Class<?> controller) {
        return readAuthAnnotations(controller);
    }

    public static ApiAuthDoc readMethod(Method method) {
        ApiAuthDoc doc = readAuthAnnotations(method);
        if (doc != null) {
            return doc;
        }
        return readController(method.getDeclaringClass());
    }

    @Nullable
    private static ApiAuthDoc readAuthAnnotations(AnnotatedElement element) {
        ApiAuthNone authNone = element.getAnnotation(ApiAuthNone.class);
        if (authNone != null) {
            return readFromApiAuthNoneAnnotation(authNone);
        }
        ApiAuthBasic authBasic = element.getAnnotation(ApiAuthBasic.class);
        if (authBasic != null) {
            return readFromApiAuthBasicAnnotation(authBasic);
        }
        ApiAuthToken authToken = element.getAnnotation(ApiAuthToken.class);
        if (authToken != null) {
            return readFromApiAuthTokenAnnotation(authToken);
        }
        return null;
    }

    @NotNull
    private static ApiAuthDoc readFromApiAuthNoneAnnotation(ApiAuthNone annotation) {
        ApiAuthDoc apiAuthDoc = new ApiAuthDoc();
        apiAuthDoc.setType(ApiAuthType.NONE);
        apiAuthDoc.addRole(ANONYMOUS);
        return apiAuthDoc;
    }

    @NotNull
    private static ApiAuthDoc readFromApiAuthBasicAnnotation(ApiAuthBasic annotation) {
        ApiAuthDoc apiAuthDoc = new ApiAuthDoc();
        apiAuthDoc.setType(ApiAuthType.BASIC_AUTH);
        apiAuthDoc.setRoles(Arrays.asList(annotation.roles()));
        for (ApiAuthBasicUser testuser : annotation.testUsers()) {
            apiAuthDoc.addTestUser(testuser.username(), testuser.password());
        }
        return apiAuthDoc;
    }

    @NotNull
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
