package stepDef.selenium;

import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class google extends Driver {

    public google(WebDriver driver) throws IOException {
        super(driver);
    }

    public WebDriver openUrl(String url) {
        driver.get(url);
        return driver;
    }

}
