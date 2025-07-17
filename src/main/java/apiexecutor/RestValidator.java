package apiexecutor;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import utilities.ExtentReportBuilder;

public class RestValidator {
	
	private static final Logger logger = LogManager.getLogger(RestValidator.class);
	private RestResponse response;
	
	public static String responseBody ;
	RestValidator(RestResponse response) {
		this.response = response;
	}

	public RestValidator expectCode(int expectedCode, String StepsDetail) throws IOException, ParseException{
		SoftAssert sa= new SoftAssert();

		int i=response.getResponseCode();
		if(expectedCode==i){
			ExtentReportBuilder.ReportTestStep(StepsDetail+" " +expectedCode, "Pass","Expected Response :"+expectedCode + ": Actual Values "+ i);
			sa.assertEquals(expectedCode, response.getResponseCode());
		}
		else{
			ExtentReportBuilder.ReportTestStep(StepsDetail + " "+ expectedCode , "Fail","Expected Response :"+expectedCode + ": Actual Values "+ i);
		
		
		}
		return this;
	}

	public RestValidator expectMessage(String message) throws IOException, ParseException {
	
		String i=response.getResponseMessage();
		 logger.info(i);
		if(message.equalsIgnoreCase(i)){
			ExtentReportBuilder.ReportTestStep("Response Message should be " +message, "Pass","Expected Response :"+message + ": Actual Values "+ i);
			Assert.assertEquals(message, response.getResponseMessage());
		}
		else{
			
			ExtentReportBuilder.ReportTestStep("Response message should be " + message , "Fail","Expected Response :"+message + ": Actual Values "+ i);
			ExtentReportBuilder.CompleteTest();
		Assert.assertEquals(message, response.getResponseMessage());
		}
		
		return this;
	}

	public RestValidator expectHeader(String headerName, String headerValue) throws IOException, ParseException {
		
		String i=response.getHeader(headerName);
		if(headerValue.equalsIgnoreCase(i)){
			ExtentReportBuilder.ReportTestStep("Header should be " +headerName +" is "+ headerValue, "Pass",i.trim());
			
			Assert.assertEquals(headerValue, response.getHeader(headerName));
		}
		else{
			ExtentReportBuilder.ReportTestStep("Header should be " + headerValue, "Fail",i);
			ExtentReportBuilder.CompleteTest();
		Assert.assertEquals(headerValue, response.getHeader(headerName));
		}

		return this;
	}
	

	public RestValidator expectHeaders(Map<String, String> headers) {
		Set<String> keys = headers.keySet();
		for (String key : keys) {
			Assert.assertEquals(headers.get(key), response.getHeader(key));
		}
		return this;
	}

	public RestValidator expectInBody(String ResponseSuccessexpectedData , String StepDefination) throws IOException, ParseException {
		
	String Actualresponse=response.getResponseBody();
	 logger.info(Actualresponse);
	if(Actualresponse.trim().contains(ResponseSuccessexpectedData.trim())){
		ExtentReportBuilder.ReportTestStep(StepDefination+" "+ ResponseSuccessexpectedData, "Pass",Actualresponse);
				
		}
	else{
		ExtentReportBuilder.ReportTestStep(StepDefination+" not correct "+ ResponseSuccessexpectedData, "Fail",Actualresponse);
				
		}
		
		return this;
	}
	
	public String get_ResponsBodyMessage(){
		
		responseBody=response.getResponseBody();
		
		return responseBody;
	}
	
public String get_ResponsMessage(){
		String responseMessage;
		responseMessage=response.getResponseMessage();
		
		return responseMessage;
	}

public int get_ResponsCode(){
	int responseMessage;
	responseMessage=response.getResponseCode();
	
	return responseMessage;
}

public String get_IdfromResponseBody() throws JSONException{
	
	responseBody=response.getResponseBody();
	JSONObject jsonObj = new JSONObject(responseBody);
	
	return jsonObj.getString("id");
}

}
