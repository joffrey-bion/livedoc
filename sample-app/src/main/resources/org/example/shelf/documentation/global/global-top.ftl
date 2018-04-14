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
<p>
    This page was generated server-side by Livedoc v${livedocInfo.version}, based on a FreeMarker template file.
</p>
