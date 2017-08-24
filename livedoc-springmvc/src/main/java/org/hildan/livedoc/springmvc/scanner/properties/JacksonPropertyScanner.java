package org.hildan.livedoc.springmvc.scanner.properties;

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
        Type type = getType(propDef);
        return new Property(name, type);
    }

    private Type getType(BeanPropertyDefinition property) {
        AnnotatedMember member = property.getAccessor();
        assert member != null : "the given property is not readable: " + property.getName();
        AnnotationIntrospector introspector = mapper.getSerializationConfig().getAnnotationIntrospector();
        Type overridenSerializationType = introspector.findSerializationType(member);
        if (overridenSerializationType != null) {
            return overridenSerializationType;
        }
        return member.getGenericType();
    }
}
