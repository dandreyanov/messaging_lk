package helpers;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
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
    public static void setData(String s, String userData) {
        $(s).setValue(userData);
    }

    @Step("Выбираем таймзону ${timeZone}")
    public static void setProfileTimeZone(Integer timeZone) {
        $("#profile_timeZone").selectOption(timeZone);
    }

    @Step("Проверяем, что в селекторе ${s} есть текст ${text}")
    public static void checkText(String s,String text)  {
        $(s).shouldHave(text(text));
    }

    @Step("Выбираем в селекторе ${s} параметр ${senderAddress}")
    public void setSender(String s, String senderAddress) {
        $(By.id(s)).selectOption(senderAddress);
    }

    @Step("Проверяем, что на странице отображается пример отправляемого сообщения {messageText}")
    public void checkResultTextMessage(String messageText) {
        $(By.cssSelector(".row > p")).shouldHave(text(messageText));
    }

    @Step("Проверяем, что на странице отображается шаблон {template}")
    public void checkResultTemplate(String template) {
        $(By.xpath("//span[contains(.,'" + template + "')]")).shouldHave(text(template));
    }

    @Step("Проверяем, что на странице отображается отправитель {sender}")
    public void checkResultSender(String sender) {
        $(By.xpath("//span[contains(.,'" + sender + "')]")).shouldHave(text(sender));
    }

    @Step("Проверяем, что на странице отображается количество контактов {contacts}")
    public void checkResultContacts(String contacts) {
        $(By.cssSelector(".row:nth-child(2) > span:nth-child(5)")).shouldHave(text(contacts));
    }
}
