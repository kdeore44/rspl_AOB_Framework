package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/** @author Abirami
 * Costume Methods which can use in any class or method.
 * */

public class FileHandlingUtilities {
	
	static String currentDir = System.getProperty("user.dir");
	private static final Logger logger = LogManager.getLogger(FileHandlingUtilities.class);
	private FileHandlingUtilities() {
	      //not called
	   }
	
	// Delete the Existing Directory.
	public static boolean deleteDirectory(File dir) {
		logger.info("Delet Directory");
		boolean flag ;
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File delFile = new File(dir, children[i]);
				if (!delFile.exists()) {
					 logger.info("Cannot find directory to delete" + delFile.getPath());
					 flag= false;
				}
				boolean success = deleteDirectory(delFile);
				if (!success) {
					 logger.info("failure during delete directory" + delFile.getPath());
					 flag= false;
				}
			}
			// The directory is now empty so now it can be smoked
		}
		return true;
	}

	// Delete All Files inside a Directory
	public static void deleteFiles(String dirName){
		File file = new File(dirName);        
		String[] myFiles;      
		if(file.isDirectory()){  
			myFiles = file.list();  
			for (int i=0; i<myFiles.length; i++) {  
				File myFile = new File(file, myFiles[i]);  
				boolean falg= myFile.delete();
				if(falg) {
					myFile.deleteOnExit();
				}  
				
			}  
		} 
	}

	// to get Last file name of current URl
	public static String getURLName(String str)
	{
		Character chr='p';
		String charString="";
		String charString1="";

		int dec=str.length();
		String stopWhile=""; 
		while(!stopWhile.equals("stop"))
		{
			chr= str.charAt(dec-6);
			charString=chr.toString();
			//  logger.info("-------------"+charString);

			if(charString.equals("/"))
			{
				stopWhile="stop";
			}
			else
			{
				dec--;
				charString1 =charString+charString1;
			}
		}
		return charString1; 
	}

	public static void readTextFile(String fileName) throws IOException{
		BufferedReader br = null;
		try {
		File file = new File(fileName);
		br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuilder sb = new StringBuilder();
		String eachLine = null;
		while ((eachLine = br.readLine()) != null) {
			sb.append(eachLine).append("\n");
		}
		br.close();
		}catch (Exception e) {

			throw (e);

		}finally {
			if(br!=null) {
				br.close();
			}
		  }
	}

	public static String lastFileModified(String dir) {
	    File fl = new File(dir);
	    File[] files = fl.listFiles();
	    if (files.length == 0) return null;
	     Arrays.sort(files, new Comparator<File>() {
	         public int compare(File o1, File o2) {
	             return new Long(o2.lastModified()).compareTo(o1.lastModified()); 
	         }
	         });
	     return files[0].getAbsolutePath();
	}
	
	
	public static String lastJmeterFileModified(String dir) {
	    File fl = new File(dir);
	    File[] files = fl.listFiles();
	    String matchFile = null;
	    if (files.length == 0) return null;
	    for(File file:files) {
	    	String filename = file.getName();
	    	if (filename.equalsIgnoreCase("index.html")) {
	               
	    	   matchFile = file.getAbsolutePath();
	            }
	    }
	   
	     return matchFile;
	}
}
