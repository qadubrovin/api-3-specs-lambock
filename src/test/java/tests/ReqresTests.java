package tests;

import lombock.LombokUserData;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;


public class ReqresTests {


    @Test
    void updateUserWithSpec() {
        String response =
                given()
                        .spec(requestToCreate)
                        .when()
                        .post("/users")
                        .then()
                        .statusCode(201)
                        .extract().path("id");

        given()
                .spec(requestToUpdate)
                .when()
                .put("/users" + response)
                .then()
                .spec(responseToUpdate);
    }

    @Test
    void checkUserNameByLombock() {

        LombokUserData data = given()
                .spec(request)
                .when()
                .get("/users/5")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(LombokUserData.class);

        assertEquals("Charles", data.getUser().getFirstName());
    }

    @Test
    void checkUserNameByGroovy() {

        given()
                .spec(request)
                .when()
                .get("/users/")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("data.findAll{it.first_name}.first_name.flatten().",
                        hasItem("Charles"));

    }

}
