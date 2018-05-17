package org.example.shelf.documentation;

import org.hildan.livedoc.core.annotations.global.ApiGlobalPage;

@ApiGlobalPage(title = "What is this?", template = "global-top.ftl")
@ApiGlobalPage(title = "Authentication", resource = "/doc/global-authentication.html")
@ApiGlobalPage(title = "Headers", resource = "/doc/global-headers.html")
@ApiGlobalPage(title = "Status codes", resource = "/doc/global-responsestatuscodes.html")
public class Documentation {

    public static final String GROUP_LIBRARY = "Library";
}
