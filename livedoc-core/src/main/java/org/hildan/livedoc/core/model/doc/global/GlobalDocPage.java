package org.hildan.livedoc.core.model.doc.global;

import org.hildan.livedoc.core.readers.LivedocIdGenerator;

public class GlobalDocPage {

    private final String title;

    private final String content;

    public GlobalDocPage(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getLivedocId() {
        return LivedocIdGenerator.asLivedocId(title);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
