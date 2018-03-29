{% assign groupId = include.groupId | default: site.groupId %}
{% assign artifactId = include.artifactId | default: site.artifactId %}
{% assign version = include.version | default: site.latestVersion %}

{% capture gradle_snippet %}
```groovy
annotationProcessor '{{groupId}}:{{artifactId}}:{{version}}'
```
{% endcapture %}

{% capture maven_snippet %}
```xml
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.6.1</version>
            <configuration>
                <annotationProcessorPaths>
                    <annotationProcessorPath>
                        <groupId>{{groupId}}</groupId>
                        <artifactId>{{artifactId}}</artifactId>
                        <version>{{version}}</version>
                    </annotationProcessorPath>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</pluginManagement>
```
{% endcapture %}

{% capture set_name %} 
tabset-{{groupId}}-{{artifactId}}-{{version}}
{% endcapture %}

{% assign build_tools = 'Gradle,Maven' | split: ',' | compact %}
{% assign code_snippets = site.emptyArray | push: gradle_snippet | push: maven_snippet %}

{% include tabs.md tab_set_name=set_name tab_names=build_tools tab_contents=code_snippets %}
