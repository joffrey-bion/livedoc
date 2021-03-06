package org.hildan.livedoc.core.model.doc;

public enum ApiVerb {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTIONS,
    TRACE;

    public boolean requiresBody() {
        return this == POST || this == PUT;
    }
}
