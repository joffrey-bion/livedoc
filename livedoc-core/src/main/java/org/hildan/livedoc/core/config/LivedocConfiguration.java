package org.hildan.livedoc.core.config;

import java.util.List;

import org.hildan.livedoc.core.LivedocReaderBuilder;
import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;

/**
 * The configuration for the doc generation. This only contains the elements that are usually customized by the
 * developers of applications using Livedoc. For a more in-depth configuration of Livedoc generation, one must use a
 * {@link LivedocReaderBuilder}.
 */
public class LivedocConfiguration {

    /**
     * Defines the packages that should be scanned to find the classes to document. The given list will be used to find
     * controllers and to filter the types to document.
     */
    private List<String> packages;

    /**
     * Whether the playground is enabled or not
     */
    private boolean playgroundEnabled = true;

    /**
     * The way methods should be displayed in the UI
     */
    private MethodDisplay displayMethodAs = MethodDisplay.URI;

    /**
     * Creates a new {@code LivedocConfiguration} with the given package white list.
     *
     * @param packages
     *         the packages that should be scanned to find the classes to document. The given list will be used to find
     *         controllers and to filter the types to document.
     */
    public LivedocConfiguration(List<String> packages) {
        this.packages = packages;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
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
}
