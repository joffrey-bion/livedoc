package org.hildan.livedoc.core.readers.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.model.LivedocDefaultType;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.jetbrains.annotations.NotNull;
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

    public static List<ParamDoc> read(ApiPathParam[] pathParamAnnotations,
            TypeReferenceProvider typeReferenceProvider) {
        return Arrays.stream(pathParamAnnotations)
                     .map(ann -> buildFromAnnotation(ann, typeReferenceProvider))
                     .collect(Collectors.toList());
    }

    @NotNull
    private static ParamDoc buildFromAnnotation(ApiPathParam annotation, TypeReferenceProvider typeReferenceProvider) {
        LivedocType type = getLivedocTypeFromAnnotation(annotation, typeReferenceProvider);
        return buildFromAnnotation(annotation, type);
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

    @NotNull
    private static ParamDoc buildFromAnnotation(ApiPathParam annotation, LivedocType livedocType) {
        String name = nullifyIfEmpty(annotation.name());
        String description = nullifyIfEmpty(annotation.description());
        String format = nullifyIfEmpty(annotation.format());
        return new ParamDoc(name, description, livedocType, "true", annotation.allowedValues(), format, null);
    }
}
