package org.hildan.livedoc.core.model;

import java.util.Collections;

import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ApiOperationDocTest {

    @Test
    public void testNotEqual() {
        ApiOperationDoc first = new ApiOperationDoc();
        first.setPaths(Collections.singletonList("/first"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET));
        ApiOperationDoc second = new ApiOperationDoc();
        second.setPaths(Collections.singletonList("/second"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET));
        Assert.assertNotEquals(0, first.compareTo(second));
    }

    @Test
    public void testEqual() {
        ApiOperationDoc first = new ApiOperationDoc();
        first.setPaths(Collections.singletonList("/test"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET));
        ApiOperationDoc second = new ApiOperationDoc();
        second.setPaths(Collections.singletonList("/test"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET));
        Assert.assertEquals(0, first.compareTo(second));
    }

    @Test
    public void testNotEqualMultipleVerbs() {
        ApiOperationDoc first = new ApiOperationDoc();
        first.setPaths(Collections.singletonList("/first"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        ApiOperationDoc second = new ApiOperationDoc();
        second.setPaths(Collections.singletonList("/second"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        Assert.assertNotEquals(0, first.compareTo(second));

        second.setPaths(Collections.singletonList("/first"));
        second.setVerbs(Sets.newHashSet(ApiVerb.PUT, ApiVerb.POST));
        Assert.assertNotEquals(0, first.compareTo(second));
    }

    @Test
    public void testEqualMultipleVerbs() {
        ApiOperationDoc first = new ApiOperationDoc();
        first.setPaths(Collections.singletonList("/test"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        ApiOperationDoc second = new ApiOperationDoc();
        second.setPaths(Collections.singletonList("/test"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        Assert.assertEquals(0, first.compareTo(second));

        second.setVerbs(Sets.newHashSet(ApiVerb.POST, ApiVerb.GET));
        Assert.assertEquals(0, first.compareTo(second));
    }

}
