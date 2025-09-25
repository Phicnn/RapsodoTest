package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import utils.ConfigReader;
import utils.Driver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static String reportPath;

    @Override
    public void onStart(ISuite suite) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportPath = "./test-reports/ExtentReport_" + timeStamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Rapsodo Test Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", ConfigReader.getDefaultBrowser());
        extent.setSystemInfo("Test Environment", "QA");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);

        test.log(Status.INFO, "Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS,
                MarkupHelper.createLabel("Test PASSED: " + result.getMethod().getMethodName(),
                        ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // Hata mesajını rapora ekle
        extentTest.get().log(Status.FAIL,
                MarkupHelper.createLabel("Test FAILED: " + testName, ExtentColor.RED));

        extentTest.get().log(Status.FAIL, "Error: " + result.getThrowable().getMessage());

        // Screenshot al ve rapora ekle
        String screenshotPath = captureScreenshot(testName);
        if (screenshotPath != null) {
            try {
                extentTest.get().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            } catch (Exception e) {
                extentTest.get().log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP,
                MarkupHelper.createLabel("Test SKIPPED: " + result.getMethod().getMethodName(),
                        ExtentColor.ORANGE));
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) {
            extent.flush();
            System.out.println("Test raporu oluşturuldu: " + reportPath);
        }
    }

    private String captureScreenshot(String testName) {
        try {
            // Driver'ı almaya çalış
            WebDriver driver = getDriverFromTest();
            if (driver == null) {
                System.out.println("Driver is null - Screenshot cannot be taken");
                return null;
            }

            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String screenshotName = testName + "_" + timeStamp + ".png";

            File screenshotDir = new File("./test-reports/screenshots/");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File destFile = new File(screenshotDir, screenshotName);
            FileUtils.copyFile(sourceFile, destFile);

            System.out.println("Screenshot captured: " + destFile.getAbsolutePath());
            return "./screenshots/" + screenshotName; // Relative path for report

        } catch (Exception e) {
            System.out.println("Screenshot capture error: " + e.getMessage());
            return null;
        }
    }

    private WebDriver getDriverFromTest() {
        try {
            // Driver sınıfından static driver'ı al
            return Driver.driver;
        } catch (Exception e) {
            System.out.println("Driver could not be retrieved: " + e.getMessage());
            return null;
        }
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void logInfo(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.INFO, message);
        }
    }

    public static void logPass(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.PASS, message);
        }
    }

    public static void logFail(String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(Status.FAIL, message);
        }
    }
}