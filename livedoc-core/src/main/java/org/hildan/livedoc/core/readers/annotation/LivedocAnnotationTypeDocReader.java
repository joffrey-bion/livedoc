package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Modifier;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;
import org.jetbrains.annotations.NotNull;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

/**
 * An implementation of {@link TypeDocReader} that reads Livedoc annotations to build the documentation of types.
 */
public class LivedocAnnotationTypeDocReader implements TypeDocReader {

    @NotNull
    @Override
    public Optional<TypeDoc> buildTypeDocBase(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        return Optional.of(read(clazz, templateProvider));
    }

    @NotNull
    @Override
    public Optional<PropertyDoc> buildPropertyDoc(Property property, TypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        return Optional.of(ApiTypePropertyDocReader.read(property, parentDoc, typeReferenceProvider));
    }

    public TypeDoc read(Class<?> clazz, TemplateProvider templateProvider) {
        String javadoc = JavadocHelper.getJavadocDescription(clazz).orElse(null);
        TypeDoc typeDoc = new TypeDoc(clazz);
        typeDoc.setName(clazz.getSimpleName());
        typeDoc.setDescription(javadoc);
        typeDoc.setSupportedVersions(ApiVersionDocReader.read(clazz));
        typeDoc.setShow(Modifier.isAbstract(clazz.getModifiers()));
        typeDoc.setStage(ApiStageReader.read(clazz));

        ApiType apiType = clazz.getAnnotation(ApiType.class);
        if (apiType != null) {
            typeDoc.setName(BeanUtils.maybeOverridden(nullifyIfEmpty(apiType.name()), clazz.getSimpleName()));
            typeDoc.setDescription(BeanUtils.maybeOverridden(nullifyIfEmpty(apiType.description()), javadoc));
            typeDoc.setGroup(apiType.group());
            typeDoc.setShow(apiType.show());
        }

        if (clazz.isEnum()) {
            typeDoc.setAllowedValues(BeanUtils.enumConstantsToStringArray(clazz.getEnumConstants()));
        }

        typeDoc.setTemplate(templateProvider.getTemplate(clazz));

        return typeDoc;
    }
}
