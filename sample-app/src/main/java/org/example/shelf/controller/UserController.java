package org.example.shelf.controller;

import java.util.List;

import org.example.shelf.documentation.DocumentationConstants;
import org.example.shelf.exception.ItemNotFoundException;
import org.example.shelf.model.User;
import org.example.shelf.repository.UserRepository;
import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiError;
import org.hildan.livedoc.core.annotations.ApiErrors;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "The user services", name = "User services")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ApiMethod(id = DocumentationConstants.USER_FIND_ONE)
    @ApiErrors(apierrors = {
            @ApiError(code = "404", description = "When the user with the given id is not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findOne(@PathVariable("id") Long id) throws ItemNotFoundException {
        User findOne = userRepository.findOne(id);
        if (findOne == null) {
            throw new ItemNotFoundException();
        } else {
            return findOne;
        }
    }

    @RequestMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
