package org.hildan.livedoc.core.model.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Group<T> implements Comparable<Group<T>> {

    private final String groupName;

    private final List<T> elements;

    public Group(String groupName, List<T> elements) {
        this.groupName = groupName;
        this.elements = elements;
    }

    public static <T extends Groupable & Comparable<T>> List<Group<T>> groupAndSort(Collection<T> elements) {
        Map<String, List<T>> groupedElements = elements.stream().collect(Collectors.groupingBy(Groupable::getGroup));
        return groupedElements.entrySet()
                              .stream()
                              .sorted(Comparator.comparing(Entry::getKey))
                              .map(e -> createSortedGroup(e.getKey(), e.getValue()))
                              .collect(Collectors.toList());
    }

    private static <T extends Comparable<T>> Group<T> createSortedGroup(String groupName, List<T> elements) {
        List<T> sortedElements = new ArrayList<>(elements);
        Collections.sort(sortedElements);
        return new Group<>(groupName, sortedElements);
    }

    public String getGroupName() {
        return groupName;
    }

    public List<T> getElements() {
        return elements;
    }

    @Override
    public int compareTo(Group<T> group) {
        return groupName.compareTo(group.groupName);
    }
}
