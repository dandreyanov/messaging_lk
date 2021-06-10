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

    String TEXT = faker.lorem().characters(),
            CLIENT_NAME = faker.name().firstName(),
            SMS_PROVIDER_NAME = "SMS Provider " + faker.name().lastName(),
            RECIPIENT = "79" + faker.phoneNumber().subscriberNumber(9),
            SENDER_ADDRESS = "79" + faker.phoneNumber().subscriberNumber(9),
            PARTNER_EMAIL = faker.internet().emailAddress(),
            PARTNER_LK_NAME = faker.name().fullName(),
            PARTNER_PASSWORD = faker.internet().password(6, 50),
            PARTNER_ID,
            SERVICE_PROVIDER_ID;

    final CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class, System.getProperties());


    @BeforeEach
    public void beforeFunction() {
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

        //Генерируем тестовые данные
        PARTNER_ID = adminData.createClient(CLIENT_NAME);
        adminData.createSender(SENDER_ADDRESS, PARTNER_ID);
        SERVICE_PROVIDER_ID = adminData.createServiceProvides(SMS_PROVIDER_NAME);
        //Создаем настройки группы поставщика услуг
        adminData.createSMSTariffZoneGroupsProvider(SERVICE_PROVIDER_ID);
        System.out.println("Создаем зону для SERVICE_PROVIDER_ID " + SERVICE_PROVIDER_ID);
        adminData.createSMSTariffZoneProvider(SERVICE_PROVIDER_ID, CLIENT_NAME);
        System.out.println("Создаем зону для PARTNER_ID " + PARTNER_ID);
        adminData.createSMSTariffZoneGroupsPartner(PARTNER_ID);
        adminData.createSMSTariffZonePartner(PARTNER_ID, CLIENT_NAME);
        adminData.createPartnerUsers(PARTNER_EMAIL, CLIENT_NAME, PARTNER_LK_NAME, PARTNER_PASSWORD);
        Authorization.authWithAPI(PARTNER_EMAIL, PARTNER_PASSWORD);

        //Получаем токен для дальнейшей авторизации по LK API
        System.out.println("LK api auth result: \n");
        tokenLk = given()
                .filter(filters().customTemplates())
                .contentType("application/json;charset=UTF-8")
                .body("{\n" +
                        "  \"username\" : \"" + PARTNER_EMAIL + "\",\n" +
                        "   \"password\" : \"" + PARTNER_PASSWORD + "\"\n" +
                        "}")
                .when()
                .post("/lk-api/user/auth")
                .then()
                .statusCode(200)
                .log().body()
                .extract().path("token");

        Authorization.authWithAPI(PARTNER_EMAIL, PARTNER_PASSWORD);
        open("my/distribution/sms/new");
    }

    @Test
    @Tag("UI") @Tag("SMS")
    @DisplayName("Sms distribution with only required parameters")
    public void smsDistributionOnlyRequired() {
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
