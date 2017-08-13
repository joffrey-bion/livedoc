package org.hildan.livedoc.springmvc.scanner;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.flow.ApiFlowSet;
import org.hildan.livedoc.core.annotation.global.ApiChangelogSet;
import org.hildan.livedoc.core.annotation.global.ApiGlobal;
import org.hildan.livedoc.core.annotation.global.ApiMigrationSet;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.JSONDocTemplate;
import org.hildan.livedoc.core.scanner.AbstractJSONDocScanner;
import org.hildan.livedoc.core.scanner.builder.JSONDocApiDocBuilder;
import org.hildan.livedoc.core.scanner.builder.JSONDocApiMethodDocBuilder;
import org.hildan.livedoc.core.scanner.builder.JSONDocApiObjectDocBuilder;
import org.hildan.livedoc.core.util.JSONDocUtils;
import org.hildan.livedoc.springmvc.scanner.builder.SpringConsumesBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringHeaderBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringObjectBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringPathVariableBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringProducesBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringQueryParamBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringRequestBodyBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringResponseStatusBuilder;
import org.hildan.livedoc.springmvc.scanner.builder.SpringVerbBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class AbstractSpringJSONDocScanner extends AbstractJSONDocScanner {

    @Override
    public Set<Method> jsondocMethods(Class<?> controller) {
        Set<Method> annotatedMethods = new LinkedHashSet<>();
        for (Method method : controller.getDeclaredMethods()) {
            if (shouldDocumentMethod(method)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    protected boolean shouldDocumentMethod(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    @Override
    public Set<Class<?>> jsondocObjects(List<String> packages) {
        return new ObjectsScanner(reflections).findJsondocObjects(packages);
    }

    @Override
    public Set<Class<?>> jsondocFlows() {
        return reflections.getTypesAnnotatedWith(ApiFlowSet.class, true);
    }

    /**
     * ApiDoc is initialized with the Controller's simple class name.
     */
    @Override
    public ApiDoc initApiDoc(Class<?> controller) {
        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setName(controller.getSimpleName());
        apiDoc.setDescription(controller.getSimpleName());
        return apiDoc;
    }

    /**
     * Once the ApiDoc has been initialized and filled with other data (version, auth, etc) it's time to merge the
     * documentation with JSONDoc annotation, if existing.
     */
    @Override
    public ApiDoc mergeApiDoc(Class<?> controller, ApiDoc apiDoc) {
        ApiDoc jsondocApiDoc = JSONDocApiDocBuilder.build(controller);
        BeanUtils.copyProperties(jsondocApiDoc, apiDoc, "methods", "supportedversions", "auth");
        return apiDoc;
    }

    @Override
    public ApiMethodDoc initApiMethodDoc(Method method, Map<Class<?>, JSONDocTemplate> jsondocTemplates) {
        ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
        apiMethodDoc.setPath(SpringPathBuilder.buildPath(method));
        apiMethodDoc.setMethod(method.getName());
        apiMethodDoc.setVerb(SpringVerbBuilder.buildVerb(method));
        apiMethodDoc.setProduces(SpringProducesBuilder.buildProduces(method));
        apiMethodDoc.setConsumes(SpringConsumesBuilder.buildConsumes(method));
        apiMethodDoc.setHeaders(SpringHeaderBuilder.buildHeaders(method));
        apiMethodDoc.setPathparameters(SpringPathVariableBuilder.buildPathVariable(method));
        apiMethodDoc.setQueryparameters(SpringQueryParamBuilder.buildQueryParams(method));
        apiMethodDoc.setBodyobject(SpringRequestBodyBuilder.buildRequestBody(method));
        apiMethodDoc.setResponse(SpringResponseBuilder.buildResponse(method));
        apiMethodDoc.setResponsestatuscode(SpringResponseStatusBuilder.buildResponseStatusCode(method));

        Integer index = JSONDocUtils.getIndexOfParameterWithAnnotation(method, RequestBody.class);
        if (index != -1) {
            apiMethodDoc.getBodyobject().setJsondocTemplate(jsondocTemplates.get(method.getParameterTypes()[index]));
        }

        return apiMethodDoc;
    }

    @Override
    public ApiMethodDoc mergeApiMethodDoc(Method method, ApiMethodDoc apiMethodDoc) {
        if (method.isAnnotationPresent(ApiMethod.class) && method.getDeclaringClass().isAnnotationPresent(Api.class)) {
            ApiMethodDoc jsondocApiMethodDoc = JSONDocApiMethodDocBuilder.build(method);
            BeanUtils.copyProperties(jsondocApiMethodDoc, apiMethodDoc, "path", "verb", "produces", "consumes",
                    "headers", "pathparameters", "queryparameters", "bodyobject", "response", "responsestatuscode",
                    "apierrors", "supportedversions", "auth", "displayMethodAs");
        }
        return apiMethodDoc;
    }

    @Override
    public ApiObjectDoc initApiObjectDoc(Class<?> clazz) {
        return SpringObjectBuilder.buildObject(clazz);
    }

    @Override
    public ApiObjectDoc mergeApiObjectDoc(Class<?> clazz, ApiObjectDoc apiObjectDoc) {
        if (clazz.isAnnotationPresent(ApiObject.class)) {
            ApiObjectDoc jsondocApiObjectDoc = JSONDocApiObjectDocBuilder.build(clazz);
            BeanUtils.copyProperties(jsondocApiObjectDoc, apiObjectDoc);
        }
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
