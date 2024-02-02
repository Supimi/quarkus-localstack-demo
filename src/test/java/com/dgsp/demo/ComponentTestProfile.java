package com.dgsp.demo;

import io.quarkus.test.junit.QuarkusTestProfile;

public class ComponentTestProfile implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "ComponentTestProfile";
    }
}
