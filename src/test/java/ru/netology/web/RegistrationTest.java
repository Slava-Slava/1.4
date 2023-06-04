package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class RegistrationTest {
    private String datePlus(int dayToAdd, String pattern) {
        return LocalDate.now().plusDays(dayToAdd).format(DateTimeFormatter.ofPattern(pattern));
    }

    String date = datePlus(3, "dd.MM.yyyy");

    @Test
    void testEnteringValidValues() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Успешно!")).shouldBe(visible);
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void testCityWithoutEnteringValue() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void testEnterCityInLatin() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Moscow");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(visible);
    }

    @Test
    void testEnterCityNotFromAdministrativeCentersConstituentEntitiesRF() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Гусь-Хрустальный");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(visible);
    }

    @Test
    void testDateNotEarlierThanFourDaysFromCurrentDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(datePlus(4, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $(withText("Успешно!")).shouldBe(visible);
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + datePlus(4,"dd.MM.yyyy")), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void testDateNotEarlierThanTwoDaysFromCurrentDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(datePlus(2, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(visible);
    }

    @Test
    void testEnterNameInLatin() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Maria Sokolova");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible);
    }

    @Test
    void testNameWithoutEnteringValue() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void testEnteringNameWithSpecialCharacters() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("!@#$% ^&*()_");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible);
    }
    @Test
    void testPhoneWithoutEnteringValue() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible);
    }
    @Test
    void testPhoneFirstTwoDigits() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+7");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible);
    }
    @Test
    void testPhoneWithoutSpecialCharacter() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("79990001122");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible);
    }
    @Test
    void testInputValidValuesWithoutCheckbox() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Мария Соколова");
        $("[data-test-id='phone'] input").setValue("+79990001122");
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Я соглашаюсь с условиями обработки и использования моих персональных данных")).shouldBe(visible);
    }
}

