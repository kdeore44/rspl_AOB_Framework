package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtils {

	public static Workbook excelWBook;
	public static Workbook excelWBook2;
	public static XSSFReader excelWBook1;
	public static Sheet excelWSheet;
	public static Sheet  excelWSheet2;
	public static Row row;
	public static Cell cell;
	public static String S;
	public static String filePath1;
	static HashMap<String, Integer> dict = new HashMap<String, Integer>();
	static HashMap<String, Integer> dict2 = new HashMap<String, Integer>();
	public static int cellType;
	public static boolean flag;
	public static int totalRowNumber;
	public static int totalColNumber;
	protected static Map<String, FileInputStream> excelMap = new HashMap<String, FileInputStream>();
	private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
	// This method is to set the File path and to open the excel file, Pass excel
	// Path with file name and Sheet name as Arguments to this method

	private ExcelUtils() {
		// not used 
	}
	
	public static void setDataFile(String filePath, String sheetName) throws IOException {
		FileInputStream excelFile = null;
		try {
			flag = false;

			// Open the excel file
			String currentDir = System.getProperty("user.dir")+"/";
			String filepath = currentDir + filePath;
			File file = new File(filepath);
			excelFile = new FileInputStream(file);
			// Access the required test data sheet
			if (filePath.contains("TestData") && !filePath.contains("FilesForUpload")) {
				excelWBook = WorkbookFactory.create(excelFile);
				excelWSheet = excelWBook.getSheet(sheetName);
				for (int col = 0; col < excelWSheet.getRow(0).getLastCellNum(); col++) {
					dict.put(getCellData(col, 0), col);
				}
			} else {
				flag = true;
				excelWBook2 = WorkbookFactory.create(excelFile);
				excelWSheet2 = excelWBook2.getSheet(sheetName);
				int loopCount=excelWSheet2.getRow(0).getLastCellNum();
				for (int col = 0; col < loopCount; col++) {
					dict2.put(getCellData(col, 0), col);
				}
			}

		} catch (Exception e) {
			 logger.info(e.getMessage());
		} finally {
			if(excelFile!=null) {
			excelFile.close(); 
			}
		  }


	}
	
	//This is used to set Non Data set file
	public static void setNonDataFile(String filePath, String sheetName) {

		try {
		
			// Open the excel file
			String currentDir = System.getProperty("user.dir")+"/";
			String filepath = currentDir + filePath;
			File file = new File(filepath);
			FileInputStream excelFile = new FileInputStream(file);
	
			flag = true;
			excelWBook2 = WorkbookFactory.create(excelFile);
			excelWSheet2 = excelWBook2.getSheet(sheetName);
			int loopCount=excelWSheet2.getRow(0).getLastCellNum();
			for (int col = 0; col < loopCount; col++) {
					dict2.put(getCellData(col, 0), col);
					
			}
		} catch (Exception e) {
			 logger.info(e.getLocalizedMessage());
			
		}
	}


	// open excel file sheet
	public static void openexcelFileSheet(String path, String sheetName) throws IOException {
		FileInputStream excelFile = null;
		try {
			
			
			ZipSecureFile.setMinInflateRatio(0);
			// Open the excel file
			String currentDir = System.getProperty("user.dir")+"/";
			String filepath = currentDir + path;
			 logger.info(filepath);

			File file = new File(filepath);

			excelFile = new FileInputStream(file);

		
			// Access the required test data sheet

			excelWBook = new XSSFWorkbook(excelFile);

			excelWSheet = excelWBook.getSheet(sheetName);
			

		} catch (Exception e) {
           logger.info(e.getLocalizedMessage());
     
		}finally {
			if(excelFile!=null) {
				excelFile.close(); 
				}
		}
	}

	// #################################################################################################
	
	// Returns the Number of Rows
	public static int rowCount() {
		if (flag) {
			totalRowNumber = excelWSheet2.getLastRowNum();
		} else {
			totalRowNumber = excelWSheet.getLastRowNum();
		}
		return totalRowNumber;
	}
		
		/**
		 * @author Abirami
		 * @purpose To get Total column count from the cofigured file
		 * @return totalColNumber
		 */
		public static int columnCount() {
			if (flag) {
				totalColNumber = excelWSheet2.getRow(0).getLastCellNum();
			} else {
				totalColNumber = excelWSheet.getRow(0).getLastCellNum();
			}
			return totalColNumber;
		}

	// This method used to get column no from sheet 
	public static int getColumnNumber(String columnName) {
		int columnNumber = 0;
		int totalCoumnNumber;
		if (flag) {
			totalCoumnNumber = excelWSheet2.getRow(0).getLastCellNum();
			for (int col = 0; col < totalCoumnNumber; col++) {
				String getCellvalue=getCellData(col, 0);
				if((columnName.toLowerCase().trim()).equalsIgnoreCase(getCellvalue.toLowerCase().trim())) {
					columnNumber=col;
					break;
				}				
			}			
		} else {
			totalCoumnNumber = excelWSheet.getRow(0).getLastCellNum();
			for (int col = 0; col < totalCoumnNumber; col++) {
				String getCellvalue=getCellData(col, 0);
				if((columnName.toLowerCase().trim()).equalsIgnoreCase(getCellvalue.toLowerCase().trim())) {
					columnNumber=col;
					break;
				}				
			}
		}
		return columnNumber;
	}

	
	// Read Column Names
	public static int getCell(String colName) {
		try {
			int value;
			value = ( dict.get(colName)).intValue();
			return value;
		} catch (NullPointerException e) {
			return (0);
		}
	}


	// This method is to read the test data from the excel cell, in this we are
	// passing parameters as Row num and Col num
	public static String getCellData(int colNum, int rowNum) {
		String cellData = null;
		
		try {
			if (flag) {
				try {
					cell = excelWSheet2.getRow(rowNum).getCell(colNum);
					if (cell.toString().equals("Undefined")) {
						flag = false;
						cell = excelWSheet.getRow(rowNum).getCell(colNum);
					}
				} catch (Exception e) {
					flag = false;
					cell = excelWSheet.getRow(rowNum).getCell(colNum);
				}
			} else {
				flag = false;
				cell = excelWSheet.getRow(rowNum).getCell(colNum);
			}
			if (cell == null) {
				return " ";
			} else {
				String typecell = cell.getCellStyle().getDataFormatString();
				if (typecell.equals("General")) {
					cellData = cell.getStringCellValue().trim();
				} else if (typecell.equals("#0")) {
					cellData = cell.getStringCellValue().trim();
				} else if (typecell.equals("#,##0")) {
					cellData = cell.getStringCellValue().trim();
				} else if (typecell.equals("@")) {
					cellData = cell.getStringCellValue().trim();
				} else if (typecell.equals("0%")) {
					double data = cell.getNumericCellValue();
					long s = (long) (data);
					cellData = Long.toString(s);
				} else if (typecell.equals("0.00%")) {
					double data = cell.getNumericCellValue() * 100;
					long s = (long) (data);
					cellData = Long.toString(s);
				} else if (typecell.equals("dd/mm/yyyy")) {
					cellData = cell.getStringCellValue().trim();
				} else if (typecell.equals("dd/mm/yy") || typecell.equalsIgnoreCase("dd\\/mm\\/yyyy") || typecell.equalsIgnoreCase("m/d/yy h:mm")) {
					Date date11 = cell.getDateCellValue();
					cellData = date11.toString();
					cellData = DateFormatUtilities.Convert_DateTime_DDMMYYYHHMMSS_to_DDMMYYYY(cellData);
				} else if (typecell.equals("dd-mm-yyyy")) {
					Date date11 = cell.getDateCellValue();
					cellData = date11.toString();
				}
			}
			return cellData;
		} catch (Exception e) {
			if (flag) {
				try {
					cell = excelWSheet2.getRow(rowNum).getCell(colNum);
					if (cell.toString().equals("Undefined")) {
						flag = false;
						cell = excelWSheet.getRow(rowNum).getCell(colNum);
					}
				} catch (Exception ie) {
					flag = false;
					cell = excelWSheet.getRow(rowNum).getCell(colNum);
				}
			} else {
				flag = false;
				cell = excelWSheet.getRow(rowNum).getCell(colNum);
			}			
			if (cell == null) {
				 logger.info("no values in cell");
				return " ";
			} else {
				double data = cell.getNumericCellValue();
				long s = (long) (data);
				cellData = Long.toString(s);
				return cellData;
			}
		}
	}

	// This method is to read the test data from the excel cell, in this we are
	// passing parameters as Row num and Col num
	

	public static String getCellDataNumbers(int rowNum, int colNum) {

		try {

			if (flag) {
				cell = excelWSheet2.getRow(rowNum).getCell(colNum);
				flag = false;
			} else {
				cell = excelWSheet.getRow(rowNum).getCell(colNum);
			}

			if (cell == null) {

				return " ";
			}
			return cell.getStringCellValue();
			
		} catch (Exception e) {
			cell = excelWSheet.getRow(rowNum).getCell(colNum);
			if (cell == null) {
				 logger.info("no values in cell");
				return " ";
			}
			double data = cell.getNumericCellValue();
			float f = (float) data;
			return String.valueOf(f);

		}

	}

	public static int getRowContains(String sTestCaseName, int colNum) {

		int i;

		try {

			int rowCount = ExcelUtils.getRowUsed();

			for (i = 0; i < rowCount; i++) {

				if (ExcelUtils.getCellData(i, colNum).equalsIgnoreCase(sTestCaseName)) {

					break;

				}

			}

			return i;

		} catch (Exception e) {

			throw (e);

		}

	}

	// get row counts
	public static int getRowUsed(){

		try {

			return excelWSheet.getLastRowNum();

			 

		} catch (Exception e) {

			 logger.info(e.getMessage());

			throw (e);

		}

	}
	
	// get row counts
		public static int getRowUsed(Sheet newWorkSheet) {
			try {
				return newWorkSheet.getLastRowNum();
				
			} catch (Exception e) {
				 logger.info(e.getMessage());
				throw (e);
			}
		}

	// Read Download file And get File Name
	public static String readDownloadexcelFile() {
		String s = null;
		try {
			
			String currentDir = System.getProperty("user.dir")+ "\\";
			String downloadpath = currentDir + "Download";
			 logger.info(downloadpath);
			File fin = new File(downloadpath);
			Thread.sleep(5000);
			File[] finlist = fin.listFiles();
			 logger.info("Download Started : "+finlist[0].getName());
			Long filesize1;
			Long filesize2;
			for (File f : finlist) {
				do {
					filesize1 = f.length();
					Thread.sleep(3000);
					filesize2 = f.length();
				} while (!filesize1.equals(filesize2));
			}
			finlist = fin.listFiles();
			 logger.info("Download Completed : "+finlist[0].getName());
			s = finlist[0].getName();
			s = "Download\\" + s;
			
		} catch (Exception e) {
			Thread thread = new Thread();
			thread.interrupt();
			 logger.info(e.getMessage());
		}
		return s;
	}

	public static String getXLSMSheetname(String path)  {
		String s = null;
		try {
			String currentDir = System.getProperty("user.dir")+"\\" ;
			String filepath = currentDir + path;
			 logger.info(filepath);
			
			excelWBook = new XSSFWorkbook(OPCPackage.open(filepath));
			s = excelWBook.getSheetName(0);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return s;
	}

	// Read Upload excel file And get File Name
	public static String readDataFromDataDifferentDataSheet(String filePath, String sheetName, String  colName,String rowName) {
		ExcelUtils.flag = false;
		String data = null;
		ExcelUtils.setNonDataFile(filePath, sheetName);
		for (int col = 0; col < excelWSheet2.getRow(0).getLastCellNum(); col++) {
			String colname =excelWSheet2.getRow(0).getCell(col).getStringCellValue().trim();			
			if(colname.equalsIgnoreCase(colName)) {
			for(int row =1; row<=excelWSheet2.getLastRowNum();row++) {
				cell =excelWSheet2.getRow(row).getCell(0);
				data = cell.getStringCellValue().trim();
				if(data.equalsIgnoreCase(rowName)) {
					cell =excelWSheet2.getRow(row).getCell(col);
					data = cell.getStringCellValue().trim();
					break ;
				}
			}
		}
		}
		return data;
	}
}
