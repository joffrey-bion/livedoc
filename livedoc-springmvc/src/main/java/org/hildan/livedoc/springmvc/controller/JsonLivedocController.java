package org.hildan.livedoc.springmvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.hildan.livedoc.core.LivedocReader;
import org.hildan.livedoc.core.config.LivedocConfiguration;
import org.hildan.livedoc.core.model.doc.ApiMetaData;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.springmvc.SpringLivedocReaderFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Spring controller that exposes the API documentation as JSON, under the endpoint {@value #JSON_DOC_ENDPOINT}.
 */
@Controller
public class JsonLivedocController {

    private static final String JSON_DOC_ENDPOINT = "/jsondoc";

    private final ApiMetaData apiInfo;

    private final LivedocReader livedocReader;

    /**
     * Creates a new {@code JsonLivedocController} with the given parameters.
     *
     * @param apiInfo
     *         some meta data about the documented API (name, version, baseUrl...)
     * @param config
     *         the configuration for the doc generation (including the packages to scan)
     * @param jacksonObjectMapper
     *         the {@link ObjectMapper} to use for property exploration and template generation, or null to use a new
     *         mapper with Spring defaults.
     */
    public JsonLivedocController(ApiMetaData apiInfo, LivedocConfiguration config, ObjectMapper jacksonObjectMapper) {
        this(apiInfo, SpringLivedocReaderFactory.getReader(config, jacksonObjectMapper));
    }

    public JsonLivedocController(ApiMetaData apiInfo, LivedocReader livedocReader) {
        this.apiInfo = apiInfo;
        this.livedocReader = livedocReader;
    }

    @RequestMapping(value = JsonLivedocController.JSON_DOC_ENDPOINT,
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    @ResponseBody
    public Livedoc getJsonLivedoc(HttpServletRequest request) {
        String userDefinedBaseUrl = apiInfo.getBaseUrl();
        if (userDefinedBaseUrl == null) {
            apiInfo.setBaseUrl(getBaseUrl(request));
        }
        return livedocReader.read(apiInfo);
    }

    private static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath;
    }
}
