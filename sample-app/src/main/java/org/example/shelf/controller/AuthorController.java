package org.example.shelf.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.example.shelf.documentation.Documentation;
import org.example.shelf.model.Author;
import org.example.shelf.repository.AuthorRepository;
import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.auth.ApiAuthToken;
import org.hildan.livedoc.core.annotations.messages.ApiMessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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

@Api(description = "The author services", name = "Author services", group = Documentation.GROUP_LIBRARY)
@ApiAuthToken(roles = {"*"}, testTokens = "abc", scheme = "Bearer")
@RestController
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@MessageMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @ApiOperation(description = "Returns the list of all authors")
    @GetMapping
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @ApiOperation(description = "Gets the author with the given ID")
    @ApiAuthToken
    @GetMapping("/{id}")
    public Author findOne(@ApiPathParam(name = "id") @PathVariable Long id) {
        return authorRepository.findOne(id);
    }

    @ApiOperation(description = "Creates a new author with the given data")
    @ApiMessageChannel(destinations = "/created",
            payloadType = String.class,
            description = "messages triggered upon " + "creation of a new author")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@ApiRequestBodyType @RequestBody Author author,
            UriComponentsBuilder uriComponentsBuilder,
            @RequestHeader(name = "some-header", defaultValue = "aDefaultValue") String someHeader) {
        authorRepository.save(author);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponentsBuilder.path("/authors/{id}").buildAndExpand(author.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @ApiOperation(description = "Deletes the author with the given ID")
    @DeleteMapping(value = "/{id}", headers = "example=value")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@ApiPathParam(name = "id") @PathVariable Long id) {
        Author author = authorRepository.findOne(id);
        authorRepository.delete(author);
    }

    /**
     * Sends a {@link MessageToAuthor} to the given author.
     *
     * @param principal
     *         the connected user
     * @param message
     *         the message to send
     * @param authorId
     *         the ID of the author to send the message to
     *
     * @return a message for everyone to see this private conversation
     */
    @MessageMapping("/message/{authorId}")
    @SendTo("/messages")
    public TriggeredMessage messageAuthor(Principal principal, MessageToAuthor message,
            @DestinationVariable String authorId) {
        // do something with the message
        return new TriggeredMessage(message.content);
    }

    /**
     * Does something upon subscription to /messages/special.
     *
     * @param principal
     *         the connected user
     */
    @ApiMessageChannel(destinations = "/messages/special",
            payloadType = SpecialMessage.class,
            description = "these are special messages")
    @SubscribeMapping("/messages/special")
    public void watchSpecialMessages(Principal principal) {
        // possibly do something with the subscription of this principal
    }

    /**
     * Does something upon subscription to /messages.
     *
     * @param principal
     *         the connected user
     *
     * @return the current list of messages at subscription time
     */
    @SubscribeMapping("/messages")
    public List<String> triggerOnSubscribe(Principal principal) {
        // possibly do something with the subscription of this principal
        return Collections.emptyList();
    }

    private static class MessageToAuthor {
        public final String title;

        public final String content;

        private MessageToAuthor(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    private static class TriggeredMessage {
        public final String content;

        private TriggeredMessage(String content) {
            this.content = content;
        }
    }

    private static class SpecialMessage {
        public final String content;

        private SpecialMessage(String content) {
            this.content = content;
        }
    }
}
