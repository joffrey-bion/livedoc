package org.hildan.livedoc.core.scanners.properties;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import static java.util.stream.Collectors.toList;

/**
 * A {@link PropertyScanner} that uses a Jackson configuration to inspect the properties of a type. This allows to
 * document the properties the same way they are serialized when using the given {@link ObjectMapper}, and is especially
 * useful when used with Spring's configuration.
 */
public class JacksonPropertyScanner implements PropertyScanner {

    private final ObjectMapper mapper;

    /**
     * Creates a new {@link JacksonPropertyScanner} based on the given {@link ObjectMapper}'s configuration.
     *
     * @param mapper
     *         the Jackson {@link ObjectMapper} that is used to serialize the objects to document
     */
    public JacksonPropertyScanner(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Property> getProperties(Type type) {
        return getJacksonProperties(type).stream()
                                         .filter(JacksonPropertyScanner::isReadable)
                                         .map(this::convertProp)
                                         .collect(toList());
    }

    private List<BeanPropertyDefinition> getJacksonProperties(Type type) {
        BeanDescription beanDescription = getBeanDescription(type);
        List<String> ignoredProps = getIgnoredProperties(beanDescription);
        List<BeanPropertyDefinition> properties = beanDescription.findProperties();
        properties.removeIf(p -> ignoredProps.contains(p.getName()));
        return properties;
    }

    private BeanDescription getBeanDescription(Type type) {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        return mapper.getSerializationConfig().introspect(javaType);
    }

    private List<String> getIgnoredProperties(BeanDescription beanDescription) {
        AnnotationIntrospector introspector = mapper.getSerializationConfig().getAnnotationIntrospector();
        String[] ignored = introspector.findPropertiesToIgnore(beanDescription.getClassInfo(), true);
        return ignored == null ? Collections.emptyList() : Arrays.asList(ignored);
    }

    private static boolean isReadable(BeanPropertyDefinition propDef) {
        return propDef.getAccessor() != null;
    }

    private Property convertProp(BeanPropertyDefinition propDef) {
        String name = propDef.getName();
        AnnotatedMember member = propDef.getAccessor();
        assert member != null : "the given property '" + name + "' must be readable";

        Class<?> type = getType(member);
        Type genericType = getGenericType(member);

        AccessibleObject accessibleObject = getAccessibleObject(member);

        Property property = new Property(name, type, genericType, accessibleObject);
        property.setRequired(propDef.isRequired());
        PropertyMetadata metadata = propDef.getMetadata();
        if (metadata.getIndex() != null) {
            property.setOrder(metadata.getIndex());
        }
        if (metadata.getDefaultValue() != null) {
            property.setDefaultValue(metadata.getDefaultValue());
        }

        AnnotatedField field = propDef.getField();
        if (field != null) {
            property.setField(field.getAnnotated());
        }
        AnnotatedMethod getter = propDef.getGetter();
        if (getter != null) {
            property.setGetter(getter.getAnnotated());
        }
        return property;
    }

    private Class<?> getType(AnnotatedMember member) {
        Class<?> overridden = getOverriddenType(member);
        return overridden != null ? overridden : member.getRawType();
    }

    private Type getGenericType(AnnotatedMember member) {
        Class<?> overridden = getOverriddenType(member);
        return overridden != null ? overridden : member.getGenericType();
    }

    private Class<?> getOverriddenType(AnnotatedMember member) {
        AnnotationIntrospector introspector = mapper.getSerializationConfig().getAnnotationIntrospector();
        return introspector.findSerializationType(member);
    }

    private AccessibleObject getAccessibleObject(AnnotatedMember member) {
        Member fieldOrMethod = member.getMember();
        assert (fieldOrMethod instanceof AccessibleObject) : member.getName() + " is not an AccessibleObject";
        return (AccessibleObject) fieldOrMethod;
    }
}
