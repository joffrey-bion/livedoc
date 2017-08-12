package org.hildan.livedoc.springmvc.scanner.builder;

import java.lang.reflect.Method;

import org.hildan.livedoc.core.pojo.ApiBodyObjectDoc;
import org.hildan.livedoc.core.util.JSONDocType;
import org.hildan.livedoc.core.util.JSONDocTypeBuilder;
import org.hildan.livedoc.core.util.JSONDocUtils;
import org.springframework.web.bind.annotation.RequestBody;

public class SpringRequestBodyBuilder {

	public static ApiBodyObjectDoc buildRequestBody(Method method) {
		Integer index = JSONDocUtils.getIndexOfParameterWithAnnotation(method, RequestBody.class);
		if (index != -1) {
			ApiBodyObjectDoc apiBodyObjectDoc = new ApiBodyObjectDoc(JSONDocTypeBuilder.build(new JSONDocType(), method.getParameterTypes()[index], method.getGenericParameterTypes()[index]));
			return apiBodyObjectDoc;
		}
		
		return null;
	}
	
}
