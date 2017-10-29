package org.hildan.livedoc.core.builders.doc;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotations.ApiObject;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.util.BeanUtils;

public class ApiObjectDocReader {

    private final PropertyScanner propertyScanner;

    public ApiObjectDocReader(PropertyScanner propertyScanner) {
        this.propertyScanner = propertyScanner;
    }

    public ApiObjectDoc read(Class<?> clazz) {
        ApiObjectDoc apiObjectDoc = new ApiObjectDoc(clazz);
        apiObjectDoc.setName(clazz.getSimpleName());
        apiObjectDoc.setSupportedversions(ApiVersionDocReader.read(clazz));
        apiObjectDoc.setShow(Modifier.isAbstract(clazz.getModifiers()));

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
            apiObjectDoc.setAllowedvalues(BeanUtils.enumConstantsToStringArray(clazz.getEnumConstants()));
        }

        apiObjectDoc.setFields(getFieldDocs(clazz, apiObjectDoc));

        return apiObjectDoc;
    }

    private Set<ApiObjectFieldDoc> getFieldDocs(Class<?> clazz, ApiObjectDoc apiObjectDoc) {
        if (clazz.isEnum()) {
            return Collections.emptySet();
        }
        Set<ApiObjectFieldDoc> fieldDocs = new TreeSet<>();
        for (Property property : propertyScanner.getProperties(clazz)) {
            fieldDocs.add(ApiObjectFieldDocReader.read(property, apiObjectDoc));
        }
        return fieldDocs;
    }

}
