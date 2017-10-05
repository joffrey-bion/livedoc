package org.hildan.livedoc.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ListJoiner<T> implements Collector<List<T>, List<T>, List<T>> {

    private final T delimiter;

    public ListJoiner(T delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, List<T>> accumulator() {
        return (list1, list2) -> {
            if (!list1.isEmpty()) {
                list1.add(delimiter);
            }
            list1.addAll(list2);
        };
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            if (!list1.isEmpty()) {
                list1.add(delimiter);
            }
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return l -> l;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.singleton(Characteristics.IDENTITY_FINISH);
    }
}
