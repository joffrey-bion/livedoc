package org.hildan.livedoc.core.model.doc;

import org.hildan.livedoc.core.model.types.LivedocType;

public class RequestBodyDoc {

    private final LivedocType type;

    private final Object template;

    public RequestBodyDoc(LivedocType type, Object template) {
        this.type = type;
        this.template = template;
    }

    public LivedocType getType() {
        return type;
    }

    public Object getTemplate() {
        return template;
    }
}
