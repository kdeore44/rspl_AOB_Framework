package automationEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.CommonUtilities;
import utilities.ConstantVariables;
import utilities.ExtentReportBuilder;

public class ApplicationSetup extends ExtentReportBuilder {
	public static Logger log = LogManager.getLogger(ApplicationSetup.class.getName());

	// public static WebDriver driver;
	public CommonUtilities objCU = new CommonUtilities();

	public static String testURL;
	public static String UID;
	public static String PAS;
	//public static WebDriver driver;
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\datapool\\" + "EnvData.properties";
	public static String browserName;
	public static String environmentName;
	static String downloadpath;
	public static String groupsDetails;
	public static String testingMethod;

	/**
	 * Launches browser and set url,uid and pwd
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @Updated Abirami on 20 Aug 2024
	 * @param environment - environment name
	 * @param browser     - browser name (chrome/firefox/edge/ie)
	 * @param nodeurl - optional
	 * @throws Exception 
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "environment", "browser", "nodeUrl", "groups","testing" })
	public synchronized void init(@Optional("") String environment,@Optional("") String browser,@Optional("") String nodeUrl,@Optional("") String groups,@Optional("") String testing) throws Exception {
		
		testURL= objCU.readPropertyFileEnvProperty("QAURL");
		environmentName = environment;
		
		System.out.println("URL is for testing before opening browser:   "+testURL);

		switch(environmentName) {
		
		case "QA":
			
			testURL=objCU.readPropertyFileEnvProperty(ConstantVariables.QAURL);
			UID=objCU.readPropertyFileEnvProperty(ConstantVariables.QAUID);
			PAS=objCU.readPropertyFileEnvProperty(ConstantVariables.QAPAS);
			break;
		
		case "Dev":
			
			testURL=objCU.readPropertyFileEnvProperty(ConstantVariables.DEVURL);
			UID=objCU.readPropertyFileEnvProperty(ConstantVariables.DEVUID);
			PAS=objCU.readPropertyFileEnvProperty(ConstantVariables.DEVPAS);
			break;
		
		case "PROD":
			
			testURL=objCU.readPropertyFileEnvProperty(ConstantVariables.PRODURL);
			UID=objCU.readPropertyFileEnvProperty(ConstantVariables.PRODUID);
			PAS=objCU.readPropertyFileEnvProperty(ConstantVariables.PRODPAS);
			break;
			
		case "Other":
			
			testURL=objCU.readPropertyFileEnvProperty(ConstantVariables.OTHERURL);
			UID=objCU.readPropertyFileEnvProperty(ConstantVariables.OTHERUID);
			PAS=objCU.readPropertyFileEnvProperty(ConstantVariables.OTHERPAS);
			break;
		
		}
		    System.out.println("URL for testing :  "+testURL);
			startBrowser(browser,nodeUrl);
			if(testing.equalsIgnoreCase("UI"))
			{
				getDriver().get(testURL);
			}			
			
		ExtentReportBuilder.initExtentReport();
		
	}

	/**
	 * Start browser - instantiate driver & set implicit wait
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @Updated Abirami on 20 Aug 2024
	 * @param browser - browser name (chrome/firefox/edge/ie), nodeUrl 
	 * @return - driver instance
	 */
	@SuppressWarnings("deprecation")
	public WebDriver startBrowser(String browser,String nodeUrl) {
		int wt = Integer.parseInt(objCU.readPropertyFile(filepath, ConstantVariables.TIMEOUTWAIT));
		log.debug("Timeout time set to : = " + wt);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		String SelGridValue = objCU.readPropertyFile(filepath, ConstantVariables.GRID);
		String headLessValue = objCU.readPropertyFile(filepath, ConstantVariables.HEADLESS_FLAG);
		boolean headLessFlag = Boolean.parseBoolean(headLessValue);
		boolean selGrid = Boolean.parseBoolean(SelGridValue);
		try {

			switch(browser){
			case "Chrome":
				WebDriverManager.chromedriver().setup();
				
			    //System.setProperty("webdriver.chrome.driver",SetObjectProperties.appConfig.getPropertyValue("ChromeDriver")); 
				objCU.printToConsole("Browser Driver executable downloaded");
				capabilities.setBrowserName(browser);
				String currentDir = System.getProperty("user.dir");
				
				ChromeOptions options = new ChromeOptions();
				System.setProperty("webdriver.chrome.whitelistedIps", "");
				
					downloadpath = currentDir + "/Download";
					options.addArguments("--ignore-certificate-errors");
					options.addArguments("--disable-extensions");
					options.addArguments("--allow-running-insecure-content");
					//options.addArguments("--disable-dev-shm-usage");
					//options.addArguments("--no-sandbox", "--headless", "--window-size=1980,1080");
					objCU.printToConsole("Started Options");
				
				LoggingPreferences logPrefs = new LoggingPreferences();
				logPrefs.enable(LogType.BROWSER, Level.ALL);
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.prompt_for_download", false);
				chromePrefs.put("download.default_directory", downloadpath);
				chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
				chromePrefs.put("profile.cookie_controls_mode", 0);
				
				objCU.printToConsole("Chrome Prefs enabled");
				
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--incognito");
				options.addArguments("--enable-extensions");
				options.addArguments("disable-popup-blocking");
				options.addArguments("disable-infobars");
				options.addArguments("--disable-gpu");
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--remote-allow-origins=*");
				if (!headLessFlag) {

					options.addArguments("--start-maximized");
				} else {

					options.addArguments("--headless");
					options.addArguments("--window-size=1980,1080");
				}
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				cap.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
				
				options.merge(cap);

				if (selGrid) {
					System.out.println("Remote Node URL: " + nodeUrl);
					
					driver.set(new RemoteWebDriver(new java.net.URL(nodeUrl), cap));
					getDriver().manage().window().maximize();
				} else {
					//driver = new ChromeDriver(options);
					driver.set(new ChromeDriver(options));
					getDriver().manage().window().maximize();
					getDriver().manage().timeouts().implicitlyWait(wt,TimeUnit.SECONDS);
				}
				objCU.printToConsole("Browser Launched");
				
			break;

			case "Firefox":  
			WebDriverManager.firefoxdriver().setup();
			driver.set(new FirefoxDriver());
			getDriver().manage().timeouts().implicitlyWait(wt, TimeUnit.SECONDS);
			log.debug(browser + " browser launch sucessfully");
		    break;

			case "Edge" :
			WebDriverManager.edgedriver().setup();
			driver.set(new EdgeDriver());
			getDriver().manage().timeouts().implicitlyWait(wt, TimeUnit.SECONDS);
			log.debug(browser + " browser launch sucessfully");
		    break;

			case "IE":
			WebDriverManager.iedriver().setup();
			driver.set(new InternetExplorerDriver());
			getDriver().manage().timeouts().implicitlyWait(wt, TimeUnit.SECONDS);
			log.debug(browser + " browser launch sucessfully");
		
		log.debug("ThreadID in case of Parallel execution : " + Thread.currentThread().getId());
		break;
			}
		
	} catch (Exception e) {
		log.info(e.getMessage());
		objCU.printToConsole(e.getMessage());
	}
		
		return getDriver();
		
	}

	/**
	 * Close browser
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 */
	@AfterTest(alwaysRun = true)
	public void tearDown() {
	    try {
	        WebDriver localDriver = driver.get();
	        if (localDriver != null) {
	            localDriver.quit();
	            log.debug("Browser closed successfully.");
	            driver.remove(); // Important to clean ThreadLocal
	        }
	    } catch (Exception e) {
	        log.error("Error while quitting the browser: " + e.getMessage());
	    } finally {
	        try {
	            // Always attempt to kill leftover drivers (e.g., zombie processes)
	            Runtime.getRuntime().exec("taskkill /F /IM chromedriver*");
	        } catch (IOException ioEx) {
	            log.error("Error killing chromedriver process: " + ioEx.getMessage());
	        }

	        // Only if you want this to run once per <test>
	        ExtentReportBuilder.ConcludeTestSuite();
	    }
	}

	/**
	 * This is used to get driver with threadlocal
	 * 
	 * @return
	 * @author Rashmi.Patel
	 */
	public static synchronized WebDriver getDriver() {
		return driver.get();
	}

	
}
