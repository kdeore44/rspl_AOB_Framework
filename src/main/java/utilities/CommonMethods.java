package utilities;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonMethods {
	public static Logger log = LogManager.getLogger(CommonMethods.class.getName());
	CommonUtilities objCU = new CommonUtilities();

	Boolean flag = null;
	WebDriverWait wait;
	String parenwindow = "", Newcurrentwin = "";
	Duration due = Duration.ofSeconds(30);

	/**
	 * Launch url in browser and maximize browser
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver - WebDriver instance
	 * @param url    - url to open
	 * @return true if url is launched
	 */
	public boolean launchURL(WebDriver driver, String url) {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			log.debug("Launching url: " + url);
			return true;
		} catch (Exception e) {
			log.error("Error in <launchURL> " + e.getMessage());
			return false;
		}
	}

	/**
	 * Identify the object on UI and highlight the object boundary with RED border
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return - WebElement
	 * @throws Exception 
	 */
	public WebElement objecLocater(WebDriver driver, By locator) throws Exception {

		WDWait(driver, locator);
		WebElement objTemp = driver.findElement(locator);
		if (objTemp.isDisplayed() || objTemp.isEnabled()) {

			try {
				objCU.objHig(driver, objTemp);
				log.debug("Object is Visible/Enabled: " + locator);
				return objTemp;
			} catch (Exception e) {
				log.error("Object failed to be highlighted: " + locator);
				return null;
			}

		} else {
			log.error("Object has not built:  " + locator);
			throw new Exception(null, null);
		}

	}

	/**
	 * Waits untill visibility of element OR max 30 sec
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return WebElement - value if the function returned something different from
	 *         null or false before the timeout expired.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public WebElement WDWait(WebDriver driver, By locator) throws IOException, ParseException {
		WebElement ele;
		try {
		wait = new WebDriverWait(driver, due);
		ele = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return ele;
		}
		catch(Exception e)
		{
			log.error(e);
			ExtentReportBuilder.ReportTestStep("FAILED", "FAIL",
					"  Used is NOT able to found the element with the exception   "+e);
			Assert.fail();
		}
		return null;
	}

	/**
	 * Identify the all objects on UI and highlight the objects boundary with RED
	 * border
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return - list of webelements matching with locator
	 */
	public List<WebElement> listObjecLocater(WebDriver driver, By locator) {
		List<WebElement> objTemp = null;
		try {
	      objTemp = driver.findElements(locator);
		for (WebElement obj2 : objTemp) {
			if (obj2.isDisplayed()) {
				objCU.objHig(driver, obj2);
			}
		}
		}
		catch(Exception e)
		{
			log.error(e);
			e.printStackTrace();
			Assert.fail();
		}
		return objTemp;
	}

	/**
	 * Select checkbox using JavascriptExecutor
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean SelectChk(WebDriver driver, By locator) throws Exception {
		try {
		WebElement objWE = objecLocater(driver, locator);

		if (objWE.isEnabled()) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", objWE);
			log.debug("Selecting checkbox with locator: " + locator);
			
		}
		}catch(Exception e)
		{
			log.error(e);
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

	/**
	 * Clears and sets text in textbox and highlight the textbox boundary with RED
	 * border
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @param txtVar  - text to set in elements
	 * @return boolean
	 */
	public boolean setText(WebDriver driver, By locator, String txtVar) {
		boolean flag;
		try {
			objecLocater(driver, locator);
			WebElement wd = driver.findElement(locator);
			wd.clear();
			wd.click();
			wd.sendKeys(txtVar);
			flag = true;
			log.debug("Setting text <" + txtVar + "> in field with locator: " + locator);
		} catch (Exception e) {
			flag = false;
			log.error("Error in <setText> " + e.getMessage());
		}

		return flag;
	}

	/**
	 * Sets text in textbox and highlight the textbox boundary with RED border
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @param txtVar  - text to set in elements
	 * @return boolean
	 */
	public boolean setTextinDD(WebDriver driver, By locator, String txtVar) {
		boolean flag;
		try {
			objecLocater(driver, locator);
			WebElement wd = driver.findElement(locator);
			wd.sendKeys(txtVar);
			flag = true;
			log.debug("Setting text <" + txtVar + "> in field with locator: " + locator);
		} catch (Exception e) {
			flag = false;
			log.error("Error in <setTextinDD> " + e.getMessage());
		}

		return flag;
	}

	/**
	 * Get text from element
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return text from element
	 */
	public String getText(WebDriver driver, By locator) {
		try {
			log.debug("Getting text from element with locator: " + locator);
			return objecLocater(driver, locator).getText();
		} catch (Exception e) {
			log.error("Error in <getText> " + e.getMessage());
			return driver.findElement(locator).getText();
		}
	}

	/**
	 * Switch to child window
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver - WebDriver instance
	 * @return boolean
	 */
	public boolean SwitchWindow(WebDriver driver) {
		try {
			parenwindow = driver.getWindowHandle();
			Set<String> chilwin = driver.getWindowHandles();
			log.debug("Total Window " + chilwin.size());

			System.out.println("Parent window " + parenwindow);
			for (String currentwin : chilwin) {
				if (!currentwin.equalsIgnoreCase(parenwindow)) {

					driver.switchTo().window(currentwin);
					Newcurrentwin = driver.getWindowHandle();
					log.debug("Current window -" + Newcurrentwin);
					log.debug("Current window Title is ---->  " + driver.getTitle());
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in <SwitchWindow> " + e.getMessage());
		}
		return flag;
	}

	/**
	 * Close focused current window
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver - WebDriver instance
	 */
	public void closeCurrentWindow(WebDriver driver) {
		driver.close();
	}

	/**
	 * Switching to parent window
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver - WebDriver instance
	 * @return boolean
	 */
	public boolean switchToParentwindow(WebDriver driver) {
		try {
			driver.switchTo().window(parenwindow);
			log.debug("Switched to parent window");
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in <switchToParentwindow> " + e.getMessage());
		}
		return flag;
	}

	/**
	 * Clicking on element identified by given locator
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean click(WebDriver driver, By locator) throws Exception {
		
		try{
			WebElement objWE = objecLocater(driver, locator);

		if (objWE.isEnabled()) {
			log.debug("Clicking on element with locator: " + locator);
			objWE.click();
		}
		}catch(Exception e)
		{
			log.error(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Clicking using JavascriptExecutor
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean JSButtonClick(WebDriver driver, By locator) throws Exception {
		WDWait(driver, locator);
		WebElement objWE = objecLocater(driver, locator);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", objWE);
		log.debug("Clicking on element with locator: " + locator);
		return true;
	}

	/**
	 * Hit Submit button by Click or JS
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return
	 * @throws Exception 
	 */
	public boolean submitButton(WebDriver driver, By locator) throws Exception {
		WDWait(driver, locator);
		WebElement objWE = objecLocater(driver, locator);

		if (objWE.getAttribute("type") == "hidden") {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", objWE);
			log.debug("Clicking on element with locator: " + locator);
			return true;
		} else {
			if (objWE.isEnabled()) {
				objWE.submit();
				log.debug("Clicking on submit");
				return true;
			} else
				return false;
		}
	}

	/**
	 * Select value from Dropdown based on Visible text / Value / index 1
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @param txtVar  - value to be selected in dropdown
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean selectValue(WebDriver driver, By locator, String txtVar) throws Exception {
		Select objWE = new Select(objecLocater(driver, locator));
		boolean flag = false;
		try {
			objWE.selectByValue(txtVar);
			flag = true;
		} catch (Exception e) {
			try {
				objWE.selectByVisibleText(txtVar);
				flag = true;
				log.debug("Selecting value <" + txtVar + "> in element with locator: " + locator);
			} catch (Exception e2) {
				try {
					objWE.selectByIndex(1);
					log.debug("Selecting value at index 1 in element with locator: " + locator);
					flag = true;
				} catch (Exception e3) {
					log.error("Error in <selectValues> " + e.getMessage());
				}
			}
		}
		return flag;
	}

	/**
	 * Select value from Dropdown based on Visible text / Value
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @param value   - value to be selected in dropdown
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean selectDDValue(WebDriver driver, By locator, String value) throws Exception {
		boolean flag = false;
		try {
			WebElement objWE = objecLocater(driver, locator);
			Select selectObject = new Select(objWE);
			try {
				selectObject.selectByValue(value);
				log.debug("Selecting value <" + value + "> in element with locator: " + locator);
			} catch (Exception e) {
				selectObject.selectByVisibleText(value);
				log.debug("Selecting value <" + value + "> in element with locator: " + locator);
			}
			flag = true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			flag = false;
			log.error("Error in <selectDDValue> " + e.getMessage());
		}
		return flag;
	}

	/**
	 * Select value from Dropdown based on Visible text
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @param value   - value to be selected in dropdown
	 * @throws Exception 
	 */
	public void setDropdownValue(WebDriver driver, By locator, String txtVar) throws Exception {
		WebElement objWE = objecLocater(driver, locator);
		Select dropDown = null;
		try {
			dropDown = new Select(objWE);
			int index = 0;
			for (WebElement option : dropDown.getOptions()) {
				if (option.getText().equalsIgnoreCase(txtVar))
					break;
				index++;
			}
			dropDown.selectByIndex(index);
			log.debug("Selecting value <" + txtVar + "> in element with locator: " + locator);
		} catch (Exception e) {
			e.printStackTrace();
			dropDown.selectByVisibleText(txtVar);
			log.debug("Selecting value <" + txtVar + "> in element with locator: " + locator);
		}

	}

	/**
	 * Checks if element present or not
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean isElementPresent(WebDriver driver, By locator) throws Exception {
		try {
			objecLocater(driver, locator);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			log.error("Error in <isElementPresent> " + e.getMessage());
			return false;
		}
	}

	/**
	 * Move to element and click on it
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver element
	 * @param locator - locator to identify element
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean onMouseHover(WebDriver driver, By locator) throws Exception {
		try {
			Actions action = new Actions(driver);
			WebElement we = objecLocater(driver, locator);
			action.moveToElement(we).click().build().perform();
			log.debug("Clicking on element with locator: " + locator);
			return true;

		} catch (org.openqa.selenium.NoSuchElementException e) {
			log.error("Error in <onMouseHover> " + e.getMessage());
			return false;
		}

	}

	/**
	 * Hover on element
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver element
	 * @param locator - locator to identify element
	 * @return boolean
	 */
	public boolean onMouseHover_withoutClick(WebDriver driver, By locator) {
		try {
			Actions action = new Actions(driver);
			WebElement we = driver.findElement(locator);
			action.moveToElement(we).moveToElement(driver.findElement(locator)).build().perform();
			log.debug("Hovering on element with locator: " + locator);
			return true;

		} catch (org.openqa.selenium.NoSuchElementException e) {
			log.error("Error in <onMouseHover_withoutClick> " + e.getMessage());
			return false;
		}
	}

	/**
	 * Select checkbox
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver driver
	 * @param locator - locator to identify element
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean selectCheckBox(WebDriver driver, By locator) throws Exception {
		try {
			WebElement objWE = objecLocater(driver, locator);
			if (objWE.isSelected()) {
				log.debug("Checkbox is already selected with locator: " + locator);
				return true;
			} else {
				objWE.click();
				log.debug("Selecting checkbox with locator: " + locator);
				return true;
			}
		} catch (NoSuchElementException e) {
			log.error("Error in <selectCheckBox> " + e.getMessage());
			return false;
		}
	}

	/**
	 * Check element visibility on page
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @param driver  - WebDriver driver
	 * @param locator - locator to identify element
	 * @return boolean
	 */
	public boolean checkElementVisibility(WebDriver driver, By locator) {
		try {
			if (driver.findElement(locator).isDisplayed()) {
				log.debug("Element is visible with locator: " + locator);
				return true;
			} else
				return false;
		} catch (NoSuchElementException e) {
			log.error("Error in <checkElementVisibility> " + e.getMessage());
			return false;
		}
	}

	/**
	 * Returns current date in MM/dd/yyyy format
	 * 
	 * @author Rashmi.Patel added on 21 Jan 2021
	 * @return date string
	 */
	public String currentDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return (date.format(formatter));
	}

	/**
	 * Checks element presence in DOM
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 */
	public boolean elementPresence(WebDriver driver, By locator) {
		try {
			driver.findElement(locator);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Returns attribute value
	 * 
	 * @param driver
	 * @param locator
	 * @param attribute
	 * @return
	 * @throws Exception 
	 */
	public String getAttribute(WebDriver driver, By locator, String attribute) throws Exception {
		String attrValue = "";
		try {
			attrValue = objecLocater(driver, locator).getAttribute(attribute);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			attrValue = "";
		}
		return attrValue;
	}

	/**
	 * Waits untill element is clickable OR max specified time
	 * 
	 * @param driver        - WebDriver instance
	 * @param locator       - locator to identify element
	 * @param timeInSeconds - maximum seconds to wait
	 * @return - the WebElement once it is located and clickable (visible and
	 *         enabled)
	 */
	public WebElement waitUntillElementClickable(WebDriver driver, By locator, Duration timeInSeconds) {
		wait = new WebDriverWait(driver, timeInSeconds);
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * Waits untill element is visible
	 * 
	 * @param driver        - WebDriver instance
	 * @param locator       - locator to identify element
	 * @param timeInSeconds - maximum seconds to wait
	 * @return - the WebElement once it is visible and enabled)
	 */
	public WebElement waitUntillElementVisible(WebDriver driver, By locator, Duration timeInSeconds) {
		wait = new WebDriverWait(driver, timeInSeconds);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Waits untill element is displayed on page and has height and width
	 * 
	 * @param driver        - WebDriver instance
	 * @param locator       - locator to identify element
	 * @param timeInSeconds - maximum seconds to wait
	 * @return - the WebElement once it is located
	 */
	public WebElement waitUntillVisibilityOfElement(WebDriver driver, By locator, Duration timeInSeconds) {
		wait = new WebDriverWait(driver, timeInSeconds);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Returns selected value of dropdown
	 * 
	 * @param driver  - WebDriver instance
	 * @param locator - locator to identify element
	 * @return selected option
	 */
	public String getSelectedOption(WebDriver driver, By locator) {
		try {
			Select objSelect = new Select(this.objecLocater(driver, locator));
			return objSelect.getFirstSelectedOption().getText();
		} catch (Exception e) {
			log.error("Error in <getSelectedOption> " + e.getMessage());
			return "";
		}
	}

	/**
	 * Performs page down action once
	 */
	public void pageDown() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_PAGE_DOWN);
			robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
		} catch (Exception e) {
			log.error("Error in <pageDown> " + e.getMessage());
		}
	}
	
	/**
	 * Finds the column index in table web element
	 * 
	 * @author Rashmi.Patel added on 28 Jan 2021
	 * @param driver     - WebDriver instance
	 * @param tableEle   - table element
	 * @param columnName - name of column
	 * @return - index of that column
	 */
	public int getColumnIndex(WebDriver driver, WebElement tableEle, String columnName) {
		try {
			int colIndex = 1;
			WebElement firstRowEle = tableEle.findElements(By.tagName("tr")).get(0);
			List<WebElement> thEles = firstRowEle.findElements(By.tagName("th"));
			for (int i = 0; i < thEles.size(); i++) {
				if (thEles.get(i).getText().trim().equalsIgnoreCase(columnName.trim())) {
					return i;
				}
			}
			return colIndex;
		} catch (Exception e) {
			log.error("Error in <getColumnIndex> " + e.getMessage());
			return 1;
		}
	}
}