package org.example.shelf.controller;

import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.core.model.doc.Livedoc;
import org.hildan.livedoc.springmvc.controller.JsonLivedocController;
import org.hildan.livedoc.springmvc.converter.LivedocMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiAuthBasic(testUsers = {
        @ApiAuthBasicUser(username = "testuser", password = "password")
})
@Controller
@RequestMapping("/secure")
public class SecureDocController {

    /**
     * This endpoint is an example of authenticated jsondoc endpoint.
     * <p>
     * It simply forwards to the {@link JsonLivedocController} added by livedoc-springboot, but requires authentication.
     *
     * @return the Livedoc documentation in the form of a JSON body
     */
    @GetMapping(value = "/jsondoc", produces = LivedocMessageConverter.APPLICATION_LIVEDOC)
    @ApiResponseBodyType(Livedoc.class) // the String is a forward, Livedoc can't know that
    public String jsondoc() {
        return "forward:/jsondoc";
    }
}
