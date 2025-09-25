package pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;
import utils.TestListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);
        this.actions = new Actions(driver);
    }

    protected void takeScreenshot(String fileName) {
        try {
            File screenshotDir = new File(ConfigReader.getScreenshotDirectory());
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String fullFileName = fileName + "_" + timeStamp + ".png";

            FileUtils.copyFile(screenshot, new File(screenshotDir, fullFileName));

            // report
            TestListener.logInfo("Screenshot captured: " + fullFileName);

        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            TestListener.logFail("Failed to capture screenshot: " + e.getMessage());
        }
    }

    protected WebElement setWait(By key) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(key));
        } catch (Exception e) {
            takeScreenshot("element_wait_failed");
            TestListener.logFail("Element could not be waited: " + key.toString());
            throw e;
        }
    }

    protected WebElement setWait(WebElement key) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(key));
        } catch (Exception e) {
            takeScreenshot("element_wait_failed");
            TestListener.logFail("Element could not be waited");
            throw e;
        }
    }

    protected void setTimer(int timer) {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    protected void clickElement(WebElement element) {
        try {
            setWait(element).click();
            TestListener.logInfo("Element clicked successfully");
        } catch (Exception e) {
            takeScreenshot("click_failed");
            TestListener.logFail("Element click failed: " + e.getMessage());
            throw e;
        }
    }

    protected void sendKeysToElement(WebElement element, String text) {
        try {
            setWait(element).clear();
            element.sendKeys(text);
            TestListener.logInfo("Text entered: " + text);
        } catch (Exception e) {
            takeScreenshot("sendkeys_failed");
            TestListener.logFail("Text input failed: " + e.getMessage());
            throw e;
        }
    }

    protected void hoverOverElement(WebElement element) {
        try {
            actions.moveToElement(element).click().perform();
            TestListener.logInfo("Hover action performed on element");
        } catch (Exception e) {
            takeScreenshot("hover_failed");
            TestListener.logFail("Hover action failed: " + e.getMessage());
            throw e;
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            TestListener.logInfo("Element visibility status: " + isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            takeScreenshot("element_not_displayed");
            TestListener.logFail("Element visibility check failed");
            return false;
        }
    }

    public WebDriver getDriver() {
        return this.driver;
    }
}