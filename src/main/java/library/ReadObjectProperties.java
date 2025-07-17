package library;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ReadObjectProperties {

	private Properties properties = new Properties();
	private File file;
	private int count = 0;
	static String objLocatorValue;
	static String parentObjLocatorValue;
	static String childObjLocatorValue;
	String exception="Selected command doesn't exist";

	public void readFile() throws IOException {
		BufferedInputStream bis = null;
		try{
			bis = new BufferedInputStream(new FileInputStream(this.file));
			this.properties.load(bis);
		}finally {
			if(bis!=null) {
				bis.close();
			}
		}
		
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public String getPropertyValue(String property) {
		if (count > 0) {
				return this.properties.getProperty(count + property);

		} else {
				return this.properties.getProperty(property);
		}
	}
	
	
	@SuppressWarnings("unused")
	private static String getTypeIdentifier(String property) {
		String[] parts = property.split("\\|");
		objLocatorValue = parts[1];
		return parts[0].trim();
	}
	@SuppressWarnings("unused")
	private static String getParentTypeIdentifier(String property) {
		String[] parts = property.split("\\|");
		parentObjLocatorValue = parts[1];
		return parts[0].trim();
	}
	@SuppressWarnings("unused")
	private static String getChildTypeIdentifier(String property) {
		String[] parts = property.split("\\|");
		childObjLocatorValue = parts[1];
		return parts[0].trim();
	}
}
