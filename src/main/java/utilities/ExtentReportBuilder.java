package utilities;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import automationEngine.ApplicationSetup;
import library.DateFormatUtilities;

public class ExtentReportBuilder {
	public static String ReportsLocation = "src/main/resources/ExtentReports/Html Reports/";
	public static String screenshotExtentReportLocation = "ExtentReports/ScreenShots/";
	private static StringBuffer sb = new StringBuffer(4000);
	private static String HeaderOpen = "";
	private static String HeaderClose = "";
	private static String BodyOpen = "";
	private static String BodyClose = "";
	private static String WindowResizeFunction = "";
	private static String ReportFunction;
	private static String TesterName = "";
	// String declaration for Test Step HTML building
	public static String TestsTableHeader = "";
	private static String TestImage = "";
	private static String TestMethodName = "";
	private static String oldTestMethodName = "";
	public static String first = "";
	private static String TestMethodStatus = "Pass";
	public static String TestMethodStatus1 = "";
	public static int TestMethodCount = 1;
	private static String TestStepStatus = "";
	private static String TestStepStatus1 = "";

	private static int TestStepCount = 0;
	private static int PassStepCount = 0;
	private static int FailStepCount = 0;
	private static int WarningStepCount = 0;
	private static int ErrorStepCount = 0;

	public static int totalTestsCount = 0;
	public static int totalPassCount = 0;
	public static int totalFailCount = 0;
	public static int totalWarningCount = 0;
	public static int totalErrorCount = 0;

	public static String HeaderCloseTemp;
	public static String TestImageTemp;
	private static String TestMethodSummary;
	private static String ReportsPath;
	private static String ImageFileName;
	private static String ReportFunctionTemp;
	private static java.util.Date ExecutionStartTime;
	protected static Date SuiteExecutionStartTime;
	public static String MasterReportLine;
	private static String TestDuration;
	private static String TestSuiteDuration;
	private static java.util.Date ExecutionEndTime;
	private static Date SuiteExecutionEndTime;
	private static String ReportFolderName;
	private static String TestImageStatus;
	private static String MastertxtFileName;
	private static String MasterhtmlFileName;
	private static String ReporttxtFileName;
	private static String ReporthtmlFileName;
	private static String TestStepExeStatus;
	private static java.util.Date ExecutionPreviousEndTime;
	private static Date SuiteExecutionPreviousEndTime;
	private static String TestDurationOld;
	private static String TestSuiteDurationOld;
	private static String ReporthtmlFileSubpath;
	private static int ittCount;
	private static String MasterReportsPath;
	private static String PieChartSummary;
	private static String MasterPieChartSummary;
	private static int testCounter = 1;
	private static final org.apache.log4j.Logger logger = LogManager.getLogger(ExtentReportBuilder.class);

	public static String APIDeployedVersion;
	static ExtentHtmlReporter htmlReporter;
	static ExtentReports extent;
	static ExtentTest test;
	static String extentReportFolder;
	static String errorMessage = "Method Execution Failed";
	private static String logsMessage;
	static String description;
	
	public static int chromeTC = 0;
	public static int firefoxTC = 0;
	

	private static void CreateBaseSyntax() {
		HeaderOpen = "<!-- saved from url=(0014)about:internet \\x0D\\x0A --> \n<!DOCTYPE html>\r\n<html>\r\n<title>Automation Test Results</title>\r\n<head>\r\n    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js\"></script> <script type=\"text/javascript\"> \r\n        var windowHeight = $(window).height();\r\n        $(window).load(function () {\r\n            $(this).resize();\r\n            Report('step1');\r\n        });\r\n\r\n";
		HeaderClose = "\r\n</script>\r\n    <style type=\"text/css\">\r\n        tbody.grid {\r\n            overflow: auto;\r\n        }\r\n    </style>\r\n</head>\r\n";
		BodyOpen = "\r\n<body bgcolor=\"#E0E0E0\">\r\n    <table id=\"Header\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=100% height=2% valign=\"top\">\r\n        <tr align='center'><td bgcolor=\"#FF9966\"><font size=\"5\"> <b>Automation Test Results</b></font></td></tr>\r\n    </table>\r\n\r\n\r\n    <!-- Test Case Header Table -->\r\n    <table id=\"Summary1\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=100% height=4%>\r\n        <tr width=25% height=2%>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Test Case ID</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\">Test ID</td>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Executed By</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><b>Tester ID</b></td>\r\n        </tr>\r\n        <tr width=25% height=2%>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Execution Started On</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\">Execution start time</td>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Environment</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\">Test Environment</td>\r\n        </tr>\r\n    </table>\r\n";
		BodyClose = "\r\n</body>\r\n</html>";
		WindowResizeFunction = "$(window).resize(function ()\r\n        {\r\n            $(\"#Header\").height(5 * $(window).height() / 100);\r\n            $(\"#DescriptionHeader\").height(2 * $(window).height() / 100);\r\n            $(\"#TestDescription\").height(10 * $(window).height() / 100);\r\n            $(\"#ResultHeader\").height(2 * $(window).height() / 100);\r\n            $(\"#Result\").height(10 * $(window).height() / 100);            \r\n            $(\"#Footer\").height(2 * $(window).height() / 100);\r\n            $(\"#tabscreenshot\").height(45 * $(window).height() / 100);\r\n            $(\"#tblBody\").height(45 * $(window).height() / 100);\r\n            $(\"#DescriptionText\").height(10 * $(window).height() / 100);\r\n            $(\"#ExpectedText\").height(10 * $(window).height() / 100);\r\n            $(\"#ActualText\").height(10 * $(window).height() / 100);\r\n\t\t\t$(\"#Summary1\").height(4*$(window).height()/100);\r\n\t\t\t$(\"#summary2\").height(5*$(window).height()/100);\r\n            // Steps will be added here\r\n//Insert\r\n        });";
		TestStepStatus = "\r\n    <!-- Test case step wise results header -->\r\n    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=100% height=50%>\r\n        <tr id=\"Body\" width=100% height=50% style=\"cursor:pointer\">\r\n            <td width=70% valign=\"top\">\r\n                <div id=\"tblBody\" style=\"overflow:scroll;\">\r\n                    <table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">\r\n                        <thead>\r\n                            <tr bgcolor=\"#505050\">\r\n                                <th><font width=\"25%\" color=\"#FFFFFF\">Step Name</font></th>\r\n                                <th><font width=\"15%\" color=\"#FFFFFF\">Status</font></th>\r\n                                <th><font width=\"25%\" color=\"#FFFFFF\">Test Data</font></th>\r\n                                <th><font color=\"#FFFFFF\">Exec Time</font></th>\r\n                                </FONT>\r\n                            </tr>\r\n                        </thead>\r\n       <!-- Test case step wise results details -->\r\n                        <tbody class=\"grid\">\r\n                            <!--  <tr id=\"step1\" onclick=\"Report('step1')\" bgColor=#C0C0C0>\r\n                                <td width=\"15%\" align=\"center\">Step 1</td>\r\n                                <td align=\"center\" width=\"10%\"><font color=\"#009933\"><b>Passed</b></font></td>\r\n                                <td align=\"center\">Data 1</br></td>\r\n                                <td align=\"center\">Step 1 Execution time</td>\r\n                            </tr>-->\r\n                            <!-- InsertStep -->";
		TestImageStatus = "\r\n\r\n                            <!-- Test step details table rows end -->\r\n                        </tbody>\r\n                    </table>\r\n                </div>\r\n            </td>\r\n            <!-- Test steps end -->\r\n\r\n            <!-- Test Step screenshots header -->\r\n            <td id=\"tabscreenshot\" width=30% valign='top' align=\"center\" bgcolor='#E0E0E0'>\r\n               \t\t\t\t               \r\n\t\t\t\t<!-- InsertImage -->";
		ReportFunction = "\r\nfunction Report(strStepID)\r\n        {\r\n            \r\n//Insert \r\n\r\n\r\n        }";
		TestMethodSummary = "\r\n    <table id=\"summary2\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\" height=\"10%\">\r\n\t\t<tr width=25% height=2%>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Total No of Steps</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><b><!-- StepCount -->Step Count</b></font></td>\r\n\t\t\t<td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Test Run Status</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><font color=\"#009933\"><b>Execution Status</b></font></td>\r\n        </tr>\r\n        <tr width=25% height=2%>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>No of Steps Passed</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><b><!-- PassCount -->PassedTets</b></td>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>No of Steps Failed</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><b><!-- FailCount -->FailedTests</b></td>\r\n        </tr>\r\n    <tr width=25% height=2%>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>No of Warnings</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><b><!-- WarningsCount -->WarningCount</b></font></td>\r\n\t\t\t<td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>No of Errors</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><b><!-- ErrorsCount -->ErrorCount</b></td>\r\n        </tr>    <tr width=25% height=2%>\r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Execution Duration</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><!-- Duration -->Test Duration</td>            \r\n            <td bgcolor=\"#505050\"><font color=\"#FFFFFF\"><b>Execution End Time</b></font></td>\r\n            <td align='center' bgcolor=\"#C0C0C0\"><!-- EndTime -->Test End Time</td>\r\n        </tr>\r\n\t\t\r\n    </table>\r\n  <table>\r \n <tr WIDTH=\"100%\"> \r \n <td WIDTH=\"40%\"> \r \n <div id=\"piechart\" ALIGN=CENTER ></div>\r \n </td> \r \n <td WIDTH=\"60%\"> \r \n <div id=\"consoleLogs\" style=\"overflow: auto; height: auto;\">\r <!-- APIRequest --> \n<b>Console Error Logs </b> \n <font color=\"#DC143C\"> \n <!-- ErrorLogs --></font> \r \n</div> \r \n </td> \r \n </table>  \r\n    <table id=\"Footer\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=100% height=2%>\r\n        <tr align='center'><td bgcolor=\"#FF9966\"><b>&copy;StockManager Automation</b></td>\r\n    </table>\r\n";
		PieChartSummary = "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script><script type=\"text/javascript\">\r\n google.charts.load('current', {'packages':['corechart']});\r\n google.charts.setOnLoadCallback(drawChart); \r\n function drawChart() { \r\n var data = google.visualization.arrayToDataTable([['Status','Counts'],['Pass',PassPieCount],['Fail',FailPieCount],['Warning',WarningPieCount],['Error',ErrorPieCount]]); var options = {'title':'Title of Report','is3D':true,  'width':400, 'height':250,  backgroundColor: 'transparent',slices: {0:{ color:'#7cfc00'}, 1:{color: '#ff2500'},2:{color: '#00f7ff'},3:{color: '#300000'} }};  var chart = new google.visualization.PieChart(document.getElementById('piechart')); chart.draw(data, options);}</script></script> <script type=\"text/javascript\"> \r\n        var windowHeight = $(window).height();\r\n        $(window).load(function () {\r\n            $(this).resize();\r\n            Report('step1');\r\n        });";
		MasterPieChartSummary = "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script><script type=\"text/javascript\">\r\n google.charts.load('current', {'packages':['corechart']});\r\n google.charts.setOnLoadCallback(drawChart); \r\n function drawChart() { \r\n var data = google.visualization.arrayToDataTable([['Status','Counts'],['Pass',PassPieCount],['Fail',FailPieCount],['Warning',WarningPieCount],['Error',ErrorPieCount]]); var options = {'title':'Title of Report','is3D':true,  'width':400, 'height':250,  backgroundColor: 'transparent',slices: {0:{ color:'#7cfc00'}, 1:{color: '#ff2500'},2:{color: '#00f7ff'},3:{color: '#300000'} }};  var chart = new google.visualization.PieChart(document.getElementById('masterpiechart')); chart.draw(data, options);}</script></script> <script type=\"text/javascript\"> \r\n        var windowHeight = $(window).height();\r\n        $(window).load(function () {\r\n            $(this).resize();\r\n            Report('step1');\r\n        });";

	}

	public static final DateFormat getDateTimeFormat(String format) {
		return new SimpleDateFormat(format);
	}

	private static Date GetSystemDate() throws ParseException, NullPointerException {
		// get current date time with Date()
		Date date = new Date();
		getDateTimeFormat("MM-dd-yyyy HH-mm-ss").format(date);
		try {
			return date;
		} catch (Exception e) {
			return null;
		}
	}

	public static void WriteToFile(String filePath, String fileContent) throws IOException, NullPointerException {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File file = new File(filePath);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				if (!file.createNewFile()) {
					System.out.println("File not found");
				}
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(fileContent);

		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			if (bw == null) {
				System.out.println("bw is null");
			} else {
				bw.close();
			}
			if (fw == null) {
				System.out.println("fw is null");
			} else {
				fw.close();
			}
		}
	}

	public static String ReadFileContent(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		StringBuilder sb = new StringBuilder();
		String everything = "";
		try {
			
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
		} finally {
			br.close();
		}
		return everything;

	}

	private static String renameToHtml(String source) {
		String target;
		String currentExtension = getFileExtension(source);

		if (currentExtension.equals("")) {
			target = source + "." + "html";
		} else {
			target = source.replaceFirst(Pattern.quote("." + currentExtension) + "$",
					Matcher.quoteReplacement("." + "html"));

		}
		if (!new File(source).renameTo(new File(target))) {
			System.out.println("File not found");
		}
		return target;
	}

	private static String renameToTxt(String source) {
		String target;
		String currentExtension = getFileExtension(source);

		if (currentExtension.equals("")) {
			target = source + "." + "html";
		} else {
			target = source.replaceFirst(Pattern.quote("." + currentExtension) + "$",
					Matcher.quoteReplacement("." + "txt"));

		}
		if (!new File(source).renameTo(new File(target))) {
			System.out.println("File not found");
		}
		return target;

	}

	private static String getFileExtension(String f) {
		String ext = "";
		int i = f.lastIndexOf('.');
		if (i > 0 && i < f.length() - 1) {
			ext = f.substring(i + 1);
		}
		return ext;
	}

	private static String AddReportFunctionStep(String stepName, String status, String testData, String imageTitle) {
		@SuppressWarnings("unused")
		String stepCode = "Step " + TestStepCount;
		String stepId = "step" + TestStepCount;
		String scrstepId = "scrstep" + TestStepCount;
		String imageCode = "link" + TestStepCount;
		String imageId = "Image" + TestStepCount;

		String reportStep = "\nif (strStepID == \"step1\")" + "\n{" + "\n$(\"#scrstep-1\").show();"
				+ "\n$(\"#step1\").css(\"background-color\", \"#FBF0F4\");"
				+ "\ndocument.getElementById(\"link1\").href = document.getElementById(\"Image1\").src;" + "\n}"
				+ "\nelse" + "\n{" + "\n$(\"#scrstep-1\").hide();"
				+ "\n$(\"#step1\").css(\"background-color\", \"#C0C0C0\");" + "\n}\n//ReportFunction";
		reportStep = reportStep.replace("step1", stepId);
		reportStep = reportStep.replace("scrstep-1", scrstepId);
		// reportStep = reportStep.Replace("step1", stepId);
		reportStep = reportStep.replace("link1", imageCode);
		reportStep = reportStep.replace("Image1", imageId);

		return reportStep;

	}

	public static void ReportInitialization(String TestName) throws NullPointerException, ParseException {
		logger.info(TestName);
		
			test = extent.createTest(TestName, "");
			
			ExecutionStartTime = GetSystemDate();
			CreateBaseSyntax();
		
		// Create folder for report generation

		String myDocumentsPath = ReportsLocation;
		File reportParentDir = new File(myDocumentsPath);
		if (Files.notExists(reportParentDir.toPath())) {
			reportParentDir.mkdir();
		}

		Date temp = GetSystemDate();
		String temp1 = getDateTimeFormat("MM-dd-yyyy").format(temp);
		String toDatePath = myDocumentsPath  + temp1.replace(" ", "");
		File reportParentDateDir = new File(toDatePath);
		if (Files.notExists(reportParentDateDir.toPath())) {
			reportParentDateDir.mkdir();
		}

		ReportFolderName = toDatePath + "/" + "-"
				+ ExecutionStartTime.toString().trim().replace(":", "").replace(" ", "");
		ReportsPath = ReportFolderName;
		MasterReportsPath = toDatePath;
		ReporthtmlFileSubpath = ExecutionStartTime.toString().trim().replace(":", "").replace(" ", "")
				+ "/";

		File reportDir = new File(ReportFolderName);
		if (Files.notExists(reportDir.toPath())) {
			reportDir.mkdir();
		}
		try {
			File imageFile = new File("src/main/resources/Config/automationLogo.png");
			File destination = new File(reportParentDateDir + "/" + imageFile.getName());
			FileUtils.copyFile(imageFile, destination);
		} catch (Exception e) {
			Logger.getLogger(e.getMessage());
		}
		testCounter++;

	}
	public static void ReportTestStep(String stepName, String status, String testData)
			throws IOException, ParseException {
		ExecutionEndTime = GetSystemDate();
		TestStepExeStatus = status;

		if (status.equalsIgnoreCase("FAIL")) {
			TestStepCount++;
			testLog(stepName);
			test.log(Status.FAIL, MarkupHelper
					.createLabel(stepName + " FAILED " + System.lineSeparator() + testData, ExtentColor.RED));
			test.info("Screenshot Details", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen("")).build());
		} else if (status.equalsIgnoreCase("PASS")) {
			TestStepCount++;
			test.log(Status.PASS, MarkupHelper
					.createLabel(stepName + " PASSED " + System.lineSeparator() + testData, ExtentColor.GREEN));
		} else if (status.equalsIgnoreCase("WARNING")) {
			TestStepCount++;
			test.log(Status.WARNING, MarkupHelper
					.createLabel(stepName + " WARNING " + System.lineSeparator() + testData, ExtentColor.ORANGE));
			test.info("Screenshot Details", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen("")).build());
		} else if (status.equalsIgnoreCase("ERROR")) {
			TestStepCount++;
			test.log(Status.ERROR, MarkupHelper
					.createLabel(stepName + " ERROR " + System.lineSeparator() + testData, ExtentColor.PURPLE));
			test.info("Screenshot Details", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen("")).build());
		}
		
	}

	

	public static void ReportTestStep(String stepName, String status, String testData, String imageTitle)
			throws IOException, ParseException {
		ExecutionEndTime = GetSystemDate();
		TestStepExeStatus = status;
		if (status.equalsIgnoreCase("FAIL")) {
			TestStepCount++;
			testLog(stepName);
			test.log(Status.FAIL, MarkupHelper
					.createLabel(stepName + " FAILED " + System.lineSeparator() + testData, ExtentColor.RED));
		} else if (status.equalsIgnoreCase("PASS")) {
			TestStepCount++;
			test.log(Status.PASS, MarkupHelper
					.createLabel(stepName + " PASSED " + System.lineSeparator() + testData, ExtentColor.GREEN));
		} else if (status.equalsIgnoreCase("WARNING")) {
			TestStepCount++;
			test.log(Status.WARNING, MarkupHelper
					.createLabel(stepName + " WARNING " + System.lineSeparator() + testData, ExtentColor.ORANGE));
		} else if (status.equalsIgnoreCase("ERROR")) {
			TestStepCount++;
			test.log(Status.ERROR, MarkupHelper
					.createLabel(stepName + " ERROR " + System.lineSeparator() + testData, ExtentColor.PURPLE));
		}
		test.info("Screenshot Details", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen("")).build());
	}


	/**
	 * 
	 * @author Abirami
	 * @throws ParseException
	 * @throws IOException
	 * @purpose - Overloaded method for ReportTestStep with Actual and Expected data
	 * @note
	 */
	public static void ReportTestStep(String stepName, String status, String actualData, String expectedData,
			String imageTitle) throws IOException, ParseException {
		ReportTestStep(stepName, status, "Actual: " + actualData + " Expected: " + expectedData, imageTitle);
	}

	private static String AddStepStatus(String stepName, String status, String testData, String imageTitle) {
		@SuppressWarnings("unused")
		String stepCode = "Step " + TestStepCount;
		String stepId = "step" + TestStepCount;
		@SuppressWarnings("unused")
		String scrstepId = "scrstep" + TestStepCount;
		@SuppressWarnings("unused")
		String imageCode = "link" + TestStepCount;
		@SuppressWarnings("unused")
		String imageId = "Image" + TestStepCount;

		// Add Step Status to Table

		String stepString = "\n<tr id=\"step1\" onclick=\"Report('step1')\" bgColor=#C0C0C0>"
				+ "\n<td width=\"25%\" align=\"center\">Step Name</td>"
				+ "\n<td align=\"center\" width=\"10%\"><font color=\"#4169E1\">Status</font></td>"
				+ "\n<td align=\"center\">Data</br></td>" + "\n<td align=\"center\">Step Execution time</td>"
				+ "\n</tr>\n<!-- InsertStep -->";

		stepString = stepString.replace("step1", stepId);
		stepString = stepString.replace("Step Name", stepName);
		stepString = stepString.replace("Status", status);
		stepString = stepString.replace("Data", testData);
		stepString = stepString.replace("Step Execution time", ExecutionEndTime.toString());

		if (status.equalsIgnoreCase("Fail")) {
			stepString = stepString.replace("#4169E1", "#DC143C");
		}
		if (status.equalsIgnoreCase("Pass")) {
			stepString = stepString.replace("#4169E1", "#009933");
		}
		if (status.equalsIgnoreCase("Warning")) {
			stepString = stepString.replace("#4169E1", "#00f7ff");
		}
		if (status.equalsIgnoreCase("Error")) {
			stepString = stepString.replace("#4169E1", "#800303");
		}

		return stepString;

	}

	@SuppressWarnings("unused")
	private static void GenerateReport() throws IOException, ParseException, NullPointerException {
		String reportData = BuildReport();
		if (TestStepExeStatus.equalsIgnoreCase("Pass"))
			reportData = reportData + "\n<!-- ********** ,1,1,0,0,0 -->";
		if (TestStepExeStatus.equalsIgnoreCase("Fail"))
			reportData = reportData + "\n<!-- ********** ,1,0,1,0,0 -->";
		if (TestStepExeStatus.equalsIgnoreCase("Warning"))
			reportData = reportData + "\n<!-- ********** ,1,0,0,1,0 -->";
		if (TestStepExeStatus.equalsIgnoreCase("Error"))
			reportData = reportData + "\n<!-- ********** ,1,0,0,0,1 -->";

		// Generate Report File Name
		String buildFileTime = "Iteration" + String.valueOf(GetSystemDate()).replace(":", "").replace(" ", "").trim();
		if (buildFileTime == null) {
			System.out.println("File is not found");
		}


		ReporttxtFileName = ReportsPath + "/" + buildFileTime + ".txt";

		// Write code in Report File
		WriteToFile(ReporttxtFileName, reportData);

		// convert text file to HTML

		ReporthtmlFileName = renameToHtml(ReporttxtFileName);

		ReporthtmlFileSubpath = ReporthtmlFileSubpath + buildFileTime + ".html";
		ExecutionPreviousEndTime = ExecutionEndTime;
		TestDurationOld = TestDuration;
		WriteToMasterReport();

	}

	@SuppressWarnings("unused")
	private static void WindowResizeFunction(int stepNumber) {
		String appendScrstepString = "\n$(\"#scrstep" + stepNumber + "\").height(45 * $(window).height() / 100);"
				+ "\n //InsertResize";

		WindowResizeFunction = WindowResizeFunction.replace("//Insert", appendScrstepString);
	}

	private static void ReportTestStepImage(String imageName) throws IOException {
		String imageFilePath = "";
		if (!imageName.equals("")) {

			ImageFileName = ReportsPath + "/" + imageName + "_Step" + TestStepCount + ".png";
			CaptureScreen(ImageFileName);
			imageFilePath = imageName + "_Step" + TestStepCount + ".png";
		}
		String testImageTemp = "\t\t\t\r\n\t\t\t<div id=\"scrstep\" style=\"background-color:#E0E0E0; overflow:auto;\">\r\n                    <!-- Test Step screen shot details -->\r\n\t\t\t\t\t<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\">\t\t\t\t\t\r\n                        <thead>                            \r\n                            <tr bgcolor=\"#505050\">\r\n                                <th><font color=\"#FFFFFF\">ScreenShots for Step</font></th>\r\n                            </tr>\r\n                        </thead>\r\n                        <!-- Test step screen shot image description -->\r\n                       <tr height=10px bgcolor='#A0A0A0'><td align='center'>Title</td></tr>\r\n                        <tr>\r\n                            <td width='22%' height=300px bgcolor='#C0C0C0' valign='top' align='center'>\r\n                                <a id=\"link1\" target=\"_blank\"><img id=\"Image1\" width=\"350\" src= \"imagepath\" height=\"280\" complete=\"complete\" /> </a>\r\n                            </td>\r\n                        </tr>\r\n                    </table>\r\n                </div>";
		testImageTemp = testImageTemp.replace("scrstep", "scrstep" + TestStepCount);
		testImageTemp = testImageTemp.replace("Title", imageName + "_Step" + TestStepCount);

		testImageTemp = testImageTemp.replace("imagepath", imageFilePath);
		testImageTemp = testImageTemp.replace("link1", "link" + TestStepCount);
		testImageTemp = testImageTemp.replace("Image1", "Image" + TestStepCount);

		TestImage = testImageTemp;
	}

	private static String CaptureScreen(String imagePath) throws IOException {

		String format;
		String fileName;
		
			format="png";
			fileName = System.getProperty("user.dir") + "/" + screenshotExtentReportLocation + "Screenshot_"
					+ CommonUtilities.generateRandomString() + "." + format;
		
		if (imagePath == null || imagePath.isEmpty())
			imagePath = fileName;
		File scrFile = null;
		try {
			scrFile = ((TakesScreenshot) ApplicationSetup.driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy somewhere
			
				File DestFile = new File(imagePath);
				com.google.common.io.Files.copy(scrFile, DestFile);
				imagePath = "../" + screenshotExtentReportLocation + "Screenshot_" + CommonUtilities.generateRandomString()+ "." + format;;
			
		} catch (TimeoutException seleniumTimeout) {
			logger.error("time out occurred", seleniumTimeout);

		}
		
		return imagePath;
	}

	private static void CreateBody() throws NullPointerException, ParseException {
		// Calculate Test Duration
		TestDuration = CalculateTestDuration();

		// insert step step status
		TestStepStatus = TestStepStatus.replace("<!-- InsertStep -->", TestStepStatus1);
		
		// insert step step image
		TestImageStatus = TestImageStatus.replace("<!-- InsertImage -->", TestImage);
		TestImageStatus = TestImageStatus + "\n<!-- InsertImage -->";
		TestImageStatus = TestImageStatus
				+ "                \r\n            </td>\r\n        </tr>\r\n    </table>\r\n\r\n";

		// insert test summary
		TestMethodSummary = TestMethodSummary.replace("PassedTets", Integer.toString(PassStepCount));
		TestMethodSummary = TestMethodSummary.replace("FailedTests", Integer.toString(FailStepCount));
		TestMethodSummary = TestMethodSummary.replace("WarningCount", Integer.toString(WarningStepCount));
		TestMethodSummary = TestMethodSummary.replace("ErrorCount", Integer.toString(ErrorStepCount));
		TestMethodSummary = TestMethodSummary.replace("Execution Status", TestMethodStatus);
		TestMethodSummary = TestMethodSummary.replace("Test End Time", String.valueOf(GetSystemDate()));
		if (TestMethodStatus.equalsIgnoreCase("Fail"))
			TestMethodSummary = TestMethodSummary.replace("#009933", "#dc143c");
		TestMethodSummary = TestMethodSummary.replace("Step Count", Integer.toString(TestStepCount));
		TestMethodSummary = TestMethodSummary.replace("Test Duration", TestDuration);

		// insert steps in report function
		ReportFunction = ReportFunction.replace("//Insert", ReportFunctionTemp);
		// pieChart
		PieChartSummary = PieChartSummary.replace("Title of Report", TestMethodName);
		PieChartSummary = PieChartSummary.replace("PassPieCount", Integer.toString(PassStepCount));
		PieChartSummary = PieChartSummary.replace("FailPieCount", Integer.toString(FailStepCount));
		PieChartSummary = PieChartSummary.replace("WarningPieCount", Integer.toString(WarningStepCount));
		PieChartSummary = PieChartSummary.replace("ErrorPieCount", Integer.toString(ErrorStepCount));

	}

	private static String CalculateTestDuration() {
		Date dateStart = ExecutionStartTime;
		Date dateStop = ExecutionEndTime;

		// Get msec from each, and subtract.
		long diff = dateStop.getTime() - dateStart.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);

		TestDuration = diffHours + ":" + diffMinutes + ":" + diffSeconds;
		return TestDuration.trim();
	}

	/**
	 * @author Abirami
	 * @purpose To Calculate overall Test Suite Duration
	 * @param
	 * @note
	 */
	private static String CalculateTestSuiteDuration() {
		Date dateStart = SuiteExecutionStartTime;
		Date dateStop = SuiteExecutionEndTime;

		// Get msec from each, and subtract.
		long diff = dateStop.getTime() - dateStart.getTime();
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);

		String TestSuiteDuration = diffHours + ":" + diffMinutes + ":" + diffSeconds;
		return TestSuiteDuration.trim();
	}

	private static String BuildSummaryString(String inputString) {
		String[] arrayString = inputString.split(" ");

		String[] statusString = arrayString[1].split(",");

		String dummyChar = "*";

		int totalTests = Integer.parseInt(statusString[1]);
		totalTests = totalTests + 1;
		int totalPassed = Integer.parseInt(statusString[2]);

		if (TestStepExeStatus.equalsIgnoreCase("Pass"))
			totalPassed = totalPassed + 1;

		int totlaFailed = Integer.parseInt(statusString[3]);
		if (TestStepExeStatus.equalsIgnoreCase("Fail"))
			totlaFailed = totlaFailed + 1;

		int totalWarning = Integer.parseInt(statusString[4]);
		if (TestStepExeStatus.equalsIgnoreCase("Warning"))
			totalWarning = totalWarning + 1;

		int totalError = Integer.parseInt(statusString[5]);
		if (TestStepExeStatus.equalsIgnoreCase("Error"))
			totalError = totalError + 1;

		String returnString = "<!-- " + dummyChar + "," + totalTests + "," + totalPassed + "," + totlaFailed + ","
				+ totalWarning + "," + totalError + " -->";

		while (returnString.toCharArray().length < 25) {
			dummyChar = dummyChar + "*";
			returnString = "<!-- " + dummyChar + "," + totalTests + "," + totalPassed + "," + totlaFailed + ","
					+ totalWarning + "," + totalError + " -->";

		}
		return returnString;
	}

	private static String BuildMasterSummaryString(String inputString) {
		String[] arrayString = inputString.split(" ");

		String[] statusString = arrayString[1].split(",");

		String dummyChar = "*";

		int totalTests = Integer.parseInt(statusString[1]);
		totalTests = totalTests + 1;
		int totalPassed = Integer.parseInt(statusString[2]);

		if (TestMethodStatus.equalsIgnoreCase("Pass") && (WarningStepCount + ErrorStepCount == 0))
			totalPassed = totalPassed + 1;

		int totlaFailed = Integer.parseInt(statusString[3]);
		if (TestMethodStatus.equalsIgnoreCase("Fail") && (WarningStepCount + ErrorStepCount == 0))
			totlaFailed = totlaFailed + 1;

		int totalWarning = Integer.parseInt(statusString[4]);
		if (WarningStepCount > 0 && ErrorStepCount == 0)
			totalWarning = totalWarning + 1;

		int totalError = Integer.parseInt(statusString[5]);
		if (ErrorStepCount > 0)
			totalError = totalError + 1;

		String returnString = "<!-- " + dummyChar + "," + totalTests + "," + totalPassed + "," + totlaFailed + ","
				+ totalWarning + "," + totalError + " -->";

		while (returnString.toCharArray().length < 25) {
			dummyChar = dummyChar + "*";
			returnString = "<!-- " + dummyChar + "," + totalTests + "," + totalPassed + "," + totlaFailed + ","
					+ totalWarning + "," + totalError + " -->";

		}
		return returnString;
	}

	private static String BuildReport() throws NullPointerException, ParseException {
		CreateBody();

		// Write to master report
		MasterReportLine = TestMethodName + "," + TestMethodStatus + "," + TestDuration + ReporthtmlFileName + ",";

		return HeaderOpen + WindowResizeFunction + ReportFunction + HeaderClose + BodyOpen + TestStepStatus
				+ TestImageStatus + TestMethodSummary + BodyClose + PieChartSummary + MasterPieChartSummary;

	}

	private static void WriteToMasterReport() throws ParseException, IOException {
		// Create folder for report generation
		String myDocumentsPath = ReportsLocation;
		Date temp = GetSystemDate();
		String temp1 = getDateTimeFormat("MM-dd-yyyy").format(temp);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
		Date startDate = dateFormatter.parse(getDateTimeFormat("MM-dd-yyyy").format(SuiteExecutionStartTime));
		Date currDate = dateFormatter.parse(temp1);
		if (currDate.after(startDate)) {
			temp1 = getDateTimeFormat("MM-dd-yyyy").format(startDate);
		}
		String nameFormat =  temp1;
		String subPath = myDocumentsPath + nameFormat;

		// Check for folder existence
		File reportDir = new File(subPath);
		if (Files.notExists(reportDir.toPath())) {
			reportDir.mkdir();
		}
		// Check for file existence
		MastertxtFileName = subPath + "/" + "index" + ".txt";
		MasterhtmlFileName = subPath + "/" + "index" + ".html";

		String hearderFooter = "";
		String methodReportTable = "";
		

		File fileTxt = new File(MastertxtFileName);
		File fileHtml = new File(MasterhtmlFileName);
		if (!fileHtml.exists()) {
			MasterPieChartSummary = MasterPieChartSummary.replace("Title of Report",
					"Automated Tests Execution Status");
			MasterPieChartSummary = MasterPieChartSummary.replace("PassPieCount", "0");
			MasterPieChartSummary = MasterPieChartSummary.replace("FailPieCount", "0");
			MasterPieChartSummary = MasterPieChartSummary.replace("WarningPieCount", "0");
			MasterPieChartSummary = MasterPieChartSummary.replace("ErrorPieCount", "0");
			if (!fileTxt.createNewFile())
				;
			{
				System.out.println("File not found");
			}
			hearderFooter = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n<HTML>\r\n<HEAD>\r\n\t<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=windows-1252\">\r\n\t<TITLE>Automation Test Suite Execution Report</TITLE><link href=\"https://fonts.googleapis.com/css?family=Roboto:400,500,700&display=swap\" rel=\"stylesheet\"><script src=\"https://use.fontawesome.com/9e2d39f50f.js\"></script><STYLE TYPE=\"text/css\">"
					+ "body { background: #f3f6f9; font-family: Open sans; margin: 0; font-family: 'Roboto', sans-serif; }     h1 { text-align: left; background: #fff; padding: 1%; line-height: 50px; box-shadow: 0 2px 6px 0 rgba(0,0,0,0.1); color: rgba(0, 0, 0, 0.3); display: block; float: left; width: 100%; margin-top: 0; box-sizing: border-box; } .left-wrapper { float: left; padding-top: 10px; }  .right-wrapper { float: right; } h1 span { font-size: 13px; line-height: 28px; display: block; }   h1 span a { color: rgba(0, 0, 0, 0.3); }   .card { box-shadow: 0 2px 6px 0 rgba(0,0,0,0.1); transition: 0.3s; width: 48%; border-radius: 5px; margin: 1%; text-align: center; height: 300px; float: left; background: #fff; position: relative; } .card:hover { box-shadow: 0 3px 10px 0 rgba(0,0,0,0.15); } .container { padding: 16px 16px; } .container table td { font-size: 15px; line-height: 25px; padding-right: 10px; padding-bottom: 10px; } .container table td .count { font-size: 24px; font-weight: 700; } /* div#masterpiechart { width: 100%; margin-top: 50px; } */ .container table { padding-top: 0; width: 100%; float: left; } .footer { width: 100%; float: left; background: rgba(239, 239, 239, 0.5); text-align: center; font-size: 12px; padding: 12px 0; position: relative; } .table__col { line-height: 1.5em; -webkit-box-flex: 1; -ms-flex: 1; flex: 1; } .bar { height: 18px; overflow: hidden; display: -webkit-inline-box; display: -ms-inline-flexbox; display: inline-flex; width: 100%; border-radius: 3px; background: #eee; } .bar__fill { display: inline-block; overflow: hidden; height: 18px; color: #fff; font-size: 12px; line-height: 18px; text-align: center; } .bar__fill_status_failed { background: #fd5a3e; } .bar__fill_status_passed { background: #97cc64; } .bar__fill_status_warning { background: #00f7ff; }  .bar__fill_status_error { background: #800303; }  .table-wrapper { width: 98%; float: left; margin: 30px 1%; box-sizing: border-box; background: #fff; padding: 15px 40px; box-shadow: 0 2px 6px 0 rgba(0,0,0,0.1); } .table-wrapper table { width: 100%; font-size: 12px; margin-bottom: 10px; border-bottom: 1px solid #ddd; padding-bottom: 10px; } .table-wrapper table td:nth-child(3) { text-align: left; line-height: 20px; color: #b1b1b1; width: 5%; } .table-wrapper table td:first-child { text-align: left; line-height: 26px; font-size: 14px; font-weight: 600; width: 35%; } .table-wrapper table td { padding-right: 30px; width: 200px; } .table-wrapper table td.bar-lines { max-width: 500px; width: 500px; } .table-wrapper table td .status { line-height: 14px; font-size: 13px; } .table-wrapper table td img { width: 20px; vertical-align: text-bottom; } .table-wrapper table td:last-child { text-align: right; } .table-wrapper table td:nth-child(3) span { font-weight: 600; color: #000; line-height: 14px; } .table-wrapper table td:nth-child(2) a { text-align: left; line-height: 26px; font-size: 14px; font-weight: 600; width: 40%; text-decoration: none; color: #1976d2; } .logo-wrapper { width: 100px; margin-right: 25px; float: left; } a:visited {color: grey !important;}  .status i.fa.fa-check { color: #97cc64; font-size: 24px; } .status i.fa.fa-times { color: #fd5a3e; font-size: 24px; } .status i.fa.fa-exclamation-triangle { color: #00f7ff; font-size: 24px; } .status i.fa.fa-bomb { color: #800303; font-size: 24px; } .container.detail { padding: 90px 16px; } .container.detail i { font-size: 40px; color: rgba(0, 0, 0, 0.1); } .container.detail table tr { text-align: center; } .container.detail table tr td { width: 275px; max-width: 100%; }"
					+ "</STYLE>" + "<BODY LANG='en-US' DIR='LTR'>\r\n" + "  <h1>    \r\n"
					+ "    <div class=\"left-wrapper\">\r\n"
					+ "    <img src=\"automationLogo.png\" class=\"logo-wrapper\">Test Suite Execution Report </div>\r\n"
					+ "    <div class=\"right-wrapper\">        \r\n"
					+ "        <span>URL: <a href=replaceURL> replaceURL</a></span>\r\n"
					+ "    <span><b>replaceVersion</b></span>\r\n" + "    </div>\r\n" + "</h1>"
					+ "  <div class='card'>\r\n" + "    <div class='container detail'>\r\n" + "      <table>\r\n"
					+ "        <tbody>\r\n" + "          <tr>\r\n"
					+ "            <td><i class=\"fa fa-file-code-o\" aria-hidden=\"true\"></i></td>\r\n"
					+ "            <td><i class=\"fa fa-check-square-o\" aria-hidden=\"true\"></i></td>\r\n"
					+ "            <td><i class=\"fa fa-window-close-o\" aria-hidden=\"true\"></i></td>\r\n"
					+ "            <td><i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i></td>\r\n"
					+ "            <td><i class=\"fa fa-bomb\" aria-hidden=\"true\"></i></td>\r\n"
					+ "          </tr>\r\n" + "          <tr>\r\n" + "            <td>\r\n"
					+ "              <div><b>Total Scenarios Executed</b></div>\r\n" + "            </td>\r\n"
					+ "            <td>\r\n" + "              <div><b>Scenarios Passed</b></div>\r\n"
					+ "            </td>\r\n" + "            <td>\r\n"
					+ "              <div><b>Scenarios Failed</b></div>\r\n" + "            </td>\r\n"
					+ "            <td>\r\n" + "              <div><b>Scenarios with Warnings</b></div>\r\n"
					+ "            </td>\r\n" + "            <td>\r\n"
					+ "              <div><b>Scenarios with Errors</b></div>\r\n" + "            </td>\r\n"
					+ "          </tr>\r\n" + "          <tr>\r\n" + "            <td>\r\n"
					+ "              <div class=\"count\" style=\"color: #adadad;\">\r\n"
					+ "                <!-- StepCount -->0</div>\r\n" + "            </td>\r\n" + "            <td>\r\n"
					+ "              <div class=\"count\" style=\"color: #97cc64;\">\r\n"
					+ "                <!-- PassCount -->0</div>\r\n" + "            </td>\r\n" + "            <td>\r\n"
					+ "              <div class=\"count\" style=\"color: #fd5a3e;\">\r\n"
					+ "                <!-- FailCount -->0</div>\r\n" + "            </td>\r\n" + "            <td>\r\n"
					+ "              <div class=\"count\" style=\"color: #00f7ff;\">\r\n"
					+ "                <!-- WarningCount -->0</div>\r\n" + "            </td>\r\n"
					+ "            <td>\r\n" + "              <div class=\"count\" style=\"color: #800303;\">\r\n"
					+ "                <!-- ErrorCount -->0</div>\r\n" + "            </td>\r\n" + "          </tr>\r\n"
					+ "        </tbody>\r\n" + "      </table>\r\n" + "    </div>\r\n" + "  </div>\r\n"
					+ "  <div class='card'>\r\n" + "    <div class='container'>\r\n" + "<table ALIGN=LEFT>\r\n"
					+ "  <tbody>" + "			<tr>\n" + "		  		<td>\n"
					+ "		  		<div> <b>Suite Execution Start Time:</b> \n"
					+ "		  		<!-- ReplaceSuiteExecutionStartTime --></div>\n" + "		  		</td></tr>\n"
					+ "		  		<tr><td>\n" + "		  		<div> <b>Suite Execution End Time:</b> \n"
					+ "		  		<!-- ReplaceSuiteExecutionEndTime --></div>\n" + "		  		</td></tr>\n"
					+ "		  		<tr><td>\n" + "		  		<div> <b>Suite Execution Duration:</b> \n"
					+ "		  		<!-- ReplaceSuiteExecutionDuration --></div>\n" + "		  		</td>\n"
					+ "		  	</tr>" + "</tbody>\r\n" + "  </table>"
					+ "      <div id='masterpiechart' ALIGN=RIGHT></div>\r\n" + "    </div>\r\n"
					+ "  </div> <!-- Test Method Name row -->\r\n" + "  <div class='table-wrapper'>\r\n"
					+ "    <!-- Insert -->\r\n" + "  </div>\r\n" + "  <DIV STYLE='margin-bottom: 0cm'></DIV>\r\n"
					+ "  <div class='footer'>\r\n" + "    <p>©Stock Manager Automation</p>\r\n" + "  </div>\r\n"
					+ "</BODY>" + MasterPieChartSummary + "</HTML><!-- ********** ,0,0,0,0,0 -->";
			SuiteExecutionEndTime = ExecutionEndTime;
			SuiteExecutionPreviousEndTime = SuiteExecutionEndTime;
			TestSuiteDuration = CalculateTestSuiteDuration();
			TestSuiteDurationOld = TestSuiteDuration;
			hearderFooter = hearderFooter.replace("<!-- ReplaceSuiteExecutionStartTime -->",
					"<!-- ReplaceSuiteExecutionStartTime -->" + SuiteExecutionStartTime.toString());
			hearderFooter = hearderFooter.replace("<!-- ReplaceSuiteExecutionEndTime -->",
					"<!-- ReplaceSuiteExecutionEndTime -->" + SuiteExecutionEndTime.toString());
			hearderFooter = hearderFooter.replace("<!-- ReplaceSuiteExecutionDuration -->",
					"<!-- ReplaceSuiteExecutionDuration -->" + TestSuiteDuration);
		} else {
			renameToTxt(MasterhtmlFileName);
			// Read data from Report Function file
			hearderFooter = ReadFileContent(MastertxtFileName);
			hearderFooter = hearderFooter.replace("<!-- New Insert -->", "\n<!-- Insert -->\n");
			SuiteExecutionEndTime = ExecutionEndTime;
			if (SuiteExecutionEndTime.after(SuiteExecutionPreviousEndTime)) {
				hearderFooter = hearderFooter.replace(
						"<!-- ReplaceSuiteExecutionEndTime -->" + SuiteExecutionPreviousEndTime,
						"<!-- ReplaceSuiteExecutionEndTime -->" + SuiteExecutionEndTime.toString());
				TestSuiteDuration = CalculateTestSuiteDuration();
				hearderFooter = hearderFooter.replace("<!-- ReplaceSuiteExecutionDuration -->" + TestSuiteDurationOld,
						"<!-- ReplaceSuiteExecutionDuration -->" + TestSuiteDuration);
				SuiteExecutionPreviousEndTime = SuiteExecutionEndTime;
				TestSuiteDurationOld = TestSuiteDuration;
			}
		}
		

		String scenarioString = "<tr><td>  TestScriptName</td>\r\n"
				+ "				 <td><a target='_blank' href='ReportLink'>\r\n"
				+ "				     <DIV ALIGN=CENTER>TestScenarioCount</DIV></td><td> <span>\r\n"
				+ "				     <!-- StartTime --><!-- TS -->TotalSteps </span><br />Total Steps</td><td class='bar-lines'>\r\n"
				+ "				   <div class='table__col'> \r\n" + "				   <div class='bar'>\r\n"
				+ "				       <div class='bar__fill bar__fill_status_passed' style='flex-grow: 1'>\r\n"
				+ "				         <!-- StartTime --><!-- PS -->StepsPassed</div>\r\n"
				+ "				       <div class='bar__fill bar__fill_status_failed' style='flex-grow: 1'>\r\n"
				+ "				         <!-- StartTime --><!-- FS -->StepsFailed</div>  \r\n"
				+ "<div class='bar__fill bar__fill_status_warning' style='flex-grow: 1'>\r\n"
				+ "				         <!-- StartTime --><!-- WS -->StepsWarning</div>  		\r\n"
				+ "<div class='bar__fill bar__fill_status_error' style='flex-grow: 1'>\r\n"
				+ "				         <!-- StartTime --><!-- ES -->StepsError</div>  							 \r\n"
				+ "				</div>\r\n" + "				   </div></td><td>  <div class='status'>\r\n"
				+ "				     <!-- StartTime --><!-- TMS -->TestMethodStatus  </div>\r\n"
				+ "				 </td> </tr>";
		// New logic table
		if (!oldTestMethodName.equalsIgnoreCase(TestMethodName)) {
			ittCount = 1;
			methodReportTable = "";
			methodReportTable = " <table>\r\n" + "    <tbody>\r\n" + "      <!-- InsertScenario -->\r\n"
					+ "    </tbody>\r\n" + "  </table>\r\n" + "  <!-- New Insert -->\r\n";
			methodReportTable = methodReportTable.replace("<!-- InsertScenario -->", scenarioString);
		} else {
			ittCount = ittCount + 1;
			methodReportTable = "";
			methodReportTable = scenarioString;
		}

		methodReportTable = methodReportTable.replace("TestScriptName", TestMethodName);
		methodReportTable = methodReportTable.replace("TestScenarioCount", "Iteration #" + ittCount);
		methodReportTable = methodReportTable.replace("TotalSteps", Integer.toString(TestStepCount));
		methodReportTable = methodReportTable.replace("StepsPassed", Integer.toString(PassStepCount));
		methodReportTable = methodReportTable.replace("StepsFailed", Integer.toString(FailStepCount));
		methodReportTable = methodReportTable.replace("StepsWarning", Integer.toString(WarningStepCount));
		methodReportTable = methodReportTable.replace("StepsError", Integer.toString(ErrorStepCount));
		methodReportTable = methodReportTable.replace("TestDuration", TestDuration);
		methodReportTable = methodReportTable.replace("ReportLink", ReporthtmlFileSubpath);
		methodReportTable = methodReportTable.replace("StartTime", ExecutionStartTime.toString().trim());

		PieChartSummary = PieChartSummary.replace("Title of Report", TestMethodName);
		PieChartSummary = PieChartSummary.replace("PassPieCount", Integer.toString(PassStepCount));
		PieChartSummary = PieChartSummary.replace("FailPieCount", Integer.toString(FailStepCount));
		PieChartSummary = PieChartSummary.replace("WarningPieCount", Integer.toString(WarningStepCount));
		PieChartSummary = PieChartSummary.replace("ErrorPieCount", Integer.toString(ErrorStepCount));

		if (TestMethodStatus.equalsIgnoreCase("Pass") && (WarningStepCount + ErrorStepCount == 0)) {
			methodReportTable = methodReportTable.replace("TestMethodStatus",
					"<i class=\"fa fa-check\" aria-hidden=\"true\"></i>");
		} else if (TestMethodStatus.equalsIgnoreCase("Fail") && (WarningStepCount + ErrorStepCount == 0)) {
			methodReportTable = methodReportTable.replace("TestMethodStatus",
					"<i class=\"fa fa-times\" aria-hidden=\"true\"></i> ");
			methodReportTable = methodReportTable.replace("#00b050", "#FF0000");
		} else if (WarningStepCount > 0 && ErrorStepCount == 0) {
			methodReportTable = methodReportTable.replace("TestMethodStatus",
					"<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>");
		} else if (ErrorStepCount > 0) {
			methodReportTable = methodReportTable.replace("TestMethodStatus",
					"<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>");
		}

		// methodReportTable = methodReportTable + "\n<P></P> \n<!-- New Insert -->\n";

		if (!oldTestMethodName.equalsIgnoreCase(TestMethodName)) {
			oldTestMethodName = TestMethodName;
			hearderFooter = hearderFooter.replace("<!-- Insert -->", methodReportTable);
		} else {
			hearderFooter = hearderFooter.replace("<!-- " + TestMethodName + " -->", methodReportTable);
		}

		// Write Report to file
		WriteToFile(MastertxtFileName, hearderFooter);
		hearderFooter = "";
		// convert text file to HTML
		renameToHtml(MastertxtFileName);

	}

	@SuppressWarnings("unused")
	private static void UpdateSummaryTable() throws IOException {
		renameToTxt(ReporthtmlFileName);
		String reportFileTemp = ReadFileContent(ReporttxtFileName);

		// Read Last 25 chars of file
		String last25 = reportFileTemp.substring(reportFileTemp.length() - 25);

		int totalCount = GetExistingCount(last25, "Total");
		int passCount = GetExistingCount(last25, "Pass");
		int failCount = GetExistingCount(last25, "Fail");
		int warningCount = GetExistingCount(last25, "Warning");
		int errorCount = GetExistingCount(last25, "Error");

		int PiepassCount = passCount;
		int PiefailCount = failCount;

		String tc = "<!-- StepCount -->" + totalCount;
		String pc = "<!-- PassCount -->" + passCount;
		String fc = "<!-- FailCount -->" + failCount;
		String wc = "<!-- WarningsCount -->" + warningCount;
		String ec = "<!-- ErrorsCount -->" + errorCount;

		String tcn = fc;
		String pcn = pc;
		String fcn = fc;
		String wcn = wc;
		String ecn = ec;

		String Piepc = "['Pass'," + passCount;
		String Piefc = "['Fail'," + failCount;
		String Piewc = "['Warning'," + warningCount;
		String Pieec = "['Error'," + errorCount;

		String pcnPie = Piepc;
		String fcnPie = Piefc;
		String wcnPie = Piewc;
		String ecnPie = Pieec;

		if (TestStepExeStatus.equalsIgnoreCase("Pass")) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			pcn = "<!-- PassCount -->" + (passCount + 1);
			pcnPie = "['Pass'," + (PiepassCount + 1);
		}

		if (TestStepExeStatus.equalsIgnoreCase("Fail")) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			fcn = "<!-- FailCount -->" + (failCount + 1);
			fcnPie = "['Fail'," + (PiefailCount + 1);
		}

		if (TestStepExeStatus.equalsIgnoreCase("Warning")) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			wcn = "<!-- WarningsCount -->" + (warningCount + 1);
			wcnPie = "['Warning'," + (warningCount + 1);
		}

		if (TestStepExeStatus.equalsIgnoreCase("Error")) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			ecn = "<!-- ErrorsCount -->" + (errorCount + 1);
			ecnPie = "['Error'," + (errorCount + 1);
		}

		reportFileTemp = reportFileTemp.replace(tc, tcn);
		reportFileTemp = reportFileTemp.replace(pc, pcn);
		reportFileTemp = reportFileTemp.replace(fc, fcn);
		reportFileTemp = reportFileTemp.replace(wc, wcn);
		reportFileTemp = reportFileTemp.replace(ec, ecn);
		reportFileTemp = reportFileTemp.replace(Piepc, pcnPie);
		reportFileTemp = reportFileTemp.replace(Piefc, fcnPie);
		reportFileTemp = reportFileTemp.replace(Piewc, wcnPie);
		reportFileTemp = reportFileTemp.replace(Pieec, ecnPie);
		reportFileTemp = reportFileTemp.replace("<!-- EndTime -->" + ExecutionPreviousEndTime,
				"<!-- EndTime -->" + ExecutionEndTime);
		ExecutionPreviousEndTime = ExecutionEndTime;
		reportFileTemp = reportFileTemp.replace("<!-- Duration -->" + TestDuration,
				"<!-- Duration -->" + CalculateTestDuration());

		reportFileTemp = reportFileTemp.replace(last25, BuildSummaryString(last25));

		if (TestStepExeStatus.equalsIgnoreCase("Fail")) {
			reportFileTemp = reportFileTemp.replace("<font color=\"#009933\"><b>Pass</b></font>",
					"<font color=\"#dc143c\"><b>Fail</b></font>");
		}

		WriteToFile(ReporttxtFileName, reportFileTemp);

		// convert text file to HTML
		renameToHtml(ReporttxtFileName);

		// ************************************************************
		// Update Step Count in Master Summary Table
		// ************************************************************

		renameToTxt(MasterhtmlFileName);
		String masterReportFileTemp = ReadFileContent(MastertxtFileName);

		String mtdOld = "<!-- " + ExecutionStartTime + " --><!-- TD -->" + TestDurationOld;
		String mtdNew = "<!-- " + ExecutionStartTime + " --><!-- TD -->" + TestDuration;
		String mtc = "<!-- " + ExecutionStartTime + " --><!-- TS -->" + totalCount;
		String mpc = "<!-- " + ExecutionStartTime + " --><!-- PS -->" + passCount;
		String mfc = "<!-- " + ExecutionStartTime + " --><!-- FS -->" + failCount;
		String mwc = "<!-- " + ExecutionStartTime + " --><!-- WS -->" + warningCount;
		String mec = "<!-- " + ExecutionStartTime + " --><!-- ES -->" + errorCount;
		String tmPass = "<!-- " + ExecutionStartTime + " --><!-- TMS -->"
				+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>";
		String tmFail = "<!-- " + ExecutionStartTime + " --><!-- TMS -->"
				+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>";

		String mtcn = mtc;
		String mpcn = mpc;
		String mfcn = mfc;
		String mwcn = mwc;
		String mecn = mec;

		if (TestStepExeStatus.equalsIgnoreCase("Pass")) {
			mtcn = "<!-- " + ExecutionStartTime + " --><!-- TS -->" + (totalCount + 1);
			mpcn = "<!-- " + ExecutionStartTime + " --><!-- PS -->" + (passCount + 1);
		}

		if (TestStepExeStatus.equalsIgnoreCase("Fail")) {
			mtcn = "<!-- " + ExecutionStartTime + " --><!-- TS -->" + (totalCount + 1);
			mfcn = "<!-- " + ExecutionStartTime + " --><!-- FS -->" + (failCount + 1);
		}

		if (TestStepExeStatus.equalsIgnoreCase("Warning")) {
			mtcn = "<!-- " + ExecutionStartTime + " --><!-- TS -->" + (totalCount + 1);
			mwcn = "<!-- " + ExecutionStartTime + " --><!-- WS -->" + (warningCount + 1);
		}

		if (TestStepExeStatus.equalsIgnoreCase("Error")) {
			mtcn = "<!-- " + ExecutionStartTime + " --><!-- TS -->" + (totalCount + 1);
			mecn = "<!-- " + ExecutionStartTime + " --><!-- ES -->" + (errorCount + 1);
		}

		if (TestMethodStatus.equalsIgnoreCase("Fail")) {
			masterReportFileTemp = masterReportFileTemp.replace(tmPass, tmFail);
			// <FONT COLOR=\"#00b050\"><!-- StartTime --><!-- TMS -->
			masterReportFileTemp = masterReportFileTemp.replace(
					"<FONT COLOR=\"#00b050\"><!-- " + ExecutionStartTime + " --><!-- TMS -->",
					"<FONT COLOR=\"#FF0000\"><!-- " + ExecutionStartTime + " --><!-- TMS -->");
		}

		masterReportFileTemp = masterReportFileTemp.replace(mtc, mtcn);
		masterReportFileTemp = masterReportFileTemp.replace(mpc, mpcn);
		masterReportFileTemp = masterReportFileTemp.replace(mfc, mfcn);
		masterReportFileTemp = masterReportFileTemp.replace(mwc, mwcn);
		masterReportFileTemp = masterReportFileTemp.replace(mec, mecn);
		masterReportFileTemp = masterReportFileTemp.replace(mtdOld, mtdNew);
		TestDurationOld = TestDuration;

		WriteToFile(MastertxtFileName, masterReportFileTemp);

		// convert text file to HTML
		renameToHtml(MastertxtFileName);

		// UpdateMasterSummaryTable();
	}

	private static int GetExistingCount(String inputString, String p) {
		String[] arrayString = inputString.split(" ");

		String[] statusString = arrayString[1].split(",");

		// <!-- InsertStep -->

		int totalTests = Integer.parseInt(statusString[1]);
		int totalPassed = Integer.parseInt(statusString[2]);
		int totalFailed = Integer.parseInt(statusString[3]);
		int totalWarning = Integer.parseInt(statusString[4]);
		int totalError = Integer.parseInt(statusString[5]);

		if (p.equalsIgnoreCase("Pass"))
			return totalPassed;
		if (p.equalsIgnoreCase("Fail"))
			return totalFailed;
		if (p.equalsIgnoreCase("Warning"))
			return totalWarning;
		if (p.equalsIgnoreCase("Error"))
			return totalError;

		return totalTests;

	}

	@SuppressWarnings("unused")
	private static void UpdateReport(String stepName, String status, String testData, String imageTitle)
			throws IOException {

		renameToTxt(ReporthtmlFileName);
		String reportFileData = ReadFileContent(ReporttxtFileName);
		// Append step in resize function for test step
		String appendScrstepString = "\n$(\"#scrstep" + TestStepCount + "\").height(45 * $(window).height() / 100);"
				+ "\n //InsertResize";
		reportFileData = reportFileData.replace("//InsertResize", appendScrstepString);

		// Append step in Report function for test step
		reportFileData = reportFileData.replace("<!-- InsertStep -->",
				AddStepStatus(stepName, status, testData, imageTitle));
		

		// append step step image
		ReportTestStepImage(imageTitle);
		reportFileData = reportFileData.replace("<!-- InsertImage -->", TestImage + "\n<!-- InsertImage -->");

		// Append report function step
		reportFileData = reportFileData.replace("//ReportFunction",
				AddReportFunctionStep(stepName, status, testData, imageTitle));

		WriteToFile(ReporttxtFileName, reportFileData);

		// convert text file to HTML
		renameToHtml(ReporttxtFileName);
	}

	private static void AddConsoleErrorLogs() throws IOException, ParseException {

		List<String> logs = analyzeLog();

		String log = "\n ";
		for (String l : logs) {
			log = log + l + "\n";
		}

		if (!logs.isEmpty()) {
			ReportTestStep("Error in Browser Console", "Fail", "", "ConsoleError");
			renameToTxt(ReporthtmlFileName);
			String reportFileTemp = ReadFileContent(ReporttxtFileName);
			reportFileTemp = reportFileTemp.replace("<!-- ErrorLogs -->", log);
			WriteToFile(ReporttxtFileName, reportFileTemp);
			// convert text file to HTML
			renameToHtml(ReporttxtFileName);
			ReportTestStep("Error in Browser Console", "Fail", "", "ConsoleError");
		}
	}


	public static void CompleteTest() throws IOException, ParseException {
		SoftAssert softAssert = new SoftAssert();
		int failCount = 0;
		// This used to close extent report after each test cases
		
			AddConsoleErrorLogs();
		
		//UpdateMasterSummaryTable();
		failCount = FailStepCount;
		HeaderOpen = "";
		HeaderClose = "";
		BodyOpen = "";
		BodyClose = "";
		WindowResizeFunction = "";
		ReportFunction = "";
		TesterName = "";

		// declaration for Test Step HTML building
		TestsTableHeader = "";
		TestImage = "";
		TestMethodName = "";
		first = "";
		TestMethodStatus = "Pass";
		TestMethodStatus1 = "";
		TestMethodCount = 1;
		TestStepStatus = "";
		TestStepStatus1 = "";

		TestStepCount = 0;
		PassStepCount = 0;
		FailStepCount = 0;
		WarningStepCount = 0;
		ErrorStepCount = 0;

		HeaderCloseTemp = "";
		TestImageTemp = "";
		TestMethodSummary = "";
		ReportsPath = "";
		ImageFileName = "";
		ReportFunctionTemp = "";
		ExecutionStartTime = null;
		MasterReportLine = "";
		TestDuration = "";
		ExecutionEndTime = null;
		ReportFolderName = "";
		TestImageStatus = "";
		MastertxtFileName = "";
		MasterhtmlFileName = "";
		ReporttxtFileName = "";
		ReporthtmlFileName = "";
		TestStepExeStatus = "";
		ExecutionPreviousEndTime = null;
		TestDurationOld = "";
		ReporthtmlFileSubpath = "";
		PieChartSummary = "";
		MasterPieChartSummary = "";
		if (failCount > 0)
			softAssert.fail("Failures in Test");
	}
	

	public static void ConcludeTestSuite() {
		try {
				extent.flush();
			
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private static void RenameFileFolder(String filePath) {
		File file = new File(filePath);

		// if file doesn't exists, then create it
		if (file.exists()) {
			String fname = file.getName();

			String newname = filePath.replace(fname,
					fname + " " + getDateTimeFormat("HHmmss").format(SuiteExecutionStartTime));

			File file1 = new File(newname);
			if (!file.renameTo(file1)) {
				System.out.println("File not found");
			}
		} else {

			Logger.getLogger("File/Folder not found");
		}
	}

	@SuppressWarnings("unused")
	private static void UpdateMasterSummaryTable() throws IOException {
		// Read Existing File Data
		renameToTxt(MasterhtmlFileName);
		String masterReportFileTemp = ReadFileContent(MastertxtFileName);

		// Read Last 25 chars of file
		String last25 = masterReportFileTemp.substring(masterReportFileTemp.length() - 25);

		int totalCount = GetExistingCount(last25, "Total");
		int passCount = GetExistingCount(last25, "Pass");
		int failCount = GetExistingCount(last25, "Fail");
		int warningCount = GetExistingCount(last25, "Warning");
		int errorCount = GetExistingCount(last25, "Error");

		int PiepassCount = passCount;
		int PiefailCount = failCount;

		String tc = "<!-- StepCount -->" + totalCount;
		String pc = "<!-- PassCount -->" + passCount;
		String fc = "<!-- FailCount -->" + failCount;
		String wc = "<!-- WarningCount -->" + warningCount;
		String ec = "<!-- ErrorCount -->" + errorCount;

		String tcn = fc;
		String pcn = pc;
		String fcn = fc;
		String wcn = wc;
		String ecn = ec;

		// String Pietc = "<!-- StepCount -->" + totalCount;
		String Piepc = "['Pass'," + passCount;
		String Piefc = "['Fail'," + failCount;
		String Piewc = "['Warning'," + warningCount;
		String Pieec = "['Error'," + errorCount;

		String pcnPie = Piepc;
		String fcnPie = Piefc;
		String wcnPie = Piewc;
		String ecnPie = Pieec;

		if (TestMethodStatus.equalsIgnoreCase("Pass") && (WarningStepCount + ErrorStepCount == 0)) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			pcn = "<!-- PassCount -->" + (passCount + 1);
			pcnPie = "['Pass'," + (PiepassCount + 1);
			totalTestsCount = totalCount + 1;
			totalPassCount = passCount + 1;
		}

		if (TestMethodStatus.equalsIgnoreCase("Fail") && (WarningStepCount + ErrorStepCount == 0)) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			fcn = "<!-- FailCount -->" + (failCount + 1);
			fcnPie = "['Fail'," + (PiefailCount + 1);
			totalTestsCount = totalCount + 1;
			totalFailCount = failCount + 1;
		}

		if (WarningStepCount > 0 && ErrorStepCount == 0) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			wcn = "<!-- WarningCount -->" + (warningCount + 1);
			wcnPie = "['Warning'," + (warningCount + 1);
			totalTestsCount = totalCount + 1;
			totalWarningCount = warningCount + 1;
		}

		if (ErrorStepCount > 0) {
			tcn = "<!-- StepCount -->" + (totalCount + 1);
			ecn = "<!-- ErrorCount -->" + (errorCount + 1);
			ecnPie = "['Error'," + (errorCount + 1);
			totalTestsCount = totalCount + 1;
			totalErrorCount = errorCount + 1;
		}

		if (!tc.equals(tcn))
			masterReportFileTemp = masterReportFileTemp.replace(tc, tcn);
		if (!fc.equals(fcn))
			masterReportFileTemp = masterReportFileTemp.replace(fc, fcn);
		if (!pc.equals(pcn))
			masterReportFileTemp = masterReportFileTemp.replace(pc, pcn);
		if (!wc.equals(wcn))
			masterReportFileTemp = masterReportFileTemp.replace(wc, wcn);
		if (!ec.equals(ecn))
			masterReportFileTemp = masterReportFileTemp.replace(ec, ecn);
		if (!pcnPie.equals(Piepc))
			masterReportFileTemp = masterReportFileTemp.replace(Piepc, pcnPie);
		if (!fcnPie.equals(Piefc))
			masterReportFileTemp = masterReportFileTemp.replace(Piefc, fcnPie);
		if (!wcnPie.equals(Piewc))
			masterReportFileTemp = masterReportFileTemp.replace(Piewc, wcnPie);
		if (!ecnPie.equals(Pieec))
			masterReportFileTemp = masterReportFileTemp.replace(Pieec, ecnPie);

		masterReportFileTemp = masterReportFileTemp.replace(last25, BuildMasterSummaryString(last25));

		if (TestMethodStatus.equalsIgnoreCase("Pass") && (WarningStepCount + ErrorStepCount == 0)) {
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>");
		} else if (TestMethodStatus.equalsIgnoreCase("Fail") && (WarningStepCount + ErrorStepCount == 0)) {
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace("#00b050", "#FF0000");
		} else if (WarningStepCount > 0 && ErrorStepCount == 0) {
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>");
		} else if (ErrorStepCount > 0) {
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-check\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-times\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>");
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>",
					"<!-- " + ExecutionStartTime + " --><!-- TMS -->"
							+ "<i class=\"fa fa-bomb\" aria-hidden=\"true\"></i>");
		}

		SuiteExecutionEndTime = ExecutionEndTime;
		if (SuiteExecutionEndTime.after(SuiteExecutionPreviousEndTime)) {
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- ReplaceSuiteExecutionEndTime -->" + SuiteExecutionPreviousEndTime,
					"<!-- ReplaceSuiteExecutionEndTime -->" + SuiteExecutionEndTime.toString());
			TestSuiteDuration = CalculateTestSuiteDuration();
			masterReportFileTemp = masterReportFileTemp.replace(
					"<!-- ReplaceSuiteExecutionDuration -->" + TestSuiteDurationOld,
					"<!-- ReplaceSuiteExecutionDuration -->" + TestSuiteDuration);
			SuiteExecutionPreviousEndTime = SuiteExecutionEndTime;
			TestSuiteDurationOld = TestSuiteDuration;
		}

		WriteToFile(MastertxtFileName, masterReportFileTemp);

		// convert text file to HTML
		renameToHtml(MastertxtFileName);

	}

	// This method used to get Console error during execution of automation test
	// suiute.
	public static List<String> analyzeLog() {
		List<String> errorLogs = new ArrayList<String>();
		
		return errorLogs;
	}

	public static void testLog(String str) {
		Reporter.log("<li style=\"color:#0000FF\"> " + str + " </li>");
		description = sb.append("-").append(str).append(" \n").toString();
	}

	/**
	 * @purpose Initialize Extent Report
	 * @author Abirami
	 * @throws Exception 
	 * @throws Throwable
	 */
	@SuppressWarnings("deprecation")
	protected
	static void initExtentReport() throws Exception {
		// initialize the HtmlReporter
		extentReportFolder = "/ExtentReports/HtmlReport_" + DateFormatUtilities.getTodaysDateTime();
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + extentReportFolder + ".html");

		// initialize ExtentReports and attach the HtmlReporter
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// configuration items to change the look and feel
		// add content, manage tests etc
		extent.setSystemInfo("Host Name", " - " + ApplicationSetup.testURL);
		extent.setSystemInfo("Environment", " - " + ApplicationSetup.environmentName.toUpperCase());
		extent.setSystemInfo("Executed By", " - " + System.getProperty("user.name"));
		extent.setSystemInfo("Suite :", ApplicationSetup.groupsDetails);
		
		// configuration items to change the look and feel
		// add content, manage tests etc
		htmlReporter.config().setDocumentTitle("Automated Test Report" + "Suite Name : ");
		htmlReporter.config().setReportName("Automated E2E Test Report");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
	}

	public static void SendEmailwithReport() {
		
		// Create object of Property file
				Properties props = new Properties();

				// this will set host of server- you can change based on your requirement 
				props.put("mail.smtp.host", "smtp.gmail.com");

				// set the port of socket factory 
				props.put("mail.smtp.socketFactory.port", "465");

				// set socket factory
				props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

				// set the authentication to true
				props.put("mail.smtp.auth", "true");

				// set the port of SMTP server
				props.put("mail.smtp.port", "465");

				// This will handle the complete authentication
				Session session = Session.getDefaultInstance(props,

						new javax.mail.Authenticator() {

							protected PasswordAuthentication getPasswordAuthentication() {

							return new PasswordAuthentication("abirami.chinnaiyan@rishabhsoft.com", "********");

							}

						});

				try {

					// Create object of MimeMessage class
					Message message = new MimeMessage(session);

					// Set the from address
					message.setFrom(new InternetAddress("abirami.chinnaiyan@rishabhsoft.com"));

					// Set the recipient address
					message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("abirami.chinnaiyan@rishabhsoft.com"));
		            
		                        // Add the subject link
					message.setSubject("Automated Test Report");

					// Create object to add multimedia type content
					BodyPart messageBodyPart1 = new MimeBodyPart();

					// Set the body of email
					messageBodyPart1.setText("Automated Test Report");

					// Create another object to add another content
					MimeBodyPart messageBodyPart2 = new MimeBodyPart();

					// Mention the file which you want to send
					String filename = System.getProperty("user.dir")+"/ExtentReports/TestReport.html";

					// Create data source and pass the filename
					DataSource source = new FileDataSource(filename);

					// set the handler
					messageBodyPart2.setDataHandler(new DataHandler(source));

					// set the file
					messageBodyPart2.setFileName(filename);

					// Create object of MimeMultipart class
					Multipart multipart = new MimeMultipart();

					// add body part 1
					multipart.addBodyPart(messageBodyPart2);

					// add body part 2
					multipart.addBodyPart(messageBodyPart1);

					// set the content
					message.setContent(multipart);

					// finally send the email
					Transport.send(message);

					System.out.println("=====Email Sent=====");
	
	} catch (MessagingException e) {

		throw new RuntimeException(e);

	}
	}
}
