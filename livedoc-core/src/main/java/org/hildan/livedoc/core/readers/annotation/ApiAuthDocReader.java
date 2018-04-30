package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.hildan.livedoc.core.model.doc.AuthType;
import org.hildan.livedoc.core.model.doc.auth.AuthDoc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiAuthDocReader {

    public static final String ANONYMOUS = "anonymous";

    public static AuthDoc readController(Class<?> controller) {
        return readAuthAnnotations(controller);
    }

    public static AuthDoc readMethod(Method method) {
        AuthDoc doc = readAuthAnnotations(method);
        if (doc != null) {
            return doc;
        }
        return readController(method.getDeclaringClass());
    }

    @Nullable
    private static AuthDoc readAuthAnnotations(AnnotatedElement element) {
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
    private static AuthDoc readFromApiAuthNoneAnnotation(ApiAuthNone annotation) {
        AuthDoc authDoc = new AuthDoc();
        authDoc.setType(AuthType.NONE);
        authDoc.addRole(ANONYMOUS);
        return authDoc;
    }

    @NotNull
    private static AuthDoc readFromApiAuthBasicAnnotation(ApiAuthBasic annotation) {
        AuthDoc authDoc = new AuthDoc();
        authDoc.setType(AuthType.BASIC_AUTH);
        authDoc.setRoles(Arrays.asList(annotation.roles()));
        for (ApiAuthBasicUser testuser : annotation.testUsers()) {
            authDoc.addTestUser(testuser.username(), testuser.password());
        }
        return authDoc;
    }

    @NotNull
    private static AuthDoc readFromApiAuthTokenAnnotation(ApiAuthToken annotation) {
        AuthDoc authDoc = new AuthDoc();
        authDoc.setType(AuthType.TOKEN);
        authDoc.setScheme(nullifyIfEmpty(annotation.scheme()));
        authDoc.setRoles(Arrays.asList(annotation.roles()));
        for (String testtoken : annotation.testTokens()) {
            authDoc.addTestToken(testtoken);
        }
        return authDoc;
    }
}
