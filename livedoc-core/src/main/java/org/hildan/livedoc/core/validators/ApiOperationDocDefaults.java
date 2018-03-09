package org.hildan.livedoc.core.validators;

import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;

public class ApiOperationDocDefaults {

    public static void complete(ApiOperationDoc doc) {

        List<ApiVerb> verbs = doc.getVerbs();
        if (verbs == null || verbs.isEmpty()) {
            doc.setVerbs(Collections.singletonList(ApiVerb.GET));
        }
    }
}
