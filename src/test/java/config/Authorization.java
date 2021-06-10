package config;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static templates.ReportTemplate.filters;

public class Authorization extends config.BaseTest {
    @Test
    public static void authWithAPI(String PARTNER_EMAIL, String PARTNER_PASSWORD) {
        Map<String, String> cookiesMap =
                given()
                        .filter(filters().customTemplates())
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("_username", PARTNER_EMAIL)
                        .formParam("_password", PARTNER_PASSWORD)
                        .when()
                        .post("/my/auth")
                        .then()
                        .statusCode(302)
                        .log().body()
                        .extract().cookies();

        open("bundles/app/images/logotype.png");
        getWebDriver().manage().addCookie(new Cookie("PHPSESSID", cookiesMap.get("PHPSESSID")));

    }

}
