package org.hildan.livedoc.springmvc.issues.invisible;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InterfaceApiTypeTest {

    @Test
    public void testInvisible() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.springmvc.issues.invisible");
        LivedocConfiguration config = new LivedocConfiguration(packages);
        LivedocReader builder = SpringLivedocReaderFactory.getReader(config, null);
        Livedoc livedoc = builder.read(new ApiMetaData());

        List<Group<TypeDoc>> typeGroups = livedoc.getTypes();
        assertEquals(1, typeGroups.size());

        Group<TypeDoc> typeGroup = typeGroups.get(0);
        assertEquals(2, typeGroup.getElements().size());

        List<Group<ApiDoc>> apiGroups = livedoc.getApis();
        assertEquals(1, apiGroups.size());

        Group<ApiDoc> apiGroup = apiGroups.get(0);
        assertEquals("", apiGroup.getGroupName());

        List<ApiDoc> apiDocs = apiGroup.getElements();
        assertEquals(1, apiDocs.size());

        ApiDoc apiDoc = apiDocs.get(0);
        assertEquals(2, apiDoc.getOperations().size());

        for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
            assertEquals("Resource Interface", apiOperationDoc.getResponseBodyType().getOneLineText());
        }
    }

}
