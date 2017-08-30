package org.hildan.livedoc.core.builders.doc;

import org.hildan.livedoc.core.annotations.ApiObjectField;
import org.hildan.livedoc.core.builders.types.LivedocType;
import org.hildan.livedoc.core.builders.types.LivedocTypeBuilder;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.util.BeanUtils;
import org.hildan.livedoc.core.util.HibernateValidationProcessor;

public class ApiObjectFieldDocReader {

    public static ApiObjectFieldDoc read(Property property) {
        ApiObjectFieldDoc apiFieldDoc = new ApiObjectFieldDoc();
        apiFieldDoc.setName(property.getName());
        apiFieldDoc.setType(getLivedocType(property));
        // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
        apiFieldDoc.setRequired(String.valueOf(property.isRequired()));
        apiFieldDoc.setOrder(property.getOrder());

        String[] allowedvalues = getAllowedValues(property);
        apiFieldDoc.setAllowedvalues(allowedvalues);

        ApiObjectField annotation = property.getAccessibleObject().getAnnotation(ApiObjectField.class);
        if (annotation != null) {
            apiFieldDoc.setName(BeanUtils.maybeOverridden(annotation.name(), property.getName()));
            apiFieldDoc.setDescription(annotation.description());
            apiFieldDoc.setAllowedvalues(BeanUtils.maybeOverridden(annotation.allowedvalues(), allowedvalues));
            // FIXME maybe DefaultDocAnnotationScanner.UNDEFINED.toUpperCase() when not set
            apiFieldDoc.setRequired(String.valueOf(annotation.required() || property.isRequired()));
            apiFieldDoc.setOrder(BeanUtils.maybeOverridden(annotation.order(), property.getOrder(), Integer.MAX_VALUE));

            if (!annotation.format().isEmpty()) {
                apiFieldDoc.addFormat(annotation.format());
            }
        }

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
        return LivedocTypeBuilder.build(new LivedocType(), property.getType(), property.getGenericType());
    }
}
