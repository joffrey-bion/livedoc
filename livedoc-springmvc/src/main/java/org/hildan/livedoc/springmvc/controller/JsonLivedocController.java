package org.hildan.livedoc.springmvc.controller;

import java.util.List;

import org.hildan.livedoc.core.LivedocBuilder;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocBuilderFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonLivedocController {

    public static final String JSON_DOC_ENDPOINT = "/jsondoc";

    private String version;

    private String basePath;

    private List<String> packages;

    private LivedocBuilder livedocBuilder;

    private boolean playgroundEnabled = true;

    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    public JsonLivedocController(String version, String basePath, List<String> packages) {
        this(version, basePath, packages, SpringLivedocBuilderFactory.springLivedocBuilder(packages));
    }

    public JsonLivedocController(String version, String basePath, List<String> packages, LivedocBuilder livedocBuilder) {
        this.version = version;
        this.basePath = basePath;
        this.packages = packages;
        this.livedocBuilder = livedocBuilder;
    }

    public boolean isPlaygroundEnabled() {
        return playgroundEnabled;
    }

    public void setPlaygroundEnabled(boolean playgroundEnabled) {
        this.playgroundEnabled = playgroundEnabled;
    }

    public MethodDisplay getDisplayMethodAs() {
        return displayMethodAs;
    }

    public void setDisplayMethodAs(MethodDisplay displayMethodAs) {
        this.displayMethodAs = displayMethodAs;
    }

    @RequestMapping(value = JsonLivedocController.JSON_DOC_ENDPOINT, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Livedoc getApi() {
        return livedocBuilder.build(version, basePath, packages, playgroundEnabled, displayMethodAs);
    }
}
