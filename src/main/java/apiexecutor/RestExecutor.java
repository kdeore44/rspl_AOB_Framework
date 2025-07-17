package apiexecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.LogManager;

import automationEngine.ApplicationSetup;
import utilities.CommonUtilities;

public class RestExecutor {
	
	/**
	 * HTTP request header constants
	 */
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String ENCODING_GZIP = "gzip";
	public static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String HEADER_ENV_ID = "env-id";
	public static final String HEADER_MARKET = "market";

	private CloseableHttpClient client;
	private String url = ApplicationSetup.testURL;
	BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
	public static String requestData;
	public String customproxyRequired;
	private static final org.apache.log4j.Logger logger = LogManager.getLogger(RestExecutor.class);
	
	
	CommonUtilities objCU = new CommonUtilities();
	
	/**
	 * Constructor for RestExecutor
	 * 
	 * @param url
	 * @throws Exception
	 */
	public RestExecutor(String url) {
		PoolingHttpClientConnectionManager connectionManager = null;
		try {
			SSLContext context = SSLContexts.custom().loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE).build();
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE)).build();
			connectionManager = new PoolingHttpClientConnectionManager(registry);

			customproxyRequired = objCU.readPropertyFileEnvProperty("CustomProxyRequired");
			if (customproxyRequired.equalsIgnoreCase("true")) {
				 //Set the custom proxy configuration
				HttpHost proxy = new HttpHost("proxy.zt.viooh.io", 3128, "http");
				DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
				client = HttpClients.custom().setRoutePlanner(routePlanner).build();
			} else {
				// will execute when no custom proxy configuration required
				client = HttpClients.custom().setConnectionManager(cm).setConnectionManagerShared(true).build();
			}
			this.url = url;
		} catch (Exception e) {
			 logger.info(e.getMessage());
			 objCU.printToConsole(""+e);
		} finally {
			if(connectionManager!=null) {
			connectionManager.close();
			}
		}

	}

	/**
	 * @author akshay.vasava
	 * @createdDate 30-Oct-2019
	 * @purpose Closing HttpClient, HttpMethod connections
	 */
	public void releaseConnection() throws IOException {
		HttpMethod method = new GetMethod(url);
		try {
			method.releaseConnection();
			client.close();
			cm.closeIdleConnections(0, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			 logger.info(e.getMessage());
		}
	}

	/**
	 * Executes GET req with Auth token and returns response json.
	 * 
	 * @param path
	 * @param Access
	 *            Token , Content Type
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_GETResponse(String path, String Access_Token, String contentType) throws IOException {
		HttpGet request = new HttpGet(path);
		CloseableHttpResponse response = null;

		/*
		 * The response object which holds the details of the response.
		 */
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();

		RestValidator rest = null;
		try {
			/*
			 * Setting the headers for the request
			 */
			//request.addHeader("Authorization", "Bearer "+Access_Token);
			request.addHeader("Content-Type", contentType);
			requestData = request.getURI().toString();

			/*
			 * Executing the GET operation
			 */
			response = client.execute(request);

			/*
			 * Obtaining the response body from the response stream.
			 */
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			/*
			 * Setting values for the response object
			 */
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			objCU.printToConsole("Response Body from Rest    : "+ resResponse.toString());
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
			objCU.printToConsole("Exception:   "+e);
		} finally {
			if (response != null)
				response.close();
		}
		/*
		 * Returns the RestValidator object providing the response object
		 */
		return rest;
	}

	/**
	 * Executes GET req with Auth token and returns response json.
	 * 
	 * @author Malay.Shah
	 * @param path
	 * @param Access
	 *            Token , Content Type
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_GETResponseForInvalidEnv(String path, String Access_Token, String contentType,
			Boolean Environment_ID) throws IOException {
		HttpGet request = new HttpGet(url + path);
		CloseableHttpResponse response = null;
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();

		RestValidator rest = null;
		try {
			/*
			 * Setting the headers for the request
			 */
			request.addHeader("Authorization", "Bearer " + Access_Token);
			request.addHeader("Content-Type", contentType);

			// If true then pass the Invalid environment
			if (Environment_ID.equals(Boolean.TRUE)) {
				request.addHeader("env-id", "market:jcd:hk:metro");
			}

			else {

				 logger.info("Env-id is missing for GET request" + request);
			}

			requestData = request.getURI().toString();

			/*
			 * Executing the GET operation
			 */
			response = client.execute(request);

			/*
			 * Obtaining the response body from the response stream.
			 */
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			/*
			 * Setting values for the response object
			 */
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		/*
		 * Returns the RestValidator object providing the response object
		 */
		return rest;
	}

	/**
	 * Executes GET req with Auth token and returns response json.
	 * 
	 * @param path
	 * @param Access
	 *            Token , Content Type
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_GETWithHeadersResponse(String path, String Access_Token, Map<String, String> headers)
			throws IOException {
		HttpGet request = new HttpGet(url+path);
		CloseableHttpResponse response = null;

		/*
		 * The response object which holds the details of the response.
		 */
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();

		RestValidator rest = null;
		try {
			/*
			 * Setting the headers for the request
			 */
			headers.forEach((k, v) -> request.addHeader(k, v));
			request.addHeader("Authorization", "Bearer " + Access_Token);
			requestData = request.getURI().toString();

			/*
			 * Executing the GET operation
			 */
			response = client.execute(request);

			/*
			 * Obtaining the response body from the response stream.
			 */
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			/*
			 * Setting values for the response object
			 */
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		/*
		 * Returns the RestValidator object providing the response object
		 */
		return rest;
	}
	
	
	

	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PutWithHeadersResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {

		HttpPut put = new HttpPut(url + path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			put.addHeader("Authorization", "Bearer " + Access_Token);
			if (headers != null)
				headers.forEach((k, v) -> put.addHeader(k, v));

			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				put.setEntity(input);
			}

			requestData = put.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;

			response = client.execute(put);
			if (response.getEntity() != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					responseString.append(line);
				}
				resResponse.setResponseBody(responseString.toString());
			}
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * Executes DELETE req and returns response json.
	 * 
	 * @param path
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_DeleteWithHeadersResponse(String path, String Access_Token,
			Map<String, String> headers) throws IOException {
		HttpDelete httpDelete = new HttpDelete(path);
		CloseableHttpResponse response = null;
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		RestValidator rest = null;
		try {
			//httpDelete.addHeader("Authorization", "Bearer " + Access_Token);
			if (headers != null)
				headers.forEach((k, v) -> httpDelete.addHeader(k, v));

			requestData = httpDelete.getURI().toString();
			response = client.execute(httpDelete);
			if (response.getEntity() != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					responseString.append(line);
				}
				resResponse.setResponseBody(responseString.toString());
			}
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
       rest = new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());

		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public RestValidator GetHTTP_PostTokenResponse(String path, String request, String contentType)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {


		SSLContext context = SSLContexts.custom().loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE).build();

		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE)).build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		CloseableHttpClient client2 = HttpClients.custom().setConnectionManager(cm)
				.setConnectionManager(connectionManager).build();

		HttpPost post = new HttpPost(path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			StringEntity input = new StringEntity(request);
			input.setContentType("json");
			input.setContentType(contentType);

			post.setEntity(input);
			response = client2.execute(post);
			requestData = post.getURI().toString();
			requestData = requestData + "&#10" + request;
			 logger.info(response.toString());

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";

			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}

			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * This is used to get Input streem buffer reader
	 * 
	 * @param HTTpResponse
	 * @param headers
	 * @throws UnsupportedOperationException
	 */
	public static BufferedReader Get_InputStreamReader(HttpResponse response) throws IOException {
		InputStreamReader ip = new InputStreamReader(response.getEntity().getContent());
		return new BufferedReader(ip);
	}

	/**
	 * This is used to get the post reponse for validation
	 * 
	 * @param path
	 * @param headers
	 * @return
	 * @throws IOException
	 */

	public RestValidator generate_Token(String tokenURL, String request, String xmlContent) throws IOException {

		HttpPost post = new HttpPost(tokenURL);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {

			StringEntity input = new StringEntity(request, "UTF-8");
			input.setContentType("application/json");

			post.setEntity(input);
			response = client.execute(post);
			requestData = post.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest = new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PostResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {

		HttpPost post = new HttpPost(url + path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				post.setEntity(getEntities(headers));

			post.addHeader("Authorization", "Bearer " + Access_Token);
			StringEntity input = new StringEntity(xmlContent, "UTF-8");
			input.setContentType(contentType);

			post.setEntity(input);
			response = client.execute(post);
			requestData = post.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}
	
	
	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PostResponseURI(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType,String envId) throws IOException {

		HttpPost post = new HttpPost(path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				post.setEntity(getEntities(headers));

			post.addHeader("Authorization", "Bearer " + Access_Token);
			post.addHeader("env-id", envId);
			StringEntity input = new StringEntity(xmlContent, "UTF-8");
			input.setContentType(contentType);

			post.setEntity(input);
			response = client.execute(post);
			requestData = post.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
		
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}
	
	

	
	
	
	
	
	
	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PostWithBlankResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {

		HttpPost post = new HttpPost(url + path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				post.setEntity(getEntities(headers));

			post.addHeader("Authorization", "Bearer " + Access_Token);
			StringEntity input = new StringEntity(xmlContent, "UTF-8");
			input.setContentType(contentType);

			post.setEntity(input);
			response = client.execute(post);
			requestData = post.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	
	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PutWithBlankResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {

		HttpPut put = new HttpPut(url + path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				put.setEntity(getEntities(headers));

			put.addHeader("Authorization", "Bearer " + Access_Token);
			StringEntity input = new StringEntity(xmlContent, "UTF-8");
			input.setContentType(contentType);

			put.setEntity(input);
			response = client.execute(put);
			requestData = put.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	
	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PatchWithBlankResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {

		HttpPatch patch = new HttpPatch(url + path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				patch.setEntity(getEntities(headers));

			patch.addHeader("Authorization", "Bearer " + Access_Token);
			StringEntity input = new StringEntity(xmlContent, "UTF-8");
			input.setContentType(contentType);

			patch.setEntity(input);
			response = client.execute(patch);
			requestData = patch.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	
	
	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and Patch Http response with direct path .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PatchWithBlankResponseURI(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {

		HttpPatch patch = new HttpPatch(path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				patch.setEntity(getEntities(headers));

			patch.addHeader("Authorization", "Bearer " + Access_Token);
			StringEntity input = new StringEntity(xmlContent, "UTF-8");
			input.setContentType(contentType);

			patch.setEntity(input);
			response = client.execute(patch);
			requestData = patch.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	
	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	@SuppressWarnings("null")
	public RestValidator GetHTTP_PatchResponse(String path, String Access_Token, String xmlContent, String contentType)
			throws IOException {

		HttpPatch patch = new HttpPatch(path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {

			//patch.addHeader("Authorization", "Bearer " + Access_Token);
			patch.addHeader("Content-Type", contentType);
			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				patch.setEntity(input);
			}

			response = client.execute(patch);
			requestData = patch.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());

		} finally {
			if (response != null)
				response.close();
		}
		
		return rest  ;
		
	}

	/**
	 * Executes Patch/Update req and returns response json.
	 * 
	 * @param path
	 * @param headers
	 * @param xmlContent
	 * @param contentType
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PatchResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {
		HttpPatch patch = new HttpPatch(url + path);
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				patch.setEntity(getEntities(headers));

			patch.addHeader("Authorization", "Bearer " + Access_Token);
			patch.addHeader("Content-Type", contentType);
			
			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				patch.setEntity(input);
			}
			response = client.execute(patch);
			requestData = patch.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * Executes DELETE req and returns response json.
	 * 
	 * @param path
	 * @param headersGetHTTP_PostWithBlankResponse
	 * @return
	 * @throws IOException
	 */
	public int GetHTTP_DeleteResonse(String path, String Access_Token, String contentType) throws IOException {
		HttpDelete delete = new HttpDelete(url + path);
		int code = 0;
		CloseableHttpResponse response = null;
		try {
			delete.addHeader("Authorization", "Bearer " + Access_Token);
			delete.addHeader("Content-Type", contentType);
			requestData = delete.getURI().toString();
			response = client.execute(delete);
			code = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			logger.info(e.getMessage());

		} finally {
			if (response != null)
				response.close();
		}
		return code;
	}

	/**
	 * Executes PUT req and returns response json.
	 * 
	 * @param path
	 * @param headers
	 * @param xmlContent
	 * @param contentType
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PutResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {
		HttpPut put = new HttpPut(path);
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				put.setEntity(getEntities(headers));

			//put.addHeader("Authorization", "Bearer " + Access_Token);
			put.addHeader("Content-Type", contentType);
			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				put.setEntity(input);
			}

			response = client.execute(put);
			requestData = put.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * This is used to set add Authorization header with Basci client credential
	 * ,content type and get Http response .
	 * 
	 * @param path
	 * @param headers
	 * @return HTTpResponse
	 * @throws IOException
	 */
	public int GetHTTP_PutResponse(String path, String Access_Token, String xmlContent, String contentType)
			throws IOException {

		HttpPut patch = new HttpPut(url + path);

		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		int value = 0 ;
		try {

			patch.addHeader("Authorization", "Bearer " + Access_Token);
			patch.addHeader("Content-Type", contentType);
			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				patch.setEntity(input);
			}

			response = client.execute(patch);
			value = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			logger.info(e.getMessage());

		} finally {
			if (response != null)
				response.close();
		}
		return value;
	}

	/**
	 * Gets the hashmap turns it in HttpEntity nameValuePair.
	 * 
	 * @param inputEntities
	 * @return
	 */
	private HttpEntity getEntities(Map<String, String> inputEntities) {
		List<BasicNameValuePair> nameValuePairs = new ArrayList(inputEntities.size());
		Set<String> keys = inputEntities.keySet();
		for (String key : keys) {
			nameValuePairs.add(new BasicNameValuePair(key, inputEntities.get(key)));
		}
		HttpEntity urlEncode = null;
		try {

			urlEncode=  new UrlEncodedFormEntity(nameValuePairs);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return urlEncode;
	}

	/**
	 * This is used to get response for HTTP POST Method with headers
	 * 
	 * @param path
	 * @param access
	 *            token
	 * @param headers
	 * @param xmlContent
	 * @param contentType
	 * @return HttpResponse
	 * @throws IOException
	 */
	public RestValidator GetHTTP_POSTWithHeadersResponse(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {
		
		HttpPost post = new HttpPost(url + path);
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			post.addHeader("Authorization", "Bearer " + Access_Token);
			if (headers != null)
				headers.forEach((k, v) -> post.addHeader(k, v));
			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				post.setEntity(input);
			}
			requestData = post.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			response = client.execute(post);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			
		rest =	new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * Executes PUT req with env-id flag and returns response json.
	 * 
	 * @author Malay.Shah
	 * @param path
	 * @param headers
	 * @param xmlContent
	 * @param contentType
	 * @param EnvIDFlag : Pass false if you don't want to pass Environment or market otherwise pass True 
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PutResponseMissingEnv(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType, Boolean EnvIDFlag) throws IOException {
		HttpPut put = new HttpPut(url + path);
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				put.setEntity(getEntities(headers));

			put.addHeader("Authorization", "Bearer " + Access_Token);
			put.addHeader("Content-Type", contentType);

			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				put.setEntity(input);
			}

			response = client.execute(put);
			requestData = put.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest = new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}
	
	/**
	 * Executes PUT req with env-id flag and returns response json.
	 * 
	 * @author Malay.Shah
	 * @param path
	 * @param headers
	 * @param xmlContent
	 * @param contentType
	 * @param EnvIDFlag : Pass false if you don't want to pass Environment or market otherwise pass True 
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PutResponseInvalidEnv(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType) throws IOException {
		HttpPut put = new HttpPut(url + path);
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				put.setEntity(getEntities(headers));

			put.addHeader("Authorization", "Bearer " + Access_Token);
			put.addHeader("Content-Type", contentType);

			put.addHeader("env-id", "InvalidEnv");

			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				put.setEntity(input);
			}

			response = client.execute(put);
			requestData = put.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest = new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

	/**
	 * Executes Patch/Update req with Env-id flag and returns response json.
	 * 
	 * @author Malay.Shah
	 * @param path
	 * @param headers
	 * @param xmlContent
	 * @param contentType
	 * @param env-id
	 *            flag
	 * @return
	 * @throws IOException
	 */
	public RestValidator GetHTTP_PatchResponseMisingEnv(String path, String Access_Token, Map<String, String> headers,
			String xmlContent, String contentType, Boolean EnvIDFlag) throws IOException {
		HttpPatch patch = new HttpPatch(url + path);
		RestResponse resResponse = new RestResponse();
		StringBuilder responseString = new StringBuilder();
		CloseableHttpResponse response = null;
		RestValidator rest = null;
		try {
			if (headers != null)
				patch.setEntity(getEntities(headers));

			patch.addHeader("Authorization", "Bearer " + Access_Token);
			patch.addHeader("Content-Type", contentType);
			
			if (!(xmlContent == null || xmlContent.isEmpty())) {
				StringEntity input = new StringEntity(xmlContent);
				input.setContentType(contentType);
				patch.setEntity(input);
			}
			response = client.execute(patch);
			requestData = patch.getURI().toString();
			requestData = requestData + "&#10" + xmlContent;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				responseString.append(line);
			}
			resResponse.setResponseBody(responseString.toString());
			resResponse.setResponseCode(response.getStatusLine().getStatusCode());
			resResponse.setResponseMessage(response.getStatusLine().getReasonPhrase());
			Header[] rheaders = response.getAllHeaders();
			for (Header header : rheaders) {
				resResponse.setHeader(header.getName(), header.getValue());
			}
			rest= new RestValidator(resResponse);
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (response != null)
				response.close();
		}
		return rest;
	}

}
