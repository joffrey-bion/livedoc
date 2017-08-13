package org.hildan.livedoc.core.scanner.readers;

import java.util.Iterator;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.hildan.livedoc.core.util.pojo.HibernateValidatorPojo;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ApiObjectReaderTest {

    DocAnnotationScanner scanner = new DefaultDocAnnotationScanner();

    @Test
    public void testApiObjectDocWithHibernateValidator() {
        Set<ApiObjectDoc> apiObjectDocs = scanner.getApiObjectDocs(
                Sets.<Class<?>>newHashSet(HibernateValidatorPojo.class));
        Iterator<ApiObjectDoc> iterator = apiObjectDocs.iterator();
        ApiObjectDoc next = iterator.next();
        Set<ApiObjectFieldDoc> fields = next.getFields();
        for (ApiObjectFieldDoc apiObjectFieldDoc : fields) {
            if (apiObjectFieldDoc.getName().equals("id")) {
                Iterator<String> formats = apiObjectFieldDoc.getFormat().iterator();
                Assert.assertEquals("a not empty id", formats.next());
                Assert.assertEquals("length must be between 2 and 2147483647", formats.next());
                Assert.assertEquals("must be less than or equal to 9", formats.next());
            }
        }
    }

}
