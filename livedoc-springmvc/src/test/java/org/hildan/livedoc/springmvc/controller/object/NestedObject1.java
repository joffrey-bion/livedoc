package org.hildan.livedoc.springmvc.controller.object;

import java.util.List;

public class NestedObject1 {

    public NestedObject2 nestedObject2;

    private List<NestedObject3> listOfNestedObjects;

    public List<NestedObject3> getListOfNestedObjects() {
        return listOfNestedObjects;
    }
}
