package org.hildan.livedoc.springmvc.readers.payload;

import java.lang.reflect.Method;
import java.security.Principal;

import org.hildan.livedoc.core.annotations.types.ApiType;
import org.hildan.livedoc.core.annotations.types.ApiTypeProperty;
import org.hildan.livedoc.core.model.doc.ApiOperationDoc;
import org.hildan.livedoc.core.scanners.types.references.DefaultTypeReferenceProvider;
import org.hildan.livedoc.springmvc.readers.request.RequestHandlerReader;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RequestMappingBodyReaderTest {

    @SuppressWarnings("unused")
    @Controller
    @RequestMapping
    public class SpringController {

        @RequestMapping("/body0")
        public void noArg() {
        }

        @RequestMapping("/body1")
        public void noBodyArg(Principal principal, @RequestParam String param) {
        }

        @RequestMapping("/body2")
        public void string(@RequestBody String string) {
        }

        @RequestMapping("/body3")
        public void pojo(@RequestBody Body body) {
        }
    }

    @SuppressWarnings("unused")
    @ApiType
    private class Body {
        @ApiTypeProperty
        private String name;

        @ApiTypeProperty
        private Integer age;
    }

    @Test
    public void testBodyOne() throws NoSuchMethodException {
        Class<?> ctrl = SpringController.class;
        Method noArg = ctrl.getDeclaredMethod("noArg");
        Method noBodyArg = ctrl.getDeclaredMethod("noBodyArg", Principal.class, String.class);
        Method string = ctrl.getDeclaredMethod("string", String.class);
        Method pojo = ctrl.getDeclaredMethod("pojo", Body.class);

        DefaultTypeReferenceProvider typeRefProv = new DefaultTypeReferenceProvider(c -> true);
        ApiOperationDoc noArgDoc = RequestHandlerReader.buildApiOperationDoc(noArg, ctrl, typeRefProv, c -> null);
        ApiOperationDoc noBodyDoc = RequestHandlerReader.buildApiOperationDoc(noBodyArg, ctrl, typeRefProv, c -> null);
        ApiOperationDoc stringDoc = RequestHandlerReader.buildApiOperationDoc(string, ctrl, typeRefProv, c -> null);
        ApiOperationDoc pojoDoc = RequestHandlerReader.buildApiOperationDoc(pojo, ctrl, typeRefProv, c -> null);

        assertNull(noArgDoc.getRequestBody());
        assertNull(noBodyDoc.getRequestBody());
        assertEquals("String", stringDoc.getRequestBody().getType().getOneLineText());
        assertEquals("Body", pojoDoc.getRequestBody().getType().getOneLineText());
    }

}
