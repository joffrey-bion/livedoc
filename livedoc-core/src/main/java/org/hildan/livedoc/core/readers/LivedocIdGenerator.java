package org.hildan.livedoc.core.readers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.jetbrains.annotations.NotNull;

import static org.hildan.livedoc.core.readers.annotation.ApiDocReader.nullifyIfEmpty;

public class LivedocIdGenerator {

    public static String asLivedocId(String userText) {
        try {
            return URLEncoder.encode(userText, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // The system should always have UTF_8
            throw new RuntimeException("Careless code change led here", e);
        }
    }

    public static String getTypeId(Class<?> clazz) {
        ApiType apiType = clazz.getAnnotation(ApiType.class);
        if (apiType != null) {
            String id = nullifyIfEmpty(apiType.id());
            if (id != null) {
                return id;
            }
        }
        return asLivedocId(clazz.getCanonicalName());
    }

    public static String defaultApiId(Class<?> clazz) {
        return asLivedocId(clazz.getCanonicalName());
    }

    public static String defaultApiOperationId(ApiOperationDoc apiOperationDoc) {
        List<ApiVerb> verbs = apiOperationDoc.getVerbs();
        String firstVerb = verbs.isEmpty() ? "" : verbs.get(0).toString();
        String firstPath = cleanFirstPath(apiOperationDoc.getPaths());
        return asLivedocId(firstVerb + firstPath);
    }

    public static String defaultMessageId(AsyncMessageDoc doc) {
        String firstPath = cleanFirstPath(doc.getDestinations());
        return asLivedocId(doc.getCommand().toString() + "-" + firstPath);
    }

    @NotNull
    private static String cleanFirstPath(List<String> paths) {
        String firstPath = paths.isEmpty() ? "" : paths.get(0);
        // remove path param syntax with braces and format, such as {id:[0-9]+}, just keep the param name
        firstPath = firstPath.replaceAll("\\{(\\w+)(:[^}]*)?}", "$1");
        firstPath = firstPath.replaceAll("/", "-");
        if (!firstPath.startsWith("-")) {
            firstPath = "-" + firstPath;
        }
        return firstPath;
    }
}
