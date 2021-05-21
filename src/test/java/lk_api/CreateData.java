package lk_api;/*
 * CreateData 21.05.2021 Denis
 * Copyright (c) 2021 WINGS.
 */

import config.BaseTest;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CreateData extends BaseTest {
    //public static String token;

    @BeforeEach
    public void beforeFunction() {
        open("");

    }

    public void createSender(String sender) {
      //  open();
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenLk)
                .body("{\n" +
                        "  \"name\" : \"" + sender + "\",\n" +
                        "  \"transport\" : \"SMS\",\n" +
                        "  \"comment\" : \"Сreated from autotests\"\n" +
                        "}")
                .when()
                .post("/lk-api/sender")
                .then()
//                .statusCode(200)
                .log().body();
//                .body("status", is("Success"));

    }
}
