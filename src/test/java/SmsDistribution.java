import admin_api.CreateAdminData;
import com.github.javafaker.Faker;
import config.Authorization;
import config.BaseTest;
import config.CredentialsConfig;
import helpers.PageHelper;
import helpers.SmsHelper;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static templates.ReportTemplate.filters;

public class SmsDistribution extends BaseTest {
    Faker faker = new Faker();

    final SmsHelper stepsSMS = new SmsHelper();
    final PageHelper stepsUser = new PageHelper();
    final CreateAdminData adminData = new CreateAdminData();

    public static final String SMS_SENDER = "sms_sender";
    public static final String TEXT_SMS = "#sms_message";
    public static final String SMS_RECIPIENTS = "#sms_recipients";

    String  TEXT = faker.lorem().characters(),
            CLIENT_NAME = faker.name().firstName(),
            RECIPIENT = "79" + faker.phoneNumber().subscriberNumber(9),
            SENDER_ADDRESS = "79" + faker.phoneNumber().subscriberNumber(9),
            PARTNER_ID;

    final CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class, System.getProperties());


    @BeforeEach
    public void beforeFunction() {
        Authorization.authWithAPI();

        //Получаем токен для дальнейшей авторизации по LK API
        System.out.println("LK api auth result: \n");
        tokenLk = given()
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

        //Получаем токен для дальнейшей авторизации по Admin API
        System.out.println("Admin api auth result: \n");
        tokenAdmin = given()
                .filter(filters().customTemplates())
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", config.adminauth())
                .when()
                .get("/acapi/auth")
                .then()
                .statusCode(200)
                .log().body()
                .extract().path("token");
        open("/my/distribution/sms/new");
    }

    @Test
    @Tag("UI") @Tag("SMS")
    @DisplayName("Sms distribution with only required parameters")
    public void smsDistributionOnlyRequired() {
        //Генерируем тестовые данные
        PARTNER_ID = adminData.createClient(CLIENT_NAME);
        adminData.createSender(SENDER_ADDRESS, PARTNER_ID);
        adminData.createServiceProvides();
//        createAdminData.createSMSTariffZoneGroupsProvider();
        adminData.createSMSTariffZoneGroupsPartner(PARTNER_ID);
        stepsSMS.clickNext();
        stepsUser.setSender(SMS_SENDER, SENDER_ADDRESS);
        stepsUser.setData(TEXT_SMS, TEXT);
        stepsSMS.clickNext();
        stepsUser.setData(SMS_RECIPIENTS, RECIPIENT);
        stepsSMS.clickNext();
        stepsSMS.clickNext();
        stepsUser.checkResultTextMessage(TEXT);
        stepsUser.checkResultTemplate("Без шаблона");
        stepsUser.checkResultSender(SENDER_ADDRESS);
        stepsUser.checkResultContacts("1");

    }

}
