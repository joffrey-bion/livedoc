package org.hildan.livedoc.springmvc.issues.invisible;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InterfaceApiTypeTest {

    @Test
    public void testInvisible() {
        List<String> packages = Collections.singletonList("org.hildan.livedoc.springmvc.issues.invisible");
        LivedocReader builder = SpringLivedocReaderFactory.getReader(packages);
        Livedoc livedoc = builder.read("version", "basePath", true, MethodDisplay.URI);
        List<Group<ApiTypeDoc>> typeGroups = livedoc.getTypes();
        assertEquals(1, typeGroups.size());
        for (Group<ApiTypeDoc> typeGroup : typeGroups) {
            assertEquals(2, typeGroup.getElements().size());
        }
        Group<ApiDoc> apiDocGroup = livedoc.getApis().get(0);
        assertEquals("", apiDocGroup.getGroupName());
        for (ApiDoc apiDoc : apiDocGroup.getElements()) {
            for (ApiOperationDoc apiOperationDoc : apiDoc.getOperations()) {
                assertEquals("Resource Interface", apiOperationDoc.getResponseBodyType().getOneLineText());
            }
        }

    }

}
