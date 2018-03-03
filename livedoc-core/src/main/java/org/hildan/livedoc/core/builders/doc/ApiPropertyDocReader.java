package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.HibernateValidationProcessor;

public class ApiPropertyDocReader {

    public static ApiPropertyDoc read(Property property, ApiTypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        ApiPropertyDoc apiPropertyDoc = new ApiPropertyDoc();
        apiPropertyDoc.setName(property.getName());
        apiPropertyDoc.setType(getLivedocType(property, typeReferenceProvider));
        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        apiPropertyDoc.setRequired(String.valueOf(property.isRequired()));
        apiPropertyDoc.setOrder(property.getOrder());

        String[] allowedvalues = getAllowedValues(property);
        apiPropertyDoc.setAllowedValues(allowedvalues);

        Field field = property.getField();
        if (field != null) {
            overrideFromMember(apiPropertyDoc, field);
        }
        Method getter = property.getGetter();
        if (getter != null) {
            overrideFromMember(apiPropertyDoc, getter);
        }

        apiPropertyDoc.setSupportedVersions(getVersionDoc(property, parentDoc));
        return apiPropertyDoc;
    }

    private static void overrideFromMember(ApiPropertyDoc apiPropertyDoc, AnnotatedElement annotatedElement) {
        ApiTypeProperty annotation = annotatedElement.getAnnotation(ApiTypeProperty.class);
        if (annotation != null) {
            overrideFromAnnotation(apiPropertyDoc, annotation);
        }
        HibernateValidationProcessor.addConstraintMessages(annotatedElement, apiPropertyDoc);
    }

    private static void overrideFromAnnotation(ApiPropertyDoc apiPropertyDoc, ApiTypeProperty annotation) {
        apiPropertyDoc.setName(BeanUtils.maybeOverridden(annotation.name(), apiPropertyDoc.getName()));
        apiPropertyDoc.setDescription(annotation.description());

        String[] allowedValues = BeanUtils.maybeOverridden(annotation.allowedValues(), apiPropertyDoc.getAllowedValues());
        apiPropertyDoc.setAllowedValues(allowedValues);

        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        boolean isRequired = Boolean.valueOf(apiPropertyDoc.getRequired());
        apiPropertyDoc.setRequired(String.valueOf(annotation.required() || isRequired));
        apiPropertyDoc.setOrder(BeanUtils.maybeOverridden(annotation.order(), apiPropertyDoc.getOrder(), Integer.MAX_VALUE));

        if (!annotation.format().isEmpty()) {
            apiPropertyDoc.addFormat(annotation.format());
        }
    }

    private static String[] getAllowedValues(Property property) {
        if (property.getType().isEnum()) {
            Object[] enumConstants = property.getType().getEnumConstants();
            return BeanUtils.enumConstantsToStringArray(enumConstants);
        }
        return null;
    }

    private static LivedocType getLivedocType(Property property, TypeReferenceProvider typeReferenceProvider) {
        return typeReferenceProvider.getReference(property.getGenericType());
    }

    private static ApiVersionDoc getVersionDoc(Property property, ApiTypeDoc parentDoc) {
        ApiVersionDoc versionDoc = parentDoc.getSupportedVersions();
        Field field = property.getField();
        if (field != null) {
            versionDoc = ApiVersionDocReader.read(field, versionDoc);
        }
        Method getter = property.getGetter();
        if (getter != null) {
            versionDoc = ApiVersionDocReader.read(getter, versionDoc);
        }
        return versionDoc;
    }
}
