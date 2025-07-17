package utilities;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import apiexecutor.RestExecutor;
import apiexecutor.RestValidator;

import com.amazonaws.services.apigateway.model.RequestValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;

public class CommonAPIUtility {
	
	private static final Logger logger = LogManager.getLogger(CommonAPIUtility.class);
	public static RestExecutor executor;
	public static String Access_Token;
	public static String accountID;
	static String Request;
	static String Respons ;
	static String Message;
	static String actualResponse;
	static String responseCode;
	static String errorCode;
	static String value ;
	static String actValue=null;
	static String AddAcountRequestresponse;
	static String createOrerResponse;
	static String expectedResponse;
	static RequestValidator resposne;
	public static String filePath;

	private CommonAPIUtility() {
	      //not called
	   }
	
	// This is use to Parse String response in Json FOrmat
	public static JsonObject convert_stringResponseInJson(String ResponsInString) {
		JsonObject jobj;
		try {
			JsonParser parse = new JsonParser();
			jobj = (JsonObject) parse.parse(ResponsInString);
		} catch (Exception e) {
			jobj = null;
		}
		return jobj;
	}

	// This is use to Parse String response in Json FOrmat
	public static String valuesFromJASONResponse(String ResponsInString, String Key) {
		try {
			JsonParser parse = new JsonParser();
			JsonObject obj = (JsonObject) parse.parse(ResponsInString);
			value = obj.get(Key).getAsString();
		} catch (Exception e) {
			value = null;
		}
		return value;
	}

	// This is use to Parse String response in Json FOrmat
	public static List<String> valuesFrameFromRequestResponse(String request) {
		LinkedList<String> li = null;
		try {
			JsonObject requestObj = CommonAPIUtility.convert_stringResponseInJson(request);
			if(requestObj !=null) {
			JsonArray frameObj = requestObj.getAsJsonArray("Targeting");
			for (JsonElement element : frameObj) {
				JsonObject requestObj1 = element.getAsJsonObject();
				String value = requestObj1.get("Target").getAsString();
				if (value.equalsIgnoreCase("FrameList")) {

					JsonArray frameObj2 = requestObj1.getAsJsonArray("TargetValues");

					for (int i = 0; i < frameObj2.size(); i++) {
						String value2 = frameObj2.get(i).getAsString();
						li.add(value2);

					}
				}
			}
			}
			return li;
		} catch (Exception e) {
			return li;
		}

	}

	// this method used to get Key values form Jason Response and request
	public static String get_KeyVlauefromJason(String jsonData, String key) {

		JsonObject jobj;
		try {
			jobj = CommonAPIUtility.convert_stringResponseInJson(jsonData);
			if(jobj!=null) {
			value= jobj.get(key).getAsString();}
			else {
				value=null;
			}
		}catch(Exception e) {
			value=null;
		}
		return value;
	}

	// this method used to get Account IOD from Excel file
	public static String add_KeyInJasonResponse(String jason, String Key, String value) {

		JsonObject jobj = CommonAPIUtility.convert_stringResponseInJson(jason);
		if(jobj!=null) {
			jobj.addProperty(Key, value);
			value= jobj.toString();
		}else {
			value=null;
		}
		
		return  value ;
	}

	/*
	 * Get Campaign Status From Create Campaign Response for API 2.0
	 * 
	 **
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 * 
	 * @Purpose: This function is used to Get Campaign Code From Create Campaign
	 * Response for API 2.0
	 */
	public static String ver2GetCampaignODDFromCreateCampaign(String value) {
		String odd = null ;
		try {
			JsonObject responseObj = CommonAPIUtility.convert_stringResponseInJson(value);
			if(responseObj!=null) {
			odd = responseObj.getAsJsonObject("campaignHeader").get("optionExpiry").toString().replaceAll("\"", "");
			}else {
				odd=null;
			}
		} catch (Exception e) {
			odd=null;
		}
		return odd;
	}
	
	/*
	 *
	 * @param jsonString
	 * @param key
	 * @param value
	 * request data in request response. Note : Note pass Json element in argument
	 */
	public static boolean compareJson(JsonElement json1, JsonElement json2) throws Throwable {

		boolean isEqual = true;

		// Check whether both jsonElement are not null
		if (json1 != null && json2 != null) {

			// Check whether both jsonElement are objects
			if (json1.isJsonObject() && json2.isJsonObject()) {
				Set<Entry<String, JsonElement>> ens1 = ((JsonObject) json1).entrySet();
				Set<Entry<String, JsonElement>> ens2 = ((JsonObject) json2).entrySet();
				JsonObject json2obj = (JsonObject) json2;
				if (ens1 != null && ens2 != null) {
					// (ens2.size() == ens1.size())
					// Iterate JSON Elements with Key values
					for (Entry<String, JsonElement> en : ens1) {

						value = en.getKey();
						isEqual = compareJson(en.getValue(), json2obj.get(en.getKey()));
						
					}
				} else {
					ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
					return false;
				}
			}

			// Check whether both jsonElement are arrays
			else if (json1.isJsonArray() && json2.isJsonArray()) {
				JsonArray jarr1 = json1.getAsJsonArray();
				JsonArray jarr2 = json2.getAsJsonArray();
				if (jarr1.size() != jarr2.size()) {
					ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
					return false;
				} else {
					int i = 0;
					// Iterate JSON Array to JSON Elements
					for (JsonElement je : jarr1) {
						isEqual = isEqual && compareJson(je, jarr2.get(i));
						i++;
					}
				}
			}
			// Check whether both jsonElement are null
			else if (json1.isJsonNull() && json2.isJsonNull()) {
				return true;
			}

			// Check whether both jsonElement are primitives
			else if (json1.isJsonPrimitive() && json2.isJsonPrimitive()) {
				String value1 = null;
				String value2 = null;
				if (json1.equals(json2)) {
					if (json1.getAsString().length() > 20) {
						value1 = json1.getAsString().substring(0, 19);
					} else {
						value1 = json1.getAsString();
					}
					if (json2.getAsString().length() > 20) {
						value2 = json2.getAsString().substring(0, 19);
					} else {
						value2 = json2.getAsString();
					}
					ExtentReportBuilder.ReportTestStep(value + " = " + value1 + " should be in response ", "Pass",
							value2.trim());
					return true;
				} else {
					ExtentReportBuilder.ReportTestStep(value + " = " + json1 + " should be in response  ", "Fail",
							value.trim());
					return false;
				}
			} else {
				return false;
			}
		} else if (json1 == null && json2 == null) {
			ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
			return true;
		} else {
			ExtentReportBuilder.ReportTestStep("Getting null value in Json response ", "Fail", "NoResponse");
			return false;
		}
		return isEqual;
	}
	
	/*
	 * Purpose: Common method to verify Verify
	 * request data in request response. Note : Note pass Json element in argument
	 */
	public static boolean compareAvailJson(JsonElement json1, JsonElement json2) throws Throwable {

		boolean isEqual = true;

		// Check whether both jsonElement are not null
		if (json1 != null && json2 != null) {

			// Check whether both jsonElement are objects
			if (json1.isJsonObject() && json2.isJsonObject()) {
				Set<Entry<String, JsonElement>> ens1 = ((JsonObject) json1).entrySet();
				Set<Entry<String, JsonElement>> ens2 = ((JsonObject) json2).entrySet();
				JsonObject json2obj = (JsonObject) json2;
				if (ens1 != null && ens2 != null) {
					// (ens2.size() == ens1.size())
					// Iterate JSON Elements with Key values
					for (Entry<String, JsonElement> en : ens1) {
						value = en.getKey();						
						isEqual = compareAvailJson(en.getValue(), json2obj.get(en.getKey()));
					}
				} else {
					ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
					return false;
				}
			}

			// Check whether both jsonElement are arrays
			else if (json1.isJsonArray() && json2.isJsonArray()) {
				JsonArray jarr1 = json1.getAsJsonArray();
				JsonArray jarr2 = json2.getAsJsonArray();
				if (jarr1.size() != jarr2.size()) {
					ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
					return false;
				} else {
					int i = 0;
					// Iterate JSON Array to JSON Elements
					for (JsonElement je : jarr1) {
						isEqual = isEqual && compareAvailJson(je, jarr2.get(i));
						i++;
					}
				}
			}
			// Check whether both jsonElement are null
			else if (json1.isJsonNull() && json2.isJsonNull()) {
				return true;
			}

			// Check whether both jsonElement are primitives
			else if (json1.isJsonPrimitive() && json2.isJsonPrimitive()) {
				String value1 = null;
				String value2 = null;
				if(value.equalsIgnoreCase("Availability") || value.equalsIgnoreCase("Price")) {
					if(json1.getAsString().equalsIgnoreCase("Available")||json1.getAsString().equalsIgnoreCase("Invalid")) {
						value1=json1.getAsString();
						value2=json2.getAsString();
						if (value1.equalsIgnoreCase(value2)) {
						ExtentReportBuilder.ReportTestStep(value + " = " + json1.getAsString() + " should be in response ", "Pass",
								json2.getAsString().trim());
						}
						else {
							ExtentReportBuilder.ReportTestStep(value + " = " + json1.getAsString() + " should be in response ", "Fail",
									json2.getAsString().trim());
						}
					}else {
											
					double p1 = Double.parseDouble(json1.getAsString());
					p1 = Math.round(p1);
					double p2 = Double.parseDouble(json2.getAsString());
					p2 = Math.round(p2);
					if (p1 == p2 || p1 + 1 == p2 || p1 - 1 == p2) {
						ExtentReportBuilder.ReportTestStep(value + " = " + json1.getAsString() + " should be in response ", "Pass",
								json2.getAsString().trim());
						return true;
					} else {
						ExtentReportBuilder.ReportTestStep(value + " = " + json1 + " should be in response  ", "Fail",
								value.trim());
						return false;
					}
					}
				} else {
					if (json1.equals(json2)) {
						if (json1.getAsString().length() > 20) {
							value1 = json1.getAsString().substring(0, 19);
						} else {
							value1 = json1.getAsString();
						}
						if (json2.getAsString().length() > 20) {
							value2 = json2.getAsString().substring(0, 19);
						} else {
							value2 = json2.getAsString();
						}
						ExtentReportBuilder.ReportTestStep(value + " = " + value1 + " sholud be in response ", "Pass",
								value2.trim());
						return true;
					} else {
						ExtentReportBuilder.ReportTestStep(value + " = " + json1 + " sholud be in response  ", "Fail",
								value.trim());
						return false;
					}
				}
			} else {
				return false;
			}
		} else if (json1 == null && json2 == null) {
			ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
			return true;
		} else {
			ExtentReportBuilder.ReportTestStep("Getting null value in Json response " + json1, "Fail", value.trim());
			return false;
		}
		return isEqual;
	}

	/*
	 * Get Campaign Code From Create Campaign Response for API 2.0
	/**
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 * @Purpose: This function is used to Get Campaign NAme From Create Campaign
	 * Response for API 2.0
	 */
	public static String ver2GetCampaignNameFromCreateCampaign(String value) {
		String name = null;
		try {
			JsonObject responseObj = CommonAPIUtility.convert_stringResponseInJson(value);
			if(responseObj!=null) {
			name = responseObj.getAsJsonObject("campaignHeader").get("campaignName").toString().replaceAll("\"", "");
			}else {
				name=null;
			}
		} catch (Exception e) {
			name=null;
		}
		return name;
	}

	/*
	 * Common method to add a key value to a JSON string
	 /**
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 */
	public static String addSingleKeyValueToJson(String jsonString, String key, String value) {
		JsonObject json;
		String text = null;
		try {
		json = convert_stringResponseInJson(jsonString);
		if(json!=null) {
		json.addProperty(key, value);
		text=  json.toString();
		}
		}catch(Exception e ) {
			 logger.info(e.getMessage());
		}
		return text;
		
	}
	
	/*
	 * Common method to add a key value to a JSON string
	 /**
	 * @author Abirami
	 * @param jsonString
	 * @param key
	 * @param value as boolean
	 */
	public static String addSingleKeyValueToJson(String jsonString, String key, boolean value) {
		JsonObject json;
		String text = null;
		try {
		json = convert_stringResponseInJson(jsonString);
		if(json!=null) {
		json.addProperty(key, value);
		text=  json.toString();
		}
		}catch(Exception e ) {
			 logger.info(e.getMessage());
		}
		return text;
		
	}
	
	/*
	 * Common method to add a key value to a JSON string
	 **
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 */
	public static String addSingleKeyValueToJson(String jsonString, String key, double val) {
		JsonObject json = convert_stringResponseInJson(jsonString);
		if(json!=null) {
		json.addProperty(key, val);
		value= json.toString();
		}else {
			value=null;
		}
		return  value;
	}
	
	/*
	 * Common method to add a key value to a JSON string
	 * @author:Abirami
	 */
	public static String addSingleKeyValueToJson(String jsonString, String key, int val) {
		JsonObject json = convert_stringResponseInJson(jsonString);
		if(json!=null) {
			json.addProperty(key, val);
			value= json.toString();
			}else {
				value=null;
			}
		return  value;
	}
	
	/*
	 * Common method to add a key value to a JSON string
	 * @author:Abirami
	 */
	public static String deletSingleKeyValueToJson(String jsonString, String key) {
		JsonObject json = convert_stringResponseInJson(jsonString);
		if(json!=null) {
		json.remove(key);
		value= json.toString();
		}else {
			value=null;
		}
	return  value;
	}
	
	/**
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return json.ToString with Added key value
	 */
	public static String addSingleKeyValueToJson(String jsonString, String key, JsonElement val) {
		JsonObject json = convert_stringResponseInJson(jsonString);
		if(json!=null) {
			json.add(key, val);
			value= json.toString();
			}else {
				value=null;
			}
		
		return value;
	}
	
	/**
	 * @purpose compare json with Strict Mode, Recommended default to be false which will skip reordering data and extending results (as long as all the expected elements are there)
	 * @param expectedJson
	 * @param actualJson
	 * @param strictMode
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws Throwable 
	 */
	public static void compareJson(String stepName, String expectedJson, String actualJson, boolean strictMode) throws IOException, ParseException {
		try {
			
			String exp = expectedJson.toString().trim();
			String act = actualJson.toString().trim();

			if(act.toLowerCase().contains(exp.toLowerCase()))
			{	
			
				ExtentReportBuilder.ReportTestStep(stepName , "Fail", expectedJson.trim());
				ExtentReportBuilder.ReportTestStep(stepName , "Fail", actualJson.trim());
			} else {
				ExtentReportBuilder.ReportTestStep(stepName + " response is as expected", "Pass -- Expected ----  ", expectedJson.replace(" ","").trim());
				ExtentReportBuilder.ReportTestStep(stepName + " response is as expected", "Pass -- Actual ----  ", actualJson.replace(" ","").trim());
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			ExtentReportBuilder.ReportTestStep("Unexpected Failure", "Fail", "compareJson");
		}
	}
	
	/**
	 * 
	 * @purpose compare json with Strict Mode, Recommended default to be false which will skip reordering data and extending results (as long as all the expected elements are there)
	 * @param expectedJson
	 * @param actualJson
	 * @param strictMode
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws Throwable 
	 */
	public static void compareJsonAssetValidation(String stepName, String expectedJson, String actualJson, boolean strictMode) throws IOException, ParseException {
		try {
			
			String exp = expectedJson.toString();
			String act = actualJson.toString();
			
			String actString = null;
			
			  HashMap<String, String> map = new HashMap<String, String>();
		        JSONObject jObject = new JSONObject(act);
		        Iterator<?> keys = jObject.keys();
		        
		        while( keys.hasNext() ){
		            String key = (String)keys.next();
		            if(key.equalsIgnoreCase("message")) {
		            String value = jObject.getString(key);
		        
		            map.put(key, value);
		            
                     actString = value;
		            }
		        }
		        
		      System.out.println("Actstring  :"+actString);  
			if(actString.trim().equalsIgnoreCase(exp.trim()))
			{	
			
				ExtentReportBuilder.ReportTestStep(stepName , "Fail", expectedJson.trim());
			} else {
				ExtentReportBuilder.ReportTestStep(stepName + " response is as expected", "Pass", expectedJson.replace(" ","").trim());
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			ExtentReportBuilder.ReportTestStep("Unexpected Failure", "Fail", "compareJson");
		}
	}
	
	/**
	 * 
	 * @param strJson
	 * @param strJPath
	 * @return String of given JsonPath
	 */
	public static JsonElement getJsonElementForJPath(String strJson, String strJPath)
	{
		Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();
		return JsonPath.using(conf).parse(strJson).read(strJPath);
	}
	
	/**
	 * 
	 * @purpose To Convert String response to formatted Json String
	 * @param stringToConvert
	 * @return Formatted Json String
	 */
	public static String convertStringtoFormattedJson(String stringToConvert)  {
		String json = null;
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();		
		JsonObject jsonObj = new JsonParser().parse(stringToConvert).getAsJsonObject();
		json = gson.toJson(jsonObj);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}		
		return json;
	}
	
	/**
	 * 
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return Added Key Value JsonString at JsonPath
	 */
	public static String addValueToJSONwithJsonPath(String jsonString, String jsonPath, String value) {
		Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();
		return JsonPath.using(conf).parse(jsonString).set(JsonPath.compile(jsonPath), value).jsonString();
	}
	
	/**
	 *
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return Added Key Value JsonString at JsonPath
	 */
	public static String addValueToJSONwithJsonPath(String jsonString, String jsonPath, int value) {
		Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();
		return JsonPath.using(conf).parse(jsonString).set(JsonPath.compile(jsonPath), value).jsonString();
	}
	
	/**
	 *
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return Added Key Value JsonString at JsonPath
	 */
	public static String addValueToJSONwithJsonPath(String jsonString, String jsonPath, Double value) {
		Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();
		return JsonPath.using(conf).parse(jsonString).set(JsonPath.compile(jsonPath), value).jsonString();
	}
	
	/**
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return Added Key Value JsonString at JsonPath
	 */
	public static String addValueToJSONwithJsonPath(String jsonString, String jsonPath,JsonElement value) {
		Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();
		return JsonPath.using(conf).parse(jsonString).set(JsonPath.compile(jsonPath), value).jsonString();
	}
	
	/**
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return Added Key Value JsonString at JsonPath
	 */
	public static String addValueToJSONwithJsonPath(String jsonString, String jsonPath,JsonArray value) {
		Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS).build();
		return JsonPath.using(conf).parse(jsonString).set(JsonPath.compile(jsonPath), value).jsonString();
	}
	
	/**
	 *
	 * @param jsonString
	 * @param key
	 * @param value
	 * @return Added Key Value JsonString at JsonPath
	 * @throws IOException 
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static String getToken(String tokenURL, String tokenRequest) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	RestValidator es = executor.GetHTTP_PostTokenResponse(tokenURL, tokenRequest, "application/json");	
	String bodyMesage =es.get_ResponsBodyMessage();
	Access_Token=CommonAPIUtility.valuesFromJASONResponse(bodyMesage, "access_token");
	return Access_Token;
	}
	
	
	/**
	 * @author Abirami
	 * @param jsonString
	 * @param market value
	 * @param Content type value
	 * @return Hash Map<String,String>
	 */
	public static HashMap<String, String> setHeaderWithParams(String market, String content)  {
	
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(RestExecutor.HEADER_ENV_ID, market);
		headers.put(RestExecutor.CONTENT_TYPE, content);
		
	return headers;
	}
	
	/**
	 * @author Abirami
	 * @param jsonString
	 * @param String value either market or Accept value
	 * @param Content type value
	 * @return Hash Map<String,String>
	 */
	public static HashMap<String, String> setHeaderWithContent(String param)  {
	
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(RestExecutor.CONTENT_TYPE, param);
		
	return headers;
	}

}
