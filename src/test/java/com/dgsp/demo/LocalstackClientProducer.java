package com.dgsp.demo;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class LocalstackClientProducer {

    @Produces
    @IfBuildProfile("ComponentTestProfile")
    @ApplicationScoped
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .httpClient(UrlConnectionHttpClient.builder().build())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("accessKey","secretKey")
                ))
                .endpointOverride(LocalstackSetup.getURI())
                .build();
    }


}
