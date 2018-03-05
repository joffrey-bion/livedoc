package org.hildan.livedoc.core.validators;

import org.hildan.livedoc.core.model.doc.types.ApiPropertyDoc;
import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;

public class ApiTypeDocValidator {

    private static final String HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION = "Add description to field: %s";

    public static ApiTypeDoc validate(ApiTypeDoc apiTypeDoc) {

        for (ApiPropertyDoc apiPropertyDoc : apiTypeDoc.getFields()) {
            if (apiPropertyDoc.getDescription() == null || apiPropertyDoc.getDescription().trim().isEmpty()) {
                String msg = String.format(HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION, apiPropertyDoc.getName());
                apiTypeDoc.addJsondocHint(msg);
            }
        }
        return apiTypeDoc;
    }
}
