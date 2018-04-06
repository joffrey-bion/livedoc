package org.example.shelf.controller;

import org.hildan.livedoc.core.annotations.auth.ApiAuthBasic;
import org.hildan.livedoc.core.annotations.auth.ApiAuthBasicUser;
import org.hildan.livedoc.springmvc.controller.JsonLivedocController;
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
    @GetMapping("/jsondoc")
    public String jsondoc() {
        return "forward:/jsondoc";
    }
}
