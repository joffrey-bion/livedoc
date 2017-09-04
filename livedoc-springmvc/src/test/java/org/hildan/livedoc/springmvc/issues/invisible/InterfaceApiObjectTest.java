package org.hildan.livedoc.springmvc.issues.invisible;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Assert;
import org.junit.Test;

public class InterfaceApiObjectTest {

    @Test
    public void testInvisible() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.springmvc.issues.invisible");
        LivedocReader builder = SpringLivedocReaderFactory.getReader(packages);
        Livedoc livedoc = builder.read("version", "basePath", true, MethodDisplay.URI);
        Assert.assertEquals(1, livedoc.getObjects().keySet().size());
        for (String string : livedoc.getObjects().keySet()) {
            Assert.assertEquals(2, livedoc.getObjects().get(string).size());
        }
        for (ApiDoc apiDoc : livedoc.getApis().get("")) {
            for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
                Assert.assertEquals("Resource Interface", apiMethodDoc.getResponse().getType().getOneLineText());
            }
        }

    }

}
