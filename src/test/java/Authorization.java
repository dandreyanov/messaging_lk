import config.CredentialsConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class Authorization {
    @Test
    public static void successfulAuth() {
        final CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class, System.getProperties());
        //open browser
        open("http://demo.wsoft.ru/my/login");
        $("#username").setValue(config.username());
        $("#password").setValue(config.password());
        $(By.tagName("button")).click();

    }

}
