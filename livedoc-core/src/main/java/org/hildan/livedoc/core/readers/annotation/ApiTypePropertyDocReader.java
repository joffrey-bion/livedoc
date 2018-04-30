package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.HibernateValidationProcessor;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiTypePropertyDocReader {

    public static PropertyDoc read(Property property, TypeDoc parentDoc, TypeReferenceProvider typeReferenceProvider) {
        PropertyDoc doc = new PropertyDoc();
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

    private static void overrideFromMember(PropertyDoc doc, AnnotatedElement annotatedElement) {
        ApiTypeProperty annotation = annotatedElement.getAnnotation(ApiTypeProperty.class);
        if (annotation != null) {
            overrideFromAnnotation(doc, annotation);
        }
        HibernateValidationProcessor.addConstraintMessages(annotatedElement, doc);
    }

    private static void overrideFromAnnotation(PropertyDoc doc, ApiTypeProperty annotation) {
        doc.setName(BeanUtils.maybeOverridden(nullifyIfEmpty(annotation.name()), doc.getName()));
        doc.setDescription(nullifyIfEmpty(annotation.description()));

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

    private static VersionDoc getVersionDoc(Property property, TypeDoc parentDoc) {
        VersionDoc versionDoc = parentDoc.getSupportedVersions();
        Field field = property.getField();
        if (field != null) {
            versionDoc = ApiVersionDocReader.read(field, versionDoc);
        }
        Method getter = property.getGetter();
        if (getter != null) {
            // FIXME should we really overwrite field information if nothing is on the getter?
            versionDoc = ApiVersionDocReader.read(getter, versionDoc);
        }
        return versionDoc;
    }
}
