package businesslogic;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import objectRepository.HomePageOR;
import utilities.CommonMethods;
import utilities.ExtentReportBuilder;

public class HomeBL extends ExtentReportBuilder {

	HomePageOR objHomePage = new HomePageOR();
	String methodName;
	CommonMethods CM = new CommonMethods();
	Duration due = Duration.ofSeconds(30);

	public void validateDashboardTitleVisibility(WebDriver driver) throws IOException, ParseException {
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

	    try {
	        // Wait until Dashboard Title is visible
	        WebElement dashboardTitle = CM.waitUntillElementVisible(driver, objHomePage.humbergerMenuDashBoard, due);

	        // Check and log result
	        if (dashboardTitle != null && dashboardTitle.isDisplayed()) {
	            ExtentReportBuilder.ReportTestStep(methodName, "PASS", "Dashboard title is visible on Home Page");
	        } else {
	            ExtentReportBuilder.ReportTestStep(methodName, "FAIL", "Dashboard title is NOT visible on Home Page");
	            Assert.fail("Dashboard title is NOT visible on Home Page");
	        }

	    } catch (TimeoutException e) {
	        ExtentReportBuilder.ReportTestStep(methodName, "FAIL", "Dashboard title did not appear in time");
	        e.printStackTrace();
	        Assert.fail("Dashboard title timeout: " + e.getMessage());
	    } catch (NoSuchElementException e) {
	        ExtentReportBuilder.ReportTestStep(methodName, "FAIL", "Dashboard title element not found");
	        e.printStackTrace();
	        Assert.fail("Dashboard title not found: " + e.getMessage());
	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep(methodName, "FAIL", "Exception occurred: " + e.getMessage());
	        e.printStackTrace();
	        Assert.fail("Exception occurred: " + e.getMessage());
	    }
	}

	
	public void navigateToOpportunityTrackerAndLogStatus(WebDriver driver) throws IOException, ParseException {
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

	    try {
	        // Click on Operations Menu
	        CM.click(driver, objHomePage.operationsMenu);

	        // Wait for Opportunity Tracker submenu
	        CM.waitUntillElementVisible(driver, objHomePage.opportunityTrackerSubMenu, due);

	        // Click on Opportunity Tracker
	        CM.JSButtonClick(driver, objHomePage.opportunityTrackerSubMenu);

	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "User navigated to Opportunity Tracker screen successfully", methodName);

	    } catch (TimeoutException e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity Tracker submenu not visible in time", methodName);
	        e.printStackTrace();
	        Assert.fail("Timeout while waiting for Opportunity Tracker submenu: " + e.getMessage());
	    } catch (NoSuchElementException e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity Tracker submenu element not found", methodName);
	        e.printStackTrace();
	        Assert.fail("Opportunity Tracker submenu not found: " + e.getMessage());
	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Failed to navigate to Opportunity Tracker: " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("Exception while navigating to Opportunity Tracker: " + e.getMessage());
	    }
	}


	
}
