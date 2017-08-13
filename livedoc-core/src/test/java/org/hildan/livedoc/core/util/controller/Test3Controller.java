package org.hildan.livedoc.core.util.controller;

import java.util.List;

import org.hildan.livedoc.core.annotation.Api;
import org.hildan.livedoc.core.annotation.ApiAuthNone;
import org.hildan.livedoc.core.annotation.ApiBodyObject;
import org.hildan.livedoc.core.annotation.ApiError;
import org.hildan.livedoc.core.annotation.ApiErrors;
import org.hildan.livedoc.core.annotation.ApiHeader;
import org.hildan.livedoc.core.annotation.ApiHeaders;
import org.hildan.livedoc.core.annotation.ApiMethod;
import org.hildan.livedoc.core.annotation.ApiPathParam;
import org.hildan.livedoc.core.annotation.ApiResponseObject;
import org.hildan.livedoc.core.annotation.ApiVersion;
import org.hildan.livedoc.core.pojo.ApiVerb;

@Api(name = "Test3Controller", description = "My test controller #3")
@ApiVersion(since = "1.0")
@ApiErrors(apierrors = {@ApiError(code = "400", description = "Common bad exception"),
        @ApiError(code = "1000", description = "This exception will be overridden by the one defined at method level")})
@ApiAuthNone
public class Test3Controller {

    @ApiMethod(path = "/test3", verb = ApiVerb.GET, description = "test method for controller 3",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiVersion(since = "1.0")
    @ApiHeaders(headers = {@ApiHeader(name = "application_id", description = "The application's ID")})
    @ApiErrors(apierrors = {@ApiError(code = "1000", description = "A test error #1"),
            @ApiError(code = "2000", description = "A test error #2")})
    public @ApiResponseObject
    List<Integer> get(@ApiPathParam(name = "id", description = "abc") String id,
            @ApiPathParam(name = "count", description = "xyz") Integer count, @ApiBodyObject String name) {
        return null;
    }

}
