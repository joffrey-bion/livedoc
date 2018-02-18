package org.hildan.livedoc.core.model.doc;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDoc {

    private final List<String> jsondocErrors;

    private final List<String> jsondocWarnings;

    private final List<String> jsondocHints;

    protected AbstractDoc() {
        this.jsondocErrors = new ArrayList<>();
        this.jsondocWarnings = new ArrayList<>();
        this.jsondocHints = new ArrayList<>();
    }

    public List<String> getJsondocErrors() {
        return jsondocErrors;
    }

    public List<String> getJsondocWarnings() {
        return jsondocWarnings;
    }

    public List<String> getJsondocHints() {
        return jsondocHints;
    }

    public void addJsondocError(String jsondocerror) {
        this.jsondocErrors.add(jsondocerror);
    }

    public void addJsondocWarning(String jsondocwarning) {
        this.jsondocWarnings.add(jsondocwarning);
    }

    public void addJsondocHint(String jsondochint) {
        this.jsondocHints.add(jsondochint);
    }
}
