package org.hildan.livedoc.core.validators;

import org.hildan.livedoc.core.model.doc.types.PropertyDoc;
import org.hildan.livedoc.core.model.doc.types.TypeDoc;

public class ApiTypeDocValidator {

    private static final String HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION = "Add description to field: %s";

    public static TypeDoc validate(TypeDoc typeDoc) {

        for (PropertyDoc propertyDoc : typeDoc.getFields()) {
            if (propertyDoc.getDescription() == null || propertyDoc.getDescription().trim().isEmpty()) {
                String msg = String.format(HINT_MISSING_API_OBJECT_FIELD_DESCRIPTION, propertyDoc.getName());
                typeDoc.addJsondocHint(msg);
            }
        }
        return typeDoc;
    }
}
