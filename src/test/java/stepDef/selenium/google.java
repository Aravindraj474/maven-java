package stepDef.selenium;

import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class google extends Driver {

    public google() throws IOException {
        super();
    }

    public WebDriver openUrl(String url) {
        createDriver().get(url);
        return driver;
    }

}
