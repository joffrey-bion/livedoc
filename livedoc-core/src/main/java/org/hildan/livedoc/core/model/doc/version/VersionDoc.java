package org.hildan.livedoc.core.model.doc.version;

public class VersionDoc {

    private String since;

    private String until;

    public VersionDoc() {
        this.since = null;
        this.until = null;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

}
