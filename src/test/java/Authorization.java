import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Authorization {
    @Test
    public static void successfulAuth() {
        //open browser
        open("http://demo.wsoft.ru/my/login");
        $("#username").setValue("test@example.com");
        $("#password").setValue("qwerty123");
        $(By.tagName("button")).click();

    }

}
