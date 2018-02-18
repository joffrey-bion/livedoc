package org.hildan.livedoc.core.builders.doc;

import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.types.ApiFieldDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.types.LivedocType;
import org.hildan.livedoc.core.model.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.model.doc.version.ApiVersionDoc;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.HibernateValidationProcessor;

public class ApiObjectFieldDocReader {

    public static ApiFieldDoc read(Property property, ApiTypeDoc parentDoc) {
        ApiFieldDoc apiFieldDoc = new ApiFieldDoc();
        apiFieldDoc.setName(property.getName());
        apiFieldDoc.setType(getLivedocType(property));
        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        apiFieldDoc.setRequired(String.valueOf(property.isRequired()));
        apiFieldDoc.setOrder(property.getOrder());

        String[] allowedvalues = getAllowedValues(property);
        apiFieldDoc.setAllowedValues(allowedvalues);

        ApiTypeProperty annotation = property.getAccessibleObject().getAnnotation(ApiTypeProperty.class);
        if (annotation != null) {
            apiFieldDoc.setName(BeanUtils.maybeOverridden(annotation.name(), property.getName()));
            apiFieldDoc.setDescription(annotation.description());
            apiFieldDoc.setAllowedValues(BeanUtils.maybeOverridden(annotation.allowedValues(), allowedvalues));
            // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
            apiFieldDoc.setRequired(String.valueOf(annotation.required() || property.isRequired()));
            apiFieldDoc.setOrder(BeanUtils.maybeOverridden(annotation.order(), property.getOrder(), Integer.MAX_VALUE));

            if (!annotation.format().isEmpty()) {
                apiFieldDoc.addFormat(annotation.format());
            }
        }
        apiFieldDoc.setSupportedVersions(getVersionDoc(property, parentDoc));

        HibernateValidationProcessor.addConstraintMessages(property.getAccessibleObject(), apiFieldDoc);

        return apiFieldDoc;
    }

    private static String[] getAllowedValues(Property property) {
        if (property.getType().isEnum()) {
            Object[] enumConstants = property.getType().getEnumConstants();
            return BeanUtils.enumConstantsToStringArray(enumConstants);
        }
        return null;
    }

    private static LivedocType getLivedocType(Property property) {
        return LivedocTypeBuilder.build(property.getGenericType());
    }

    private static ApiVersionDoc getVersionDoc(Property property, ApiTypeDoc parentDoc) {
        return ApiVersionDocReader.read(property.getAccessibleObject(), parentDoc.getSupportedVersions());
    }
}
