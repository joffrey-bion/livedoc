package org.hildan.livedoc.core.readers.combined;

public interface Mergeable<T> {

    T merge(T override, DocMerger merger);
}
