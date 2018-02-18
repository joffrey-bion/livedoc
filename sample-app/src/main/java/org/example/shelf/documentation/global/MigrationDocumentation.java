package org.example.shelf.documentation.global;

import org.hildan.livedoc.core.annotations.global.ApiMigration;
import org.hildan.livedoc.core.annotations.global.ApiMigrationSet;

@ApiMigrationSet(migrations = {
        @ApiMigration(fromVersion = "1.1", steps = {"Step #3"}, toVersion = "2.0"),
        @ApiMigration(fromVersion = "1.0", steps = {"Step #1", "Step #2"}, toVersion = "1.1")
})
public class MigrationDocumentation {

}
