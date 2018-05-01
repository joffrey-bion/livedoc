package org.hildan.livedoc.springmvc.readers.payload;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.springframework.http.ResponseEntity;

import com.google.common.util.concurrent.ListenableFuture;

import static org.hildan.livedoc.springmvc.utils.ClasspathUtils.isCompletableFutureOnClasspath;
import static org.hildan.livedoc.springmvc.utils.ClasspathUtils.isCompletionStageOnClasspath;

public class SpringReturnTypeReader {

    public static Type getActualReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (ResponseEntity.class.equals(method.getReturnType())) {
            return unwrap((ParameterizedType) returnType);
        }
        if (ListenableFuture.class.equals(method.getReturnType())) {
            return unwrap((ParameterizedType) returnType);
        }
        if (isCompletableFutureOnClasspath() && CompletableFuture.class.equals(method.getReturnType())) {
            return unwrap((ParameterizedType) returnType);
        }
        if (isCompletionStageOnClasspath() && CompletionStage.class.equals(method.getReturnType())) {
            return unwrap((ParameterizedType) returnType);
        }
        return returnType;
    }

    private static Type unwrap(ParameterizedType returnType) {
        return returnType.getActualTypeArguments()[0];
    }
}
