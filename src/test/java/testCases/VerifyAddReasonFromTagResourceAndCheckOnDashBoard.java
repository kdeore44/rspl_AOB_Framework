package testCases;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import automationEngine.ApplicationSetup;
import businesslogic.HomeBL;
import businesslogic.OpportunityTrackerBL;
import businesslogic.TagResourcesBL;
import utilities.ExtentReportBuilder;

public class VerifyAddReasonFromTagResourceAndCheckOnDashBoard extends ApplicationSetup {

    HomeBL objHomeBL = new HomeBL();
    OpportunityTrackerBL objOppoTracker = new OpportunityTrackerBL();
    TagResourcesBL objTagResource = new TagResourcesBL();

    @Test(description = "Verify User can Add Reason for tagged resource")
    @Parameters({"oppoID", "reason"})
    public void verifyUserCanAddReasonforTaggedResourceAndValidateitisVisibleOnDashBoard(String oppoID, String reason)
            throws Exception {

        ExtentReportBuilder.ReportInitialization("Verify User can Add Reason for tagged resource");
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        ExtentReportBuilder.ReportTestStep(methodName, "Test started: Verify User can Add Reason for tagged resource", "INFO");

        // Step 1:  Validate dashboard visibility
        objHomeBL.validateDashboardTitleVisibility(driver);
        
        //Step 2: Navigate to Opportunity Tracker screen
        objHomeBL.navigateToOpportunityTrackerAndLogStatus(driver);

        // Step 3: Search for Opportunity
        objOppoTracker.searchOpportunityAndLogStatus(driver, oppoID);

        // Step 4: Click on Tag Resource Allocation
        objOppoTracker.clickTaggedButtonInResourcesColumnAndClickOnEditResource(driver, oppoID);

        // Step 5: Add Reason for corresponding Tech
        objTagResource.addReasonforTaggedResource(driver, reason);

        // Step 6: Validate Comment/Reason on Pending Action section
        objTagResource.verifyReasonAppearOnDashBoardScreen(driver, oppoID);
    }
}
