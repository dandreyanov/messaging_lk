package config;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Collections;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;
import static helpers.DriverHelper.getSessionId;

public class BaseTest {

    public static String tokenLk,
            tokenAdmin;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://192.168.128.215/";
        Configuration.baseUrl = "http://192.168.128.215/";
        final EnvironmentConfig config = ConfigFactory.create(EnvironmentConfig.class, System.getProperties());
        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        Configuration.browser = config.browser();
        Configuration.browserVersion = config.browserVersion();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
        Configuration.startMaximized = true;

        RestAssured.filters(Collections.singletonList(new AllureRestAssured()));

        if (System.getProperty("remote.browser.url") != null)
            Configuration.remote = config.webDriverUrl();


    }

    @AfterEach
    public void afterEach() {
        String sessionId = getSessionId();

        /*Убрать после настройки селеноида
//        attachScreenshot("Last screenshot");
//        attachPageSource();
//        attachAsText("Browser console logs", getConsoleLogs());
//        if (isVideoOn()) attachVideo(sessionId);*/

        closeWebDriver();

    }
}

