package org.hildan.livedoc.springmvc.scanner;

import org.hildan.livedoc.core.annotation.ApiObject;
import org.hildan.livedoc.core.annotation.ApiObjectField;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Sets;

public class SpringRequestBodyBuilderTest {

	private JSONDocScanner jsondocScanner = new Spring3JSONDocScanner();

	@Controller
	@RequestMapping
	public class SpringController {

		@RequestMapping(value = "/body-one")
		public void bodyOne(@RequestBody String string) {

		}
		
		@RequestMapping(value = "/body-two")
		public void bodyTwo(@RequestBody Body body) {

		}
		
	}
	
	@ApiObject
	private class Body {
		@ApiObjectField
		private String name;
		@ApiObjectField
		private Integer age;
	}
	
	@Test
	public void testBodyOne() {
		ApiDoc apiDoc = jsondocScanner.getApiDocs(Sets.<Class<?>> newHashSet(SpringController.class), MethodDisplay.URI).iterator().next();
		Assert.assertEquals("SpringController", apiDoc.getName());
		Assert.assertEquals(2, apiDoc.getMethods().size());
		for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
			if (apiMethodDoc.getPath().contains("/body-one")) {
				Assert.assertNotNull(apiMethodDoc.getBodyobject());
				Assert.assertEquals("string", apiMethodDoc.getBodyobject().getJsondocType().getOneLineText());
			}
			if (apiMethodDoc.getPath().contains("/body-two")) {
				Assert.assertNotNull(apiMethodDoc.getBodyobject());
				Assert.assertEquals("body", apiMethodDoc.getBodyobject().getJsondocType().getOneLineText());
			}
		}
	}

}
