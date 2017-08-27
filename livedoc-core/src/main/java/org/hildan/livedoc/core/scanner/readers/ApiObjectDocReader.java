package org.hildan.livedoc.core.scanner.readers;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.scanner.properties.Property;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;
import org.hildan.livedoc.core.util.BeanUtils;

public class ApiObjectDocReader {

    private final PropertyScanner propertyScanner;

    public ApiObjectDocReader(PropertyScanner propertyScanner) {
        this.propertyScanner = propertyScanner;
    }

    public ApiObjectDoc read(Class<?> clazz) {
        ApiObjectDoc apiObjectDoc = new ApiObjectDoc();
        apiObjectDoc.setName(clazz.getSimpleName());
        apiObjectDoc.setFields(getFieldDocs(clazz));
        apiObjectDoc.setSupportedversions(ApiVersionDocReader.read(clazz));

        ApiObject apiObject = clazz.getAnnotation(ApiObject.class);
        if (apiObject != null) {
            apiObjectDoc.setName(BeanUtils.maybeOverridden(apiObject.name(), clazz.getSimpleName()));
            apiObjectDoc.setDescription(apiObject.description());
            apiObjectDoc.setGroup(apiObject.group());
            apiObjectDoc.setVisibility(apiObject.visibility());
            apiObjectDoc.setStage(apiObject.stage());
            apiObjectDoc.setShow(apiObject.show());
        }

        if (clazz.isEnum()) {
            apiObjectDoc.setAllowedvalues(
                    DefaultDocAnnotationScanner.enumConstantsToStringArray(clazz.getEnumConstants()));
        }

        return apiObjectDoc;
    }

    private Set<ApiObjectFieldDoc> getFieldDocs(Class<?> clazz) {
        if (clazz.isEnum()) {
            return Collections.emptySet();
        }
        Set<ApiObjectFieldDoc> fieldDocs = new TreeSet<>();
        for (Property property : propertyScanner.getProperties(clazz)) {
            fieldDocs.add(getApiObjectFieldDoc(property, clazz));
        }
        return fieldDocs;
    }

    private ApiObjectFieldDoc getApiObjectFieldDoc(Property property, Class<?> declaringClass) {
        ApiObjectFieldDoc fieldDoc = ApiObjectFieldDocReader.read(property);
        fieldDoc.setSupportedversions(ApiVersionDocReader.read(property.getAccessibleObject(), declaringClass));
        return fieldDoc;
    }
}
