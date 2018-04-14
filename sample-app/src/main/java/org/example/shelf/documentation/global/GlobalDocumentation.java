package org.example.shelf.documentation.global;

import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;

import static org.hildan.livedoc.core.annotations.global.PageContentType.FREEMARKER;
import static org.hildan.livedoc.core.annotations.global.PageContentType.TEXT_FILE;

@ApiGlobal({
        @ApiGlobalPage(title = "What is this?", content = "global-top.ftl", type = FREEMARKER),
        @ApiGlobalPage(title = "Authentication", content = "/doc/global-authentication.html", type = TEXT_FILE),
        @ApiGlobalPage(title = "Headers", content = "/doc/global-headers.html", type = TEXT_FILE),
        @ApiGlobalPage(title = "Status codes", content = "/doc/global-responsestatuscodes.html", type = TEXT_FILE)
})
public class GlobalDocumentation {

}
