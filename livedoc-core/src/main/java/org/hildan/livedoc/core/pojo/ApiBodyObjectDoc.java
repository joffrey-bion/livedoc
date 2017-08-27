package org.hildan.livedoc.core.pojo;

import java.util.UUID;

import org.hildan.livedoc.core.scanner.templates.ObjectTemplate;
import org.hildan.livedoc.core.util.LivedocType;

public class ApiBodyObjectDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private LivedocType type;

    private ObjectTemplate template;

    public ApiBodyObjectDoc(LivedocType type) {
        this.type = type;
    }

    public LivedocType getType() {
        return type;
    }

    public void setType(LivedocType type) {
        this.type = type;
    }

    public ObjectTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ObjectTemplate template) {
        this.template = template;
    }

}
