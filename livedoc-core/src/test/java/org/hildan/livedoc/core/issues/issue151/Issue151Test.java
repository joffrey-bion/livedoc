package org.hildan.livedoc.core.issues.issue151;

import org.hildan.livedoc.core.pojo.JSONDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DefaultJSONDocScanner;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

//@ApiObject ignored for ParameterizedType return objects
//https://github.com/fabiomaffioletti/jsondoc/issues/151
public class Issue151Test {

    JSONDocScanner jsondocScanner = new DefaultJSONDocScanner();

    @Test
    public void testIssue151() {
        JSONDoc jsonDoc = jsondocScanner.getJSONDoc("", "",
                Lists.newArrayList("org.hildan.livedoc.core.issues.issue151"), true, MethodDisplay.URI);
        Assert.assertEquals(2, jsonDoc.getObjects().keySet().size());
        Assert.assertEquals(1, jsonDoc.getObjects().get("bargroup").size());
        Assert.assertEquals(1, jsonDoc.getObjects().get("foogroup").size());
    }

}
