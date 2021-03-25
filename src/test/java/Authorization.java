import config.CredentialsConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

public class Authorization extends config.BaseTest {
    @Test
    public static void successfulAuth() {
        final CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class, System.getProperties());
        Map<String, String> cookiesMap =
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("_username", config.username())
                        .formParam("_password", config.password())
                        .when()
                        .post("/my/auth")
                        .then()
                        .statusCode(302)
                        .log().body()
                        .extract().cookies();

        //open browser
        open("/bundles/app/images/logotype.png");
        getWebDriver().manage().addCookie(new Cookie("PHPSESSID", cookiesMap.get("PHPSESSID")));
        open("");

    }

}
