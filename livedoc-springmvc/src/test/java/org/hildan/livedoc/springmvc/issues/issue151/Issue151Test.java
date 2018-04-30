package org.hildan.livedoc.springmvc.issues.issue151;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Assert;
import org.junit.Test;

// @ApiType ignored for ParameterizedType return objects
// https://github.com/fabiomaffioletti/jsondoc/issues/151
public class Issue151Test {

    @Test
    public void testIssue151() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.springmvc.issues.issue151");
        LivedocReader builder = SpringLivedocReaderFactory.getReader(new LivedocConfiguration(packages), null);
        Livedoc livedoc = builder.read(new ApiMetaData());

        List<Group<TypeDoc>> types = livedoc.getTypes();
        Assert.assertEquals(2, types.size());

        Group<TypeDoc> barGroup = types.get(0);
        Assert.assertEquals(1, barGroup.getElements().size());
        Assert.assertEquals("bargroup", barGroup.getGroupName());

        Group<TypeDoc> fooGroup = types.get(1);
        Assert.assertEquals(1, fooGroup.getElements().size());
        Assert.assertEquals("foogroup", fooGroup.getGroupName());
    }

}
