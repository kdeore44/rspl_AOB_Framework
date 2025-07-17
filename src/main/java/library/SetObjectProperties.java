package library;

import java.io.File;
import java.io.IOException;

public class SetObjectProperties {
	// create reference of ReadProperties class.
		public static ReadObjectProperties envProp;
		
		public SetObjectProperties() throws IOException {
			
			// create instance of ReadProperties class.
			envProp = new ReadObjectProperties();
					
			// Read AppConfig properties file
			envProp.setFile(new File("src/main/resources/datapool/EnvData.properties"));
			envProp.readFile();			
		
		}
}
	
	
