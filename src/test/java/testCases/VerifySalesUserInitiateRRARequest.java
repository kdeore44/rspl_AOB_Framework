package testCases;

import java.io.IOException;
import java.text.ParseException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import automationEngine.ApplicationSetup;
import businesslogic.HomeBL;
import businesslogic.OpportunityTrackerBL;
import businesslogic.ResourceAllocationBL;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import utilities.ExtentReportBuilder;

//Allure annotations
@Epic("Resource Allocation Module")
@Feature("RRA Request by Sales User")
@Owner("kalpesh.deore")
public class VerifySalesUserInitiateRRARequest extends ApplicationSetup {

    // Instantiate Business Logic classes
    HomeBL objHomeBL = new HomeBL();
    OpportunityTrackerBL objOppoTracker = new OpportunityTrackerBL();
    ResourceAllocationBL objResourceAllocation = new ResourceAllocationBL();

    //Allure report
    @Severity(SeverityLevel.CRITICAL)
    @Story("Sales User should be able to raise RRA request for a given Opportunity ID")
    @Description("This test verifies that a Sales user can initiate a Resource Request Allocation process from the Opportunity Tracker.")
    @TmsLink("TC-RRA-001")
    @Test(description = "Verify Sales user can initiate Resource Request Allocation")
    @Parameters("oppoID") 
    
    public void verifySalesUserInitiateRRARequest(String oppoID) throws IOException, ParseException, InterruptedException {

        ExtentReportBuilder.ReportInitialization("Verify Sales User Initiate RRA Request");

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        ExtentReportBuilder.ReportTestStep(methodName, "Test started: verify Sales user can initiate RRA request", "INFO");

        // Step 1: Validate Dashboard visibility
        objHomeBL.validateDashboardTitleVisibility();
        
     // Step 2: Navigate to Opportunity Tracker screen
        objHomeBL.navigateToOpportunityTrackerAndLogStatus();

        // Step 3: Search for a specific Opportunity ID
        objOppoTracker.searchOpportunityAndLogStatus(oppoID);

        // Step 4: Verify Opportunity ID exists and click "Request Resource Allocation"
        objOppoTracker.verifyOppoIDAndClickRequestAllocation(oppoID);
        

        // Step 5: Verify Techstack available and click on Submit button
        objResourceAllocation.handleResourceAllocationFormAndClickOnSubmitButton();
    }
}
