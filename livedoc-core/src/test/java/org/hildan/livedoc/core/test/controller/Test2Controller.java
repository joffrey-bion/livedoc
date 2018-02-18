package org.hildan.livedoc.core.test.controller;

import java.util.List;
import java.util.Map;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.test.pojo.Parent;

@Api(name = "Test2Controller", description = "My test controller #2")
public class Test2Controller {

    @ApiMethod(path = "/test2", verbs = ApiVerb.POST, description = "test method for controller 2",
            consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    @ApiVersion(since = "1.0", until = "2.12")
    @ApiAuthBasic(roles = "ROLE_USER",
            testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
    @ApiResponseBodyType
    public String save(@ApiRequestBodyType List<String> names) {
        return null;
    }

    @ApiMethod(path = "/test2", verbs = ApiVerb.PUT, description = "update method for controller 2",
            consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    @ApiVersion(since = "1.0", until = "2.12")
    @ApiAuthBasic(roles = "ROLE_USER",
            testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
    @ApiResponseBodyType
    public String update(@ApiRequestBodyType List<String> names) {
        return null;
    }

    @ApiMethod(path = "/test2", verbs = ApiVerb.PATCH, description = "patch update method for controller 2",
            consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    @ApiVersion(since = "1.0", until = "2.12")
    @ApiAuthBasic(roles = "ROLE_USER",
            testUsers = {@ApiAuthBasicUser(username = "test-username", password = "test-password")})
    @ApiResponseBodyType
    public String patch(@ApiRequestBodyType Map<String, Object> properties) {
        return null;
    }

    @ApiMethod(path = "/testMap", verbs = ApiVerb.GET, description = "map method for controller 2",
            consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    @ApiResponseBodyType
    public Map<String, Parent> map(@ApiRequestBodyType List<String> names) {
        return null;
    }

    @ApiMethod(path = "/testMapBody", verbs = ApiVerb.GET, description = "map body method for controller 2",
            consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    @ApiResponseBodyType
    public String map(@ApiRequestBodyType Map<String, Parent> names) {
        return null;
    }

    @ApiMethod(path = "/testDelete", verbs = ApiVerb.DELETE, description = "delete test method for controller 2",
            consumes = {}, produces = {"application/json", "application/xml"})
    @ApiResponseBodyType
    public void delete(@ApiPathParam(name = "parent", description = "A parent object") Parent parent) {

    }

    @ApiMethod(path = "/testOptions", verbs = ApiVerb.OPTIONS, description = "options test method for controller 2",
            consumes = {}, produces = {"application/json", "application/xml"})
    @ApiResponseBodyType
    public String options() {
        return "options";
    }

    @ApiMethod(path = "/testHead", verbs = ApiVerb.HEAD, description = "head test method for controller 2",
            consumes = {}, produces = {"application/json", "application/xml"})
    @ApiResponseBodyType
    public String head() {
        return "head";
    }

    @ApiMethod(path = "/testtrace", verbs = ApiVerb.TRACE, description = "trace test method for controller 2",
            consumes = {}, produces = {"application/json", "application/xml"})
    @ApiResponseBodyType
    public String trace() {
        return "trace";
    }
}
