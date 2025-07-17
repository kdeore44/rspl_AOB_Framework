package apiexecutor;

import java.util.HashMap;
import java.util.Map;

public class RestResponse {
	
	private int responseCode;
	public static String responseBody;
	private HashMap<String, String> headers;
	private String responseMessage;

	@SuppressWarnings("unchecked")
	public RestResponse() {
		headers = new HashMap();
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		RestResponse.responseBody = responseBody;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

	public void setHeader(String name, String value) {
		this.headers.put(name, value);
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}


}
