import com.github.javafaker.Faker;
import config.BaseTest;
import helpers.PageHelper;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfileTest extends BaseTest {
    Faker faker = new Faker();

    String fullName = faker.name().fullName(),
            companyName = faker.company().name(),
            phoneNumber = faker.phoneNumber().subscriberNumber(11);
    Integer index = faker.number().numberBetween(1, 24);

    @Test
    public void saveWithoutFullname() {
        Authorization.successfulAuth();
        PageHelper.openPersonalData();
        $("#profile_name").setValue("");
        $x("//button[contains(text(),'Сохранить')]").click();
        $x("//div[@class='flashMessage-message']").shouldHave(text("Ошибка обновления личных данных"));
        $x("//div[@class='errors']//ul").shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    public void openPersonalDataPage() {
        Authorization.successfulAuth();
        PageHelper.openPersonalData();
        //set random personal data
        $("#profile_name").setValue(fullName);
        $("#profile_company").setValue(companyName);
        $("#profile_phone").setValue(phoneNumber);
        $("#profile_timeZone").selectOption(index);

        $x("//button[contains(text(),'Сохранить')]").click();

        $x("//div[contains(text(),'Личные данные обновлены')]").shouldHave(visible);
    }

}
