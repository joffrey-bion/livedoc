package org.hildan.livedoc.springmvc.test;

import java.util.Collections;
import java.util.Optional;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;

import static org.junit.Assert.assertTrue;

public class TestUtils {

    public static ApiDoc buildDoc(Class<?> controller) {
        LivedocConfiguration config = new LivedocConfiguration(Collections.emptyList());
        LivedocReader builder = SpringLivedocReaderFactory.getReader(config, null);
        Optional<ApiDoc> apiDoc = builder.readApiDoc(controller);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }

}
