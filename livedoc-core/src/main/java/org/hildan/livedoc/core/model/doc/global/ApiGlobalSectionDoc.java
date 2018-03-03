package org.hildan.livedoc.core.model.doc.global;

import java.util.List;
import java.util.UUID;

public class ApiGlobalSectionDoc {

    public final String livedocId = UUID.randomUUID().toString();

    private String title;

    private List<String> paragraphs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }
}
