package lk_api;

import config.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static templates.ReportTemplate.filters;

public class ProfileDataTest extends BaseTest {

    public static String token;

    @BeforeAll
    static void beforeFunction() {
        token = given()
                .filter(filters().customTemplates())
                .contentType("application/json;charset=UTF-8")
                .body("{\n" +
                        "  \"username\" : \"test@example.com\",\n" +
                        "   \"password\" : \"qwerty123\"\n" +
                        "}")
                .when()
                .post("/lk-api/user/auth")
                .then()
                .statusCode(200)
                .log().body()
                .extract().path("token");
    }

    @Test
    public void successAuth() {
        given()
                .filter(filters().customTemplates())
                .contentType("application/json;charset=UTF-8")
                .body("{\n" +
                        "  \"username\" : \"test@example.com\",\n" +
                        "   \"password\" : \"qwerty123\"\n" +
                        "}")
                .when()
                .post("/lk-api/user/auth")
                .then()
                .statusCode(200)
                .log().body()
                .extract().path("token");
    }

    @Test
    void getProfileData() {
        given()
                .filter(filters().customTemplates())
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", token)
                .when()
                .get("/lk-api/user/info/profile")
                .then()
                .statusCode(200)
                .log().body()
                .body("email", is("test@example.com"))
                .body("msisdn", is(notNullValue()))
                .extract().response();
    }

    @Test
    void putProfileData() {
        given()
                .filter(filters().customTemplates())
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", token)
                .body("{\n" +
                        "  \"msisdn\" : \"79001234567\",\n" +
                        "  \"name\" : \"test\",\n" +
                        "  \"company\" : \"com1\",\n" +
                        "  \"timeZone\" : \"UTC+2\"\n" +
                        "}")
                .when()
                .put("/lk-api/user/info/profile")
                .then()
                .statusCode(200)
                .log().body()
                .body("status", is("Success"))
                .extract().response();
    }

}
