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
        ApiPropertyDoc doc = new ApiPropertyDoc();
        doc.setName(property.getName());
        doc.setType(getLivedocType(property, typeReferenceProvider));
        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        doc.setRequired(String.valueOf(property.isRequired()));
        doc.setOrder(property.getOrder());

        String[] allowedvalues = getAllowedValues(property);
        doc.setAllowedValues(allowedvalues);

        Field field = property.getField();
        if (field != null) {
            overrideFromMember(doc, field);
        }
        Method getter = property.getGetter();
        if (getter != null) {
            overrideFromMember(doc, getter);
        }

        doc.setSupportedVersions(getVersionDoc(property, parentDoc));
        return doc;
    }

    private static void overrideFromMember(ApiPropertyDoc doc, AnnotatedElement annotatedElement) {
        ApiTypeProperty annotation = annotatedElement.getAnnotation(ApiTypeProperty.class);
        if (annotation != null) {
            overrideFromAnnotation(doc, annotation);
        }
        HibernateValidationProcessor.addConstraintMessages(annotatedElement, doc);
    }

    private static void overrideFromAnnotation(ApiPropertyDoc doc, ApiTypeProperty annotation) {
        doc.setName(BeanUtils.maybeOverridden(annotation.name(), doc.getName()));
        doc.setDescription(annotation.description());

        String[] allowedValues = BeanUtils.maybeOverridden(annotation.allowedValues(), doc.getAllowedValues());
        doc.setAllowedValues(allowedValues);

        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        boolean isRequired = Boolean.valueOf(doc.getRequired());
        doc.setRequired(String.valueOf(annotation.required() || isRequired));

        Integer order = BeanUtils.maybeOverridden(annotation.order(), doc.getOrder(), Integer.MAX_VALUE);
        doc.setOrder(order);

        if (!annotation.format().isEmpty()) {
            doc.addFormat(annotation.format());
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
