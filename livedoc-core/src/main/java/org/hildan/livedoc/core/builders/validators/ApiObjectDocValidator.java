package org.hildan.livedoc.core.builders.validators;

import org.hildan.livedoc.core.pojo.ApiObjectDoc;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;

public class ApiObjectDocValidator {

    public static ApiObjectDoc validate(ApiObjectDoc apiObjectDoc) {
        final String HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION = "Add description to field: %s";

        for (ApiObjectFieldDoc apiObjectFieldDoc : apiObjectDoc.getFields()) {
            if (apiObjectFieldDoc.getDescription() == null || apiObjectFieldDoc.getDescription().trim().isEmpty()) {
                apiObjectDoc.addJsondochint(
                        String.format(HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION, apiObjectFieldDoc.getName()));
            }
        }
        return apiObjectDoc;
    }

}
