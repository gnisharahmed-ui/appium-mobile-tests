package com.sdet.screens;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Screen object for the Sauce Labs sample app Cart screen.
 */
public class CartScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-Cart Content")
    private WebElement cartContent;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Item']")
    private List<WebElement> cartItems;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    private WebElement checkoutButton;

    @AndroidFindBy(accessibility = "test-CONTINUE SHOPPING")
    private WebElement continueShoppingButton;

    public CartScreen() {
        waitForVisible(cartContent);
    }

    @Step("Get number of items in cart")
    public int getCartItemCount() {
        return cartItems.size();
    }

    @Step("Proceed to checkout")
    public CheckoutScreen proceedToCheckout() {
        tap(checkoutButton);
        return new CheckoutScreen();
    }

    @Step("Continue shopping")
    public ProductsScreen continueShopping() {
        tap(continueShoppingButton);
        return new ProductsScreen();
    }
}
