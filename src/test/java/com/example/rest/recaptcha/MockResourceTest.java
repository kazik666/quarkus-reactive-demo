package com.example.rest.recaptcha;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class MockResourceTest {

    @Test
    void testSiteVerifyEndpoint() {
        given().contentType(ContentType.URLENC)
                .formParam("response", "97738bde-3393-4b48-aefe-5233c0aeffd0")
                .formParam("secret", "0000000000000000000000000000000000000000")
                .when().post("/recaptcha/api/siteverify")
                .then()
                .statusCode(200).contentType(ContentType.JSON)
                .body("success", equalTo(true))
                .body("score", equalTo((float) 1.0))
                .body("action", equalTo("ACTION"))
                .body("hostname", equalTo("localhost"))
                .body("challenge_ts", notNullValue());
    }
}
