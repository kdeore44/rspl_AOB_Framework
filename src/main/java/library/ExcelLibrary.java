package library;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.Reporter;

/**
 * 
 * @author Abirami
 *
 */
public class ExcelLibrary {
	static Map<String, Workbook> workbooktable = new HashMap<String, Workbook>();
	protected static Map<String, Integer> dict = new HashMap<String, Integer>();
	protected static List<String> list = new ArrayList<String>();
	static String defaultSheetName;
	static String defaultDataSheetPath;
	static String currentDir = System.getProperty("user.dir")+"/";
	private static final Logger logger = LogManager.getLogger(ExcelLibrary.class);
	/**
	 * @author Abirami To get the excel sheet workbook
	 * @throws IOException 
	 * @throws EncryptedDocumentException 
	 */
	public static Workbook getWorkbook(String path) throws IOException {
		logger.info("Set up Excel File");
		String filepath = currentDir + path;
		Workbook workbook = null;
		
			if (workbooktable.containsKey(filepath)) {
				workbook = workbooktable.get(filepath);
			} else {
				File file = new File(filepath);
				FileInputStream fis = new FileInputStream(file);
				workbook = WorkbookFactory.create(fis);
				workbooktable.put(filepath, workbook);
			}
		
		return workbook;
	}

	/**
	 * @author Abirami
	 * 
	 * @Purpose To get the number of sheets in excel suite
	 * 
	 * @throws IOException
	 */
	public static List<String> getNumberOfSheetsinSuite(String testPath) throws IOException {
		List<String> listOfSheets = new ArrayList();
		try (Workbook workbook = getWorkbook(testPath)) {
			if (workbook != null) {
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					listOfSheets.add(workbook.getSheetName(i));
				}
			}
		}
		return listOfSheets;
	}

	/**
	 * @author Abirami
	 * @Purpose To get the number of sheets in test data sheet
	 * @throws IOException
	 */
	public static List<String> getNumberOfSheetsinTestDataSheet(String testPath) throws IOException {
		List<String> listOfSheets = new ArrayList();
		try (Workbook workbook = getWorkbook(testPath)) {
			if (workbook != null) {
				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					if (!(workbook.getSheetName(i)).equalsIgnoreCase(("TestCase_SheetName"))) {
						listOfSheets.add(workbook.getSheetName(i));
					}
				}
			}
		}
		return listOfSheets;

	}

	/**
	 * @author Abirami
	 * @Purpose Get the total rows present in excel sheet
	 */
	public static int getRows(String testSheetName, String pathOfFile) throws IOException {
		Sheet sheet = null;
		int rowno;
		try (Workbook workbook = getWorkbook(pathOfFile)) {
			if (workbook != null) {
				Reporter.log("getting total number of rows");
				sheet = workbook.getSheet(testSheetName);
				rowno = sheet.getLastRowNum();
			}else {
				rowno=0;
			}
		}
		return rowno;

	}

	/**
	 * @author Abirami
	 * @Purpose Get the total columns inside excel sheet
	 */
	public static int getColumns(String testSheetName, String pathOfFile) throws IOException {
		Sheet sheet = null;
		short colu = 0;
		try (Workbook workbook = getWorkbook(pathOfFile)) {
			if (workbook != null) {
				Reporter.log("getting total number of columns");
				sheet = workbook.getSheet(testSheetName);
				colu = sheet.getRow(0).getLastCellNum();
			}
		}
		return colu;

	}

	/**
	 * @author Abirami
	 * @Puspose Get the column names inside excel sheet
	 */
	public static List<String> getColumnNames(String testSheetName, String pathOfFile)
			throws  IOException {
		try (Workbook workbook = getWorkbook(pathOfFile)) {
			if (workbook != null) {
				Sheet sheet = workbook.getSheet(testSheetName);
				int lastCol = getColumns(testSheetName, pathOfFile);
				for (int i = 0; i <= lastCol; i++) {
					if (sheet.getRow(0).getCell(i) != null) {
						list.add(sheet.getRow(0).getCell(i).getStringCellValue());
					}
				}
			}
		}
		return list;

	}

	/**
	 * @author Abirami
	 * @Purpose Get the column names inside excel sheet
	 */
	public static int getColumnIndex(String testSheetName, String pathOfFile, String columnName)
			throws  IOException {
		int columnIndex = 0;
		try (Workbook wBook = getWorkbook(pathOfFile)) {
			if (wBook != null) {
				Sheet sheet = wBook.getSheet(testSheetName);
				int lastCol = getColumns(testSheetName, pathOfFile);
				for (int i = 0; i <= lastCol; i++) {
					if (sheet.getRow(0).getCell(i) != null && sheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {					
							columnIndex = i;
							break;					
					}
				}
			}
		}
		return columnIndex;

	}

	/*
	 * @author Abirami
	 * 
	 * @Purpose To Read Column Name
	 */
	public static int getCell(String colName) {
		int value;
		try {
			value = getColumnIndex(defaultSheetName, defaultDataSheetPath, colName);
			return value;
		} catch (Exception e) {
			return (0);
		}
	}

	/**
	 * @author Abirami
	 * @Purpose Get the total number of rows for each column inside excel sheet
	 */
	public static void getNumberOfRowsPerColumn(String testSheetName, String pathOfFile)
			throws IOException {
		try (Workbook wBook = getWorkbook(pathOfFile)) {
			if (wBook != null) {
				Sheet sheet = wBook.getSheet(testSheetName);
				int totColumns = sheet.getRow(0).getLastCellNum();
				for (int i = 0; i <= totColumns; i++) {
					if (sheet.getRow(0).getCell(i) != null) {
						list.add(sheet.getRow(0).getCell(i).getStringCellValue());
					}
				}
			} 
		}
	}

	/**
	 * @author Abirami
	 * @Purpose Read the content of the cell
	 * @throws IOException
	 */
	public static String readCell(int rowNum, int colNum, String testSheetName, String pathOfFile) throws IOException {
		String cellValue = null;
		try (Workbook wBook = getWorkbook(pathOfFile)) {
			if (wBook != null) {
				Sheet sheet = wBook.getSheet(testSheetName);
				Row row = sheet.getRow(rowNum);
				if (row != null) {
					Cell cell = row.getCell(colNum);
					if (cell != null) {
						DataFormatter dataFormatter = new DataFormatter();
						String data = dataFormatter.formatCellValue(cell);
						cellValue = data;
					}
				}
			} 
		}
		return cellValue;
	}

	/**
	 * @Purpose Read the content of the cell
	 * @author Abirami
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static String readCell(int rowNum, String colName, String testSheetName, String pathOfFile)
			throws  IOException {
		String cellValue = null;
		try (Workbook workbook = getWorkbook(pathOfFile)) {
			if (workbook != null) {
				Sheet sheet = workbook.getSheet(testSheetName);
				Row row = sheet.getRow(rowNum);
				if (row != null) {
					int colNum = getColumnIndex(testSheetName, pathOfFile, colName);
					Cell cell = row.getCell(colNum);
					if (cell != null) {
						DataFormatter dataFormatter = new DataFormatter();
						String data = dataFormatter.formatCellValue(cell);
						cellValue = data;
					}
				}
			}
		}
		return cellValue;
	}

	/*
	 * @Purpose This method is to read the test data from the Excel cell, in this we
	 * are passing parameters as Row num and Col num
	 * 
	 * @author Abirami
	 * 
	 * @throws IOException
	 */
	public static String getCellData(int ColNum, int RowNum) {
		String cellData = null;
		try {
			cellData = readCell(RowNum, ColNum, defaultSheetName, defaultDataSheetPath);
			return cellData;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return cellData;
	}

	/**
	 * @author Abirami
	 * @Purpose To clear the worktable and list
	 */
	public void clean() {
		workbooktable.clear();
		list.clear();
	}

	/**
	 * @author Abirami
	 * @Purpose Write the content to cell of any workbook
	 * @throws IOException
	 */
	public static void writeCellData(String pathOfFile, String testSheetName, int rowNum, int colNum, String cellValue)
			throws IOException {
		String filepath = currentDir + pathOfFile;
		File file = new File(filepath);
		try (FileInputStream fis = new FileInputStream(file)) {
			try (Workbook workbook = WorkbookFactory.create(fis)) {
				if (workbook != null) {
					Sheet sheet = workbook.getSheet(testSheetName);
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						Cell cell = row.getCell(colNum);
						if (cell != null) {
							cell.removeCellComment();
							cell.setCellValue(cellValue);
						} else {
							row.createCell(colNum).setCellValue(cellValue);
						}
						try (FileOutputStream out = new FileOutputStream(file)) {
							workbook.write(out);
							workbooktable.put(filepath, workbook);
						}
					} 
				} 
			}
		}
	}

	/**
	 * @author Abirami
	 * @Purpose Write the content to cell of any workbook
	 * @throws IOException
	 */
	public static void writeCellData(String pathOfFile, String testSheetName, int rowNum, String colName,
			String cellValue) throws IOException {
		int colNum = 0;
		try {
			colNum = getColumnIndex(testSheetName, pathOfFile, colName);
			writeCellData(pathOfFile, testSheetName, rowNum, colNum, cellValue);
		} catch (IOException e) {

			logger.info(e.getMessage());
		}
	}

}