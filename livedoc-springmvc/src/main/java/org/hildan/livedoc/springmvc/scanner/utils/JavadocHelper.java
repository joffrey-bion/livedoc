package org.hildan.livedoc.springmvc.scanner.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.DefaultParameterNameDiscoverer;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.ParamJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;

public class JavadocHelper {

    private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final CommentFormatter COMMENT_FORMATTER = new CommentFormatter();

    @NotNull
    public static Optional<String> getJavadocDescription(@NotNull Method method) {
        return getMethodJavadoc(method).map(jd -> COMMENT_FORMATTER.format(jd.getComment()));
    }

    @NotNull
    private static Optional<MethodJavadoc> getMethodJavadoc(@NotNull Method method) {
        Optional<ClassJavadoc> javadoc = RuntimeJavadoc.getJavadoc(method.getDeclaringClass());
        return javadoc.map(ClassJavadoc::getMethods).flatMap(mDocs -> findMethod(mDocs, method));
    }

    @NotNull
    private static Optional<MethodJavadoc> findMethod(List<MethodJavadoc> methodDocs, Method method) {
        return methodDocs.stream().filter(m -> isMethod(m, method)).findAny();
    }

    private static boolean isMethod(MethodJavadoc methodJavadoc, Method method) {
        boolean sameName = methodJavadoc.getName().equals(method.getName());
        if (!sameName) {
            return false;
        }
        // params are not necessarily ordered in the Javadoc
        Set<String> paramNames = Arrays.stream(PARAM_NAME_DISCOVERER.getParameterNames(method))
                                       .collect(Collectors.toSet());
        Set<String> paramNamesInDoc = methodJavadoc.getParams()
                                                   .stream()
                                                   .map(ParamJavadoc::getName)
                                                   .collect(Collectors.toSet());
        return paramNames.equals(paramNamesInDoc);
    }

    @NotNull
    public static Optional<String> getJavadocDescription(@NotNull Method method, @NotNull String paramName) {
        return getParamDoc(method, paramName).map(jd -> COMMENT_FORMATTER.format(jd.getComment()));
    }

    @NotNull
    private static Optional<ParamJavadoc> getParamDoc(@NotNull Method method, @NotNull String actualParamName) {
        return getMethodJavadoc(method).map(MethodJavadoc::getParams)
                                       .flatMap(pDocs -> findParam(pDocs, actualParamName));
    }

    @NotNull
    private static Optional<ParamJavadoc> findParam(@NotNull List<ParamJavadoc> paramDocs, String actualParamName) {
        return paramDocs.stream().filter(m -> m.getName().equals(actualParamName)).findAny();
    }
}
