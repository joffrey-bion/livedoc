package org.hildan.livedoc.core.validators;

import java.util.Collections;
import java.util.Set;

import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;

public class ApiOperationDocDefaults {

    public static void complete(ApiOperationDoc doc) {

        Set<ApiVerb> verbs = doc.getVerbs();
        if (verbs == null || verbs.isEmpty()) {
            doc.setVerbs(Collections.singleton(ApiVerb.GET));
        }
    }
}
