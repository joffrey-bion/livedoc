package org.hildan.livedoc.springmvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A Spring controller that exposes the API documentation as JSON, under the endpoint {@value #JSON_DOC_ENDPOINT}.
 */
@Controller
public class JsonLivedocController {

    public static final String JSON_DOC_ENDPOINT = "/jsondoc";

    private String version;

    private String basePath;

    private LivedocReader livedocReader;

    private boolean playgroundEnabled = true;

    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    /**
     * Creates a new {@code JsonLivedocController} with the given parameters.
     *
     * @param version
     *         the current API version
     * @param packages
     *         the packages to scan
     * @param jacksonObjectMapper
     *         the {@link ObjectMapper} to use for property exploration and template generation, or null to use a new
     *         mapper with Spring defaults.
     */
    public JsonLivedocController(String version, List<String> packages, ObjectMapper jacksonObjectMapper) {
        this(version, SpringLivedocReaderFactory.getReader(packages, jacksonObjectMapper));
    }

    public JsonLivedocController(String version, LivedocReader livedocReader) {
        this.version = version;
        this.livedocReader = livedocReader;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
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

    @RequestMapping(name = JsonLivedocController.JSON_DOC_ENDPOINT,
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    @ResponseBody
    public Livedoc getJsonLivedoc(HttpServletRequest request) {
        String baseUrl = basePath == null ? getBaseUrl(request) : basePath;
        return livedocReader.read(version, baseUrl, playgroundEnabled, displayMethodAs);
    }

    private static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath;
    }
}
