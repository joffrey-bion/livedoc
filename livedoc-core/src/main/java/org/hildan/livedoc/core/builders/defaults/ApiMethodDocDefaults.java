package org.hildan.livedoc.core.builders.defaults;

import java.util.Collections;
import java.util.Set;

import org.hildan.livedoc.core.model.doc.ApiMethodDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;

public class ApiMethodDocDefaults {

    public static void complete(ApiMethodDoc doc) {

        Set<ApiVerb> verbs = doc.getVerbs();
        if (verbs == null || verbs.isEmpty()) {
            doc.setVerbs(Collections.singleton(ApiVerb.GET));
        }
    }
}
