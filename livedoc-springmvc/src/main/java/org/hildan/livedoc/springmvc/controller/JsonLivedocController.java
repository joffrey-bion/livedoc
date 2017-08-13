package org.hildan.livedoc.springmvc.controller;

import java.util.List;

import org.hildan.livedoc.core.pojo.Livedoc;
import org.hildan.livedoc.core.pojo.Livedoc.MethodDisplay;
import org.hildan.livedoc.core.scanner.DocAnnotationScanner;
import org.hildan.livedoc.springmvc.scanner.Spring3DocAnnotationScanner;
import org.hildan.livedoc.springmvc.scanner.Spring4DocAnnotationScanner;
import org.springframework.core.SpringVersion;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonLivedocController {

    private String version;

    private String basePath;

    private List<String> packages;

    private DocAnnotationScanner docAnnotationScanner;

    private boolean playgroundEnabled = true;

    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    public final static String JSON_DOC_ENDPOINT = "/jsondoc";

    private final static Integer SPRING_MAJOR_VERSION_3 = 3;

    public JsonLivedocController(String version, String basePath, List<String> packages) {
        this(version, basePath, packages, getSpringJsonDocScanner());
    }

    public JsonLivedocController(String version, String basePath, List<String> packages, DocAnnotationScanner scanner) {
        this.version = version;
        this.basePath = basePath;
        this.packages = packages;
        this.docAnnotationScanner = scanner;
    }

    private static DocAnnotationScanner getSpringJsonDocScanner() {
        if (isSpring4OrMoreOnClasspath()) {
            return new Spring4DocAnnotationScanner();
        } else {
            return new Spring3DocAnnotationScanner();
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

    @RequestMapping(value = JsonLivedocController.JSON_DOC_ENDPOINT, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Livedoc getApi() {
        return docAnnotationScanner.getLivedoc(version, basePath, packages, playgroundEnabled, displayMethodAs);
    }
}
