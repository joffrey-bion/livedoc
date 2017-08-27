package org.hildan.livedoc.core.scanner.readers;

import java.lang.reflect.Field;

import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.util.HibernateValidationProcessor;
import org.hildan.livedoc.core.util.LivedocType;
import org.hildan.livedoc.core.util.LivedocTypeBuilder;

public class ApiObjectFieldDocReader {

    public static ApiObjectFieldDoc read(ApiObjectField annotation, Field field) {
        ApiObjectFieldDoc apiPojoFieldDoc = new ApiObjectFieldDoc();
        if (!annotation.name().trim().isEmpty()) {
            apiPojoFieldDoc.setName(annotation.name());
        } else {
            apiPojoFieldDoc.setName(field.getName());
        }
        apiPojoFieldDoc.setDescription(annotation.description());
        apiPojoFieldDoc.setLivedocType(
                LivedocTypeBuilder.build(new LivedocType(), field.getType(), field.getGenericType()));
        // if allowedvalues property is populated on an enum field, then the enum values are overridden with the
        // allowedvalues ones
        if (field.getType().isEnum() && annotation.allowedvalues().length == 0) {
            apiPojoFieldDoc.setAllowedvalues(
                    DefaultDocAnnotationScanner.enumConstantsToStringArray(field.getType().getEnumConstants()));
        } else {
            apiPojoFieldDoc.setAllowedvalues(annotation.allowedvalues());
        }
        apiPojoFieldDoc.setRequired(String.valueOf(annotation.required()));
        apiPojoFieldDoc.setOrder(annotation.order());

        if (!annotation.format().isEmpty()) {
            apiPojoFieldDoc.addFormat(annotation.format());
        }

        HibernateValidationProcessor.addConstraintMessages(field, apiPojoFieldDoc);

        return apiPojoFieldDoc;
    }

}
