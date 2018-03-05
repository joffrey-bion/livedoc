package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.readers.annotation.ApiPropertyDocReader;
import org.hildan.livedoc.core.readers.annotation.ApiStageReader;
import org.hildan.livedoc.core.readers.annotation.ApiVersionDocReader;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;

public class ApiTypeDocReader {

    private final PropertyScanner propertyScanner;

    public ApiTypeDocReader(PropertyScanner propertyScanner) {
        this.propertyScanner = propertyScanner;
    }

    public ApiTypeDoc read(Class<?> clazz, TypeReferenceProvider typeReferenceProvider) {
        ApiTypeDoc apiTypeDoc = new ApiTypeDoc(clazz);
        apiTypeDoc.setName(clazz.getSimpleName());
        apiTypeDoc.setSupportedVersions(ApiVersionDocReader.read(clazz));
        apiTypeDoc.setShow(Modifier.isAbstract(clazz.getModifiers()));
        apiTypeDoc.setStage(ApiStageReader.read(clazz));

        ApiType apiType = clazz.getAnnotation(ApiType.class);
        if (apiType != null) {
            apiTypeDoc.setName(BeanUtils.maybeOverridden(apiType.name(), clazz.getSimpleName()));
            apiTypeDoc.setDescription(apiType.description());
            apiTypeDoc.setGroup(apiType.group());
            apiTypeDoc.setShow(apiType.show());
        }

        if (clazz.isEnum()) {
            apiTypeDoc.setAllowedValues(BeanUtils.enumConstantsToStringArray(clazz.getEnumConstants()));
        }

        apiTypeDoc.setFields(getFieldDocs(clazz, apiTypeDoc, typeReferenceProvider));

        return apiTypeDoc;
    }

    private Set<ApiPropertyDoc> getFieldDocs(Class<?> clazz, ApiTypeDoc apiTypeDoc,
            TypeReferenceProvider typeReferenceProvider) {
        if (clazz.isEnum()) {
            return Collections.emptySet();
        }
        Set<ApiPropertyDoc> fieldDocs = new TreeSet<>();
        for (Property property : propertyScanner.getProperties(clazz)) {
            fieldDocs.add(ApiPropertyDocReader.read(property, apiTypeDoc, typeReferenceProvider));
        }
        return fieldDocs;
    }
}
