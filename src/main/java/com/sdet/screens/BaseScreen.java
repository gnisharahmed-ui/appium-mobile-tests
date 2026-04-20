package com.sdet.screens;

import com.sdet.utils.DriverManager;
import com.sdet.utils.GestureUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base screen for all mobile screen objects.
 * Uses Appium's Screen Factory pattern with @AndroidFindBy annotations.
 */
public abstract class BaseScreen {

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;
    private static final Logger log = LoggerFactory.getLogger(BaseScreen.class);
    private static final int DEFAULT_TIMEOUT = 15;

    protected BaseScreen() {
        this.driver = DriverManager.getDriver();
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(DEFAULT_TIMEOUT)), this);
    }

    @Step("Tap element")
    protected void tap(WebElement element) {
        log.debug("Tapping: {}", element);
        waitForClickable(element).click();
    }

    @Step("Enter text: {text}")
    protected void enterText(WebElement element, String text) {
        log.debug("Entering text: {}", text);
        WebElement el = waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    protected String getText(WebElement element) {
        return waitForVisible(element).getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void swipeUp() {
        GestureUtils.swipe(driver, GestureUtils.SwipeDirection.UP);
    }

    protected void swipeDown() {
        GestureUtils.swipe(driver, GestureUtils.SwipeDirection.DOWN);
    }

    protected void longPress(WebElement element) {
        GestureUtils.longPress(driver, element);
    }

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForText(WebElement element, String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
}
