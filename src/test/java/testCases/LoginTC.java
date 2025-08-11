package testCases;


import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import automationEngine.ApplicationSetup;
import businesslogic.LoginBL;
import utilities.CommonUtilities;
import utilities.ExtentReportBuilder;
import utilities.JsonReader;

public class LoginTC extends ApplicationSetup {
	LoginBL objLoginBL = new LoginBL();
	CommonUtilities objCU = new CommonUtilities();
	JsonObject jsonObj = new JsonObject();
	JsonReader jsR = new JsonReader();

	@SuppressWarnings("deprecation")
	@Test(groups = { "Smoke", "Regression" })
	@Parameters({"browser"})
	public void ddLoginTC(String browser) throws Exception {
		ExtentReportBuilder.ReportInitialization("Login TC" + " - " + browser);
		try {
		objCU.browserTCcounter(browser);
	
		objLoginBL.userLogin(getDriver(), ApplicationSetup.UID, ApplicationSetup.PAS);
				
		ExtentReportBuilder.ReportTestStep("Console Messages:  ","Pass",""+objCU.printTheConsoleMessage());
		ExtentReportBuilder.ReportTestStep("Login Function Passed " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"PASS", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
	}catch(Exception e)
	{
		Thread thread = new Thread();
	    thread.interrupt();
		log.info(e.getMessage());
		ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"ERROR", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
		Assert.fail();
	}
	}
}
