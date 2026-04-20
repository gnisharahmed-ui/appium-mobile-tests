package com.sdet.screens;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Screen object for the Sauce Labs sample app Products screen.
 */
public class ProductsScreen extends BaseScreen {

    @AndroidFindBy(accessibility = "test-PRODUCTS")
    private WebElement productsHeader;

    @AndroidFindBy(accessibility = "test-Item title")
    private List<WebElement> productTitles;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-ADD TO CART']")
    private List<WebElement> addToCartButtons;

    @AndroidFindBy(accessibility = "test-Cart")
    private WebElement cartIcon;

    @AndroidFindBy(accessibility = "test-Cart Badge")
    private WebElement cartBadge;

    @AndroidFindBy(accessibility = "test-Modal Selector Button")
    private WebElement sortButton;

    public ProductsScreen() {
        waitForVisible(productsHeader);
    }

    @Step("Get products screen header text")
    public String getHeaderText() {
        return getText(productsHeader);
    }

    @Step("Get count of visible products")
    public int getProductCount() {
        return productTitles.size();
    }

    @Step("Get all product names")
    public List<String> getProductNames() {
        return productTitles.stream()
                .map(this::getText)
                .collect(Collectors.toList());
    }

    @Step("Add first product to cart")
    public ProductsScreen addFirstProductToCart() {
        if (!addToCartButtons.isEmpty()) {
            tap(addToCartButtons.get(0));
        }
        return this;
    }

    @Step("Get cart badge count")
    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) return 0;
        return Integer.parseInt(getText(cartBadge));
    }

    @Step("Navigate to cart")
    public CartScreen goToCart() {
        tap(cartIcon);
        return new CartScreen();
    }

    @Step("Scroll down to see more products")
    public ProductsScreen scrollDown() {
        swipeUp();
        return this;
    }

    @Step("Open sort options")
    public ProductsScreen tapSortButton() {
        tap(sortButton);
        return this;
    }
}
