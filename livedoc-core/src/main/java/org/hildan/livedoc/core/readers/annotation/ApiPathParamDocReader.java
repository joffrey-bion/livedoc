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
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.Nullable;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiPathParamDocReader {

    public static List<ParamDoc> read(Method method, TypeReferenceProvider typeReferenceProvider) {
        Set<ParamDoc> docs = new LinkedHashSet<>();

        ApiParams apiParams = method.getAnnotation(ApiParams.class);
        if (apiParams != null) {
            for (ApiPathParam apiParam : apiParams.pathParams()) {
                LivedocType type = getLivedocTypeFromAnnotation(apiParam, typeReferenceProvider);
                ParamDoc paramDoc = buildFromAnnotation(apiParam, type);
                docs.add(paramDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiPathParam) {
                    ApiPathParam annotation = (ApiPathParam) parametersAnnotations[i][j];
                    Type type = method.getGenericParameterTypes()[i];
                    LivedocType livedocType = typeReferenceProvider.getReference(type);
                    ParamDoc paramDoc = buildFromAnnotation(annotation, livedocType);
                    // if not named, it serves no purpose and can't be merged with a param from another reader
                    if (paramDoc.getName() != null) {
                        docs.add(paramDoc);
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

    private static ParamDoc buildFromAnnotation(ApiPathParam annotation, LivedocType livedocType) {
        String name = nullifyIfEmpty(annotation.name());
        String description = nullifyIfEmpty(annotation.description());
        String format = nullifyIfEmpty(annotation.format());
        return new ParamDoc(name, description, livedocType, "true", annotation.allowedValues(), format, null);
    }
}
