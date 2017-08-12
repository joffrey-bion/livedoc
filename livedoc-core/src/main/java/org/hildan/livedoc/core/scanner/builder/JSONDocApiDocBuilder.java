package org.hildan.livedoc.core.scanner.builder;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.annotation.Api;

public class JSONDocApiDocBuilder {
	
	public static ApiDoc build(Class<?> controller) {
		Api api = controller.getAnnotation(Api.class);
		ApiDoc apiDoc = new ApiDoc();
		apiDoc.setDescription(api.description());
		apiDoc.setName(api.name());
		apiDoc.setGroup(api.group());
		apiDoc.setVisibility(api.visibility());
		apiDoc.setStage(api.stage());
		return apiDoc;
	}

}
