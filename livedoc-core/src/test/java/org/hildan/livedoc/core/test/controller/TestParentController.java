package org.hildan.livedoc.core.test.controller;

import java.util.List;

import org.hildan.livedoc.core.annotations.ApiOperation;

public abstract class TestParentController {

    @ApiOperation(path = "/testParent")
    public List<Integer> get(String id, String name) {
        return null;
    }

}
