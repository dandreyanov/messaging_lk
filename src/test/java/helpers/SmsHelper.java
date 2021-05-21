package helpers;/*
 * SmsHelper 14.05.2021 Denis
 * Copyright (c) 2021 WINGS.
 */
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SmsHelper {

    @Step("Нажимаем далее")
    public static void clickNext() {
        $("button.btn._green").click();
    }
}
