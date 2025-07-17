package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RWExcel {
	public static Logger log = LogManager.getLogger(RWExcel.class.getName());

	XSSFWorkbook workbook;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;
	String tdMod;
	CommonUtilities objCU = new CommonUtilities();
	String filepath = System.getProperty("user.dir") + "\\src\\main\\resources\\datapool\\" + "EnvData.properties";
	String resourceFolderPath = objCU.readPropertyFile(filepath, "ResourcesPath");
	String filePath = System.getProperty("user.dir") + resourceFolderPath + ("\\dataPool\\TestData.xlsx");

	int rc, cc;

	/**
	 * Construct XSSFWorkbook object to read excel file
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName - worksheet name
	 */
	public void readExcel(String sheetName) {
		File file;
		FileInputStream fin;
		try {
			file = new File(filePath);
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);
			fin.close();
		} catch (FileNotFoundException e) {
			log.error("Excel file not found: " + filePath);
		} catch (IOException e) {
			log.error("Error while reading a Test Data file <readExcel>: " + e.getMessage());
		}
	}

	/**
	 * Count the number for rows in sheet
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName - worksheet name
	 * @return - count of used rows
	 */
	public int countRows(String sheetName) {
		readExcel(sheetName);
		sheet = workbook.getSheet(sheetName);
		return sheet.getLastRowNum() - sheet.getFirstRowNum();
	}

	/**
	 * Count the number for columns in sheet
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName - worksheet name
	 * @return - count of used columns
	 */
	public int countCol(String sheetName) {
		sheetName = sheetName.trim();
		readExcel(sheetName);
		row = workbook.getSheet(sheetName).getRow(0);

		return cc = row.getLastCellNum() - row.getFirstCellNum();
	}

	/**
	 * read specific column value from row 2 in excel
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName - worksheet name
	 * @param colName   - name of column name
	 * @return - test data for given column name
	 */
	public String readCell(String sheetName, String colName) {
		sheetName = sheetName.trim();
		String cellValue = null;
		int cc = countCol(sheetName);
		for (int i = 0; i < cc; i++) {
			String fetchValue = row.getCell(i).getStringCellValue().trim();

			if (colName.equalsIgnoreCase(fetchValue)) {
				row = workbook.getSheet(sheetName).getRow(1);
				XSSFCell cell = row.getCell(i);
				if (!(cell == null)) {
					if (cell.getCellType() == CellType.STRING) {
						cellValue = row.getCell(i).getStringCellValue();
					} else if (cell.getCellType() == CellType.BOOLEAN) {
						cellValue = String.valueOf(row.getCell(i).getBooleanCellValue());
					} else if (cell.getCellType() == CellType.NUMERIC) {
						cellValue = String.valueOf(row.getCell(i).getNumericCellValue());
					} else if (cell.getCellType() == CellType.FORMULA) {
						try {
							cellValue = row.getCell(i).getStringCellValue();
						} catch (Exception e) {
							cellValue = NumberToTextConverter.toText(row.getCell(i).getNumericCellValue());
						}
					}

				} else {
					cellValue = "";
				}
				break;
			}
		}
		return cellValue;
	}

	/**
	 * Reading all value for the given Coloum
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName - worksheet name
	 * @param colName   - namme of column
	 * @return - arraylist of all values in specific column
	 */
	public ArrayList<String> readColValues(String sheetName, String colName) {
		sheetName = sheetName.trim();
		int cc = countCol(sheetName);
		int rc = countRows(sheetName);

		ArrayList<String> cellValue = new ArrayList<String>();

		// Checking if the Col is available in Sheet
		for (int i = 0; i < cc; i++) {
			String fetchValue = row.getCell(i).getStringCellValue().trim();
			if (colName.equalsIgnoreCase(fetchValue)) {
				for (int j = 1; j <= rc; j++) {
					row = workbook.getSheet(sheetName).getRow(j);
					cellValue.add(row.getCell(i).getStringCellValue());
				}
				break;

			}
		}
		log.debug("Total value in col :" + cellValue.size());
		return cellValue;
	}

	/**
	 * Method for identifying Test Data sheet for corosponding Test Scenario in RTM
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName  - worksheet name
	 * @param modMainCol - main momdule name
	 * @param subMod1Col - sub module1 name
	 * @param subMod2Col - aub module 2 name
	 */
	public void readTDSheetName(String sheetName, String modMainCol, String subMod1Col, String subMod2Col) {
		sheetName = sheetName.trim();

		if (readColValues(sheetName, "Main_Module").contains(modMainCol)) {
			System.out.println("Debug : " + readColValues(sheetName, "Main_Module").indexOf(modMainCol));

			if (readColValues(sheetName, "Sub_Module").contains(subMod1Col)) {
				System.out.println(readColValues(sheetName, "Sub_Module").indexOf(subMod1Col));
				if (readColValues(sheetName, "Sub_Module_2").contains(subMod2Col)) {
					System.out.println(readColValues(sheetName, "Sub_Module_2").indexOf(subMod2Col));
					System.out.println("Test Data sheet name  >>>" + readCell(sheetName, "TestDataSheet"));
				}
			}
		}

	}

	/**
	 * Retrives all columns header with respective values in row 2
	 * 
	 * @author Rashmi.Patel added on 20 Jan 2021
	 * @param sheetName - worksheet name
	 * @return - map with key as column header and value as row 2 value
	 */
	public Multimap<String, String> readRowData(String sheetName) {
		sheetName = sheetName.trim();
		int cc = countCol(sheetName);
		int rc = countRows(sheetName);
		log.debug(cc + " X " + rc);

		Row row = sheet.getRow(0);
		Row row1 = sheet.getRow(1);

		Multimap<String, String> dataSet = LinkedListMultimap.create();

		for (int i = 0; i < cc; i++) {
			String cHeader = "";
			String cValue = "";
			String Ctype = row.getCell(i).getCellType().toString();
			String Ctype1 = row1.getCell(i).getCellType().toString();

			if (Ctype.equalsIgnoreCase("String"))
				cHeader = row.getCell(i).getStringCellValue();
			else if (Ctype.equalsIgnoreCase("NUMERIC"))
				cHeader = Double.toString(row.getCell(i).getNumericCellValue());
			if (Ctype1.equalsIgnoreCase("String"))
				cValue = row1.getCell(i).getStringCellValue();
			else if (Ctype1.equalsIgnoreCase("NUMERIC"))
				cValue = Double.toString(row1.getCell(i).getNumericCellValue());
			dataSet.put(cHeader, cValue);
		}
		return dataSet;
	}

	/**
	 * Read test data from specific row number
	 * 
	 * @param sheetName Worksheet name
	 * @param colName   Column header
	 * @param rowNum    Rownumber
	 * @return
	 */
	public String readDataForRow(String sheetName, String colName, int rowNum) {
		File file;
		FileInputStream fin;
		String cellValue = null;
		try {
			file = new File(filePath);
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);
			sheetName = sheetName.trim();

			XSSFRow firstRow = workbook.getSheet(sheetName).getRow(0);
			row = workbook.getSheet(sheetName).getRow(rowNum);
			int cc = firstRow.getLastCellNum() - firstRow.getFirstCellNum();

			for (int i = 0; i < cc; i++) {
				String fetchValue = firstRow.getCell(i).getStringCellValue().trim();

				if (colName.equalsIgnoreCase(fetchValue)) {
					XSSFCell cell = row.getCell(i);
					if (!(cell == null)) {
						if (cell.getCellType() == CellType.STRING) {
							cellValue = row.getCell(i).getStringCellValue();
						} else if (cell.getCellType() == CellType.BOOLEAN) {
							cellValue = String.valueOf(row.getCell(i).getBooleanCellValue());
						} else if (cell.getCellType() == CellType.NUMERIC) {
							cellValue = NumberToTextConverter.toText(row.getCell(i).getNumericCellValue());
						} else if (cell.getCellType() == CellType.FORMULA) {
							try {
								cellValue = row.getCell(i).getStringCellValue();
							} catch (Exception e) {
								cellValue = NumberToTextConverter.toText(row.getCell(i).getNumericCellValue());
							}
						}
					} else {
						cellValue = "";
					}

					break;
				}
			}
			fin.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found ");
		} catch (IOException e) {
			System.out.println("Error while reading a Test Data file ");
		}

		return cellValue;
	}

	public int getTestDataCount(String sheetName) {
		File file;
		FileInputStream fin;
		sheetName = sheetName.trim();
		int totalTestDataCount = 1;
		try {
			file = new File(filePath);
			fin = new FileInputStream(file);
			workbook = new XSSFWorkbook(fin);

			sheet = workbook.getSheet(sheetName);
			totalTestDataCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			fin.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found ");
			totalTestDataCount = 1;
		} catch (IOException e) {
			System.out.println("Error while reading a Test Data file ");
			totalTestDataCount = 1;
		}
		return totalTestDataCount;
	}
}
