package businesslogic;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import automationEngine.ApplicationSetup;
import objectRepository.HomePageOR;
import objectRepository.LoginPageORAOB;
import utilities.CommonMethods;
import utilities.CommonUtilities;
import utilities.ExtentReportBuilder;

public class LoginBLAOB extends ExtentReportBuilder {
	String username, password;
	String methodName;
	CommonMethods objCM = new CommonMethods();
	CommonUtilities objCU = new CommonUtilities();
	LoginPageORAOB objLoginOR = new LoginPageORAOB();
	Duration due = Duration.ofSeconds(10);
	HomePageOR objHomePage = new HomePageOR();

	public void clickOnMicrosoftSignInbtn () throws IOException, ParseException {
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();
		try {
			objCM.waitUntillElementClickable(driver, objLoginOR.btnSigninMicrosoft, due);
			objCM.onMouseHover(driver, objLoginOR.btnSigninMicrosoft);

			ExtentReportBuilder.ReportTestStep(methodName, "PASS", "Successfully clicked on MSSignIn button");

		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep(methodName, "FAIL", "Exception during Microsoft Sign-In: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Exception occurred: " + e.getMessage());
		}
	}

	public void enterUsername(String Username) throws IOException, ParseException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();

		try {
			objCM.waitUntillElementClickable(driver, objLoginOR.txtUsername, due);
			objCM.setText(driver, objLoginOR.txtUsername, Username);
			ExtentReportBuilder.ReportTestStep(methodName, "PASSED", "PASS", "Username entered Successfully");
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep(methodName, "FAILED", "FAIL", "Username not entered: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Exception occurred while entering username: " + e.getMessage());
		}

	}

	public void enterPassword(String password) throws IOException, ParseException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();
		try {
			objCM.waitUntillElementClickable(driver, objLoginOR.txtPassword, due);
			objCM.setText(driver, objLoginOR.txtPassword, password);
			ExtentReportBuilder.ReportTestStep(methodName, "PASSED", "PASS", "Password entered successfully");
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep(methodName, "FAILED", "FAIL", "Password not entered: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Exception occurred while entering password: " + e.getMessage());
		}
	}

	public void clickOnNextBtn() throws IOException, ParseException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();
		try {
			objCM.waitUntillElementClickable(driver,objLoginOR.nextbtn, due);
			objCM.click(driver, objLoginOR.nextbtn);
			ExtentReportBuilder.ReportTestStep(methodName, "PASSED", "PASS", "Next button clicked successfully");
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep(methodName, "FAILED", "FAIL",
					"Next button not clicked: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Exception occurred while clicking Next button: " + e.getMessage());
		}
	}

	public void clickOnSignInBtn() throws IOException, ParseException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();
		try {
			objCM.waitUntillElementClickable(driver,objLoginOR.signInbtn, due);
			objCM.JSButtonClick(driver, objLoginOR.signInbtn);
			
			ExtentReportBuilder.ReportTestStep(methodName, "PASSED", "PASS", "Sign In button clicked successfully");
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep(methodName, "FAILED", "FAIL",
					"SignIn button not clicked: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Exception occurred while clicking SignIn button: " + e.getMessage());
		}
	}
	public void selectCheckBoxAndClickonYes() throws IOException, ParseException {

		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();
		try {
			
			objCM.JSButtonClick(driver, objLoginOR.checkBox);
			Thread.sleep(1000);
			objCM.waitUntillElementClickable(driver,objLoginOR.yesbtn, due);
			objCM.JSButtonClick(driver, objLoginOR.yesbtn);
			
			ExtentReportBuilder.ReportTestStep(methodName, "PASSED", "PASS", "Checkbox selected and Yes button clicked successfully");
		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep(methodName, "FAILED", "FAIL",
					"SignIn button not clicked: " + e.getMessage());
			e.printStackTrace();
			Assert.fail("Exception occurred while clicking SignIn button: " + e.getMessage());
		}
	}


	public static void waitForManualLogin() {
		WebDriver driver = ApplicationSetup.getDriver();
		Scanner scanner = new Scanner(System.in);
		System.out.println("🔒 Please complete Microsoft login manually and then press Enter to continue...");
		scanner.nextLine();
		System.out.println("✅ Login confirmed. Resuming automation...");
	}

}
