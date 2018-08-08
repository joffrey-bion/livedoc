package org.hildan.livedoc.core.model.doc;

public class LivedocMetaData {

    /**
     * The version of the specification schema. This is what marks non-backward-compatible changes on the JSON output.
     */
    private static final int API_SPEC_VERSION = 5;

    /**
     * The Livedoc version used to generate the doc.
     */
    private String version = "<unknown>";

    /**
     * The date of the build of the Livedoc version used to generate the doc.
     */
    private String buildDate = "<unknown>";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getSpecVersion() {
        return API_SPEC_VERSION;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    @Override
    public String toString() {
        return "LivedocMetaData{" + "version='" + version + '\'' + ", buildDate='" + buildDate + '\'' + '}';
    }
}
