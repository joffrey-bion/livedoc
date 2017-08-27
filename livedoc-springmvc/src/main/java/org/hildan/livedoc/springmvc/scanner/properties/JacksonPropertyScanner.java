package org.hildan.livedoc.springmvc.scanner.properties;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.scanner.properties.Property;
import org.hildan.livedoc.core.scanner.properties.PropertyScanner;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import static java.util.stream.Collectors.toList;

/**
 * A {@link PropertyScanner} that uses a Jackson configuration to read properties in the exact way they are serialized.
 */
public class JacksonPropertyScanner implements PropertyScanner {

    private final ObjectMapper mapper;

    public JacksonPropertyScanner(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Property> getProperties(Class<?> clazz) {
        return getJacksonProperties(clazz).stream()
                                          .filter(JacksonPropertyScanner::isReadable)
                                          .map(this::convertProp)
                                          .collect(toList());
    }

    private List<BeanPropertyDefinition> getJacksonProperties(Class<?> clazz) {
        BeanDescription beanDescription = getBeanDescription(clazz);
        List<String> ignoredProps = getIgnoredProperties(beanDescription);
        List<BeanPropertyDefinition> properties = beanDescription.findProperties();
        properties.removeIf(p -> ignoredProps.contains(p.getName()));
        return properties;
    }

    private BeanDescription getBeanDescription(Class<?> clazz) {
        JavaType javaType = mapper.getTypeFactory().constructType(clazz);
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
        if (propDef.getMetadata().getIndex() != null) {
            property.setOrder(propDef.getMetadata().getIndex());
        }
        if (propDef.getMetadata().getDefaultValue() != null) {
            property.setDefaultValue(propDef.getMetadata().getDefaultValue());
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
