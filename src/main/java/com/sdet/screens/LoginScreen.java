package com.sdet.screens;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

/**
 * Screen object for the Sauce Labs sample app Login screen.
 * App: https://github.com/saucelabs/sample-app-mobile
 */
public class LoginScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-Username")
    private WebElement usernameField;

    @AndroidFindBy(accessibility = "test-Password")
    private WebElement passwordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    private WebElement errorMessage;

    @Step("Login with username='{username}'")
    public ProductsScreen loginAs(String username, String password) {
        enterText(usernameField, username);
        enterText(passwordField, password);
        tap(loginButton);
        return new ProductsScreen();
    }

    @Step("Attempt login with invalid credentials")
    public LoginScreen loginWithInvalidCredentials(String username, String password) {
        enterText(usernameField, username);
        enterText(passwordField, password);
        tap(loginButton);
        return this;
    }

    @Step("Get error message")
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}
