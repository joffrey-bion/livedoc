package org.hildan.livedoc.springmvc.test;

import java.util.Collections;
import java.util.Optional;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;

import static org.junit.Assert.assertTrue;

public class TestUtils {

    public static ApiDoc buildDoc(Class<?> controller) {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        Optional<ApiDoc> apiDoc = builder.readApiDoc(controller);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }

}
