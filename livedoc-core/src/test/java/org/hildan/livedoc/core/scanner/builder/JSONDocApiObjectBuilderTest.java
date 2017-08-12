package org.hildan.livedoc.core.scanner.builder;

import java.util.Iterator;
import java.util.Set;

import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.util.pojo.HibernateValidatorPojo;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class JSONDocApiObjectBuilderTest {
	
	JSONDocScanner jsondocScanner = new DefaultJSONDocScanner();
	
	@Test
	public void testApiObjectDocWithHibernateValidator() {
		Set<ApiObjectDoc> apiObjectDocs = jsondocScanner.getApiObjectDocs(Sets.<Class<?>>newHashSet(HibernateValidatorPojo.class));
		Iterator<ApiObjectDoc> iterator = apiObjectDocs.iterator();
		ApiObjectDoc next = iterator.next();
		Set<ApiObjectFieldDoc> fields = next.getFields();
		for (ApiObjectFieldDoc apiObjectFieldDoc : fields) {
			if(apiObjectFieldDoc.getName().equals("id")) {
				Iterator<String> formats = apiObjectFieldDoc.getFormat().iterator();
				Assert.assertEquals("a not empty id", formats.next());
				Assert.assertEquals("length must be between 2 and 2147483647", formats.next());
				Assert.assertEquals("must be less than or equal to 9", formats.next());
			}
		}
	}

}
