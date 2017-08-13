package org.hildan.livedoc.core.scanner.builder;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;

public class JSONDocApiObjectDocBuilder {

    public static ApiObjectDoc build(Class<?> clazz) {
        ApiObject apiObject = clazz.getAnnotation(ApiObject.class);
        ApiObjectDoc apiObjectDoc = new ApiObjectDoc();

        Set<ApiObjectFieldDoc> fieldDocs = new TreeSet<ApiObjectFieldDoc>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(ApiObjectField.class) != null) {
                ApiObjectFieldDoc fieldDoc = JSONDocApiObjectFieldDocBuilder.build(
                        field.getAnnotation(ApiObjectField.class), field);
                fieldDoc.setSupportedversions(JSONDocApiVersionDocBuilder.build(field));
                fieldDocs.add(fieldDoc);
            }
        }

        Class<?> c = clazz.getSuperclass();
        if (c != null) {
            if (c.isAnnotationPresent(ApiObject.class)) {
                ApiObjectDoc objDoc = build(c);
                fieldDocs.addAll(objDoc.getFields());
            }
        }

        if (clazz.isEnum()) {
            apiObjectDoc.setAllowedvalues(DefaultJSONDocScanner.enumConstantsToStringArray(clazz.getEnumConstants()));
        }

        if (apiObject.name().trim().isEmpty()) {
            apiObjectDoc.setName(clazz.getSimpleName());
        } else {
            apiObjectDoc.setName(apiObject.name());
        }

        apiObjectDoc.setDescription(apiObject.description());
        apiObjectDoc.setFields(fieldDocs);
        apiObjectDoc.setGroup(apiObject.group());
        apiObjectDoc.setVisibility(apiObject.visibility());
        apiObjectDoc.setStage(apiObject.stage());
        apiObjectDoc.setShow(apiObject.show());

        return apiObjectDoc;
    }
}
