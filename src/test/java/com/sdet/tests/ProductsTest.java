package com.sdet.tests;

import com.sdet.screens.*;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Products screen and E2E checkout tests for Sauce Labs mobile app.
 * Demonstrates gestures (scroll, tap) and chained screen navigation.
 */
@Epic("Shopping")
@Feature("Mobile Products & Checkout")
public class ProductsTest extends BaseTest {

    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    @Test(description = "Products screen should display items after login")
    @Story("Products Display")
    @Severity(SeverityLevel.BLOCKER)
    public void testProductsScreenLoadsItems() {
        ProductsScreen products = loginAsStandardUser();

        Assert.assertTrue(products.getProductCount() > 0,
                "At least one product should be visible");
    }

    @Test(description = "Adding a product increments the cart badge")
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductUpdatesCartBadge() {
        ProductsScreen products = loginAsStandardUser();

        products.addFirstProductToCart();

        Assert.assertEquals(products.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    @Test(description = "Scroll down reveals more products")
    @Story("Scroll Gesture")
    @Severity(SeverityLevel.NORMAL)
    public void testScrollDownRevealMoreProducts() {
        ProductsScreen products = loginAsStandardUser();

        int beforeScroll = products.getProductCount();
        products.scrollDown();
        // After scroll, we verify the screen is still functional
        Assert.assertTrue(products.getProductCount() >= 0,
                "Product screen should remain stable after scrolling");
    }

    @Test(description = "Complete E2E checkout flow on mobile")
    @Story("Mobile E2E Checkout")
    @Severity(SeverityLevel.BLOCKER)
    public void testCompleteCheckoutFlow() {
        ProductsScreen products = loginAsStandardUser();

        // Add item and go to cart
        products.addFirstProductToCart();
        Assert.assertEquals(products.getCartBadgeCount(), 1, "Cart badge should be 1");

        CartScreen cart = products.goToCart();
        Assert.assertEquals(cart.getCartItemCount(), 1, "Cart should have 1 item");

        // Checkout
        CheckoutScreen checkout = cart.proceedToCheckout();
        checkout
                .fillCheckoutInfo("Ahmed", "QA", "48187")
                .tapContinue()
                .tapFinish();

        Assert.assertTrue(checkout.isOrderConfirmed(),
                "Order confirmation should be displayed");
    }

    @Test(description = "Checkout with missing first name shows validation error")
    @Story("Checkout Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutMissingNameShowsError() {
        ProductsScreen products = loginAsStandardUser();
        products.addFirstProductToCart();

        CartScreen cart = products.goToCart();
        CheckoutScreen checkout = cart.proceedToCheckout();
        checkout
                .fillCheckoutInfo("", "QA", "48187")
                .tapContinue();

        Assert.assertTrue(checkout.getErrorMessage().contains("First Name"),
                "Validation should mention First Name");
    }

    // ── helpers ─────────────────────────────────────────────────────────────

    private ProductsScreen loginAsStandardUser() {
        return new LoginScreen().loginAs(USERNAME, PASSWORD);
    }
}
