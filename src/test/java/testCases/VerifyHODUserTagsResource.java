package testCases;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import automationEngine.ApplicationSetup;
import businesslogic.HomeBL;
import businesslogic.OpportunityTrackerBL;
import businesslogic.TagResourcesBL;
import utilities.ExtentReportBuilder;

public class VerifyHODUserTagsResource extends ApplicationSetup {

    HomeBL objHomeBL = new HomeBL();
    OpportunityTrackerBL objOppoTracker = new OpportunityTrackerBL();
    TagResourcesBL objTagResource = new TagResourcesBL();

    @Test(description = "Verify that HOD user can tag a resource for a given opportunity")
    @Parameters({"oppoID"})
    public void verifyHODUserCanTagResource(String oppoID) throws Exception {

        ExtentReportBuilder.ReportInitialization("Verify HOD User Tags Resource");

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        ExtentReportBuilder.ReportTestStep(methodName, "Test started: Verify HOD user can tag resource", "INFO");

        // Step 1: Validate dashboard visibility
        
        objHomeBL.validateDashboardTitleVisibility();
        System.out.println("Adding line to verify the jenkin flow");

        // Step 2: Navigate to Opportunity Tracker screen
        objHomeBL.navigateToOpportunityTrackerAndLogStatus();

        // Step 3: Search for the given Opportunity ID
        objOppoTracker.searchOpportunityAndLogStatus(oppoID);

        // Step 4: Click on the 'Tag Resources' button for given Opportunity
        objOppoTracker.clickTagResourcesButtonForOppoID(oppoID);

        // Step 5: Tag the resource on the Tag Resource screen
        objTagResource.handleTagResourceAndAddResource();
    }
}
