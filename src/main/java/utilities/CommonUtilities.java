package utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import automationEngine.ApplicationSetup;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class CommonUtilities {
	public static Logger log = LogManager.getLogger(CommonUtilities.class.getName());
	WebDriver driver;
	Properties prop;

	File file = new File(System.getProperty("user.dir")
			+ "\\src\\main\\resources\\dataPool\\EnvData.properties");
	String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\datapool\\" + "EnvData.properties";
	String resourcesPath = this.readPropertyFile(filepath, "ResourcesPath");
	String reportsFolderPath = this.readPropertyFile(filepath, "TestReport");

	/**
	 * Highlight element on UI
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param driver  - driver instance
	 * @param element - element to be highligted
	 */
	public void objHig(WebDriver driver, WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	/**
	 * Reading values from property file
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param key - Key which need to be read from properties file
	 * @return - value of that specific key
	 */
	public String readPropertyFile(String filePath, String key) {
		String value = null;
		try {
			prop = new Properties();
			FileInputStream fin = new FileInputStream(filePath);
			prop.load(fin);
			value = prop.getProperty(key);
		} catch (Exception e) {
			log.error("Error while reading property file");
			return null;
		}
		return value;
	}
	
	public String readPropertyFileEnvProperty(String key) {
		String value = null;
		try {
			prop = new Properties();
			FileInputStream fin = new FileInputStream(filepath);
			prop.load(fin);
			value = prop.getProperty(key);
		} catch (Exception e) {
			log.error("Error while reading property file");
			return null;
		}
		return value;
	}

	/**
	 * Capture screenshot for full screen
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param driver   - driver instance
	 * @param tcName   - name of test case
	 * @param tcStatus - status of test case (Pass / Failed)
	 * @return - screenshot location
	 */
	public String CaptureScreenShot(WebDriver driver, String tcName, String tcStatus) {

		String tcSSPath = System.getProperty("user.dir") + reportsFolderPath;
		String passTCfolder = "\\Pass\\";
		String failTCfolder = "\\Failed\\";
		String destinationFolder = null;
		String fileName = new SimpleDateFormat("yyyyMMMDDhhmmss").format(new Date());

		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			if (tcStatus.equalsIgnoreCase("Pass")) {
				destinationFolder = tcSSPath + passTCfolder + tcName + "_" + fileName + "_Pass.png";
				FileHandler.copy(srcFile, new File(destinationFolder));

			} else if (tcStatus.equalsIgnoreCase("Failed")) {
				destinationFolder = tcSSPath + failTCfolder + tcName + "_" + fileName + "Failed.png";
				FileHandler.copy(srcFile, new File(destinationFolder));
			}
		} catch (IOException e) {
			log.error("Error in zipping report " + e.getMessage());
		}
		return destinationFolder;
	}

	/**
	 * Clean the files from Reports folder as per flag
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 */
	public void folderCleanUp() {
		try {
			RWExcel objExcel = new RWExcel();
			String reportflag = objExcel.readCell("Configuration", "PRESERVE_REPORT");
			if (reportflag.equalsIgnoreCase("false")) {
				File r[] = new File[4];
				r[0] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\TestReports\\");
				r[1] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\Failed\\");
				r[2] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\Pass\\");
				r[3] = new File(System.getProperty("user.dir") + reportsFolderPath + "\\zip\\");

				for (File i : r) {
					File[] filelist = i.listFiles();
					for (File fl : filelist) {
						if (!fl.getName().contains(".txt")) {
							if (fl.isFile())
								fl.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error in zipping report " + e.getMessage());
		}
	}

	/**
	 * zip report folder and save to zip folder under resources
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param env - environment name
	 * @param grp - group name
	 * @throws ZipException
	 */
	public void zipper(String env, String grp) throws ZipException {
		ZipFile zp = null;
		try {
			DateFormat df = new SimpleDateFormat("ddMMMYYY-HHmm");
			Date dt = new Date();

			zp = new ZipFile(System.getProperty("user.dir") + resourcesPath + "\\zip\\Report_" + env + "_" + grp + "_"
					+ df.format(dt) + ".zip");

			zp.addFolder(new File(System.getProperty("user.dir") + resourcesPath + "\\Reports\\"));

		} catch (ZipException e) {
			log.error("Error in zipping report " + e.getMessage());
		} finally {
			zp.addFolder(new File(System.getProperty("user.dir") + resourcesPath + "\\Reports\\"));
		}
	}
	
	/**
	 * Get pdf content
	 * 
	 * @author Rashmi.Patel added on 5 Feb 21
	 * @param url - pdf url
	 * @return - complete content of pdf
	 * @throws IOException
	 */
	public String readPdfContent(String url) throws IOException {
		try {
			URL pdfUrl = new URL(url);
			InputStream in = pdfUrl.openStream();
			BufferedInputStream bf = new BufferedInputStream(in);
			PDDocument doc = PDDocument.load(bf);
			int numberOfPages = getPageCount(doc);
			log.debug("The total number of pages in pdf: " + numberOfPages);
			String content = new PDFTextStripper().getText(doc);
			doc.close();
			return content;
		} catch (Exception e) {
			log.error("Error in <readPdfContent> " + e.getMessage());
			return "";
		}
	}

	/**
	 * Get the total number of pages in the pdf document
	 * 
	 * @author Rashmi.Patel added on 5 Feb 21
	 * @param doc - object of PDDocument
	 * @return - number of pages
	 */
	public int getPageCount(PDDocument doc) {
		try {
			int pageCount = doc.getNumberOfPages();
			return pageCount;
		} catch (Exception e) {
			log.error("Error in <getPageCount> " + e.getMessage());
			return 0;
		}
	}
	
	public synchronized void browserTCcounter(String browserName) {		
		if(browserName.equalsIgnoreCase("chrome")) {
			ExtentReportBuilder.chromeTC = ExtentReportBuilder.chromeTC  + 1;
		} else if(browserName.equalsIgnoreCase("firefox")) {
			ExtentReportBuilder.firefoxTC = ExtentReportBuilder.firefoxTC + 1;
		} else if(browserName.equalsIgnoreCase("edge")) {
			ExtentReportBuilder.edgeTC=ExtentReportBuilder.edgeTC +1;
		}
	}
	
	public static String generateRandomString() {

	  	Calendar currentDate = Calendar.getInstance();
	  	SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
	  	String dateNow = formatter.format(currentDate.getTime());
	  	return dateNow.replace(" ", "").replace(":", "").replace(",", "");
		}
	
	/**
	 * @author Abirami
	 *@param String which needs to be print in console
	 * @throws IOException
	 */
	public void printToConsole(String string) {
		
		System.out.println(string);
		
	}
	
	/**
	 * @author Abirami
	 *@param Generating the Random numeric String
	 * @throws IOException
	 */
	public String generateRandomNumericString() {
		return RandomStringUtils.randomNumeric(4);
		
	}
	
	/**
	 * @author Abirami
	 *@param Print the console logs in the Report
	 * @throws IOException
	 */
	public ArrayList<String> printTheConsoleMessage()
	{
		LogEntries logEntries = ApplicationSetup.driver.manage().logs().get(LogType.BROWSER);
		ArrayList<String> entryMsg = new ArrayList<String>();
		 
		 for(LogEntry entry : logEntries){
		        System.out.println(entry.getMessage());
		        entryMsg.add(entry.toString());
		}
	return entryMsg;
	}
	
	/**
	 * @author Abirami
	 *@param Delete the file from download folder
	 * @throws IOException
	 */
	public static void deletDownLoadFile() {
		try {
			Thread.sleep(2000);
			String currentDir = System.getProperty(ConstantVariables.PROPERTIES_USER_DIRECTORY);
			String downloadpath = currentDir + "/Download";
			File fin = new File(downloadpath);
			if (!fin.exists()) {
				fin.mkdir();
			}
			deleteFolder(fin);
		} catch (IOException | InterruptedException e) {

			log.error("Exception Occured at delete download file ", e);
		}
	}
	
	/**
	 * @author Abirami
	 *@param Deleting the complete folder
	 * @throws IOException
	 */
	static void deleteFolder(File file) throws IOException {
		for (File subFile : file.listFiles()) {
			if (subFile.isDirectory()) {
				deleteFolder(subFile);
			} else {
				boolean result = subFile.delete();
				log.info("File Deleted Successfully " + result);
			}
		}
		boolean status = file.delete();
		log.info("File Delete Success " + status);
	}
	
}
