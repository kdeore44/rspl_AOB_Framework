package businesslogic;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import objectRepository.LoginPageOR;
import utilities.CommonMethods;
import utilities.ExtentReportBuilder;

public class LoginBL extends ExtentReportBuilder {
	String username, password;
	String mthName;
	CommonMethods objCM = new CommonMethods();
	LoginPageOR objLoginOR = new LoginPageOR();
	Duration due = Duration.ofSeconds(30);

	public void launchApplication(WebDriver driver, String url) throws IOException, ParseException {
		try {
			mthName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			if (objCM.launchURL(driver, url)) {
				ExtentReportBuilder.ReportTestStep("PASS", "Launching url in browser.", "Application launched successfully", mthName);
				Thread.sleep(5000);
			} else {
				Assert.assertTrue(false);
				ExtentReportBuilder.ReportTestStep("FAILED", "Launching url in browser.", "Application is NOT launched successfully", mthName);
			}
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep("Error in", "Launching url in browser.", "Exception while launching url.", mthName);
			Assert.fail();
		}

	}

	public void userLogin(WebDriver driver, String username, String password) throws IOException, ParseException {
		try {
			mthName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			Thread.sleep(5000);
			objCM.waitUntillElementClickable(driver, objLoginOR.buttLogin, due);

			objCM.WDWait(driver, objLoginOR.txtUsername);

			objCM.setText(driver, objLoginOR.txtUsername, username);
			objCM.setText(driver, objLoginOR.txtPwd, password);
			objCM.onMouseHover(driver, objLoginOR.buttLogin);
			Thread.sleep(4000);

			if (objCM.isElementPresent(driver, objLoginOR.txtUsername)) {
				ExtentReportBuilder.ReportTestStep("PASSED", "PASS",
						"Used logged in to application successfully", mthName);
			} else {
				ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
						"Used is NOT logged in to application successfully", mthName);
			}
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
					"Used is NOT logged in to application successfully"+e, mthName);
			e.printStackTrace();
			Assert.fail();
		}

	}

}
