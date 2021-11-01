package com.example.rest.dotpay;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class MockResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
                .queryParam("email", "jantestowy@test.com")
                .when().get("/s2/login/api/purchase_history/")
                .then()
                .statusCode(200).contentType(ContentType.JSON);
    }
}