package testCases;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import automationEngine.ApplicationSetup;
import businesslogic.HomeBL;
import businesslogic.OpportunityTrackerBL;
import businesslogic.TagResourcesBL;
import utilities.ExtentReportBuilder;
import utilities.RWExcel;

public class VerifySearchFunctionalityWithLowerCaseInput extends ApplicationSetup{
	
	HomeBL objHomeBL = new HomeBL();
    OpportunityTrackerBL objOppoTracker = new OpportunityTrackerBL();
    TagResourcesBL objTagResource = new TagResourcesBL();
    
    RWExcel excelRead= new RWExcel();
    
    
    @Test(description = "Verify Search Functionality With LowerCase Input")
    @Parameters({"oppoID"})
    public void verifysearchfunctionalitywithlowercaseinput(String oppoID)
            throws Exception {
    	String expectedOppoID= excelRead.readCell("testdata", "Expected Oppo ID");
    	System.out.println("Expected Oppo ID from Excel file"+" : " +expectedOppoID);

        ExtentReportBuilder.ReportInitialization("Verify User can Add Reason for tagged resource");
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        ExtentReportBuilder.ReportTestStep(methodName, "Test started: Verify Search Functionality With LowerCase Input", "INFO");

        // Step 1:  Validate dashboard visibility
        objHomeBL.validateDashboardTitleVisibility();
        
        //Step 2: Navigate to Opportunity Tracker screen
        objHomeBL.navigateToOpportunityTrackerAndLogStatus();

        // Step 3: Search for Opportunity
        objOppoTracker.searchOpportunityAndLogStatus(oppoID);
        
        //Step 4: Verify opportunity name with search data
      
        objOppoTracker.verifyOppoIDInAllOpportunitySection(expectedOppoID);
        

    }
	

}
