package com.sdet.utils;

import com.sdet.config.CapabilitiesConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

/**
 * Thread-safe Appium driver manager.
 * Supports Android emulator, real device, and BrowserStack.
 */
public class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> driverThread = new ThreadLocal<>();

    // Appium server URL (local or BrowserStack)
    private static final String APPIUM_URL_LOCAL       = "http://127.0.0.1:4723";
    private static final String APPIUM_URL_BROWSERSTACK = "https://hub-cloud.browserstack.com/wd/hub";

    private DriverManager() {}

    public static AndroidDriver getDriver() {
        if (driverThread.get() == null) {
            initDriver();
        }
        return driverThread.get();
    }

    public static void initDriver() {
        String profile = System.getProperty("device.profile", "android-emulator");
        boolean useBrowserStack = Boolean.parseBoolean(System.getProperty("browserstack", "false"));

        log.info("Initializing Appium driver | profile={} | browserstack={}", profile, useBrowserStack);

        Map<String, Object> caps = CapabilitiesConfig.load(profile);

        UiAutomator2Options options = new UiAutomator2Options();
        caps.forEach(options::setCapability);

        try {
            String serverUrl = useBrowserStack ? APPIUM_URL_BROWSERSTACK : APPIUM_URL_LOCAL;
            AndroidDriver driver = new AndroidDriver(new URL(serverUrl), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverThread.set(driver);
            log.info("Appium driver initialized successfully");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL", e);
        }
    }

    public static void quitDriver() {
        AndroidDriver driver = driverThread.get();
        if (driver != null) {
            log.info("Quitting Appium driver");
            driver.quit();
            driverThread.remove();
        }
    }
}
