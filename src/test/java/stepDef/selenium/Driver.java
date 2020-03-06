package stepDef.selenium;

import cucumber.api.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Driver {
    public WebDriver driver;
    private Properties properties = new Properties();
    private String screenshotFolder = "target//screenshots";
    private String outputFullPath;

    public void logFiles(WebDriver driver, String scenarioTitle) throws IOException {
        outputFullPath = screenshotFolder + scenarioTitle;
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File elementSnapshot = screenshot.getScreenshotAs(OutputType.FILE);
        File screenshotFile = new File(outputFullPath + File.separator + "test.png");
        FileUtils.copyFile(elementSnapshot, screenshotFile);
    }

    public Properties loadProperty() throws IOException {
        InputStream input = new FileInputStream("C:\\Users\\jaibalax\\Desktop\\newtest\\src\\test\\resources\\Browser.properties");
        properties.load(input);

        return properties;
    }

    public Driver(WebDriver driver) throws IOException {
        this.driver = driver;
        loadProperty();
    }

    public WebDriver createDriver() throws IOException {
        String path = loadProperty().getProperty("browserPath");
        System.setProperty("webdriver.gecko.driver", path + "\\" + "geckodriver.exe");
        this.driver = new FirefoxDriver();
        return driver;
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public void click(By locator) throws IOException {
        findElement(locator).click();

    }

    public void sendKeys(By locator, String text) throws IOException {
        findElement(locator).click();
        findElement(locator).sendKeys(text);
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File elementSnapshot = screenshot.getScreenshotAs(OutputType.FILE);
        File screenshotFile = new File(screenshotFolder + File.separator + "test.png");
        FileUtils.copyFile(elementSnapshot, screenshotFile);
    }

}
