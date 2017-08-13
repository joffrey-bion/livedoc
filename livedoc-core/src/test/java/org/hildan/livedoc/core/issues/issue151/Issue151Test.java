package org.hildan.livedoc.core.issues.issue151;

import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultDocAnnotationScanner;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

//@ApiObject ignored for ParameterizedType return objects
//https://github.com/fabiomaffioletti/jsondoc/issues/151
public class Issue151Test {

    DocAnnotationScanner scanner = new DefaultDocAnnotationScanner();

    @Test
    public void testIssue151() {
        Livedoc livedoc = scanner.getLivedoc("", "",
                Lists.newArrayList("org.hildan.livedoc.core.issues.issue151"), true, MethodDisplay.URI);
        Assert.assertEquals(2, livedoc.getObjects().keySet().size());
        Assert.assertEquals(1, livedoc.getObjects().get("bargroup").size());
        Assert.assertEquals(1, livedoc.getObjects().get("foogroup").size());
    }

}
