package com.dgsp.demo.dao;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;


@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class UserData {
    private String userId;
    private String firstname;
    private String lastName;
    private Integer age;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "user_id")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute(value = "first_name")
    public String getFirstname() {
        return firstname;
    }

    @DynamoDbAttribute(value = "last_name")
    public String getLastName() {
        return lastName;
    }

    @DynamoDbAttribute(value = "birthday")
    public Integer getAge() {
        return age;
    }
}
