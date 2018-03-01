package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.types.ApiFieldDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.HibernateValidationProcessor;

public class ApiObjectFieldDocReader {

    public static ApiFieldDoc read(Property property, ApiTypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        ApiFieldDoc apiFieldDoc = new ApiFieldDoc();
        apiFieldDoc.setName(property.getName());
        apiFieldDoc.setType(getLivedocType(property, typeReferenceProvider));
        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        apiFieldDoc.setRequired(String.valueOf(property.isRequired()));
        apiFieldDoc.setOrder(property.getOrder());

        String[] allowedvalues = getAllowedValues(property);
        apiFieldDoc.setAllowedValues(allowedvalues);

        Field field = property.getField();
        if (field != null) {
            overrideFromMember(apiFieldDoc, field);
        }
        Method getter = property.getGetter();
        if (getter != null) {
            overrideFromMember(apiFieldDoc, getter);
        }

        apiFieldDoc.setSupportedVersions(getVersionDoc(property, parentDoc));
        return apiFieldDoc;
    }

    private static void overrideFromMember(ApiFieldDoc apiFieldDoc, AnnotatedElement annotatedElement) {
        ApiTypeProperty annotation = annotatedElement.getAnnotation(ApiTypeProperty.class);
        if (annotation != null) {
            overrideFromAnnotation(apiFieldDoc, annotation);
        }
        HibernateValidationProcessor.addConstraintMessages(annotatedElement, apiFieldDoc);
    }

    private static void overrideFromAnnotation(ApiFieldDoc apiFieldDoc, ApiTypeProperty annotation) {
        apiFieldDoc.setName(BeanUtils.maybeOverridden(annotation.name(), apiFieldDoc.getName()));
        apiFieldDoc.setDescription(annotation.description());

        String[] allowedValues = BeanUtils.maybeOverridden(annotation.allowedValues(), apiFieldDoc.getAllowedValues());
        apiFieldDoc.setAllowedValues(allowedValues);

        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        boolean isRequired = Boolean.valueOf(apiFieldDoc.getRequired());
        apiFieldDoc.setRequired(String.valueOf(annotation.required() || isRequired));
        apiFieldDoc.setOrder(BeanUtils.maybeOverridden(annotation.order(), apiFieldDoc.getOrder(), Integer.MAX_VALUE));

        if (!annotation.format().isEmpty()) {
            apiFieldDoc.addFormat(annotation.format());
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
