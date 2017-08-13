package org.hildan.livedoc.core.pojo;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDoc {

    private List<String> jsondocerrors;

    private List<String> jsondocwarnings;

    private List<String> jsondochints;

    AbstractDoc() {
        this.jsondocerrors = new ArrayList<>();
        this.jsondocwarnings = new ArrayList<>();
        this.jsondochints = new ArrayList<>();
    }

    public List<String> getJsondocerrors() {
        return jsondocerrors;
    }

    public void setJsondocerrors(List<String> jsondocerrors) {
        this.jsondocerrors = jsondocerrors;
    }

    public List<String> getJsondocwarnings() {
        return jsondocwarnings;
    }

    public void setJsondocwarnings(List<String> jsondocwarnings) {
        this.jsondocwarnings = jsondocwarnings;
    }

    public List<String> getJsondochints() {
        return jsondochints;
    }

    public void setJsondochints(List<String> jsondochints) {
        this.jsondochints = jsondochints;
    }

    public void addJsondocerror(String jsondocerror) {
        this.jsondocerrors.add(jsondocerror);
    }

    public void addJsondocwarning(String jsondocwarning) {
        this.jsondocwarnings.add(jsondocwarning);
    }

    public void addJsondochint(String jsondochint) {
        this.jsondochints.add(jsondochint);
    }

}
