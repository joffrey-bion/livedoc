package org.hildan.livedoc.springmvc.issues.issue174;

import java.util.HashMap;
import java.util.Map;

class TestResponse<T> {

    private Map<String, T> data;

    TestResponse() {
        this.data = new HashMap<>();
    }
}
