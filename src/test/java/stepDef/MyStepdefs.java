package stepDef;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.xml.sax.SAXException;
import stepDef.selenium.Driver;
import stepDef.selenium.google;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class MyStepdefs {
    private WebDriver driver;
    private RestCalls calls;
    private Driver driverObject;

    @Before("@selenium")
    public void loadSettings() throws IOException {
        driverObject = new Driver();
        this.driver = driverObject.createDriver();
    }

    @After("@selenium")
    public void closeDrive() {
        driver.quit();
    }

    @Before("@rest")
    public void prepareLogs(Scenario scenario) {
        calls.logFiles(scenario);
    }


    public MyStepdefs() throws IOException {
        calls = new RestCalls();
    }

    @Given("^link step def$")
    public void linkStepDef() {
        System.out.println("inside the step def");

    }

    @When("^the \"(.*)\" method is hit on \"(.*)\"$")
    public void theMethodIsHitOn(String method, String resource) throws IOException {
        calls.methodHit(method, resource);
    }

    @Given("^the request body from \"(.*)\" is set$")
    public void theRequestBodyFromIsSet(String path) throws IOException, ParserConfigurationException, SAXException {
        String requestPath = "request/" + path + ".xml";
        calls.getRequest(requestPath);
    }

    @And("^the page \"(.*)\" is opened$")
    public void thePageIsOpened(String url) throws Throwable {
        google googleHome = new google();
        googleHome.openUrl(url);
        driverObject.click(By.xpath("//*[@title=\"Search\"]"));
        driverObject.sendKeys(By.xpath("//*[@title=\"Search\"]"), "Success");
//        driverObject.click(By.xpath(""));
    }

    @Given("^the request body from \"(.*)\" is set with following substitutions:$")
    public void theRequestBodyFromIsSetWithFollowingSubstitutions(String xmlName, Map<String, String> tagValues) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String path = "src/test/resources/request/" + xmlName + ".xml";
        calls.substituteXmlFile(tagValues, path);
    }

    @Given("^the endpoint \"(.*)\" is set for request$")
    public void theEndpointIsSetForRequest(String url) throws Throwable {
        calls.setEndpoint(url);
    }

    @And("^the follwoing headers are set:$")
    public void theFollwoingHeadersAreSet(Map<String, String> headers) {
        for (String headerName : headers.keySet()) {
            calls.setHeaders(headerName, headers.get(headerName));
        }
    }
}
