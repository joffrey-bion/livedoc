package org.hildan.livedoc.core.issues.issue151;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocBuilder;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.junit.Assert;
import org.junit.Test;

//@ApiObject ignored for ParameterizedType return objects
//https://github.com/fabiomaffioletti/jsondoc/issues/151
public class Issue151Test {

    @Test
    public void testIssue151() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.core.issues.issue151");
        LivedocBuilder builder = LivedocBuilder.basicAnnotationBuilder(packages);
        Livedoc livedoc = builder.build("", "", packages, true, MethodDisplay.URI);
        Assert.assertEquals(2, livedoc.getObjects().keySet().size());
        Assert.assertEquals(1, livedoc.getObjects().get("bargroup").size());
        Assert.assertEquals(1, livedoc.getObjects().get("foogroup").size());
    }

}
