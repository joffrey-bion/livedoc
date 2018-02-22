package org.hildan.livedoc.core.builders.validators;

import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.types.ApiFieldDoc;

public class ApiTypeDocValidator {

    private static final String HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION = "Add description to field: %s";

    public static ApiTypeDoc validate(ApiTypeDoc apiTypeDoc) {

        for (ApiFieldDoc apiFieldDoc : apiTypeDoc.getFields()) {
            if (apiFieldDoc.getDescription() == null || apiFieldDoc.getDescription().trim().isEmpty()) {
                String msg = String.format(HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION, apiFieldDoc.getName());
                apiTypeDoc.addJsondocHint(msg);
            }
        }
        return apiTypeDoc;
    }
}
