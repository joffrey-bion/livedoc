package org.hildan.livedoc.springmvc.readers.request;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.RequestBodyDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.LivedocUtils;
import org.hildan.livedoc.springmvc.readers.mappings.MappingsResolver;
import org.hildan.livedoc.springmvc.readers.mappings.PathVariableReader;
import org.hildan.livedoc.springmvc.readers.payload.SpringReturnTypeReader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RequestHandlerReader {

    public static ApiOperationDoc buildApiOperationDoc(Method method, Class<?> controller,
            TypeReferenceProvider typeReferenceProvider, TemplateProvider templateProvider) {
        ApiOperationDoc apiOperationDoc = new ApiOperationDoc();
        apiOperationDoc.setPaths(MappingsResolver.getRequestMappings(method, controller));
        apiOperationDoc.setName(method.getName());
        apiOperationDoc.setVerbs(HttpMethodReader.buildVerb(method, controller));
        apiOperationDoc.setProduces(HttpMediaTypeReader.buildProduces(method, controller));
        apiOperationDoc.setConsumes(HttpMediaTypeReader.buildConsumes(method, controller));
        apiOperationDoc.setHeaders(HttpHeadersReader.buildHeadersDoc(method, controller));
        apiOperationDoc.setPathParameters(PathVariableReader.buildPathVariableDocs(method, typeReferenceProvider));
        apiOperationDoc.setQueryParameters(QueryParamReader.buildParamDocs(method, controller, typeReferenceProvider));
        apiOperationDoc.setRequestBody(buildRequestBody(method, typeReferenceProvider, templateProvider));
        apiOperationDoc.setResponseBodyType(getResponseType(method, typeReferenceProvider));
        apiOperationDoc.setResponseStatusCode(buildResponseStatusCode(method));
        return apiOperationDoc;
    }

    private static RequestBodyDoc buildRequestBody(Method method, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        int index = LivedocUtils.getIndexOfParameterWithAnnotation(method, RequestBody.class);
        if (index < 0) {
            return null;
        }
        Type bodyParamType = method.getGenericParameterTypes()[index];
        Object template = templateProvider.getTemplate(bodyParamType);
        LivedocType livedocType = typeReferenceProvider.getReference(bodyParamType);
        return new RequestBodyDoc(livedocType, template);
    }

    private static LivedocType getResponseType(Method method, TypeReferenceProvider typeReferenceProvider) {
        Type returnType = SpringReturnTypeReader.getActualReturnType(method);
        return typeReferenceProvider.getReference(returnType);
    }

    private static String buildResponseStatusCode(Method method) {
        ResponseStatus responseStatus = method.getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value().toString() + " - " + responseStatus.value().getReasonPhrase();
        }
        return HttpStatus.OK.toString() + " - " + "OK";
    }
}
