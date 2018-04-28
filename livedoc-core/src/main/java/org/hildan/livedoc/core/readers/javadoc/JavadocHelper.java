package org.hildan.livedoc.core.readers.javadoc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.Comment;
import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.FieldJavadoc;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.ParamJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;

public class JavadocHelper {

    private static final CommentFormatter COMMENT_FORMATTER = new CommentFormatter();

    private static String formatComment(Comment comment) {
        return COMMENT_FORMATTER.format(comment).trim();
    }

    @NotNull
    public static Optional<String> getJavadocDescription(@NotNull Class<?> clazz) {
        return RuntimeJavadoc.getJavadoc(clazz).map(ClassJavadoc::getComment).map(JavadocHelper::formatComment);
    }

    @NotNull
    public static Optional<String> getJavadocDescription(@NotNull Method method) {
        return RuntimeJavadoc.getJavadoc(method).map(MethodJavadoc::getComment).map(JavadocHelper::formatComment);
    }

    @NotNull
    public static Optional<String> getJavadocDescription(@NotNull Field field) {
        return RuntimeJavadoc.getJavadoc(field).map(FieldJavadoc::getComment).map(JavadocHelper::formatComment);
    }

    @NotNull
    public static Optional<String> getReturnDescription(@NotNull Method method) {
        Optional<String> returnComment = RuntimeJavadoc.getJavadoc(method)
                                           .map(MethodJavadoc::getReturns)
                                           .map(JavadocHelper::formatComment);
        if (!returnComment.isPresent() || returnComment.get().isEmpty()) {
            return getJavadocDescription(method);
        }
        return returnComment;
    }

    @NotNull
    public static Optional<String> getJavadocDescription(@NotNull Method method, @NotNull String paramName) {
        return getParamDoc(method, paramName).map(jd -> COMMENT_FORMATTER.format(jd.getComment()).trim());
    }

    @NotNull
    private static Optional<ParamJavadoc> getParamDoc(@NotNull Method method, @NotNull String actualParamName) {
        return RuntimeJavadoc.getJavadoc(method)
                             .map(MethodJavadoc::getParams)
                             .flatMap(pDocs -> findParam(pDocs, actualParamName));
    }

    @NotNull
    private static Optional<ParamJavadoc> findParam(@NotNull List<ParamJavadoc> paramDocs, String actualParamName) {
        return paramDocs.stream().filter(m -> m.getName().equals(actualParamName)).findAny();
    }
}
