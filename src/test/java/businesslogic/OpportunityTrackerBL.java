package businesslogic;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import automationEngine.ApplicationSetup;
import objectRepository.OpportunityTrackerOR;
import utilities.CommonMethods;
import utilities.ExtentReportBuilder;

public class OpportunityTrackerBL extends ExtentReportBuilder {

	String methodName;
	OpportunityTrackerOR objOT = new OpportunityTrackerOR();
	CommonMethods CM = new CommonMethods();
	Duration due = Duration.ofSeconds(30);
	String oppoID = "";

	public void searchOpportunityAndLogStatus(String oppoID) throws IOException, ParseException {
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    WebDriver driver = ApplicationSetup.getDriver();
	    try {
	        // Wait for Opportunity Search Field
	        WebElement searchField = CM.waitUntillElementVisible(driver, objOT.oppotunitySearchField, due);

	        // Enter Opportunity ID
	        CM.setText(driver, objOT.oppotunitySearchField, oppoID);

	        // Press Enter using Actions
	        Actions actions = new Actions(driver);
	        actions.sendKeys(Keys.ENTER).perform();

	        // Report success
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Entered Opportunity ID and pressed Enter: " + oppoID, methodName);

	    } catch (TimeoutException e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity Search Field not visible in time", methodName);
	        e.printStackTrace();
	        Assert.fail("Timeout while waiting for Opportunity Search Field: " + e.getMessage());
	    } catch (NoSuchElementException e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity Search Field element not found", methodName);
	        e.printStackTrace();
	        Assert.fail("Opportunity Search Field not found: " + e.getMessage());
	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception occurred while searching opportunity: " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("Exception occurred while searching opportunity: " + e.getMessage());
	    }
	}


	public void verifyOppoIDAndClickRequestAllocation(String oppoID)
	        throws IOException, ParseException {

	    // Initialize method name for reporting
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    WebDriver driver = ApplicationSetup.getDriver();
	    try {
	        // Step 1: Scroll to Opportunity Table and wait for search result
	        CM.scrollToElement(driver, objOT.allOppoTable);
	        CM.waitForSearchResult(driver, oppoID, 15);

	        // Step 2: Locate Opportunity ID cell
	        WebElement resultCell = driver.findElement(By.xpath("//td[contains(text(),'" + oppoID + "')]"));

	        if (resultCell.isDisplayed()) {
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Opportunity ID '" + oppoID + "' found", methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity ID '" + oppoID + "' not found", methodName);
	            Assert.fail("Opportunity ID '" + oppoID + "' not visible in the table.");
	            return;
	        }

	        // Step 3: Scroll to Action column
	        CM.scrollToElement(driver, By.xpath("//th[text()='Action']"));

	        // Step 4: Find 'Request Resource Allocation' button in same row
	        WebElement button = driver.findElement(By.xpath("//td[contains(text(),'" + oppoID
	                + "')]//ancestor::tr//span[text()='Request Resource Allocation']"));

	        // Step 5: Click the button if visible and enabled
	        if (button.isDisplayed() && button.isEnabled()) {
	            CM.JSButtonClick(driver, button);
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS",
	                    "'Request Resource Allocation' button clicked for OppoID: " + oppoID, methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
	                    "'Request Resource Allocation' button is not clickable for OppoID: " + oppoID, methodName);
	            Assert.fail("Button is not clickable for Opportunity ID: " + oppoID);
	        }

	    } catch (NoSuchElementException e) {
	        // Step 6: Element not found error handling
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
	                "'Request Resource Allocation' button not found for OppoID: " + oppoID, methodName);
	        e.printStackTrace();
	        Assert.fail("Button not found for Opportunity ID '" + oppoID + "': " + e.getMessage());

	    } catch (Exception e) {
	        // Step 7: Generic failure handling
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception: " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("Exception occurred while handling Opportunity ID '" + oppoID + "': " + e.getMessage());
	    }
	}


	public void clickTagResourcesButtonForOppoID(String oppoID) throws Exception {
	    // Initialize method name for reporting
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    WebDriver driver = ApplicationSetup.getDriver();
	    ExtentReportBuilder.ReportTestStep(methodName, "Started execution of clickTagResourcesButtonForOppoID", "INFO");

	    try {
	        // Step 1: Scroll to the opportunity table and wait for the result row for given Opportunity ID
	        CM.scrollToElement(driver, objOT.allOppoTable);
	        CM.waitForSearchResult(driver, oppoID, 15);

	        // Step 2: Locate the cell containing the Opportunity ID and verify it is visible
	        By oppoCellLocator = By.xpath("//td[contains(text(),'" + oppoID + "')]");
	        WebElement resultCell = driver.findElement(oppoCellLocator);

	        if (resultCell.isDisplayed()) {
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Opportunity ID '" + oppoID + "' found", methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity ID '" + oppoID + "' not found", methodName);
	            Assert.fail("Opportunity ID '" + oppoID + "' not found");
	            return;
	        }

	        // Step 3: Scroll to Action column header and locate the 'Tag Resources' button
	        CM.scrollToElement(driver, By.xpath("//th[text()='Action']"));
	        By tagResourceBtnLocator = By.xpath("//td[contains(text(),'" + oppoID + "')]//ancestor::tr//span[text()='Tag Resources']");
	        WebElement tagResourceButton = driver.findElement(tagResourceBtnLocator);

	        // Step 4: Verify if button is displayed and enabled, then click it using JavaScript
	        if (tagResourceButton.isDisplayed() && tagResourceButton.isEnabled()) {
	            CM.JSButtonClick(driver, tagResourceBtnLocator);
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "'Tag Resources' button clicked for OppoID: " + oppoID, methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "'Tag Resources' button is not clickable for OppoID: " + oppoID, methodName);
	            Assert.fail("'Tag Resources' button is not clickable for OppoID: " + oppoID);
	        }

	    } catch (NoSuchElementException e) {
	        // Step 5: Handle element not found scenario
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "'Tag Resources' button not found for OppoID: " + oppoID + ". Exception: " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("'Tag Resources' button not found for OppoID: " + oppoID + ". Exception: " + e.getMessage());
	    } catch (Exception e) {
	        // Step 6: Handle any unexpected exception
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception while clicking 'Tag Resources': " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("Exception while clicking 'Tag Resources': " + e.getMessage());
	    }
	}




// Verify Add resource from Tagged Resource and verify on dashboard
// Function
	public void clickTaggedButtonInResourcesColumnAndClickOnEditResource(String oppoID) throws Exception {
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    WebDriver driver = ApplicationSetup.getDriver();
	    ExtentReportBuilder.ReportTestStep(methodName, "Started execution of clickTaggedButtonInResourcesColumnAndClickOnEditResource for: " + oppoID, "INFO");

	    try {
	        // Step 1: Scroll to opportunity table and wait for data
	        ExtentReportBuilder.ReportTestStep(methodName, "Scrolling to Opportunity Table", "INFO");
	        CM.scrollToElement(driver, objOT.allOppoTable);

	        ExtentReportBuilder.ReportTestStep(methodName, "Waiting for search result with OppoID: " + oppoID, "INFO");
	        CM.waitForSearchResult(driver, oppoID, 15);

	        // Step 2: Locate the result cell by Opportunity ID
	        ExtentReportBuilder.ReportTestStep(methodName, "Locating result cell for OppoID: " + oppoID, "INFO");
	        By oppoCellLocator = By.xpath("//td[contains(text(),'" + oppoID + "')]");
	        WebElement resultCell = driver.findElement(oppoCellLocator);

	        if (resultCell.isDisplayed()) {
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Opportunity ID '" + oppoID + "' found", methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Opportunity ID '" + oppoID + "' not found", methodName);
	            Assert.fail("Opportunity ID '" + oppoID + "' not found");
	            return;
	        }

	        // Step 3: Scroll to Action column header
	        ExtentReportBuilder.ReportTestStep(methodName, "Scrolling to Action column", "INFO");
	        CM.scrollToElement(driver, By.xpath("//th[text()='Action']"));

	        // Step 4: Locate the Tag Resource button within the same row
	        ExtentReportBuilder.ReportTestStep(methodName, "Locating 'Tagged' button for OppoID: " + oppoID, "INFO");
	        By taggedBtnLocator = By.xpath("//td[contains(normalize-space(text()),'" + oppoID + "')]//ancestor::tr//a[contains(normalize-space(text()),'Tagged')]");
	        WebElement tagResourceButton = driver.findElement(taggedBtnLocator);

	        // Step 5: Validate Tagged button visibility and click on EDIT Resource button 
	        if (tagResourceButton.isDisplayed() && tagResourceButton.isEnabled()) {
	            ExtentReportBuilder.ReportTestStep(methodName, "'Tagged' button is visible and enabled", "INFO");
	            CM.JSButtonClick(driver, taggedBtnLocator);

	            ExtentReportBuilder.ReportTestStep(methodName, "Clicked on 'Tagged' button. Waiting for 'Edit Resource' button.", "INFO");
	            CM.waitUntillElementClickable(driver, By.xpath("//button[text()='Edit Resource']"), due);
	            CM.JSButtonClick(driver, By.xpath("//button[text()='Edit Resource']"));

	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "'Edit Resource' button clicked for OppoID: " + oppoID, methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "'Tag Resources' button is not clickable for OppoID: " + oppoID, methodName);
	            Assert.fail("'Tag Resources' button is not clickable for OppoID: " + oppoID);
	        }

	    } catch (NoSuchElementException e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "'Tag Resources' button not found for OppoID: " + oppoID + ". Exception: " + e.getMessage(), methodName);
	        Assert.fail("'Tag Resources' button not found for OppoID: " + oppoID + ". Exception: " + e.getMessage());
	        throw e;
	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception while clicking 'Tag Resources': " + e.getMessage(), methodName);
	        Assert.fail("Exception while clicking 'Tag Resources': " + e.getMessage());
	        throw e;
	    }
	}


}
