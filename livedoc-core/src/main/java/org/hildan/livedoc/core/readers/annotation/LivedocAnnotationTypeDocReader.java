package org.hildan.livedoc.core.readers.annotation;

import java.lang.reflect.Modifier;
import java.util.Optional;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.readers.DocReader;
import org.hildan.livedoc.core.readers.TypeDocReader;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.core.scanners.properties.Property;
import org.hildan.livedoc.core.scanners.templates.TemplateProvider;
import org.hildan.livedoc.core.scanners.types.references.TypeReferenceProvider;
import org.hildan.livedoc.core.util.BeanUtils;
import org.jetbrains.annotations.NotNull;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

/**
 * An implementation of {@link DocReader} that reads Livedoc annotations to build the documentation. In this
 * implementation, controllers are classes annotated with {@link Api}, and methods are only found and documented if
 * annotated with {@link ApiOperation}.
 */
public class LivedocAnnotationTypeDocReader implements TypeDocReader {

    @NotNull
    @Override
    public Optional<ApiTypeDoc> buildTypeDocBase(@NotNull Class<?> clazz,
            @NotNull TypeReferenceProvider typeReferenceProvider, @NotNull TemplateProvider templateProvider) {
        return Optional.of(read(clazz, templateProvider));
    }

    @NotNull
    @Override
    public Optional<ApiPropertyDoc> buildPropertyDoc(Property property, ApiTypeDoc parentDoc,
            TypeReferenceProvider typeReferenceProvider) {
        return Optional.of(ApiPropertyDocReader.read(property, parentDoc, typeReferenceProvider));
    }

    public ApiTypeDoc read(Class<?> clazz, TemplateProvider templateProvider) {
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

        apiTypeDoc.setTemplate(templateProvider.getTemplate(clazz));

        return apiTypeDoc;
    }
}
