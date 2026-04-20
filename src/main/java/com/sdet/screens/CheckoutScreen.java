package com.sdet.screens;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * Screen object for the Sauce Labs sample app Checkout screen.
 */
public class CheckoutScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-First Name")
    private WebElement firstNameField;

    @AndroidFindBy(accessibility = "test-Last Name")
    private WebElement lastNameField;

    @AndroidFindBy(accessibility = "test-Zip/Postal Code")
    private WebElement postalCodeField;

    @AndroidFindBy(accessibility = "test-CONTINUE")
    private WebElement continueButton;

    @AndroidFindBy(accessibility = "test-FINISH")
    private WebElement finishButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='THANK YOU FOR YOU ORDER']")
    private WebElement confirmationText;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    private WebElement errorMessage;

    public CheckoutScreen() {
        waitForVisible(firstNameField);
    }

    @Step("Fill checkout form: {firstName} {lastName}, zip {postalCode}")
    public CheckoutScreen fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        enterText(firstNameField, firstName);
        enterText(lastNameField, lastName);
        enterText(postalCodeField, postalCode);
        return this;
    }

    @Step("Tap Continue")
    public CheckoutScreen tapContinue() {
        tap(continueButton);
        return this;
    }

    @Step("Tap Finish to place order")
    public CheckoutScreen tapFinish() {
        tap(finishButton);
        return this;
    }

    @Step("Get order confirmation text")
    public String getConfirmationText() {
        return getText(confirmationText);
    }

    public boolean isOrderConfirmed() {
        return isDisplayed(confirmationText);
    }

    @Step("Get validation error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
