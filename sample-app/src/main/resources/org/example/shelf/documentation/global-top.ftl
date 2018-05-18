<#-- @ftlvariable name="" type="org.hildan.livedoc.core.templating.GlobalTemplateData" -->
<h1>${apiInfo.name!"About this documentation"}</h1>
<hr>
<p>
    This is a generated documentation for Livedoc's demo API, based on the code in the following packages:
</p>
<ul>
    <#list scannedPackages as pkg>
        <li>${pkg}</li>
    </#list>
</ul>
<p>
    Check out the links at the top of this page to browse the APIs and Types documentation. The code of this demo is
    available at <a href="https://github.com/joffrey-bion/livedoc/tree/master/sample-app">on GitHub</a>.
</p>

<h4>About this page</h4>
<p>
    This page was generated server-side by Livedoc v${livedocInfo.version}, based on a FreeMarker template file.
</p>
<p>
    We can make references to pieces of documentation from FreeMarker pages.

    For instance, here is a reference to the <@type_ref type="org.example.shelf.model.Author">Author type</@type_ref>.
    You can also use references to an <@api_ref api="org.example.shelf.controller.AuthorController">API</@api_ref> or an
    <@api_ref api="org.example.shelf.controller.AuthorController" operation="get-authors-id">API operation</@api_ref>.
</p>
