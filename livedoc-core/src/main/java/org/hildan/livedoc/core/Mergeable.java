package org.hildan.livedoc.core;

public interface Mergeable<T> {

    void merge(T source, DocMerger merger);
}
