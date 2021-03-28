package helpers;

import static com.codeborne.selenide.Selenide.$;

public class PageHelper {

    public static void clickSave() {
        $("button.btn._green").click();
    }

    public static void clickChange() {
        $("button.btn._blue").click();
    }

    public static void setProfileData(String s, String userData) {
        $(s).setValue(userData);
    }

    public static void setProfileTimeZone(Integer timeZone) {
        $("#profile_timeZone").selectOption(timeZone);
    }

}
