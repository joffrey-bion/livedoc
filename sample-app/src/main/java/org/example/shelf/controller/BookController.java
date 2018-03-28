package org.example.shelf.controller;

import java.util.List;

import org.example.shelf.documentation.DocumentationConstants;
import org.example.shelf.model.Book;
import org.example.shelf.repository.BookRepository;
import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
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

/**
 * This is the controller that handles books.
 */
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "The books controller", name = "Books services", group = DocumentationConstants.GROUP_LIBRARY)
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Gets a book given the book ID.
     *
     * @param id
     *         the ID of the book to find
     *
     * @return the book with the given ID, ro null if none were found
     */
    @ApiOperation(id = DocumentationConstants.BOOK_FIND_ONE)
    @ApiResponseBodyType
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Book findOne(@ApiPathParam(name = "id") @PathVariable Long id) {
        return bookRepository.findOne(id);
    }

    /**
     * Gets all books.
     *
     * @return all the books
     */
    @ApiOperation(id = DocumentationConstants.BOOK_FIND_ALL)
    @RequestMapping(method = RequestMethod.GET)
    @ApiResponseBodyType
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Saves the given book.
     *
     * @param book
     *         the book to save
     * @param uriComponentsBuilder
     *         helper for redirection
     *
     * @return nothing (wrapped in a {@link ResponseEntity})
     */
    @ApiOperation(id = DocumentationConstants.BOOK_SAVE)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponseBodyType
    public ResponseEntity<Void> save(@ApiRequestBodyType @RequestBody Book book,
            UriComponentsBuilder uriComponentsBuilder) {
        bookRepository.save(book);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/books/{id}").buildAndExpand(book.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    /**
     * Deletes the book with the given ID.
     *
     * @param id
     *         the ID of the book to delete
     */
    @ApiOperation(id = DocumentationConstants.BOOK_DELETE)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@ApiPathParam(name = "id") @PathVariable Long id) {
        Book book = bookRepository.findOne(id);
        bookRepository.delete(book);
    }
}
