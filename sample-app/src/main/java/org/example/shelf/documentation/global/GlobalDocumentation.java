package org.example.shelf.documentation.global;

import org.hildan.livedoc.core.annotations.global.ApiGlobal;
import org.hildan.livedoc.core.annotations.global.ApiGlobalSection;

@ApiGlobal(sections = {
        @ApiGlobalSection(title = "What is this?", paragraphs = {"/livedocfile:/doc/global-top.html"}),
        @ApiGlobalSection(title = "Authentication", paragraphs = {"/livedocfile:/doc/global-authentication.html"}),
        @ApiGlobalSection(title = "Headers", paragraphs = {"/livedocfile:/doc/global-headers.html"}),
        @ApiGlobalSection(title = "Status codes", paragraphs = {"/livedocfile:/doc/global-responsestatuscodes.html"})
})
public class GlobalDocumentation {

}
