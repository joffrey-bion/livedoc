package org.hildan.livedoc.springmvc.controller;

import java.util.List;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonLivedocController {

    public static final String JSON_DOC_ENDPOINT = "/jsondoc";

    private String version;

    private String basePath;

    private LivedocReader livedocReader;

    private boolean playgroundEnabled = true;

    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    public JsonLivedocController(String version, String basePath, List<String> packages) {
        this(version, basePath, SpringLivedocReaderFactory.getReader(packages));
    }

    public JsonLivedocController(String version, String basePath, LivedocReader livedocReader) {
        this.version = version;
        this.basePath = basePath;
        this.livedocReader = livedocReader;
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
    @CrossOrigin
    @ResponseBody
    public Livedoc getApi() {
        return livedocReader.read(version, basePath, playgroundEnabled, displayMethodAs);
    }
}
