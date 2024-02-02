package com.dgsp.demo.repository;

import com.dgsp.demo.dao.UserData;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserRepository {
    private final DynamoDbTable<UserData> table;


    public UserRepository(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.table = dynamoDbEnhancedClient.table("user-data", TableSchema.fromBean(UserData.class));
    }

    public List<UserData> getAll(){
        return this.table.scan().stream()
                .flatMap(userDataPage -> userDataPage.items().stream())
                .collect(Collectors.toList());
    }

    public void saveNonNull(UserData userData){
        UpdateItemEnhancedRequest<UserData> updateRequest = UpdateItemEnhancedRequest.builder(UserData.class)
                .item(userData)
                .ignoreNulls(true)
                .build();
        this.table.updateItem(updateRequest);
    }

    public Optional<UserData> getUser(String userId){
        return Optional.ofNullable(
                this.table.getItem(Key.builder().partitionValue(userId).build()));
    }
}
