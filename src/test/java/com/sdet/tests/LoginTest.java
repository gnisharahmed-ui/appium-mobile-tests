package com.sdet.tests;

import com.sdet.screens.LoginScreen;
import com.sdet.screens.ProductsScreen;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Login screen tests for Sauce Labs mobile app.
 */
@Epic("Authentication")
@Feature("Mobile Login")
public class LoginTest extends BaseTest {

    private static final String VALID_PASSWORD = "secret_sauce";

    @Test(description = "Valid login should navigate to Products screen")
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    public void testValidLoginNavigatesToProducts() {
        ProductsScreen products = new LoginScreen()
                .loginAs("standard_user", VALID_PASSWORD);

        Assert.assertEquals(products.getHeaderText(), "PRODUCTS",
                "Header should say PRODUCTS after successful login");
        Assert.assertTrue(products.getProductCount() > 0,
                "Products screen should show at least one product");
    }

    @Test(description = "Invalid credentials should show error message")
    @Story("Invalid Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidLoginShowsError() {
        LoginScreen loginScreen = new LoginScreen()
                .loginWithInvalidCredentials("standard_user", "wrong_pass");

        Assert.assertTrue(loginScreen.isErrorDisplayed(),
                "Error message should be visible");
        Assert.assertTrue(loginScreen.getErrorMessage()
                .contains("Username and password do not match"),
                "Error text should match expected message");
    }

    @Test(description = "Locked-out user gets specific error message")
    @Story("Locked Out User")
    @Severity(SeverityLevel.NORMAL)
    public void testLockedOutUserShowsError() {
        LoginScreen loginScreen = new LoginScreen()
                .loginWithInvalidCredentials("locked_out_user", VALID_PASSWORD);

        Assert.assertTrue(loginScreen.getErrorMessage().contains("locked out"),
                "Locked-out user should see appropriate error");
    }

    @Test(description = "Data-driven login with multiple valid users",
          dataProvider = "validUsers")
    @Story("Data Driven Login")
    @Severity(SeverityLevel.NORMAL)
    public void testMultipleValidUsers(String username) {
        ProductsScreen products = new LoginScreen()
                .loginAs(username, VALID_PASSWORD);

        Assert.assertTrue(products.getProductCount() > 0,
                "User '" + username + "' should see products after login");
    }

    @DataProvider(name = "validUsers")
    public Object[][] provideValidUsers() {
        return new Object[][] {
            {"standard_user"},
            {"performance_glitch_user"},
        };
    }
}
