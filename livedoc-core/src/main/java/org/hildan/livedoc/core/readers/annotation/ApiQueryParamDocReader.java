package org.hildan.livedoc.core.readers.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hildan.livedoc.core.annotations.ApiParams;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.model.LivedocDefaultType;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiQueryParamDocReader {

    public static List<ParamDoc> read(Method method, TypeReferenceProvider typeReferenceProvider) {
        Set<ParamDoc> docs = new LinkedHashSet<>();

        ApiParams apiParams = method.getAnnotation(ApiParams.class);
        if (apiParams != null) {
            for (ApiQueryParam apiParam : apiParams.queryParams()) {
                LivedocType livedocType = typeReferenceProvider.getReference(apiParam.type());
                ParamDoc paramDoc = buildFromAnnotation(apiParam, livedocType);
                docs.add(paramDoc);
            }
        }

        Annotation[][] parametersAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersAnnotations.length; i++) {
            for (int j = 0; j < parametersAnnotations[i].length; j++) {
                if (parametersAnnotations[i][j] instanceof ApiQueryParam) {
                    ApiQueryParam annotation = (ApiQueryParam) parametersAnnotations[i][j];
                    LivedocType livedocType = typeReferenceProvider.getReference(method.getGenericParameterTypes()[i]);
                    ParamDoc paramDoc = buildFromAnnotation(annotation, livedocType);
                    // if not named, it serves no purpose and can't be merged with a param from another reader
                    if (paramDoc.getName() != null) {
                        docs.add(paramDoc);
                    }
                }
            }
        }

        ArrayList<ParamDoc> paramDocs = new ArrayList<>(docs);
        Collections.sort(paramDocs);
        return paramDocs;
    }

    private static ParamDoc buildFromAnnotation(ApiQueryParam annotation, LivedocType livedocType) {
        String name = nullifyIfEmpty(annotation.name());
        String description = nullifyIfEmpty(annotation.description());
        String format = nullifyIfEmpty(annotation.format());
        boolean hasDefault = annotation.defaultValue().equals(LivedocDefaultType.DEFAULT_NONE);
        String defaultValue = hasDefault ? null : annotation.defaultValue();
        return new ParamDoc(name, description, livedocType, String.valueOf(annotation.required()),
                annotation.allowedValues(), format, defaultValue);
    }
}
