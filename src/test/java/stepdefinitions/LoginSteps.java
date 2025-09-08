package stepdefinitions;

import java.io.IOException;
import java.text.ParseException;

import automationEngine.ApplicationSetup;
import businesslogic.LoginBLAOB;
import utilities.CommonUtilities;
import utilities.ExtentReportBuilder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;


public class LoginSteps extends ApplicationSetup {
	  LoginBLAOB objLoginBLAOB = new LoginBLAOB();
	    CommonUtilities objCU = new CommonUtilities();

	    @Given("I open the application in {string} browser")
	    public void i_open_the_application_in_browser(String browser) throws Exception {
	        ExtentReportBuilder.ReportInitialization("Login TC" + " - " + browser);
	        objCU.browserTCcounter(browser);
	    }

	    @When("I click on Microsoft Sign In button")
	    public void i_click_on_microsoft_sign_in_button() throws IOException, ParseException {
	        objLoginBLAOB.clickOnMicrosoftSignInbtn();
	    }

	    @When("I enter username {string}")
	    public void i_enter_username(String username) throws IOException, ParseException {
	        objLoginBLAOB.enterUsername(username);
	        System.out.println(username);
	    }

	    @When("I click on Next button")
	    public void i_click_on_next_button() throws IOException, ParseException {
	        objLoginBLAOB.clickOnNextBtn();
	    }

	    @When("I enter password {string}")
	    public void i_enter_password(String password) throws IOException, ParseException {
	        objLoginBLAOB.enterPassword(password);
	        System.out.println(password);
	    }

	    @When("I click on Sign In button")
	    public void i_click_on_sign_in_button() throws IOException, ParseException {
	        objLoginBLAOB.clickOnSignInBtn();
	    }

	    @When("I select the checkbox and click Yes")
	    public void i_select_the_checkbox_and_click_yes() throws IOException, ParseException {
	        objLoginBLAOB.selectCheckBoxAndClickonYes();
	        System.out.println("CheckBox Selected");
	    }

	    @Then("I should be logged in successfully")
	    public void i_should_be_logged_in_successfully() throws IOException, ParseException {
	        ExtentReportBuilder.ReportTestStep(
	                "Console Messages:  ", "Pass", "" + objCU.printTheConsoleMessage());
	        ExtentReportBuilder.ReportTestStep(
	                "Login Function Passed", "PASS",
	                objCU.generateRandomNumericString());
	        ExtentReportBuilder.CompleteTest();
	    }

}
