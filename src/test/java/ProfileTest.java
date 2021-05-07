import com.github.javafaker.Faker;
import config.Authorization;
import config.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;

import static helpers.PageHelper.*;

public class ProfileTest extends BaseTest {
    public static final String PROFILE_NAME = "#profile_name";
    public static final String PROFILE_COMPANY = "#profile_company";
    public static final String PROFILE_PHONE = "#profile_phone";
    public static final String DIV_FLASH_MESSAGE_MESSAGE = "div.flashMessage-message";
    public static final String DIV_ERRORS = "div.errors";
    public static final String USER_PASSWORD_PASSWORD = "#user_password_password";
    public static final String USER_PASSWORD_NEW_PASSWORD_AGAIN = "#user_password_newPasswordAgain";

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
    @Tag("UI")
    @DisplayName("Successful save form")
    public void openPersonalDataPage() {
        setProfileData(PROFILE_NAME, fullName);
        setProfileData(PROFILE_COMPANY, companyName);
        setProfileData(PROFILE_PHONE, phoneNumber);
        setProfileTimeZone(timeZone);
        clickSave();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Личные данные обновлены"));
    }


    @Test
    @Tag("UI")
    @DisplayName("Save form without fullname")
    public void saveWithoutFullname() {
        setProfileData(PROFILE_NAME, "");
        clickSave();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Ошибка обновления личных данных"));
        $(DIV_ERRORS).shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Save form without phone")
    public void saveWithoutPhone() {
        setProfileData(PROFILE_PHONE, "");
        clickSave();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Ошибка обновления личных данных"));
        $(DIV_ERRORS).shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Save form without password confirmation")
    public void saveWithoutPassConfirm() {
        setProfileData(USER_PASSWORD_PASSWORD, password);
        setProfileData(USER_PASSWORD_NEW_PASSWORD_AGAIN, "");
        clickChange();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Ошибка обновления пароля"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Save form with another password confirmation")
    public void saveWithAnotherPassConfirm() {
        setProfileData(USER_PASSWORD_PASSWORD, password);
        setProfileData(USER_PASSWORD_NEW_PASSWORD_AGAIN, newPassword);
        clickChange();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Пароли не совпадают, повторите ввод"));
    }

}
