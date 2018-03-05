package org.hildan.livedoc.core.test.controller;

import java.util.List;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.errors.ApiError;
import org.hildan.livedoc.core.annotations.errors.ApiErrors;
import org.hildan.livedoc.core.annotations.ApiHeader;
import org.hildan.livedoc.core.annotations.ApiHeaders;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.model.doc.ApiVerb;

@Api(name = "Test1Controller", description = "My test controller #1")
@ApiVersion(since = "1.0")
@ApiAuthNone
public class Test1Controller {

    @ApiOperation(path = "/test1", verbs = ApiVerb.GET, description = "test method for controller 1",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiVersion(since = "1.0")
    @ApiHeaders(headers = {@ApiHeader(name = "application_id", description = "The application's ID")})
    @ApiErrors({
            @ApiError(code = "1000", description = "A test error #1"),
            @ApiError(code = "2000", description = "A test error #2")
    })
    @ApiResponseBodyType
    public List<Integer> get(@ApiPathParam(name = "id", description = "abc") String id,
            @ApiPathParam(name = "count", description = "xyz") Integer count, @ApiRequestBodyType String name) {
        return null;
    }
}
