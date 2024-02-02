package com.dgsp.demo;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

public class TestResource implements QuarkusTestResourceLifecycleManager {
    @Override
    public Map<String, String> start() {
        LocalstackSetup.startLocalStack();
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        LocalstackSetup.stopLocalStack();
    }
}
