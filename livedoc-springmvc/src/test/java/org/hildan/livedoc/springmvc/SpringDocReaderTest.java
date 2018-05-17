package org.hildan.livedoc.springmvc;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.hildan.livedoc.core.annotations.Api;
import org.hildan.livedoc.core.annotations.ApiOperation;
import org.hildan.livedoc.core.annotations.ApiPathParam;
import org.hildan.livedoc.core.annotations.ApiQueryParam;
import org.hildan.livedoc.core.annotations.ApiRequestBodyType;
import org.hildan.livedoc.core.annotations.ApiResponseBodyType;
import org.hildan.livedoc.core.annotations.ApiVersion;
import org.hildan.livedoc.core.annotations.auth.ApiAuthNone;
import org.hildan.livedoc.core.annotations.errors.ApiError;
import org.hildan.livedoc.core.annotations.errors.ApiErrors;
import org.hildan.livedoc.core.annotations.messages.ApiMessageChannel;
import org.hildan.livedoc.core.annotations.messages.StompCommand;
import org.hildan.livedoc.core.model.doc.ApiDoc;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.model.doc.ApiVerb;
import org.hildan.livedoc.core.model.doc.AuthType;
import org.hildan.livedoc.core.model.doc.ParamDoc;
import org.hildan.livedoc.core.model.doc.async.AsyncMessageDoc;
import org.hildan.livedoc.core.model.doc.auth.AuthDoc;
import org.hildan.livedoc.core.model.doc.headers.HeaderDoc;
import org.hildan.livedoc.core.model.doc.version.VersionDoc;
import org.hildan.livedoc.core.readers.javadoc.JavadocHelper;
import org.hildan.livedoc.springmvc.test.TestUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SpringDocReaderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
    private static class SpringController {

        @RequestMapping(path = "/string/{name}",
                headers = "header=test",
                params = "delete",
                method = RequestMethod.POST,
                consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        @ResponseBody
        public String handlerMethod(@PathVariable("test") String name,
                @RequestParam(value = "id", defaultValue = "0") Integer id, @RequestParam Long query,
                @RequestHeader(name = "header-param", defaultValue = "header-test") String header,
                @RequestBody String requestBody) {
            return "ok";
        }

        private void someUtilityMethod() {
            // should not be documented
        }
    }

    @Test
    public void testOperations_nothingOverridden() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringController.class);
        assertEquals("SpringController", apiDoc.getName());
        assertNotNull(apiDoc.getGroup());
        List<ApiOperationDoc> operations = apiDoc.getOperations();
        assertEquals(1, operations.size());
        assertTrue(apiDoc.getMessages().isEmpty());

        ApiOperationDoc opDoc = operations.get(0);
        assertEquals("handlerMethod", opDoc.getName());
        assertNull(opDoc.getSummary());
        assertNull(opDoc.getDescription());

        assertEquals(ApiVerb.POST, opDoc.getVerbs().get(0));
        assertSingletonList("/api/string/{name}", opDoc.getPaths());

        assertEquals(2, opDoc.getHeaders().size());
        List<HeaderDoc> headers = opDoc.getHeaders();
        HeaderDoc headerTwo = headers.get(0);
        assertEquals("header-param", headerTwo.getName());
        assertEquals("header-test", headerTwo.getDefaultValue());
        HeaderDoc headerTest = headers.get(1);
        assertEquals("header", headerTest.getName());
        assertEquals("test", headerTest.getValues().get(0));

        assertEquals("String", opDoc.getRequestBody().getType().getOneLineText());
        assertEquals("String", opDoc.getResponseBodyType().getOneLineText());
        assertSingletonList("application/json", opDoc.getProduces());
        assertSingletonList("application/json", opDoc.getConsumes());
        assertEquals("201 - Created", opDoc.getResponseStatusCode());

        List<ParamDoc> queryParams = opDoc.getQueryParameters();
        assertEquals(3, queryParams.size());

        ParamDoc param0 = queryParams.get(0);
        assertEquals("delete", param0.getName());
        assertEquals("true", param0.getRequired());
        assertNull(param0.getDefaultValue());
        assertEquals(0, param0.getAllowedValues().length);

        ParamDoc param1 = queryParams.get(1);
        assertEquals("id", param1.getName());
        assertEquals("true", param1.getRequired());
        assertEquals("Integer", param1.getType().getOneLineText());
        assertEquals("0", param1.getDefaultValue());

        ParamDoc param2 = queryParams.get(2);
        assertEquals("query", param2.getName());
        assertEquals("true", param2.getRequired());
        assertNull(param2.getDefaultValue());

        ParamDoc pathParam = opDoc.getPathParameters().get(0);
        assertEquals("test", pathParam.getName());

        assertNull(opDoc.getAuth());
        assertNull(opDoc.getSupportedVersions());
        assertNull(opDoc.getStage());
        assertTrue(opDoc.getApiErrors().isEmpty());
    }

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping(name = "/api") // should not affect messages
    @MessageMapping("/ws")
    private static class SpringControllerWithMessages {

        private static class MsgBody {
            public String title;

            public String content;
        }

        @MessageMapping("/send/param/{name}")
        public void simpleSend(@DestinationVariable("name") String name, MsgBody implicitBody) {
            // nothing happens
        }

        @MessageMapping("/send/return")
        public String sendWithReturn() {
            return "sent back to user";
        }

        @MessageMapping("/send/triggers/something")
        @SendTo("/ws/notifications")
        public String sendWithOverriddenReturnDest() {
            return "this is a triggered notification";
        }

        @SubscribeMapping("/sub")
        public void simpleSubscribe(Principal principal) {
            // nothing happens
        }

        @SubscribeMapping("/sub/return")
        public String subscribeWithReturn(Principal principal) {
            return "on-subscribe msg";
        }

        @SubscribeMapping("/sub/trigger")
        @SendToUser("/some/special/queue")
        public String subscribeWithOverriddenReturnDest(Principal principal) {
            return "on-subscribe msg for user";
        }

        @SubscribeMapping("/notifications")
        public void subscribeOnExistingTrigger(Principal principal) {
            // nothing happens
        }

        private void someUtilityMethod() {
            // should not be documented
        }
    }

    @Test
    public void testMessages() {
        ApiDoc apiDoc = TestUtils.buildDoc(SpringControllerWithMessages.class);
        assertEquals("SpringControllerWithMessages", apiDoc.getName());
        assertNotNull(apiDoc.getGroup());
        assertTrue(apiDoc.getOperations().isEmpty());
        List<AsyncMessageDoc> messages = apiDoc.getMessages();
        assertEquals(8, messages.size());

        assertUserQueue(messages.get(0));
        assertNotifications(messages.get(1));
        assertSimpleSubscribe(messages.get(2));
        assertSubscribeWithReturn(messages.get(3));
        assertSubscribeWithOverriddenReturnDest(messages.get(4));
        assertSimpleSend(messages.get(5));
        assertSendWithReturn(messages.get(6));
        assertSendWithOverriddenReturnDest(messages.get(7));

        messages.forEach(doc -> {
            assertNull(doc.getSummary());
            assertNull(doc.getDescription());
            assertNull(doc.getAuth());
            assertNull(doc.getSupportedVersions());
            assertNull(doc.getStage());
        });
    }

    private static void assertUserQueue(AsyncMessageDoc doc) {
        assertNull(doc.getName());
        assertEquals(StompCommand.SUBSCRIBE, doc.getCommand());
        assertSingletonList("/user/some/special/queue", doc.getDestinations());
    }

    private static void assertNotifications(AsyncMessageDoc doc) {
        assertEquals("subscribeOnExistingTrigger", doc.getName());
        assertEquals(StompCommand.SUBSCRIBE, doc.getCommand());
        assertSingletonList("/ws/notifications", doc.getDestinations());
    }

    private static void assertSimpleSend(AsyncMessageDoc doc) {
        assertEquals("simpleSend", doc.getName());
        assertEquals(StompCommand.SEND, doc.getCommand());
        assertSingletonList("/ws/send/param/{name}", doc.getDestinations());
        assertEquals("MsgBody", doc.getPayloadType().getOneLineText());
    }

    private static void assertSendWithReturn(AsyncMessageDoc doc) {
        assertEquals("sendWithReturn", doc.getName());
        assertEquals(StompCommand.SEND, doc.getCommand());
        assertSingletonList("/ws/send/return", doc.getDestinations());
    }

    private static void assertSendWithOverriddenReturnDest(AsyncMessageDoc doc) {
        assertEquals("sendWithOverriddenReturnDest", doc.getName());
        assertEquals(StompCommand.SEND, doc.getCommand());
        assertSingletonList("/ws/send/triggers/something", doc.getDestinations());
    }

    private static void assertSimpleSubscribe(AsyncMessageDoc doc) {
        assertEquals("simpleSubscribe", doc.getName());
        assertEquals(StompCommand.SUBSCRIBE, doc.getCommand());
        assertSingletonList("/ws/sub", doc.getDestinations());
    }

    private static void assertSubscribeWithReturn(AsyncMessageDoc doc) {
        assertEquals("subscribeWithReturn", doc.getName());
        assertEquals(StompCommand.SUBSCRIBE, doc.getCommand());
        assertSingletonList("/ws/sub/return", doc.getDestinations());
    }

    private static void assertSubscribeWithOverriddenReturnDest(AsyncMessageDoc doc) {
        assertEquals("subscribeWithOverriddenReturnDest", doc.getName());
        assertEquals(StompCommand.SUBSCRIBE, doc.getCommand());
        assertSingletonList("/ws/sub/trigger", doc.getDestinations());
    }

    private static <T> void assertSingletonList(T expectedElement, List<T> actualList) {
        assertEquals(Collections.singletonList(expectedElement), actualList);
    }

    /**
     * Test controller.
     */
    @SuppressWarnings("unused")
    @Api(description = "A spring controller", name = "Spring controller")
    @RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiAuthNone
    @ApiVersion(since = "1.0")
    @ApiErrors({@ApiError(code = "100", description = "error-100")})
    private static class SpringControllerWithLivedocAnnotations {

        /**
         * Gets an integer.
         *
         * @param input
         *         a test input
         *
         * @return something
         */
        @GetMapping
        @ApiMessageChannel(destinations = "/sometopic",
                description = "Multiple sources can send string messages here",
                payloadType = String.class)
        @ResponseBody
        public Integer integer(@RequestParam String input) {
            return 0;
        }

        @ApiOperation(description = "Gets a string", path = "/overridden", verbs = ApiVerb.GET)
        @RequestMapping(value = "/string/{name}",
                headers = "header=test",
                params = "delete",
                method = RequestMethod.POST,
                consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        @ApiResponseBodyType
        @ResponseBody
        public String string(@ApiPathParam(name = "name") @PathVariable("test") String name,
                @ApiQueryParam @RequestParam("id") Integer id,
                @ApiQueryParam(name = "query", defaultValue = "test") @RequestParam("myquery") Long query,
                @RequestBody @ApiRequestBodyType String requestBody) {
            return "ok";
        }

        /**
         * Sends a message to /sometopic
         *
         * @param content
         *         the content of the message
         *
         * @return the message sent by the user
         */
        @MessageMapping("/send/msg")
        @SendTo("/sometopic")
        public String sendToTopic(String content) {
            return content;
        }

        @MessageMapping("/send/msg2")
        @SendTo("/sometopic")
        public String sendTwice(String content) {
            return content + "\n" + content;
        }
    }

    @Test
    public void testMergeApiDoc_withLivedocAnnotations() {
        if (!JavadocHelper.getJavadocDescription(SpringControllerWithLivedocAnnotations.class).isPresent()) {
            fail("Javadoc processor was not enabled");
        }
        ApiDoc apiDoc = TestUtils.buildDoc(SpringControllerWithLivedocAnnotations.class);
        assertEquals("A spring controller", apiDoc.getDescription());
        assertEquals("Spring controller", apiDoc.getName());

        List<ApiOperationDoc> operations = apiDoc.getOperations();
        assertEquals(2, operations.size());

        checkIntegerOperation(operations.get(0));
        checkStringOperation(operations.get(1));

        List<AsyncMessageDoc> messages = apiDoc.getMessages();
        assertEquals(3, messages.size());

        assertSubscribeTopic(messages.get(0));
        assertSendToTopic(messages.get(1));
        assertSendTwice(messages.get(2));
    }

    private static void checkIntegerOperation(ApiOperationDoc opDoc) {
        assertEquals("integer", opDoc.getName());
        assertEquals("Gets an integer.", opDoc.getDescription());

        assertAuthNone(opDoc.getAuth());
        assertVersion(opDoc.getSupportedVersions());
        assertFalse(opDoc.getApiErrors().isEmpty());

        assertNull(opDoc.getRequestBody());
        assertEquals("Integer", opDoc.getResponseBodyType().getOneLineText());

        assertEquals(1, opDoc.getVerbs().size());
        assertEquals(ApiVerb.GET, opDoc.getVerbs().get(0));

        List<ParamDoc> queryParameters = opDoc.getQueryParameters();
        assertEquals(1, queryParameters.size());

        ParamDoc paramDoc = queryParameters.get(0);
        assertEquals("input", paramDoc.getName());
        assertEquals("a test input", paramDoc.getDescription());
        assertEquals("true", paramDoc.getRequired());
        assertNull(paramDoc.getDefaultValue());
        assertEquals(0, paramDoc.getAllowedValues().length);
    }

    private static void checkStringOperation(ApiOperationDoc opDoc) {
        assertEquals("string", opDoc.getName());
        assertEquals("Gets a string", opDoc.getDescription());

        assertAuthNone(opDoc.getAuth());
        assertVersion(opDoc.getSupportedVersions());
        assertFalse(opDoc.getApiErrors().isEmpty());

        assertEquals("String", opDoc.getRequestBody().getType().getOneLineText());
        assertEquals("String", opDoc.getResponseBodyType().getOneLineText());

        // livedoc annotations override Spring config
        assertEquals("/overridden", opDoc.getPaths().get(0));
        assertEquals(1, opDoc.getVerbs().size());
        assertEquals(ApiVerb.GET, opDoc.getVerbs().get(0));

        assertEquals("application/json", opDoc.getProduces().get(0));
        assertEquals("application/json", opDoc.getConsumes().get(0));
        assertEquals("201 - Created", opDoc.getResponseStatusCode());

        List<HeaderDoc> headers = opDoc.getHeaders();
        HeaderDoc header = headers.get(0);
        assertEquals("header", header.getName());
        assertEquals("test", header.getValues().get(0));

        List<ParamDoc> queryParameters = opDoc.getQueryParameters();
        assertEquals(3, queryParameters.size());

        ParamDoc paramDoc = queryParameters.get(0);
        assertEquals("delete", paramDoc.getName());
        assertEquals("true", paramDoc.getRequired());
        assertNull(paramDoc.getDefaultValue());
        assertEquals(0, paramDoc.getAllowedValues().length);

        paramDoc = queryParameters.get(1);
        assertEquals("id", paramDoc.getName());
        assertEquals("true", paramDoc.getRequired());
        assertNull(paramDoc.getDefaultValue());

        paramDoc = queryParameters.get(2);
        assertEquals("query", paramDoc.getName());
        assertEquals("true", paramDoc.getRequired());
        assertEquals("test", paramDoc.getDefaultValue());

        paramDoc = opDoc.getPathParameters().get(0);
        assertEquals("name", paramDoc.getName());
    }

    private static void assertSendToTopic(AsyncMessageDoc doc) {
        assertEquals("sendToTopic", doc.getName());
        assertEquals(StompCommand.SEND, doc.getCommand());
        assertSingletonList("/send/msg", doc.getDestinations());
        assertEquals("String", doc.getPayloadType().getOneLineText());
    }

    private static void assertSendTwice(AsyncMessageDoc doc) {
        assertEquals("sendTwice", doc.getName());
        assertEquals(StompCommand.SEND, doc.getCommand());
        assertSingletonList("/send/msg2", doc.getDestinations());
    }

    private static void assertSubscribeTopic(AsyncMessageDoc doc) {
        assertNull(doc.getName());
        assertEquals(StompCommand.SUBSCRIBE, doc.getCommand());
        assertSingletonList("/sometopic", doc.getDestinations());
    }

    private static void assertAuthNone(AuthDoc authDoc) {
        assertEquals(AuthType.NONE, authDoc.getType());
    }

    private static void assertVersion(VersionDoc supportedVersions) {
        assertEquals("1.0", supportedVersions.getSince());
        assertNull(supportedVersions.getUntil());
    }
}
