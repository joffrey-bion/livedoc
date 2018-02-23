package org.hildan.livedoc.core.model.groups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group<T> implements Comparable<Group<T>> {

    private final String groupName;

    private final List<T> elements;

    public Group(String groupName, List<T> elements) {
        this.groupName = groupName;
        this.elements = elements;
    }

    public static <T extends Comparable<T>> Group<T> sorted(String groupName, List<T> elements) {
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
