package helpers;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PageHelper {
    public static void openPersonalData() {
        $x("//span[contains(text(),'Профиль')]").click();
        $x("//a[contains(text(),'Личные данные')]").click();
    }

    public static void clickSave() {
        $x("//button[contains(text(),'Сохранить')]").click();
    }

    public static void clickChange() {
        $x("//button[contains(text(),'Изменить')]").click();
    }

    public static void setProfileData(String s, String userData) {
        $(s).setValue(userData);
    }
}
