package org.hildan.livedoc.springmvc.test;

import java.util.Collections;
import java.util.Optional;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.pojo.ApiDoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;

import static org.junit.Assert.assertTrue;

public class TestUtils {

    public static ApiDoc buildDoc(Class<?> controller, MethodDisplay methodDisplay) {
        LivedocReader builder = SpringLivedocReaderFactory.getReader(Collections.emptyList());
        Optional<ApiDoc> apiDoc = builder.readApiDoc(controller, methodDisplay);
        assertTrue(apiDoc.isPresent());
        return apiDoc.get();
    }

}
