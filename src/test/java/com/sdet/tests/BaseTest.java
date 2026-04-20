package com.sdet.tests;

import com.sdet.utils.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * Base test class for all mobile tests.
 * Manages Appium driver lifecycle and captures screenshots on failure.
 */
public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        log.info("Starting mobile test: {}", result.getMethod().getMethodName());
        DriverManager.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: {}", testName);
            captureScreenshot(testName);
        }

        DriverManager.quitDriver();
        log.info("Driver quit for test: {}", testName);
    }

    private void captureScreenshot(String testName) {
        try {
            AndroidDriver driver = DriverManager.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot - " + testName,
                    "image/png", new ByteArrayInputStream(screenshot), "png");
            log.info("Screenshot captured for: {}", testName);
        } catch (Exception e) {
            log.warn("Could not capture screenshot: {}", e.getMessage());
        }
    }
}
