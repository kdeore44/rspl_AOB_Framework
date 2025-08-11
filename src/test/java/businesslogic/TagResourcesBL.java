package businesslogic;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import automationEngine.ApplicationSetup;
import objectRepository.HomePageOR;
import objectRepository.OpportunityTrackerOR;
import objectRepository.ResourceAllocationFormOR;
import objectRepository.TagResourcePageOR;
import utilities.CommonMethods;
import utilities.ExtentReportBuilder;

public class TagResourcesBL extends ExtentReportBuilder {
	String methodName;
	ResourceAllocationFormOR objRAF = new ResourceAllocationFormOR();
	CommonMethods CM = new CommonMethods();
	Duration due = Duration.ofSeconds(30);
	TagResourcePageOR objTR = new TagResourcePageOR();
	HomeBL objHomeBL = new HomeBL();
	HomePageOR objHomePage = new HomePageOR();
	OpportunityTrackerBL objOppoTracker = new OpportunityTrackerBL();
	OpportunityTrackerOR objOT = new OpportunityTrackerOR();
	// String resourceName;

	public void handleTagResourceAndAddResource() throws Exception {
		// Initialize method name for reporting
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = ApplicationSetup.getDriver();
		ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Started execution of handleTagResourceAndAddResource",
				methodName);

		try {
			// Step 1: Get all rows from the Tag Resources table
			List<WebElement> rows = driver.findElements(objTR.tagtableRows);
			ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Total rows found: " + rows.size(), methodName);

			// Step 2: Define expected tech stacks and initialize tracking flag
			List<String> expectedStacks = Arrays.asList("Java Full Stack", "Testing");
			boolean anyResourceAdded = false;

			// Step 3: Iterate over each row in the table
			for (int i = 0; i < rows.size(); i++) {
				try {
					// Get the current row freshly from DOM
					WebElement row = driver.findElements(objTR.tagtableRows).get(i);
					String techStackName = row.findElement(By.xpath("./td[2]")).getText().trim();
					ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Tech stack found: " + techStackName,
							methodName);

					// Step 4: Check if tech stack matches expected list
					if (expectedStacks.contains(techStackName)) {
						// Click the Add icon for matching tech stack
						WebElement addIcon = row.findElement(By.xpath(".//span[text()='add']"));
						CM.JSButtonClick(driver, addIcon);
						ExtentReportBuilder.ReportTestStep("PASSED", "PASS",
								"Clicked on Add icon for: " + techStackName, methodName);

						// Optional wait for modal to load
						Thread.sleep(2000);

						// Step 5: Choose resource name based on tech stack
						String resourceName = "";
						if (techStackName.equals("Java Full Stack")) {
							resourceName = "hiten";
						} else if (techStackName.equals("Testing")) {
							resourceName = "apurva";
						}

						// Step 6: Select resource from dropdown and confirm
						selectResourceAndConfirm(driver, resourceName);
						ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Resource added for: " + techStackName,
								methodName);

						anyResourceAdded = true;
					}

				} catch (Exception e) {
					// Step 7: Log row-specific failures but continue processing other rows
					ExtentReportBuilder.ReportTestStep("WARNING", "INFO",
							"Skipping row due to error: " + e.getMessage(), methodName);
				}
			}

			// Step 8: Click Submit button if at least one resource was added
			if (anyResourceAdded) {
				System.out.println("Waited till button clickable");
				CM.waitUntillElementClickable(driver,
						By.xpath("(//button[@type='submit'][contains(text(),'Submit')])[1]"), due);
				System.out.println("Clicked on submit button");
				CM.JSButtonClick(driver, By.xpath("(//button[@type='submit'][contains(text(),'Submit')])[1]"));

				ExtentReportBuilder.ReportTestStep("PASSED", "PASS",
						"Clicked on Submit button after processing all resources", methodName);
			} else {
				ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
						"No matching Tech Stack found. Skipping resource addition.", methodName);
				Assert.fail("No matching Tech Stack found. Resource was not added.");
			}

		} catch (TimeoutException e) {
			// Step 9: Handle timeout failure
			ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
					"Timeout during Tag Resource handling: " + e.getMessage(), methodName);
			Assert.fail("Timeout during Tag Resource handling: " + e.getMessage());
		} catch (Exception e) {
			// Step 10: Handle unexpected exception
			ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
					"Exception in handleTagResourceAndAddResource: " + e.getMessage(), methodName);
			Assert.fail("Exception in handleTagResourceAndAddResource: " + e.getMessage());
		}
	}

	public void selectResourceAndConfirm(WebDriver driver, String resrcName) throws Exception {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Started execution of selectResourceAndConfirm", methodName);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			CM.setText(driver, objTR.tagSearchField, resrcName);
			ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Entered resource name: " + resrcName, methodName);

			WebElement suggestion = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'" + resrcName + "')]")));
			suggestion.click();
			ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Selected suggestion: " + resrcName, methodName);

			CM.setText(driver, objTR.tagresourcehoursfield, "2");
			ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Entered allocation hours: 2", methodName);

			CM.JSButtonClick(driver, objTR.confirmbtn);
			ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked on Confirm button", methodName);

		} catch (Exception e) {
			ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
					"Exception in selectResourceAndConfirm: " + e.getMessage(), methodName);
			throw e;
		}
	}


	public void addReasonforTaggedResource(String reason) throws Exception {
	    String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
	    WebDriver driver = ApplicationSetup.getDriver();
	    ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Started execution of addReasonforTaggedResource", methodName);

	    try {
	        // 🔍 Zoom out the screen
	       // JavascriptExecutor js = (JavascriptExecutor) driver;
	        //js.executeScript("document.body.style.zoom='80%'");
	        //ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Zoom set to 80%", methodName);
	        //Thread.sleep(2000);

	        boolean isReasonAdded = false;

	        // ✅ Expected tech stacks
	        List<String> expectedStacks = Arrays.asList("Logistic Domain", "Java Full Stack", "Testing");

	        // 📋 Get all rows
	        List<WebElement> rows = driver.findElements(objTR.rowsAddReason);
	        ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Total rows found: " + rows.size(), methodName);

	        for (WebElement row : rows) {
	            String techStackName = row.findElement(By.xpath("./td[2]")).getText().trim();
	            ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Tech stack found: " + techStackName, methodName);

	            if (!expectedStacks.contains(techStackName)) {
	                ExtentReportBuilder.ReportTestStep("SKIPPED", "SKIP", "Tech stack not in expected list: " + techStackName, methodName);
	                continue;
	            }

	            try {
	                // 🎯 Click Add Reason icon
	                WebElement addReasonIcon = row.findElement(By.xpath(".//button[@mattooltip='Untag']/preceding-sibling::button[1]"));
	                CM.scrollToElement(driver, By.xpath("(//tbody[(@role='rowgroup')])[4]//tr[1]//button[@mattooltip='Untag']"));
	                ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Scrolled to Add Reason icon for: " + techStackName, methodName);
	                Thread.sleep(3000);
	                
	                ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Waited for visibility of Add Reason icon for: " + techStackName, methodName);

	                try {
	                    
	                	addReasonIcon.click();
	                    ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked on Add Reason icon for: " + techStackName, methodName);
	                } catch (Exception e) {
	                    ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Failed to click on Add Reason icon for: " + techStackName + ". Exception: " + e.getMessage(), methodName);
	                    Assert.fail("Failed to click on Add Reason icon for: " + techStackName + ". Exception: " + e.getMessage());
	                    continue;
	                }

	                // 📌 Select reason
	                CM.waitUntillVisibilityOfElement(driver, objTR.leaveaReasonLabel, due);
	                CM.click(driver, objTR.selectReasonDropdown);
	                Thread.sleep(1000);

	                try {
	                    CM.click(driver, By.xpath("//span[normalize-space(text())='" + reason + "']"));
	                    ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Reason selected: " + reason, methodName);
	                } catch (Exception e) {
	                    ExtentReportBuilder.ReportTestStep("SKIPPED", "SKIP", "Reason not found: " + reason + " for tech stack: " + techStackName, methodName);
	 	                }

	                // ✅ Add Reason
	                CM.waitUntillElementClickable(driver, objTR.addReasonbtnOnReasonSection, due);
	                CM.click(driver, objTR.addReasonbtnOnReasonSection);
	                ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked Add Reason button", methodName);
	                Thread.sleep(1000);

	                // 🧾 Verify success
	                String successMsg = driver.findElement(objTR.SuccessMsgAddReson).getText().trim();
	                Assert.assertEquals(successMsg, "Reason Added Successfully");
	                ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Success Message Verified: " + successMsg, methodName);

	                // ❌ Close popup
	                CM.JSButtonClick(driver, objTR.closeIconSuccessReasonPopUp);
	                ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Closed success popup", methodName);
	                Thread.sleep(2000);

	                isReasonAdded = true;

	            } catch (Exception e) {
	                ExtentReportBuilder.ReportTestStep("SKIPPED", "SKIP", "Skipping row due to error: " + e.getMessage(), methodName);
	            }
	        }

	        // 📤 Final Submit
	        if (isReasonAdded) {
	            CM.waitUntillElementClickable(driver, By.xpath("(//button[@type='submit'][contains(text(),'Submit')])[2]"), due);
	            CM.JSButtonClick(driver, By.xpath("(//button[@type='submit'][contains(text(),'Submit')])[2]"));
	            ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Clicked Submit after processing all valid reasons", methodName);
	        } else {
	            ExtentReportBuilder.ReportTestStep("SKIPPED", "SKIP", "No valid tech stack and reason found. Submit skipped.", methodName);
	        }

	    } catch (Exception e) {
	        ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Exception occurred: " + e.getMessage(), methodName);
	        Assert.fail("Exception occurred in addReasonforTaggedResource: " + e.getMessage());
	        throw e;
	    }
	}

	
	public void verifyReasonAppearOnDashBoardScreen(String oppoID) throws Exception {
		// Get the current method name for logging
		String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		 WebDriver driver = ApplicationSetup.getDriver();
		ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Started Execution to Verify Reason Appear on DashBoard",
				methodName);

		try {
			// Step 1: Click on Dashboard menu
			CM.JSButtonClick(driver, objHomePage.dashBoardSideMenu);
			ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Clicked on Dashboard Side Menu", methodName);

			// Step 2: Verify Dashboard title is visible
			objHomeBL.validateDashboardTitleVisibility();
			ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "DashBoard is Visible", methodName);

			// Step 3: Wait for the Pending Comment Reason Added Count to appear
			CM.waitUntillVisibilityOfElement(driver, objHomePage.pendingCommentReasonAddedCount, due);
			ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Pending Comment Reason count visible", methodName);

			// Step 4: Click on the Pending Comment Reason count
			CM.JSButtonClick(driver, objHomePage.pendingCommentReasonAddedCount);
			ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Clicked on Pending Comment Reason Count", methodName);

			try {
				// Step 5: Scroll to Opportunity Table
				CM.scrollToElement(driver, objOT.allOppoTable);
				ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Scrolled to Opportunity Table", methodName);

				// Step 6: Wait for Opportunity ID to be visible in search result
				CM.waitForSearchResult(driver, oppoID, 15);
				ExtentReportBuilder.ReportTestStep("INFO", "INFO", "Waited for Opportunity ID: " + oppoID, methodName);

				// Step 7: Find the cell with Opportunity ID and verify visibility
				WebElement resultCell = driver.findElement(By.xpath("//td[contains(text(),'" + oppoID + "')]"));

				if (resultCell.isDisplayed() && resultCell.getText().equalsIgnoreCase(oppoID)) {
					ExtentReportBuilder.ReportTestStep("PASSED", "PASS", "Opportunity ID '" + oppoID + "' found",
							methodName);
				} else {
					ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
							"Opportunity ID '" + oppoID + "' not found (invisible)", methodName);
					Assert.fail("Opportunity ID '" + oppoID + "' is not visible in dashboard table.");
					return;
				}

			} catch (Exception e) {
				// Handle exception while verifying Opportunity ID
				ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
						"Error verifying Opportunity ID: " + e.getMessage(), methodName);
				Assert.fail("Exception while verifying Opportunity ID: " + e.getMessage());
			}

		} catch (Exception e) {
			// Handle any failure in the dashboard flow
			ExtentReportBuilder.ReportTestStep("FAILED", "FAIL", "Error in dashboard flow: " + e.getMessage(),
					methodName);
			Assert.fail("Exception in dashboard flow: " + e.getMessage());
			throw e;
		}
	}

}
