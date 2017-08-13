package org.hildan.livedoc.springmvc.controller;

import java.util.List;

import org.hildan.livedoc.core.pojo.JSONDoc;
import org.hildan.livedoc.core.pojo.JSONDoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.JSONDocScanner;
import org.hildan.livedoc.springmvc.scanner.Spring3JSONDocScanner;
import org.hildan.livedoc.springmvc.scanner.Spring4JSONDocScanner;
import org.springframework.core.SpringVersion;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JSONDocController {

    private String version;

    private String basePath;

    private List<String> packages;

    private JSONDocScanner jsondocScanner;

    private boolean playgroundEnabled = true;

    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    public final static String JSONDOC_DEFAULT_PATH = "/jsondoc";

    private final static Integer SPRING_MAJOR_VERSION_3 = 3;

    public JSONDocController(String version, String basePath, List<String> packages) {
        this(version, basePath, packages, getSpringJsonDocScanner());
    }

    public JSONDocController(String version, String basePath, List<String> packages, JSONDocScanner jsonDocScanner) {
        this.version = version;
        this.basePath = basePath;
        this.packages = packages;
        this.jsondocScanner = jsonDocScanner;
    }

    private static JSONDocScanner getSpringJsonDocScanner() {
        if (isSpring4OrMoreOnClasspath()) {
            return new Spring4JSONDocScanner();
        } else {
            return new Spring3JSONDocScanner();
        }
    }

    private static boolean isSpring4OrMoreOnClasspath() {
        String springVersion = SpringVersion.getVersion();
        if (springVersion != null && !springVersion.isEmpty()) {
            Integer majorSpringVersion = Integer.parseInt(springVersion.split("\\.")[0]);
            return majorSpringVersion > SPRING_MAJOR_VERSION_3;
        } else {
            return isRestControllerAnnotationOnClassPath();
        }
    }

    private static boolean isRestControllerAnnotationOnClassPath() {
        try {
            Class.forName("org.springframework.web.bind.annotation.RestController");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
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

    @RequestMapping(value = JSONDocController.JSONDOC_DEFAULT_PATH, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    JSONDoc getApi() {
        return jsondocScanner.getJSONDoc(version, basePath, packages, playgroundEnabled, displayMethodAs);
    }

}
