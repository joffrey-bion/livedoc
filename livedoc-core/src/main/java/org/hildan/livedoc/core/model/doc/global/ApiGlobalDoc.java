package org.hildan.livedoc.core.model.doc.global;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

public class ApiGlobalDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private Set<ApiGlobalSectionDoc> sections;

    private ApiChangelogsDoc changelogSet;

    private ApiMigrationsDoc migrationSet;

    public ApiGlobalDoc() {
        this.sections = Sets.newLinkedHashSet();
        this.changelogSet = new ApiChangelogsDoc();
        this.migrationSet = new ApiMigrationsDoc();
    }

    public Set<ApiGlobalSectionDoc> getSections() {
        return sections;
    }

    public void setSections(Set<ApiGlobalSectionDoc> sections) {
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

    public void addApiGlobalSectionDoc(ApiGlobalSectionDoc apiGlobalSectionDoc) {
        this.sections.add(apiGlobalSectionDoc);
    }

}
