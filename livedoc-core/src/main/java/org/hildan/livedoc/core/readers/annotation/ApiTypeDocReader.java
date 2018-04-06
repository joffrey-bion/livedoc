package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.properties.PropertyScanner;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class ApiTypeDocReader {

    private final PropertyScanner propertyScanner;

    public ApiTypeDocReader(PropertyScanner propertyScanner) {
        this.propertyScanner = propertyScanner;
    }

    public ApiTypeDoc read(Class<?> clazz, TypeReferenceProvider typeReferenceProvider,
            TemplateProvider templateProvider) {
        String javadoc = JavadocHelper.getJavadocDescription(clazz).orElse(null);
        ApiTypeDoc apiTypeDoc = new ApiTypeDoc(clazz);
        apiTypeDoc.setName(clazz.getSimpleName());
        apiTypeDoc.setDescription(javadoc);
        apiTypeDoc.setSupportedVersions(ApiVersionDocReader.read(clazz));
        apiTypeDoc.setShow(Modifier.isAbstract(clazz.getModifiers()));
        apiTypeDoc.setStage(ApiStageReader.read(clazz));

        ApiType apiType = clazz.getAnnotation(ApiType.class);
        if (apiType != null) {
            apiTypeDoc.setName(BeanUtils.maybeOverridden(nullifyIfEmpty(apiType.name()), clazz.getSimpleName()));
            apiTypeDoc.setDescription(BeanUtils.maybeOverridden(nullifyIfEmpty(apiType.description()), javadoc));
            apiTypeDoc.setGroup(apiType.group());
            apiTypeDoc.setShow(apiType.show());
        }

        if (clazz.isEnum()) {
            apiTypeDoc.setAllowedValues(BeanUtils.enumConstantsToStringArray(clazz.getEnumConstants()));
        }

        apiTypeDoc.setFields(getFieldDocs(clazz, apiTypeDoc, typeReferenceProvider));
        apiTypeDoc.setTemplate(templateProvider.getTemplate(clazz));

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
