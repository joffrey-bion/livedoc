package org.hildan.livedoc.core.builders.defaults;

import java.util.Collections;
import java.util.Set;

import org.hildan.livedoc.core.pojo.ApiMethodDoc;
import org.hildan.livedoc.core.pojo.ApiVerb;

public class ApiMethodDocDefaults {

    public static void complete(ApiMethodDoc doc) {

        Set<ApiVerb> verbs = doc.getVerb();
        if (verbs == null || verbs.isEmpty() || verbs.contains(ApiVerb.UNDEFINED)) {
            doc.setVerb(Collections.singleton(ApiVerb.GET));
        }
    }
}
