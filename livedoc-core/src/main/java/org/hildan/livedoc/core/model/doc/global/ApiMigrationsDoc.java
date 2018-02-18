package org.hildan.livedoc.core.model.doc.global;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class ApiMigrationsDoc {
    public final String livedocId = UUID.randomUUID().toString();

    private Set<ApiMigrationDoc> migrations;

    public ApiMigrationsDoc() {
        this.migrations = new LinkedHashSet<>();
    }

    public Set<ApiMigrationDoc> getMigrations() {
        return migrations;
    }

    public void setMigrations(Set<ApiMigrationDoc> migrations) {
        this.migrations = migrations;
    }

    public void addMigration(ApiMigrationDoc apiMigrationDoc) {
        this.migrations.add(apiMigrationDoc);
    }

}
