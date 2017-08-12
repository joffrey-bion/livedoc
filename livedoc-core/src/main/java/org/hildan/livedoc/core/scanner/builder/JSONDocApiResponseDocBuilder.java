package org.hildan.livedoc.core.scanner.builder;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.annotation.ApiResponseObject;
import org.hildan.livedoc.core.pojo.ApiResponseObjectDoc;
import org.hildan.livedoc.core.util.JSONDocDefaultType;
import org.hildan.livedoc.core.util.JSONDocType;
import org.hildan.livedoc.core.util.JSONDocTypeBuilder;

public class JSONDocApiResponseDocBuilder {

	public static ApiResponseObjectDoc build(Method method) {
		if(method.isAnnotationPresent(ApiResponseObject.class)) {
			ApiResponseObject annotation = method.getAnnotation(ApiResponseObject.class);
			
			if(annotation.clazz().isAssignableFrom(JSONDocDefaultType.class)) {
				return new ApiResponseObjectDoc(JSONDocTypeBuilder.build(new JSONDocType(), method.getReturnType(), method.getGenericReturnType()));
			} else { 
				return new ApiResponseObjectDoc(JSONDocTypeBuilder.build(new JSONDocType(), annotation.clazz(), annotation.clazz()));
			}
		}
		
		return null;
	}

}
