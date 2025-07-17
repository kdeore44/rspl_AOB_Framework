package utilities;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


public class JsonReader {
	

    private static final ObjectMapper objectMapper = new ObjectMapper(); 
    private final String JSON_FILE_PATH =System.getProperty("user.dir")+"//src//main//resources//datapool//TestData.json";

    /**
     * Reads JSON data from a file.
     *
     * @return JsonObject containing the JSON data.
     */
    @SuppressWarnings("deprecation")
	public JsonObject readJsonFromFile() {
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            JsonParser parser = new JsonParser();
            return parser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }

    /**
     * Writes JSON data to a file.
     *
     * @param jsonObject JsonObject containing the JSON data to write.
     */
    public void writeJsonToFile(JsonObject jsonObject) {
        try (FileWriter writer = new FileWriter(JSON_FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a specific key in the JsonObject.
     *
     * @param jsonObject JsonObject to update.
     * @param key        Key to update.
     * @param value      New value for the key.
     */
    public void addUpdateKey(JsonObject jsonObject, String key, String value) {
        if (jsonObject.has(key)) {
            String oldValue = jsonObject.get(key).getAsString();
            if (!oldValue.equals(value)) {
                jsonObject.addProperty(key, value);
            }
        } else {
            // Key not found, add a new key
            jsonObject.addProperty(key, value);
        }
        writeJsonToFile(jsonObject);
    }

    /**
     * Delete  a specific key in the JsonObject.
     *
     * @param jsonObject JsonObject to update.
     * @param key        Key to update.
     */
    public void deleteKey(JsonObject jsonObject, String key) {
        // Check if the key exists in the JsonObject
        if (jsonObject.has(key)) {
            // If the key exists, remove it
            jsonObject.remove(key);
            // Write the modified JsonObject back to file
            writeJsonToFile(jsonObject);
        }
    }

    /**
     * Converts a given request object to a JSON string.
     *
     * @param requestObject the request object to convert
     * @return the JSON string representation of the request object
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws JsonProcessingException 
     */
    public String convertRequestObjectToJson(Object requestObject) throws IllegalArgumentException, IllegalAccessException, JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>();
        // Get all fields of the request object class
        Field[] fields = requestObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Ensure field is accessible (in case it's private)
            field.setAccessible(true);
            Object value = field.get(requestObject);
            // Add field name and value to the map if the value is not null
            if (value != null) {
                requestMap.put(field.getName(), value);
            }
        }
        // Serialize the Map to JSON
        return objectMapper.writeValueAsString(requestMap);
    }
    
    /**
     * Reads JSON data from a file using Path.
     *
     * @return JsonNode containing the Formatted JSon Data.
     */
    public JsonNode readTestDataForJsonKey(String jsonKey) throws JsonProcessingException, IOException {

        var testCaseDataJsonFile = new File(JSON_FILE_PATH);
        var testCaseDataJsonNode = objectMapper.readTree(testCaseDataJsonFile);
        return testCaseDataJsonNode.get(jsonKey);
    }
    
    
    /**
     * Reads JSON data from a file using Json Path.
     *
     * @return String containing single value from Json Data.
     */
    public String readTestDataUsingJsonPath(String jsonKey) throws JsonProcessingException, IOException {

        var testCaseDataJsonFile = new File(JSON_FILE_PATH);
        DocumentContext doc = JsonPath.parse(testCaseDataJsonFile);
        return doc.read(jsonKey);
    }
    
    /**
     * Reads JSON data from a file using Json Path.
     *
     * @return List of String containing List of values based on Json Path.
     */
    public List<String> readTestDataListUsingJsonPath(String jsonKey) throws JsonProcessingException, IOException {

        var testCaseDataJsonFile = new File(JSON_FILE_PATH);
        DocumentContext doc = JsonPath.parse(testCaseDataJsonFile);
        return doc.read(jsonKey);
    }
    
   
    

}
