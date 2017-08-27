package org.hildan.livedoc.springmvc.issues.invisible;

import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.hildan.livedoc.springmvc.scanner.SpringDocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class InterfaceApiObjectTest {

    DocAnnotationScanner scanner = new SpringDocAnnotationScanner();

    @Test
    public void testInvisible() {
        Livedoc livedoc = scanner.getLivedoc("version", "basePath",
                Lists.newArrayList("org.hildan.livedoc.springmvc.issues.invisible"), true, MethodDisplay.URI);
        Assert.assertEquals(1, livedoc.getObjects().keySet().size());
        for (String string : livedoc.getObjects().keySet()) {
            Assert.assertEquals(2, livedoc.getObjects().get(string).size());
        }
        for (ApiDoc apiDoc : livedoc.getApis().get("")) {
            for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
                Assert.assertEquals("Resource Interface", apiMethodDoc.getResponse().getLivedocType().getOneLineText());
            }
        }

    }

}
