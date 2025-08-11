package testCases;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import automationEngine.ApplicationSetup;
import businesslogic.HomeBL;
import businesslogic.NewResourceRequisitionRequestBL;
import businesslogic.OpportunityTrackerBL;
import utilities.ExtentReportBuilder;

public class AddNewResourceRequisition extends ApplicationSetup {

    HomeBL objHomeBL = new HomeBL();
    OpportunityTrackerBL objOppoTracker = new OpportunityTrackerBL();
    NewResourceRequisitionRequestBL objNewResourceRequest = new NewResourceRequisitionRequestBL();

    @Test(description = "Verify that HOD user can add New Resource")
    @Parameters({"oppoID", "yearOfExp", "noOfPositions", "resourceType", "departmentName"})
    public void addNewResourceRequisition(String oppoID, String yearOfExp, String noOfPositions,
            String resourceType, String departmentName) throws Exception {

        ExtentReportBuilder.ReportInitialization("Add New Resource Requisition");

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        ExtentReportBuilder.ReportTestStep(methodName, "Test started: Verify HOD user can tag resource", "INFO");

        // Step 1: Validate dashboard visibility
        objHomeBL.validateDashboardTitleVisibility();

        // Step 1.1: Navigate to Opportunity Tracker screen
        objHomeBL.navigateToOpportunityTrackerAndLogStatus();

        // Step 2: Search for the given Opportunity ID
        objOppoTracker.searchOpportunityAndLogStatus(oppoID);

        // Step 3: Click on the 'Tag Resources' button for given Opportunity
        objOppoTracker.clickTagResourcesButtonForOppoID(oppoID);
        
        // Step 4: Fill out the requisition form and submit it
        objNewResourceRequest.fillTheFormAndSubmit(yearOfExp, noOfPositions, resourceType, departmentName);
    }
}
