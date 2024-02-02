package com.dgsp.demo;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;

public class LocalstackSetup {
    public static final DockerImageName LOCALSTACK_DOCKER_IMG = DockerImageName.parse("localstack/localstack:3.1.0") ;

    private static final LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_DOCKER_IMG)
            .withServices(LocalStackContainer.Service.DYNAMODB)
            .withEnv("HOSTNAME_EXTERNAL","localhost")
            .withExposedPorts(4566);

    public static void startLocalStack(){
        localStackContainer.start();
    }

    public static void stopLocalStack(){
        localStackContainer.stop();
    }

    public static URI getURI(){
        return localStackContainer.getEndpoint();
    }

}
