package org.hildan.livedoc.core.merger;

public interface Mergeable<T> {

    T merge(T override, DocMerger merger);
}
