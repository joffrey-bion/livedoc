package org.hildan.livedoc.core.scanner;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiBodyObject;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotation.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotation.global.ApiGlobal;
import org.hildan.livedoc.core.annotation.global.ApiMigrationSet;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.scanner.templates.ObjectTemplate;
import org.hildan.livedoc.core.scanner.readers.ApiDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiMethodDocReader;
import org.hildan.livedoc.core.scanner.readers.ApiObjectDocReader;
import org.hildan.livedoc.core.util.LivedocUtils;

public class DefaultDocAnnotationScanner extends AbstractDocAnnotationScanner {

    public static final String UNDEFINED = "undefined";

    public static final String ANONYMOUS = "anonymous";

    @Override
    public Set<Class<?>> jsondocControllers() {
        return reflections.getTypesAnnotatedWith(Api.class, true);
    }

    @Override
    public Set<Method> jsondocMethods(Class<?> controller) {
        Set<Method> annotatedMethods = new LinkedHashSet<>();
        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ApiMethod.class)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    @Override
    public Set<Class<?>> jsondocObjects(List<String> packages) {
        return reflections.getTypesAnnotatedWith(ApiObject.class, true);
    }

    @Override
    public Set<Class<?>> jsondocFlows() {
        return reflections.getTypesAnnotatedWith(ApiFlowSet.class, true);
    }

    @Override
    public ApiDoc initApiDoc(Class<?> controller) {
        return ApiDocReader.read(controller);
    }

    @Override
    public ApiDoc mergeApiDoc(Class<?> controller, ApiDoc apiDoc) {
        return apiDoc;
    }

    @Override
    public ApiMethodDoc initApiMethodDoc(Method method, Map<Class<?>, ObjectTemplate> objectTemplates) {
        ApiMethodDoc apiMethodDoc = ApiMethodDocReader.read(method);

        if (method.isAnnotationPresent(ApiBodyObject.class)) {
            apiMethodDoc.getBodyobject()
                        .setTemplate(objectTemplates.get(method.getAnnotation(ApiBodyObject.class).clazz()));
        }
        Integer index = LivedocUtils.getIndexOfParameterWithAnnotation(method, ApiBodyObject.class);
        if (index != -1) {
            apiMethodDoc.getBodyobject().setTemplate(objectTemplates.get(method.getParameterTypes()[index]));
        }

        return apiMethodDoc;
    }

    @Override
    public ApiMethodDoc mergeApiMethodDoc(Method method, ApiMethodDoc apiMethodDoc) {
        return apiMethodDoc;
    }

    @Override
    public ApiObjectDoc initApiObjectDoc(Class<?> clazz) {
        return ApiObjectDocReader.read(clazz);
    }

    @Override
    public ApiObjectDoc mergeApiObjectDoc(Class<?> clazz, ApiObjectDoc apiObjectDoc) {
        return apiObjectDoc;
    }

    @Override
    public Set<Class<?>> jsondocGlobal() {
        return reflections.getTypesAnnotatedWith(ApiGlobal.class, true);
    }

    @Override
    public Set<Class<?>> jsondocChangelogs() {
        return reflections.getTypesAnnotatedWith(ApiChangelogSet.class, true);
    }

    @Override
    public Set<Class<?>> jsondocMigrations() {
        return reflections.getTypesAnnotatedWith(ApiMigrationSet.class, true);
    }

}
