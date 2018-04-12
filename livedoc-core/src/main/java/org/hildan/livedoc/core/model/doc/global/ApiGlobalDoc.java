package org.hildan.livedoc.core.model.doc.global;

public class ApiGlobalDoc {

    private String general;

    private ApiChangelogsDoc changelogSet;

    private ApiMigrationsDoc migrationSet;

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
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
