package org.hildan.livedoc.core.validators;

import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.readers.LivedocIdGenerator;

public class AsyncMessageDocDefaults {

    public static void complete(AsyncMessageDoc doc) {
        if (doc.getLivedocId() == null) {
            // this has to be set here as a last step, because the ID depends on the operation doc's data
            doc.setLivedocId(LivedocIdGenerator.defaultMessageId(doc));
        }
    }
}
