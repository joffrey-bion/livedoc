package org.hildan.livedoc.springmvc.issues.invisible;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.JSONDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.hildan.livedoc.springmvc.scanner.Spring4JSONDocScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class InterfaceApiObjectTest {
	
	JSONDocScanner jsondocScanner = new Spring4JSONDocScanner();
	
	@Test
	public void testInvisible() {
		JSONDoc jsonDoc = jsondocScanner.getJSONDoc("version", "basePath", Lists.newArrayList("org.hildan.livedoc.springmvc.issues.invisible"), true, MethodDisplay.URI);
		Assert.assertEquals(1, jsonDoc.getObjects().keySet().size());
		for (String string : jsonDoc.getObjects().keySet()) {
			Assert.assertEquals(2, jsonDoc.getObjects().get(string).size());
		}
		for (ApiDoc apiDoc : jsonDoc.getApis().get("")) {
			for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
				Assert.assertEquals("Resource Interface", apiMethodDoc.getResponse().getJsondocType().getOneLineText());
			}
		}
		
	}

}
