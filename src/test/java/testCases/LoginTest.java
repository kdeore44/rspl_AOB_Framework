package testCases;

import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import automationEngine.ApplicationSetup;
import businesslogic.LoginBLAOB;
import utilities.CommonUtilities;
import utilities.ExtentReportBuilder;
import utilities.JsonReader;

public class LoginTest extends ApplicationSetup {

	LoginBLAOB objLoginBLAOB = new LoginBLAOB();
	CommonUtilities objCU = new CommonUtilities();
	JsonObject jsonObj = new JsonObject();
	JsonReader jsR = new JsonReader();

	
	@Test	
	@Parameters({ "browser","Username","Password"})
	public void LoginTC(String browser,String Username,String Password) throws Exception {
		ExtentReportBuilder.ReportInitialization("Login TC" + " - " + browser);
		try {
			objCU.browserTCcounter(browser);

			objLoginBLAOB.clickOnMicrosoftSignInbtn(driver);
			//LoginBLAOB.waitForManualLogin(driver);
			objLoginBLAOB.enterUsername(driver, Username);
			System.out.println(Username);
			objLoginBLAOB.clickOnNextBtn(driver);
			objLoginBLAOB.enterPassword(driver, Password);
			System.out.println(Password);
			objLoginBLAOB.clickOnSignInBtn(driver);
			
			objLoginBLAOB.selectCheckBoxAndClickonYes(driver);
			System.out.println("CheckBox Selected");
			
			

			ExtentReportBuilder.ReportTestStep("Console Messages:  ", "Pass", "" + objCU.printTheConsoleMessage());
			ExtentReportBuilder.ReportTestStep(
					"Login Function Passed " + Thread.currentThread().getStackTrace()[2].getMethodName(), "PASS",
					objCU.generateRandomNumericString());
			ExtentReportBuilder.CompleteTest();
		} catch (Exception e) {
			Thread thread = new Thread();
			thread.interrupt();
			log.info(e.getMessage());
			ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
					"ERROR", objCU.generateRandomNumericString());
			
			Assert.fail();
		} finally {
			ExtentReportBuilder.CompleteTest();
		}
	}

}
