package stepDef;

import cucumber.api.java.en.Given;

public class MyStepdefs {
    @Given("^link step def$")
    public void linkStepDef() {
        System.out.println("inside the step def");

    }
}
