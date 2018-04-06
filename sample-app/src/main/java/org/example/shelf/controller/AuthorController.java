package org.example.shelf.controller;

import java.util.List;

import org.example.shelf.documentation.DocumentationConstants;
import org.example.shelf.model.Author;
import org.example.shelf.repository.AuthorRepository;
import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Api(description = "The author services", name = "Author services", group = DocumentationConstants.GROUP_LIBRARY)
@ApiAuthToken(roles = {"*"}, testTokens = "abc", scheme = "Bearer")
@RestController
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @ApiOperation(id = DocumentationConstants.AUTHOR_FIND_ALL, description = "Returns the list of all authors")
    @GetMapping
    @ApiResponseBodyType
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @ApiOperation(id = DocumentationConstants.AUTHOR_FIND_ONE, description = "Gets the author with the given ID")
    @ApiAuthToken
    @GetMapping("/{id}")
    @ApiResponseBodyType
    public Author findOne(@ApiPathParam(name = "id") @PathVariable Long id) {
        return authorRepository.findOne(id);
    }

    @ApiOperation(id = DocumentationConstants.AUTHOR_SAVE, description = "Creates a new author with the given data")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponseBodyType
    public ResponseEntity<Void> save(@ApiRequestBodyType @RequestBody Author author,
            UriComponentsBuilder uriComponentsBuilder,
            @RequestHeader(name = "some-header", defaultValue = "aDefaultValue") String someHeader) {
        authorRepository.save(author);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/authors/{id}").buildAndExpand(author.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @ApiOperation(id = DocumentationConstants.AUTHOR_DELETE, description = "Deletes the author with the given ID")
    @DeleteMapping(value = "/{id}", headers = "example=value")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@ApiPathParam(name = "id") @PathVariable Long id) {
        Author author = authorRepository.findOne(id);
        authorRepository.delete(author);
    }

}
