package org.hildan.livedoc.core.pojo;

import java.util.UUID;

import org.hildan.livedoc.core.util.JSONDocType;

public class ApiResponseObjectDoc {
	public final String jsondocId = UUID.randomUUID().toString();
	private JSONDocType jsondocType;

	public ApiResponseObjectDoc(JSONDocType jsondocType) {
		this.jsondocType = jsondocType;
	}

	public JSONDocType getJsondocType() {
		return jsondocType;
	}

}
