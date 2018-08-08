package org.example.shelf.controller;

import org.hildan.livedoc.core.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("upload")
public class UploadController {

    @RequestMapping(path = "/{target}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upload(@PathVariable("target") long targetId, @RequestParam("file") MultipartFile file) {
    }

    @ApiOperation(id = "custom") // to avoid duplicate ID
    @RequestMapping(path = "/{target}", method = RequestMethod.POST)
    public void upload(@PathVariable("target") long targetId, @RequestBody Object data) {
    }
}
