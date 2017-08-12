package org.hildan.livedoc.core.scanner.builder;

import java.lang.reflect.Field;

import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;
import org.hildan.livedoc.core.util.JSONDocHibernateValidatorProcessor;
import org.hildan.livedoc.core.util.JSONDocType;
import org.hildan.livedoc.core.util.JSONDocTypeBuilder;

public class JSONDocApiObjectFieldDocBuilder {

	public static ApiObjectFieldDoc build(ApiObjectField annotation, Field field) {
		ApiObjectFieldDoc apiPojoFieldDoc = new ApiObjectFieldDoc();
		if (!annotation.name().trim().isEmpty()) {
			apiPojoFieldDoc.setName(annotation.name());
		} else {
			apiPojoFieldDoc.setName(field.getName());
		}
		apiPojoFieldDoc.setDescription(annotation.description());
		apiPojoFieldDoc.setJsondocType(JSONDocTypeBuilder.build(new JSONDocType(), field.getType(), field.getGenericType()));
		// if allowedvalues property is populated on an enum field, then the enum values are overridden with the allowedvalues ones
		if (field.getType().isEnum() && annotation.allowedvalues().length == 0) {
			apiPojoFieldDoc.setAllowedvalues(DefaultJSONDocScanner.enumConstantsToStringArray(field.getType().getEnumConstants()));
		} else {
			apiPojoFieldDoc.setAllowedvalues(annotation.allowedvalues());
		}
		apiPojoFieldDoc.setRequired(String.valueOf(annotation.required()));
		apiPojoFieldDoc.setOrder(annotation.order());
		
		if(!annotation.format().isEmpty()) {
			apiPojoFieldDoc.addFormat(annotation.format());
		}
		
		JSONDocHibernateValidatorProcessor.processHibernateValidatorAnnotations(field, apiPojoFieldDoc);
		
		return apiPojoFieldDoc;
	}
	
}
