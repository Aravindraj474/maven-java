package stepDef;

import com.jcabi.xml.XMLDocument;
import cucumber.api.Scenario;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestCalls {
    RequestSpecification req;
    private String outputPath = "target\\restlogs";
    private String outputFullPath;
    Response response = null;

    public RequestSpecification getRequest() {
        return req;
    }

    public RequestSpecification getRequest(String path) throws IOException, ParserConfigurationException, SAXException {
        String request = new String(Files.readAllBytes(Paths.get(path)));
        String requestXml = new XMLDocument(parseXmlFile(path)).toString();

        return req.body(requestXml);
    }

    private Document parseXmlFile(String path) throws ParserConfigurationException, IOException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
    }

    public String substituteXmlFile(Map<String, String> tagToSub, String path) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Document xml = parseXmlFile(path);
        for (String value : tagToSub.keySet()) {
            NodeList list = (NodeList) XPathFactory.newInstance().newXPath().compile(value).
                    evaluate(xml, XPathConstants.NODESET);
            for (int i = 0; i < list.getLength(); i++) {
                list.item(i).setTextContent(tagToSub.get(value));
            }
        }
        String requestBody = new XMLDocument(xml).toString();
        req.body(requestBody);

        return requestBody;
    }

    public RequestSpecification setEndpoint(String url) {
        req = given().baseUri(url);

        return req;
    }

    public RequestSpecification setHeaders(String headerName, String headerValue) {
        return req.header(headerName, headerValue);
    }

    public Response methodHit(String method, String resource) throws IOException {
        File file = new File(outputFullPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("Failed to create directory for logs: " + outputPath);
            }
        } else if (!file.isDirectory()) {
            throw new IOException("Non-directory file already exists for RestCall logs directory: " + outputPath);
        }
        PrintStream stream = new PrintStream(outputFullPath + File.separator +
                "request-and-response.txt");
        logRequest(stream);
        response = getRequest().request(method, resource);
        logResponse(stream);

        return response;
    }

    public void logFiles(String scenarioTitle) {
        outputFullPath = outputPath + scenarioTitle;
    }

    private void logRequest(PrintStream stream) throws FileNotFoundException {

        getRequest().config(RestAssuredConfig.config().logConfig(LogConfig.logConfig().defaultStream(stream)));
        stream.println();
        stream.println("--REQUEST--");
        getRequest().log().all();
    }

    private void logResponse(PrintStream stream) {
        stream.println();
        stream.println("--RESPONSE--");
        getResponse().then().log().all();
        stream.flush();
        stream.close();
    }

    public Response getResponse() {

        return response;
    }


}
