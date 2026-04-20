package com.sdet.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Reusable gesture utilities for mobile: swipe, scroll, long press, tap.
 */
public class GestureUtils {

    private static final Logger log = LoggerFactory.getLogger(GestureUtils.class);
    private static final PointerInput FINGER = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    private GestureUtils() {}

    public enum SwipeDirection { UP, DOWN, LEFT, RIGHT }

    /**
     * Swipe across the screen in the given direction.
     */
    public static void swipe(AndroidDriver driver, SwipeDirection direction) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;

        int startX, startY, endX, endY;

        switch (direction) {
            case UP -> {
                startX = centerX; startY = (int)(size.height * 0.8);
                endX   = centerX; endY   = (int)(size.height * 0.2);
            }
            case DOWN -> {
                startX = centerX; startY = (int)(size.height * 0.2);
                endX   = centerX; endY   = (int)(size.height * 0.8);
            }
            case LEFT -> {
                startX = (int)(size.width * 0.8); startY = centerY;
                endX   = (int)(size.width * 0.2); endY   = centerY;
            }
            default -> { // RIGHT
                startX = (int)(size.width * 0.2); startY = centerY;
                endX   = (int)(size.width * 0.8); endY   = centerY;
            }
        }

        performSwipe(driver, startX, startY, endX, endY, 600);
        log.debug("Swiped {}", direction);
    }

    /**
     * Scroll until an element is visible (up to maxScrolls attempts).
     */
    public static void scrollToElement(AndroidDriver driver, WebElement target, int maxScrolls) {
        for (int i = 0; i < maxScrolls; i++) {
            try {
                if (target.isDisplayed()) {
                    log.debug("Element found after {} scroll(s)", i);
                    return;
                }
            } catch (Exception ignored) {}
            swipe(driver, SwipeDirection.UP);
        }
        log.warn("Element not found after {} scrolls", maxScrolls);
    }

    /**
     * Long press on a WebElement.
     */
    public static void longPress(AndroidDriver driver, WebElement element) {
        int x = element.getLocation().getX() + element.getSize().getWidth() / 2;
        int y = element.getLocation().getY() + element.getSize().getHeight() / 2;

        Sequence sequence = new Sequence(FINGER, 0)
                .addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(FINGER.createPointerMove(Duration.ofMillis(1500), PointerInput.Origin.viewport(), x, y))
                .addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(sequence));
        log.debug("Long pressed at ({}, {})", x, y);
    }

    private static void performSwipe(AndroidDriver driver, int startX, int startY, int endX, int endY, long durationMs) {
        Sequence sequence = new Sequence(FINGER, 0)
                .addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(FINGER.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY))
                .addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(sequence));
    }
}
