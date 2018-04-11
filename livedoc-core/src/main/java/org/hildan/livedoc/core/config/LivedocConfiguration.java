package org.hildan.livedoc.core.config;

import org.hildan.livedoc.core.model.doc.Livedoc.MethodDisplay;

public class LivedocConfiguration {

    /**
     * Whether the playground is enabled or not
     */
    private boolean playgroundEnabled = true;

    /**
     * The way methods should be displayed in the UI
     */
    private MethodDisplay displayMethodAs = MethodDisplay.URI;

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
