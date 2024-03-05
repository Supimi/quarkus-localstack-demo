package com.dgsp.demo;

import com.dgsp.demo.dao.UserData;
import com.dgsp.demo.dto.User;
import com.dgsp.demo.dto.UserResponse;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestProfile(ComponentTestProfile.class)
@QuarkusTestResource(value = TestResource.class, restrictToAnnotatedClass = true)
@QuarkusTest
class UserResourceTest {

    @Inject
    DynamoDbClient dynamoDbClient;

    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    DynamoDbTable<UserData> userDataTable;
    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setup() {
        setupDB();
        seedData();
    }


    @Test
    void getUsersTest() {
        UserResponse response = given()
                .contentType("application/json")
                .accept("application/json")
                .when().get("/user")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserResponse.class);

        assertTrue(response.success());
        assertEquals(1, response.body().size());
    }

    @Test
    void registerUserTest() throws JsonProcessingException {
        User user = User.builder()
                .userId("Jone")
                .lastName("Doe")
                .age(56)
                .build();
        UserResponse response = given()
                .contentType("application/json")
                .accept("application/json")
                .body(objectMapper.writeValueAsBytes(user))
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserResponse.class);

        assertTrue(response.success());
        assertFalse(response.body().isEmpty());
        assertNotNull(response.body().get(0).userId());
        UserData saved = userDataTable.getItem(Key.builder()
                .partitionValue(response.body().get(0).userId())
                .build());

        //assert with saved values
        assertEquals(user.firstname(),saved.getFirstname());
        assertEquals(user.lastName(),saved.getLastName());
        assertEquals(user.age(),saved.getAge());
    }

    private void setupDB() {
        if (dynamoDbEnhancedClient == null) {
            dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }
        userDataTable = dynamoDbEnhancedClient.table("user-data",
                TableSchema.fromBean(UserData.class));
        if (isExist(userDataTable)) {
            userDataTable.deleteTable();
        }
        userDataTable.createTable();
    }

    private boolean isExist(DynamoDbTable<UserData> userDataTable) {
        try {
            userDataTable.describeTable();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void seedData() {
        UserData userData = UserData.builder()
                .userId("test-user-id")
                .firstname("jack")
                .lastName("Nicola")
                .age(15)
                .build();
        userDataTable.putItem(userData);

    }


}