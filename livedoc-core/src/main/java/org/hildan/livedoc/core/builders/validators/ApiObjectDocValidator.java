package org.hildan.livedoc.core.builders.validators;

import org.hildan.livedoc.core.model.doc.types.ApiTypeDoc;
import org.hildan.livedoc.core.model.doc.types.ApiFieldDoc;

public class ApiObjectDocValidator {

    public static ApiTypeDoc validate(ApiTypeDoc apiTypeDoc) {
        final String HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION = "Add description to field: %s";

        for (ApiFieldDoc apiFieldDoc : apiTypeDoc.getFields()) {
            if (apiFieldDoc.getDescription() == null || apiFieldDoc.getDescription().trim().isEmpty()) {
                apiTypeDoc.addJsondocHint(
                        String.format(HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION, apiFieldDoc.getName()));
            }
        }
        return apiTypeDoc;
    }

}
