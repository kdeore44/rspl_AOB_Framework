package testCases;


import java.util.HashMap;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;

import apiexecutor.RestExecutor;
import apiexecutor.RestValidator;
import automationEngine.ApplicationSetup;
import businesslogic.LoginBL;
import utilities.CommonAPIUtility;
import utilities.CommonUtilities;
import utilities.ConstantVariables;
import utilities.ExtentReportBuilder;
import utilities.JsonReader;

public class APITC extends ApplicationSetup{

	LoginBL objLoginBL = new LoginBL();
	CommonUtilities objCU = new CommonUtilities();
	Object jsonObj = new Object();
	JsonReader jsR = new JsonReader();
	RestValidator response;
	String createdId = null;
	

	@Test(groups = { "Smoke", "Regression"},priority=1)
	@Parameters({"Browser"})
	public void GetObjectsTC() throws Exception {
		ExtentReportBuilder.ReportInitialization("Getting Objects from API");
		try {
			// Assigning the URL value and creating the Rest executor
		String URL = objCU.readPropertyFileEnvProperty(ConstantVariables.API_URL);
		RestExecutor restExecutor = new RestExecutor(URL);
		
		// Executing the Get request
		response = restExecutor.GetHTTP_GETResponse(URL, ConstantVariables.TOKEN, RestExecutor.CONTENT_TYPE_JSON);
		int expResCode = ConstantVariables.STATUS_200;
		
		// Adding Test step into Report
		ExtentReportBuilder.ReportTestStep("URL Details :  ",  ConstantVariables.STATUS_PASS,URL);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,response.get_ResponsBodyMessage());
		
		// Verifying the response code with expected
		response.expectCode(expResCode, "Getting the Objects by sending request body details   "+response);
		
		ExtentReportBuilder.CompleteTest();
	}catch(Exception e)
	{
		Thread thread = new Thread();
	    thread.interrupt();
		log.info(e.getMessage());
		ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"ERROR", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
	}
	}
	
	
	@Test(groups = { "Smoke", "Regression" },priority=2)
	@Parameters({"Browser"})
	public void PostObjectsTC() throws Exception {
		ExtentReportBuilder.ReportInitialization("Creating Objects from API");
		try {
		// Setting up the URL	
		String URL = objCU.readPropertyFileEnvProperty(ConstantVariables.API_URL);
		
		String request = null;
		JsonNode requestList = jsR.readTestDataForJsonKey("System_Details");
		objCU.printToConsole("Fetched Request  ::   "+requestList);
		request = requestList.toString();
		request = CommonAPIUtility.convertStringtoFormattedJson(request);
		objCU.printToConsole(" Request Data:    "+request);
		
		RestExecutor restExecutor = new RestExecutor(URL);
		response = restExecutor.GetHTTP_PostTokenResponse(URL, request, RestExecutor.CONTENT_TYPE_JSON);
		int expResCode = ConstantVariables.STATUS_200;
		ExtentReportBuilder.ReportTestStep("URL Details :  ",  ConstantVariables.STATUS_PASS,URL);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,request);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,response.get_ResponsBodyMessage());		
		
		response.expectCode(expResCode, "Getting the Objects by sending request body details   "+response);
		 
		
		ExtentReportBuilder.CompleteTest();
	}catch(Exception e)
	{
		Thread thread = new Thread();
	    thread.interrupt();
		log.info(e.getMessage());
		ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"ERROR", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
	}
	}
	

	@Test(groups = { "Smoke", "Regression" },priority=3)
	@Parameters({"Browser"})
	public void PutObjectsTC() throws Exception {
		ExtentReportBuilder.ReportInitialization("Updating Objects from API with PUT");
		try {
		// Setting up the URL
			
		createdId = response.get_IdfromResponseBody();
			
		String URL = objCU.readPropertyFileEnvProperty(ConstantVariables.API_URL)+"/"+createdId;
		System.out.println("Update URL :" + URL);
		String request = null;
		JsonNode requestList = jsR.readTestDataForJsonKey("Update_System_Details");
		objCU.printToConsole("Fetched Request  ::   "+requestList);
		request = requestList.toString();
		request = CommonAPIUtility.convertStringtoFormattedJson(request);
		objCU.printToConsole(" Request Data:    "+request);
				
		
		RestExecutor restExecutor = new RestExecutor(URL);
		RestValidator response = restExecutor.GetHTTP_PutResponse(URL, null, null,request, RestExecutor.CONTENT_TYPE_JSON);
		int expResCode = ConstantVariables.STATUS_200;
		ExtentReportBuilder.ReportTestStep("URL Details :  ",  ConstantVariables.STATUS_PASS,URL);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,request);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,response.get_ResponsBodyMessage());		
		
		response.expectCode(expResCode, "Updating the Objects by sending request body details   "+response);
		
		ExtentReportBuilder.CompleteTest();
	}catch(Exception e)
	{
		Thread thread = new Thread();
	    thread.interrupt();
		log.info(e.getMessage());
		ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"ERROR", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
	}
	}
	
	
	
	@Test(groups = { "Smoke", "Regression" },priority=4)
	@Parameters({"Browser"})
	public void PatchObjectsTC() throws Exception {
		ExtentReportBuilder.ReportInitialization("Updating Objects from API with PATCH");
		try {
		// Setting up the URL
			
		createdId = response.get_IdfromResponseBody();
			
		String URL = objCU.readPropertyFileEnvProperty(ConstantVariables.API_URL)+"/"+createdId;
		System.out.println("Update URL :" + URL);
		String request = null;
		JsonNode requestList = jsR.readTestDataForJsonKey("Update_System_Details");
		objCU.printToConsole("Fetched Request  ::   "+requestList);
		request = requestList.toString();
		request = CommonAPIUtility.convertStringtoFormattedJson(request);
		objCU.printToConsole(" Request Data:    "+request);			
		
		RestExecutor restExecutor = new RestExecutor(URL);
		RestValidator response = restExecutor.GetHTTP_PatchResponse(URL, null, request, RestExecutor.CONTENT_TYPE_JSON);
		int expResCode = ConstantVariables.STATUS_200;
		
		ExtentReportBuilder.ReportTestStep("URL Details :  ",  ConstantVariables.STATUS_PASS,URL);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,request);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,response.get_ResponsBodyMessage());		
		
		response.expectCode(expResCode, "Updating the Objects by sending request body details with Patch request   "+response);
		
		ExtentReportBuilder.CompleteTest();
	}catch(Exception e)
	{
		Thread thread = new Thread();
	    thread.interrupt();
		log.info(e.getMessage());
		ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"ERROR", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
	}
	}
	
	
	@Test(groups = { "Smoke", "Regression" },priority=5)
	@Parameters({"Browser"})
	public void DeleteObjectsTC() throws Exception {
		ExtentReportBuilder.ReportInitialization("Deleting Objects using API");
		try {
		// Setting up the URL	
		String URL = objCU.readPropertyFileEnvProperty(ConstantVariables.API_URL)+"/"+createdId;
		RestExecutor restExecutor = new RestExecutor(URL);
		
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(RestExecutor.CONTENT_TYPE, RestExecutor.CONTENT_TYPE_JSON);
		
		RestValidator response = restExecutor.GetHTTP_DeleteWithHeadersResponse(URL,null, headers);
		int expResCode = ConstantVariables.STATUS_200;
		ExtentReportBuilder.ReportTestStep("URL Details :  ",  ConstantVariables.STATUS_PASS,URL);
		ExtentReportBuilder.ReportTestStep("Response Details :  ",  ConstantVariables.STATUS_PASS,response.get_ResponsBodyMessage());		
		
		response.expectCode(expResCode, "Updating the Objects by sending request body details   "+response);
		
		ExtentReportBuilder.CompleteTest();
	}catch(Exception e)
	{
		Thread thread = new Thread();
	    thread.interrupt();
		log.info(e.getMessage());
		ExtentReportBuilder.ReportTestStep("Error in " + Thread.currentThread().getStackTrace()[2].getMethodName(),
				"ERROR", objCU.generateRandomNumericString());
		ExtentReportBuilder.CompleteTest();
	}
	}
}
