package org.hildan.livedoc.core.test.controller;

import java.util.List;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;

@Api(name = "TestChildController", description = "My test child controller")
public class TestChildController extends TestParentController {

    @ApiOperation(path = "/testChild")
    public List<Integer> get() {
        return null;
    }
}
