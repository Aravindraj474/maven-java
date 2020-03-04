package stepDef.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Driver {
    public WebDriver driver;
    private Properties properties = new Properties();

    public Properties loadProperty() throws IOException {
        InputStream input = new FileInputStream("C:\\Users\\jaibalax\\Desktop\\newtest\\src\\test\\resources\\Browser.properties");
        properties.load(input);

        return properties;
    }

    public Driver() throws IOException {
        loadProperty();
        String path = loadProperty().getProperty("browserPath");
        System.setProperty("webdriver.gecko.driver", path + "\\" + "geckodriver.exe");
        this.driver = new FirefoxDriver();
    }

    public WebDriver createDriver() {
        return driver;
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public void click(By locator) {
        findElement(locator).click();
    }

    public void sendKeys(By locator, String text) {
        findElement(locator).click();
        findElement(locator).sendKeys(text);
    }

}
