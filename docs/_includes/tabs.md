{% assign tab_set_id = include.tab_set_name | default: "tabset" | slugify %}
<div id="{{tab_set_id}}">
    <ul>
{% for name in include.tab_names %}
        <li><a href="#{{tab_set_id}}-{{forloop.index0}}">{{ name | strip }}</a></li>
{% endfor %}
    </ul>
{% for content in include.tab_contents %}
    <div id="{{tab_set_id}}-{{forloop.index0}}">
    {{ content | markdownify }}
    </div>
{% endfor %}
</div>
<script>$(function(){$("#{{tab_set_id}}").tabs();});</script>
