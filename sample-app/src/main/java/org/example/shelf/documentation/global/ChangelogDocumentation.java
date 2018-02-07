package org.example.shelf.documentation.global;

import org.hildan.livedoc.core.annotations.global.ApiChangelog;
import org.hildan.livedoc.core.annotations.global.ApiChangelogSet;

@ApiChangelogSet(changlogs = {
        @ApiChangelog(changes = {"Changelog #5", "Changelog #6"}, version = "2.0"),
        @ApiChangelog(changes = {"Changelog #2", "Changelog #3", "Changelog #4"}, version = "1.1"),
        @ApiChangelog(changes = {"Changelog #1"}, version = "1.0")
})
public class ChangelogDocumentation {

}
