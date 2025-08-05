package businesslogic;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import objectRepository.NewResourceRequisitionRequestOR;
import objectRepository.OpportunityTrackerOR;
import utilities.CommonMethods;
import utilities.ExtentReportBuilder;

public class NewResourceRequisitionRequestBL extends ExtentReportBuilder {
	String methodName;
	OpportunityTrackerOR objOT = new OpportunityTrackerOR();
	CommonMethods CM = new CommonMethods();
	Duration due = Duration.ofSeconds(30);
	NewResourceRequisitionRequestOR objNRR = new NewResourceRequisitionRequestOR();
	
	
	public void fillTheFormAndSubmit(WebDriver driver,
	        String yearOfExp,
	        String noOfPositions,
	        String resourceType,
	        String departmentName) throws Exception {

	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    ExtentReportBuilder.ReportTestStep(methodName, "Started execution of fillTheFormAndSubmit", "INFO");

	    try {
	        // Step 1: Click 'Hire New Resource' button
	        CM.waitUntillElementClickable(driver, objNRR.hireNewResourceBtn, due);
	        CM.JSButtonClick(driver, objNRR.hireNewResourceBtn);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked on 'Hire New Resource' button", methodName);

	        // Step 2: Select Year of Experience
	        WebElement yearOfExpElem = CM.waitUntillElementVisible(driver, objNRR.yearOfExperience, due);
	        CM.JSButtonClick(driver, yearOfExpElem);
	        WebElement expOption = CM.waitUntillElementClickable(driver,
	                By.xpath("//span[contains(text(),'" + yearOfExp + "')]"), due);
	        CM.JSButtonClick(driver, expOption);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected Year Of Experience: " + yearOfExp, methodName);
	        Thread.sleep(500);

	        // Step 3: Enter Number of Positions
	        CM.setText(driver, objNRR.NoOfPositionfield, noOfPositions);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Entered No. of Positions: " + noOfPositions, methodName);
	        Thread.sleep(500);

	        // Step 4: Select Resource Type
	        CM.JSButtonClick(driver, objNRR.resourceTypefield);
	        WebElement resOption = CM.waitUntillElementClickable(driver,
	                By.xpath("//span[contains(text(),'" + resourceType + "')]"), due);
	        CM.JSButtonClick(driver, resOption);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected Resource Type: " + resourceType, methodName);
	        Thread.sleep(500);

	        // Step 5: Select Required By Date (Today)
	        CM.JSButtonClick(driver, objNRR.requiredDate);
	        WebElement today = CM.waitUntillElementClickable(driver,
	                By.xpath("//span[contains(@class,'mat-calendar-body-today')]"), due);
	        Thread.sleep(500);
	        CM.JSButtonClick(driver, today);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected Required By Date: Today", methodName);
	        Thread.sleep(500);

	        // Step 6: Select Department
	        CM.JSButtonClick(driver, objNRR.departmentField);
	        WebElement deptOption = CM.waitUntillElementClickable(driver,
	                By.xpath("//span[contains(text(),'" + departmentName + "')]"), due);
	        CM.JSButtonClick(driver, deptOption);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected Department: " + departmentName, methodName);
	        Thread.sleep(500);

	        // Step 7: Select Technology Stack (First available)
	        CM.JSButtonClick(driver, objNRR.technologyStack);
	        WebElement firstOption = CM.waitUntillElementClickable(driver, By.xpath("//mat-option//span[1]"), due);
	        CM.JSButtonClick(driver, firstOption);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected Technology Stack", methodName);
	        Thread.sleep(500);

	        // Step 8: Click Submit Button
	        WebElement submitBtn = CM.waitUntillElementClickable(driver, objNRR.submitBtn, due);
	        if (submitBtn.isDisplayed() && submitBtn.isEnabled()) {
	            CM.JSButtonClick(driver, submitBtn); // Uncomment if you want to actually submit
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Form submitted successfully", methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Submit button not interactable", methodName);
	            Assert.fail("Submit button is not visible or enabled.");
	        }

	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception in fillTheFormAndSubmit(): " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("Exception in fillTheFormAndSubmit(): " + e.getMessage());
	    }
	}

	
	
	public void selectYOE(WebDriver driver, String yoeValue) throws IOException, ParseException {
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    ExtentReportBuilder.ReportTestStep(methodName, "Started execution of selectYOE", "INFO");

	    try {
	        // Step 1: Open Year of Experience dropdown
	        WebElement expDropdown = CM.waitUntillElementClickable(driver, objNRR.yearOfExperience, due);
	        CM.JSButtonClick(driver, expDropdown);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked on Year of Experience dropdown", methodName);

	        // Step 2: Get all options
	        By optionsList = By.xpath("//div[@role='listbox']/mat-option");
	        List<WebElement> options = driver.findElements(optionsList);

	        boolean isSelected = false;

	        // Step 3: Loop to find matching YOE value
	        for (WebElement option : options) {
	            String text = option.getText().trim();
	            ExtentReportBuilder.ReportTestStep(methodName, "Dropdown option: " + text, "INFO");

	            if (text.equalsIgnoreCase(yoeValue)) {
	                CM.JSButtonClick(driver, option);
	                ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected YOE value: " + text, methodName);
	                isSelected = true;
	                break;
	            }
	        }

	        // Step 4: If not found, fail
	        if (!isSelected) {
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "YOE value '" + yoeValue + "' not found in dropdown", methodName);
	            Assert.fail("YOE value '" + yoeValue + "' not found in dropdown");
	        }

	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception while selecting YOE: " + e.getMessage(), methodName);
	        e.printStackTrace();
	        Assert.fail("Exception while selecting YOE: " + e.getMessage());
	    }
	}

}
