package com.dgsp.demo;

import com.dgsp.demo.dao.UserData;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@TestProfile(ComponentTestProfile.class)
@QuarkusTestResource(value = TestResource.class, restrictToAnnotatedClass = true)
@QuarkusTest
class UserResourceTest {

    @Inject
    DynamoDbClient dynamoDbClient;

    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    DynamoDbTable<UserData> userDataTable;


    @BeforeEach
    public void setup() {
        setupDB();
        seedData();
    }


    @Test
    void testGetUsers() {
        given()
                .contentType("application/json")
                .accept("application/json")
                .when().get("/user")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .log();
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