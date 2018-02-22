package org.hildan.livedoc.springmvc.issues.invisible;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Assert;
import org.junit.Test;

public class InterfaceApiTypeTest {

    @Test
    public void testInvisible() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.springmvc.issues.invisible");
        LivedocReader builder = SpringLivedocReaderFactory.getReader(packages);
        Livedoc livedoc = builder.read("version", "basePath", true, MethodDisplay.URI);
        Assert.assertEquals(1, livedoc.getTypes().keySet().size());
        for (String string : livedoc.getTypes().keySet()) {
            Assert.assertEquals(2, livedoc.getTypes().get(string).size());
        }
        for (ApiDoc apiDoc : livedoc.getApis().get("")) {
            for (ApiMethodDoc apiMethodDoc : apiDoc.getMethods()) {
                Assert.assertEquals("Resource Interface", apiMethodDoc.getResponseBodyType().getOneLineText());
            }
        }

    }

}
