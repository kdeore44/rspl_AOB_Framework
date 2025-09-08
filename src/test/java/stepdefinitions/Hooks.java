package stepdefinitions;

import java.io.IOException;
import java.text.ParseException;

import automationEngine.ApplicationSetup;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.ExtentReportBuilder;

public class Hooks extends ApplicationSetup {

	@Before
	 public void beforeScenario(Scenario scenario) throws Exception {
        String scenarioName = scenario.getName();
        ExtentReportBuilder.ReportInitialization(scenarioName);
    }

	@After
	public void afterScenario() throws IOException, ParseException {
		ExtentReportBuilder.CompleteTest();
	}

}
