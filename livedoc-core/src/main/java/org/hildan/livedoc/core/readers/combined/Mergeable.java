package org.hildan.livedoc.core.readers.combined;

/**
 * Represent a type for which multiple instances can be merged into one.
 *
 * @param <T>
 *         the type of objects that can be merged
 */
public interface Mergeable<T> {

    /**
     * Merges this object with the given other object to return a new object. The given object's properties take
     * precedence over this object's properties.
     *
     * @param override
     *         the overriding other object
     * @param merger
     *         the {@link DocMerger} to use to delegate the merge of sub-elements
     *
     * @return a new object combining the properties of this object and the provided one
     */
    T merge(T override, DocMerger merger);
}
