package com.techmaster.sparrow.imports.extraction;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * @author hillangat
 *
 */
public interface ExcelExtractor  {
	
	/**
	 * This will be a list of constant string to retrieve extractors.
	 */
	public static final String STATUS_STR = "STATUS";
	public static final String STATUS_SUCCESS_STR = "SUCCESS";
	public static final String STATUS_FAILED_STR = "FAILED";
	public static final String ERRORS_STR = "ERRORS";
	public static final String SURFACE_VALIDATION = "SURFACE_VALIDATION";
	public static final String DATA_BEANS = "DATA_BEANS";
	public static final String RETURNED_WORKBOOK = "RETURNED_WORKBOOK";
	public static final String EXTRACTED_BUNDLE = "EXTRACTED_BUNDLE";
	public static final String SESSION_ID = "SESSION_ID";
	
	public static final String LOCATION_EXTRACTOR = "LOCATION_EXTRACTOR";

	/**
	 * @param workbook : The excel sheet workbook
	 * @param workbook : The sheet from where the headers are extracted.  
	 * @return String[]: The extracted headers as array of strings
	 */
	public abstract String[] extractHeaders(String sheetName, Workbook workbook);
	
	/**
	 * @param inputHeaders : Headers from the sheet as extracted in {@link this.extractHeaders()}
	 * @param validHeaders : known headers as expected in the sheet.
	 * @return String[] returns headers strings that are not found.
	 */
	public abstract String[] validateHeaders(String[] inputHeaders, String[] validHeaders);
	
	/**
	 * @param workbook : The excel sheet workbook.
	 * @return Map<Short, List<Object>>: The map of rows. The keys for the big map is the excel row number. 
	 * The list in the returned map is the list of cell values ordered in cell numbers. 
	 */
	public abstract Map<Integer, List<Object>> extractData(String sheetName, Workbook workbook);
	
	/**
	 * @param data : The extracted map of list of cell values of all rows
	 * @return Map<Integer, List<String>>: Returns a map of list of errors for a given row.
	 */
	public abstract Map<Integer, List<String>> validate(Map<Integer, List<Object>> data);
	
	/**
	 * Creates a column with the header "Errors" if such a header does not exist.
	 * Writes comma separated error messages in a cell of the given row. 
	 * Checks first and create a column with "ERRORS" header. 
	 * It creates the header if it does not and takes the header row number and the rown number passed in to create 
	 * comma separated error messages.
	 * @param workbook : The excel sheet workbook.
	 * @param rowNum : The row number of the error message header. It specifies exactly where to write error messages.
	 * @param errors: The errors obtained while validating the row
	 */
	public abstract void createErrorCell(String sheetName, Workbook workbook, Integer rowNum, String[] errors);
	
	/**
	 * Creates a column with the header "Status" if such a header does not exist. 
	 * The only values for thsi cell is "Success" with green color and "Failed" with red color.
	 * Checks first and create a column with "STATUS" header. 
	 * It creates the header if it does not and takes the header row number and the rown number passed in to create 
	 * @param workbook : The excel sheet workbook.
	 * @param rowNum : The last row number. The cell with stretch down to the last row.
	 * @param status: The string "Fail" or "Success".
	 */
	public abstract void createStatusCell(String sheetName, Workbook workbook, Integer rowNum, String status);
	
	/**
	 * @param data : The extracted map of list of cell values of all rows
	 * @return List<Object>: Returns the beans created from the extracted data
	 */
	public abstract List<?> getDataBeans(Map<Integer, List<Object>> data);
	
	
	/**
	 * @param cell : The cell for which the value needs to be extracted.
	 * @return Object: Returns the cell value as an object
	 */
	public abstract Object getCellValue(Cell cell);
	
	/**
	  * @return Map<String, Object> : returns errors, updated workbook, status, etc.
	 */
	public abstract Map<String, Object> execute();

	/**
	 * @return boolean : returns true if the operation succeeded.
	 */
	public abstract boolean success();
	
	
}

