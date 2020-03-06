package stepDef;

import cucumber.api.Scenario;

import java.io.File;

public class prepareLogFiles {

    public String logFiles(Scenario scenario) {
        String[] featureId = scenario.getId().split(".feature:");
        String feature = featureId[0].trim().toLowerCase().
                replaceAll("[^0-9a-z]+", "-").
                replaceFirst("src-test-resources-features-", "");
        String name = scenario.getName().trim().toLowerCase().
                replaceAll("\\s+", "-");

        return feature + File.separator + name + featureId[1];
    }
}
