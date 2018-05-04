package org.hildan.livedoc.core.readers.javadoc;

import com.github.therapi.runtimejavadoc.CommentFormatter;
import com.github.therapi.runtimejavadoc.InlineLink;

public class LivedocCommentFormatter extends CommentFormatter {

    @Override
    protected String renderLink(InlineLink e) {
        return "<code>" + e.getLink().getLabel() + "</code>";
    }
}
