package org.hildan.livedoc.core.issues.issue151;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.junit.Assert;
import org.junit.Test;

//@ApiType ignored for ParameterizedType return objects
//https://github.com/fabiomaffioletti/jsondoc/issues/151
public class Issue151Test {

    @Test
    public void testIssue151() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.core.issues.issue151");
        LivedocReader reader = LivedocReader.basicAnnotationReader(packages);
        Livedoc livedoc = reader.read("", "", true, MethodDisplay.URI);
        Assert.assertEquals(2, livedoc.getObjects().keySet().size());
        Assert.assertEquals(1, livedoc.getObjects().get("bargroup").size());
        Assert.assertEquals(1, livedoc.getObjects().get("foogroup").size());
    }

}
