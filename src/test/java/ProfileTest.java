import com.github.javafaker.Faker;
import config.Authorization;
import config.BaseTest;
import helpers.PageHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;

public class ProfileTest extends BaseTest {

    final PageHelper steps = new PageHelper();

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
        steps.setProfileData(PROFILE_NAME, fullName);
        steps.setProfileData(PROFILE_COMPANY, companyName);
        steps.setProfileData(PROFILE_PHONE, phoneNumber);
        steps.setProfileTimeZone(timeZone);
        steps.clickSave();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Личные данные обновлены"));
    }


    @Test
    @Tag("UI")
    @DisplayName("Save form without fullname")
    public void saveWithoutFullname() {
        steps.setProfileData(PROFILE_NAME, "");
        steps.clickSave();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Ошибка обновления личных данных"));
        $(DIV_ERRORS).shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Save form without phone")
    public void saveWithoutPhone() {
        steps.setProfileData(PROFILE_PHONE, "");
        steps.clickSave();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Ошибка обновления личных данных"));
        $(DIV_ERRORS).shouldHave(text("Значение не должно быть пустым"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Save form without password confirmation")
    public void saveWithoutPassConfirm() {
        steps.setProfileData(USER_PASSWORD_PASSWORD, password);
        steps.setProfileData(USER_PASSWORD_NEW_PASSWORD_AGAIN, "");
        steps.clickChange();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Ошибка обновления пароля"));
    }

    @Test
    @Tag("UI")
    @DisplayName("Save form with another password confirmation")
    public void saveWithAnotherPassConfirm() {
        steps.setProfileData(USER_PASSWORD_PASSWORD, password);
        steps.setProfileData(USER_PASSWORD_NEW_PASSWORD_AGAIN, newPassword);
        steps.clickChange();
        $(DIV_FLASH_MESSAGE_MESSAGE).shouldHave(text("Пароли не совпадают, повторите ввод"));
    }

}
