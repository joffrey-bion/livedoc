package org.hildan.livedoc.core.scanner.builder;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiAuthToken;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class JSONDocApiAuthBuilderTest {
	
	JSONDocScanner jsondocScanner = new DefaultJSONDocScanner();
	
	@Api(name = "test-token-auth", description = "Test token auth")
	@ApiAuthToken(roles = { "" }, testtokens = { "abc", "cde" })
	private class Controller {
		
		@ApiMethod(path = "/inherit")
		public void inherit() {
			
		}
		
		@ApiMethod(path = "/override")
		@ApiAuthToken(roles = { "" }, scheme = "Bearer", testtokens = { "xyz" })
		public void override() {
			
		}
		
	}
	
	@Test
	public void testApiAuthToken() {
		ApiDoc apiDoc = jsondocScanner.getApiDocs(Sets.<Class<?>> newHashSet(Controller.class), MethodDisplay.URI).iterator().next();
		Assert.assertEquals("TOKEN", apiDoc.getAuth().getType());
		Assert.assertEquals("", apiDoc.getAuth().getScheme());
		Assert.assertEquals("abc", apiDoc.getAuth().getTesttokens().iterator().next());
		
		for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
			if(apiMethodDoc.getPath().contains("/inherit")) {
				Assert.assertEquals("TOKEN", apiMethodDoc.getAuth().getType());
				Assert.assertEquals("", apiMethodDoc.getAuth().getScheme());
				Assert.assertEquals("abc", apiMethodDoc.getAuth().getTesttokens().iterator().next());
			}
			if(apiMethodDoc.getPath().contains("/override")) {
				Assert.assertEquals("TOKEN", apiMethodDoc.getAuth().getType());
				Assert.assertEquals("Bearer", apiMethodDoc.getAuth().getScheme());
				Assert.assertEquals("xyz", apiMethodDoc.getAuth().getTesttokens().iterator().next());
			}
		}
		
	}

}
