package org.hildan.livedoc.core.readers.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.model.LivedocDefaultType;
import org.hildan.livedoc.core.model.doc.ApiParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.Nullable;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiPathParamDocReader {

    public static List<ApiParamDoc> read(Method method, TypeReferenceProvider typeReferenceProvider) {
        Set<ApiParamDoc> docs = new LinkedHashSet<>();

        ApiParams apiParams = method.getAnnotation(ApiParams.class);
        if (apiParams != null) {
            for (ApiPathParam apiParam : apiParams.pathParams()) {
                LivedocType type = getLivedocTypeFromAnnotation(apiParam, typeReferenceProvider);
                ApiParamDoc apiParamDoc = buildFromAnnotation(apiParam, type);
                docs.add(apiParamDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiPathParam) {
                    ApiPathParam annotation = (ApiPathParam) parametersAnnotations[i][j];
                    Type type = method.getGenericParameterTypes()[i];
                    LivedocType livedocType = typeReferenceProvider.getReference(type);
                    ApiParamDoc apiParamDoc = buildFromAnnotation(annotation, livedocType);
                    // if not named, it serves no purpose and can't be merged with a param from another reader
                    if (apiParamDoc.getName() != null) {
                        docs.add(apiParamDoc);
                    }
                }
            }
        }

        return new ArrayList<>(docs);
    }

    @Nullable
    private static LivedocType getLivedocTypeFromAnnotation(ApiPathParam apiParam,
            TypeReferenceProvider typeReferenceProvider) {
        Class<?> annType = apiParam.type();
        if (annType.equals(LivedocDefaultType.class)) {
            return null;
        }
        return typeReferenceProvider.getReference(annType);
    }

    private static ApiParamDoc buildFromAnnotation(ApiPathParam annotation, LivedocType livedocType) {
        String name = nullifyIfEmpty(annotation.name());
        String description = nullifyIfEmpty(annotation.description());
        String format = nullifyIfEmpty(annotation.format());
        return new ApiParamDoc(name, description, livedocType, "true", annotation.allowedValues(), format, null);
    }
}
