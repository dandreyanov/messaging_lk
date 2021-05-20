import com.github.javafaker.Faker;
import config.Authorization;
import config.BaseTest;
import helpers.PageHelper;
import helpers.SmsHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class SmsDistribution extends BaseTest {
    Faker faker = new Faker();

    final SmsHelper stepsSMS = new SmsHelper();
    final PageHelper stepsUser = new PageHelper();

    public static final String SMS_SENDER = "sms_sender";
    public static final String SENDER_ADDRESS = "senderAddress";
    public static final String TEXT_SMS = "#sms_message";
    public static final String SMS_RECIPIENTS = "#sms_recipients";

    String TEXT = faker.lorem().characters(),
    RECIPIENT = faker.phoneNumber().subscriberNumber();


    @BeforeEach
    public void beforeFunction() {
        Authorization.authWithAPI();
        open("/my/distribution/sms/new");
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
