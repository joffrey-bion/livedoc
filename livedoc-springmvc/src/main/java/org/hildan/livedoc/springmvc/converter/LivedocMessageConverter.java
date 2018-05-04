package org.hildan.livedoc.springmvc.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An implementation of {@link AbstractJackson2HttpMessageConverter} that uses an independent {@link ObjectMapper} for
 * the {@value #APPLICATION_LIVEDOC} media type.
 */
public class LivedocMessageConverter extends AbstractJackson2HttpMessageConverter {

    public static final String APPLICATION_LIVEDOC = "application/livedoc+json";

    private static final MediaType APPLICATION_LIVEDOC_MEDIA_TYPE = MediaType.valueOf(APPLICATION_LIVEDOC);

    public LivedocMessageConverter() {
        super(new ObjectMapper(), APPLICATION_LIVEDOC_MEDIA_TYPE);
    }
}
