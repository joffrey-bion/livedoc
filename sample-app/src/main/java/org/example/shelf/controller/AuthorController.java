package org.example.shelf.controller;

import java.util.List;

import org.example.shelf.documentation.DocumentationConstants;
import org.example.shelf.model.Author;
import org.example.shelf.repository.AuthorRepository;
import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiAuthToken;
import org.hildan.livedoc.core.annotations.ApiBodyObject;
import org.hildan.livedoc.core.annotations.ApiMethod;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "The author services", name = "Author services", group = DocumentationConstants.GROUP_LIBRARY)
@ApiAuthToken(roles = {"*"}, testtokens = "abc", scheme = "Bearer")
public class AuthorController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @ApiMethod(id = DocumentationConstants.AUTHOR_FIND_ONE)
    @ApiAuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiResponseObject
    public Author findOne(@ApiPathParam(name = "id") @PathVariable Long id) {
        return authorRepository.findOne(id);
    }

    @ApiMethod(id = DocumentationConstants.AUTHOR_FIND_ALL)
    @RequestMapping(method = RequestMethod.GET)
    @ApiResponseObject
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @ApiMethod(id = DocumentationConstants.AUTHOR_SAVE)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponseObject
    public ResponseEntity<Void> save(@ApiBodyObject @RequestBody Author author,
            UriComponentsBuilder uriComponentsBuilder) {
        authorRepository.save(author);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/authors/{id}").buildAndExpand(author.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @ApiMethod(id = DocumentationConstants.AUTHOR_DELETE)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@ApiPathParam(name = "id") @PathVariable Long id) {
        Author author = authorRepository.findOne(id);
        authorRepository.delete(author);
    }

}