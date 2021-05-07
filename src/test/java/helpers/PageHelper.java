package helpers;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PageHelper {

    @Step("Нажимаем сохранить")
    public static void clickSave() {
        $("button.btn._green").click();
    }

    @Step("Нажимаем изменить")
    public static void clickChange() {
        $("button.btn._blue").click();
    }

    @Step("Вставляем ${userData} в селектор ${s}")
    public static void setProfileData(String s, String userData) {
        $(s).setValue(userData);
    }

    @Step("Выбираем таймзону ${timeZone}")
    public static void setProfileTimeZone(Integer timeZone) {
        $("#profile_timeZone").selectOption(timeZone);
    }

}
