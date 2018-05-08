package org.hildan.livedoc.core.meta;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hildan.livedoc.core.model.doc.LivedocMetaData;
import org.jetbrains.annotations.NotNull;

/**
 * This component is responsible for reading metadata about the Livedoc library itself, like the version or build date.
 */
public class LivedocMetaDataReader {

    private static final String META_DATA_RESOURCE = "livedoc.properties";

    /**
     * Reads metadata about Livedoc, such as version or build date.
     *
     * @return a filled up {@link LivedocMetaData} object
     */
    @NotNull
    public static LivedocMetaData read() {
        InputStream in = LivedocMetaDataReader.class.getResourceAsStream(META_DATA_RESOURCE);
        if (in == null) {
            return new LivedocMetaData();
        }
        try {
            Properties props = new Properties();
            props.load(in);
            return buildFromProps(props);
        } catch (IOException e) {
            return new LivedocMetaData();
        }
    }

    private static LivedocMetaData buildFromProps(Properties props) {
        LivedocMetaData metaData = new LivedocMetaData();
        metaData.setVersion(props.getProperty("version"));
        metaData.setBuildDate(props.getProperty("buildDate"));
        return metaData;
    }
}
