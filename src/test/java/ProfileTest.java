import com.github.javafaker.Faker;
import config.BaseTest;
import helpers.PageHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static helpers.PageHelper.*;

public class ProfileTest extends BaseTest {
    Faker faker = new Faker();

    String fullName = faker.name().fullName(),
            companyName = faker.company().name(),
            phoneNumber = faker.phoneNumber().subscriberNumber(11),
            password = faker.internet().password(1,20),
            newPassword = faker.internet().password(1,20);
    Integer index = faker.number().numberBetween(1, 24);

    @Test
    @DisplayName("Successful save form")
    public void openPersonalDataPage() {
        Authorization.successfulAuth();
        openPersonalData();
        //set random personal data
        $("#profile_name").setValue(fullName);
        $("#profile_company").setValue(companyName);
        $("#profile_phone").setValue(phoneNumber);
        $("#profile_timeZone").selectOption(index);
        clickSave();
        $x("//div[contains(text(),'Личные данные обновлены')]").shouldHave(visible);
    }

    @Test
    @DisplayName("Save form without fullname")
    public void saveWithoutFullname() {
        Authorization.successfulAuth();
        openPersonalData();
        $("#profile_name").setValue("");
        clickSave();
        $x("//div[@class='flashMessage-message']").shouldHave(text("Ошибка обновления личных данных"));
        $x("//div[@class='errors']//ul").shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @DisplayName("Save form without phone")
    public void saveWithoutPhone() {
        Authorization.successfulAuth();
        openPersonalData();
        $("#profile_phone").setValue("");
        clickSave();
        $x("//div[@class='flashMessage-message']").shouldHave(text("Ошибка обновления личных данных"));
        $x("//div[@class='errors']//ul").shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @DisplayName("Save form without password confirmation")
    public void saveWithoutPassConfirm() {
        Authorization.successfulAuth();
        openPersonalData();
        $("#user_password_password").setValue(password);
        $("#user_password_newPasswordAgain").setValue("");
        clickChange();
        $x("//div[@class='flashMessage-message']").shouldHave(text("Ошибка обновления пароля"));
    }

    @Test
    @DisplayName("Save form with another password confirmation")
    public void saveWithAnotherPassConfirm() {
        Authorization.successfulAuth();
        openPersonalData();
        $("#user_password_password").setValue(password);
        $("#user_password_newPasswordAgain").setValue(newPassword);
        clickChange();
        $x("//div[@class='flashMessage-message']").shouldHave(text("Отсутствует обязательное поле/Неверный формат запроса"));
    }

}
