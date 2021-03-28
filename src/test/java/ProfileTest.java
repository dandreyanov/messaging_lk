import com.github.javafaker.Faker;
import config.Authorization;
import config.BaseTest;
import org.junit.jupiter.api.BeforeEach;
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
            password = faker.internet().password(5, 20),
            newPassword = faker.internet().password(5, 20);
    Integer timeZone = faker.number().numberBetween(1, 24);

    @BeforeEach
    public void beforeFunction() {
        Authorization.authWithAPI();
        open("/my/user/profile/profile");
    }

    @Test
    @DisplayName("Successful save form")
    public void openPersonalDataPage() {
        setProfileData("#profile_name", fullName);
        setProfileData("#profile_company", companyName);
        setProfileData("#profile_phone", phoneNumber);
        setProfileTimeZone(timeZone);
        clickSave();
        $("div.flashMessage-message").shouldHave(text("Личные данные обновлены"));
    }


    @Test
    @DisplayName("Save form without fullname")
    public void saveWithoutFullname() {
        setProfileData("#profile_name", "");
        clickSave();
        $("div.flashMessage-message").shouldHave(text("Ошибка обновления личных данных"));
        $("div.errors").shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @DisplayName("Save form without phone")
    public void saveWithoutPhone() {
        setProfileData("#profile_phone", "");
        clickSave();
        $("div.flashMessage-message").shouldHave(text("Ошибка обновления личных данных"));
        $("div.errors").shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @DisplayName("Save form without password confirmation")
    public void saveWithoutPassConfirm() {
        setProfileData("#user_password_password", password);
        setProfileData("#user_password_newPasswordAgain", "");
        clickChange();
        $("div.flashMessage-message").shouldHave(text("Ошибка обновления пароля"));
    }

    @Test
    @DisplayName("Save form with another password confirmation")
    public void saveWithAnotherPassConfirm() {
        setProfileData("#user_password_password", password);
        setProfileData("#user_password_newPasswordAgain", newPassword);
        clickChange();
        $("div.flashMessage-message").shouldHave(text("Пароли не совпадают, повторите ввод"));
    }

}
