package org.hildan.livedoc.core.test.controller;

import java.util.List;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiMethod;

@Api(name = "TestChildController", description = "My test child controller")
public class TestChildController extends TestParentController {

    @ApiMethod(path = "/testChild")
    public List<Integer> get() {
        return null;
    }
}
