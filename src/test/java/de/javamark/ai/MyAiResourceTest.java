package de.javamark.ai;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class MyAiResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello/ai")
                .then()
                .statusCode(200);
    }

}