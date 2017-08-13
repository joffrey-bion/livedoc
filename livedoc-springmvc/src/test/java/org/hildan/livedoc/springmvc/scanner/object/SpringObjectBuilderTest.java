package org.hildan.livedoc.springmvc.scanner.object;

import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.springmvc.scanner.builder.SpringObjectBuilder;
import org.hildan.livedoc.springmvc.scanner.object.pojo.MyObject;
import org.junit.Assert;
import org.junit.Test;

public class SpringObjectBuilderTest {

    @Test
    public void testApiVerb() {
        ApiObjectDoc buildObject = SpringObjectBuilder.buildObject(MyObject.class);
        Assert.assertEquals("MyObject", buildObject.getName());
        Assert.assertEquals(3, buildObject.getFields().size());
    }

}
