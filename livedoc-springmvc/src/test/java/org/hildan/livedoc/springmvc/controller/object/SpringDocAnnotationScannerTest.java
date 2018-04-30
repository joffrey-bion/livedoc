package org.hildan.livedoc.springmvc.controller.object;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;
import org.hildan.livedoc.core.model.groups.Group;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.junit.Test;

import static org.junit.Assert.fail;

public class SpringDocAnnotationScannerTest {

    @Test
    public void findsNestedObject() {
        LivedocConfiguration config = new LivedocConfiguration(Collections.emptyList());
        LivedocReader builder = SpringLivedocReaderFactory.getReader(config, null);
        Livedoc doc = builder.read(new ApiMetaData());

        List<Group<TypeDoc>> typeGroups = doc.getTypes();
        for (Group<TypeDoc> types : typeGroups) {
            assertContainsDoc(types, "NestedObject1");
        }
    }

    @Test
    public void findsDeeplyNestedObjects() {
        LivedocConfiguration config = new LivedocConfiguration(Collections.emptyList());
        LivedocReader builder = SpringLivedocReaderFactory.getReader(config, null);
        Livedoc doc = builder.read(new ApiMetaData());

        List<Group<TypeDoc>> typeGroups = doc.getTypes();
        for (Group<TypeDoc> types : typeGroups) {
            assertContainsDoc(types, "NestedObject2");
            assertContainsDoc(types, "NestedObject3");
        }
    }

    private void assertContainsDoc(Group<TypeDoc> typesGroup, String name) {
        for (TypeDoc typeDoc : typesGroup.getElements()) {
            if (typeDoc.getName().equals(name)) {
                return;
            }
        }
        fail("Could not find TypeDoc with name " + name);
    }
}
