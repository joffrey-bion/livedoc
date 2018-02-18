package org.hildan.livedoc.core.model;

import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ApiMethodDocTest {

    @Test
    public void testNotEqual() {
        ApiMethodDoc first = new ApiMethodDoc();
        first.setPaths(Sets.newHashSet("/first"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET));
        ApiMethodDoc second = new ApiMethodDoc();
        second.setPaths(Sets.newHashSet("/second"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET));
        Assert.assertNotEquals(0, first.compareTo(second));
    }

    @Test
    public void testEqual() {
        ApiMethodDoc first = new ApiMethodDoc();
        first.setPaths(Sets.newHashSet("/test"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET));
        ApiMethodDoc second = new ApiMethodDoc();
        second.setPaths(Sets.newHashSet("/test"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET));
        Assert.assertEquals(0, first.compareTo(second));
    }

    @Test
    public void testNotEqualMultipleVerbs() {
        ApiMethodDoc first = new ApiMethodDoc();
        first.setPaths(Sets.newHashSet("/first"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        ApiMethodDoc second = new ApiMethodDoc();
        second.setPaths(Sets.newHashSet("/second"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        Assert.assertNotEquals(0, first.compareTo(second));

        second.setPaths(Sets.newHashSet("/first"));
        second.setVerbs(Sets.newHashSet(ApiVerb.PUT, ApiVerb.POST));
        Assert.assertNotEquals(0, first.compareTo(second));
    }

    @Test
    public void testEqualMultipleVerbs() {
        ApiMethodDoc first = new ApiMethodDoc();
        first.setPaths(Sets.newHashSet("/test"));
        first.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        ApiMethodDoc second = new ApiMethodDoc();
        second.setPaths(Sets.newHashSet("/test"));
        second.setVerbs(Sets.newHashSet(ApiVerb.GET, ApiVerb.POST));
        Assert.assertEquals(0, first.compareTo(second));

        second.setVerbs(Sets.newHashSet(ApiVerb.POST, ApiVerb.GET));
        Assert.assertEquals(0, first.compareTo(second));
    }

}
