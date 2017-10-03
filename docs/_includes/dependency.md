{% assign groupId = include.groupId | default: site.groupId %}
{% assign artifactId = include.artifactId | default: site.artifactId %}
{% assign version = include.version | default: site.latestVersion %}

{% capture gradle_snippet %}
```groovy
compile '{{groupId}}:{{artifactId}}:{{version}}'
```
{% endcapture %}

{% capture maven_snippet %}
```xml
<dependency>
  <groupId>{{groupId}}</groupId>
  <artifactId>{{artifactId}}</artifactId>
  <version>{{version}}</version>
</dependency>
```
{% endcapture %}

{% capture set_name %} 
tabset-{{groupId}}-{{artifactId}}-{{version}}
{% endcapture %}

{% assign build_tools = 'Gradle,Maven' | split: ',' | compact %}
{% assign code_snippets = site.emptyArray | push: gradle_snippet | push: maven_snippet %}

{% include tabs.md tab_set_name=set_name tab_names=build_tools tab_contents=code_snippets %}
