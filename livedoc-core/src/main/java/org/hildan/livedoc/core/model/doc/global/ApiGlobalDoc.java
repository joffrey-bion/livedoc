package org.hildan.livedoc.core.model.doc.global;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiGlobalDoc {

    public final String livedocId = UUID.randomUUID().toString();

    private List<ApiGlobalSectionDoc> sections;

    private ApiChangelogsDoc changelogSet;

    private ApiMigrationsDoc migrationSet;

    public ApiGlobalDoc() {
        this.sections = new ArrayList<>();
        this.changelogSet = new ApiChangelogsDoc();
        this.migrationSet = new ApiMigrationsDoc();
    }

    public List<ApiGlobalSectionDoc> getSections() {
        return sections;
    }

    public void setSections(List<ApiGlobalSectionDoc> sections) {
        this.sections = sections;
    }

    public ApiChangelogsDoc getChangelogSet() {
        return changelogSet;
    }

    public void setChangelogSet(ApiChangelogsDoc changelogSet) {
        this.changelogSet = changelogSet;
    }

    public ApiMigrationsDoc getMigrationSet() {
        return migrationSet;
    }

    public void setMigrationSet(ApiMigrationsDoc migrationSet) {
        this.migrationSet = migrationSet;
    }

}
