package businesslogic;

import java.io.IOException;

import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import automationEngine.ApplicationSetup;
import objectRepository.ResourceAllocationFormOR;
import utilities.CommonMethods;
import utilities.ExtentReportBuilder;

public class ResourceAllocationBL extends ExtentReportBuilder {

	String methodName;
	ResourceAllocationFormOR objRAF = new ResourceAllocationFormOR();
	CommonMethods CM = new CommonMethods();
	Duration due = Duration.ofSeconds(10);
	
	
	public void handleResourceAllocationFormAndClickOnSubmitButton() throws IOException, ParseException {
	    // Initialize method name for logging and reporting
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    WebDriver driver = ApplicationSetup.getDriver();

	    try {
	        // Step 1: Wait for the Resource Allocation form to become visible
	        CM.waitUntillVisibilityOfElement(driver, objRAF.formTable, due);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Resource Allocation form is visible", methodName);

	        // Step 2: Get all rows in the tech stack table
	        List<WebElement> rows = driver.findElements(objRAF.tableRows);
	        ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Total tech stack rows found: " + rows.size(), methodName);

	        // Step 3: Define expected tech stacks and their desired dropdown values
	        Map<String, String> expectedStacks = new HashMap<>();
	        expectedStacks.put("Testing", "Billable");
	        expectedStacks.put("Java Full Stack", "Shadow");
	        

	        boolean anySelectionMade = false;

	        // Step 4: Iterate through all rows and interact with matching tech stacks
	        for (WebElement row : rows) {
	            try {
	                // Get the tech stack name from the second column
	                String techStackName = row.findElement(By.xpath("./td[2]")).getText().trim();
	                ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Tech stack found: " + techStackName, methodName);

	                if (expectedStacks.containsKey(techStackName)) {
	                    // Step 5: If the tech stack is in expected list, perform dropdown selection
	                    String userSelectedValue = expectedStacks.get(techStackName);

	                    try {
	                        WebElement dropdown = row.findElement(By.xpath(".//mat-select"));
	                        CM.JSButtonClick(driver, dropdown);
	                        ExtentReportBuilder.ReportTestStep("PASSED", "PASS",
	                                "Clicked Opportunity Type dropdown for: " + techStackName, methodName);

	                        Thread.sleep(1000);

	                        By valueXpath = By.xpath("//mat-option//span[normalize-space(text())='" + userSelectedValue + "']");
	                        CM.waitUntillElementClickable(driver, valueXpath, due);
	                        CM.JSButtonClick(driver, valueXpath);

	                        System.out.println("Dropdown clicked for: " + techStackName);
	                        System.out.println("Waiting for option: " + userSelectedValue);

	                        ExtentReportBuilder.ReportTestStep("PASSED", "PASS",
	                                "Selected '" + userSelectedValue + "' for: " + techStackName, methodName);

	                        anySelectionMade = true;
	                        Thread.sleep(3000);
	                        // Optional: CM.waitUntilInvisibilityOfElement(driver, valueXpath, due);

	                    } catch (Exception e) {
	                        // Step 6: Log failure if dropdown selection fails
	                        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
	                                "Dropdown selection failed for: " + techStackName + " - " + e.getMessage(), methodName);
	                    }
	                }
	            } catch (Exception e) {
	                // Step 7: Log warning for any error encountered while processing a row
	                ExtentReportBuilder.ReportTestStep("WARNING", "INFO",
	                        "Skipping row due to error: " + e.getMessage(), methodName);
	            }
	        }

	        // Step 8: If any dropdown selections were made, click the Submit button
	        if (anySelectionMade) {
	            try {
	                CM.waitUntillElementClickable(driver, objRAF.submitBtn, due);
	                CM.scrollToElement(driver, objRAF.submitBtn);
	               //CM.JSButtonClick(driver, objRAF.submitBtn);
	                ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked Submit button successfully", methodName);
	            } catch (Exception e) {
	                ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Submit button not clickable: " + e.getMessage(), methodName);
	            }
	        } else {
	            // Step 9: If no matching tech stack was found
	            ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "No matching Tech Stack found in the table", methodName);
	        }

	    } catch (TimeoutException e) {
	        // Step 10: Handle timeout on form load
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
	                "Timeout while interacting with Resource Allocation form: " + e.getMessage(), methodName);
	    } catch (Exception e) {
	        // Step 11: Handle unexpected error
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception occurred: " + e.getMessage(), methodName);
	    }
	}
}


	

	


