package com.techmaster.sparrow.imports.extraction;

import com.techmaster.sparrow.util.SparrowUtility;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class ExcelExtractorUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelExtractorUtil.class);

	// private extractor
	private ExcelExtractorUtil() {
		super();
	}
	
	private static ExcelExtractorUtil extractorUtil = null;
	
	static {
		if(extractorUtil == null){
			synchronized (ExcelExtractorUtil.class) {
				extractorUtil = new ExcelExtractorUtil();
			}
		}
	}
	
	public static ExcelExtractorUtil getInstance(){
		return extractorUtil;
	}
	
	public static List<String> validateSheets(List<String> sheets, Workbook workbook){
		List<String> messages = new ArrayList<>();
		for(String sheetName : sheets){
			Sheet sheet = workbook.getSheet(sheetName);
			if(sheet == null){
				messages.add("Sheet '" + sheetName + "' does not exist!");  
			}
		}
		return messages;
	}
	
	public int getLastRowNumber(String sheetName, Workbook workbook){
		if(sheetName == null) throw new IllegalArgumentException("Sheet name provided is null >> " + sheetName);
		Sheet sheet =workbook.getSheet(sheetName);
		int lastRow = sheet.getLastRowNum();
		logger.debug("Obtained the last row number (" + lastRow + ") from the sheet >> " + sheetName); 
		return lastRow;
	}
	
	public Row getLastRow(String sheetName, Workbook workbook){
		Sheet sheet = workbook.getSheet(sheetName);
		int lastRowNum = getLastRowNumber(sheetName, workbook);
		Row row = sheet.getRow(lastRowNum);
		return row;
	}
	
	public static Cell getLastCell(int rowNum, Sheet sheet){
		Row row = sheet.getRow(rowNum);
		int cellNum = 0;
		if(row != null){
			Iterator<Cell> cellItr = row.cellIterator();
			while(cellItr.hasNext())
				cellNum++;
		}else{
			throw new IllegalArgumentException("No row found of the row number given in the sheet passed in.");
		}
		Cell cell = row.getCell(cellNum);
		return cell;
	}
	
	public static Cell getLastCell(Row row){
		int cellNum = 0;
		if(row != null){
			Iterator<Cell> cellItr = row.cellIterator();
			while(cellItr.hasNext())
				cellNum++;
		}
		Cell cell = row.getCell(cellNum);
		return cell;
	}
	
	public String[] extractHeaders(String sheetName, Workbook workbook) {
		Sheet sheet = workbook.getSheet(sheetName);
		Row headerRow = sheet.getRow(0);
		String[] headers = new String[0];
		int lastRow = headerRow.getLastCellNum();
		for(int i=0; i<lastRow ; i++){
			Cell header = headerRow.getCell(i);
			if(header == null){
				logger.info("header is null. i=(" + i + ")" );
			}
			String name = header.getStringCellValue();
			name = name == null ? null : name.trim();
			headers = SparrowUtility.initArrayAndInsert(headers, name);
		}
		return headers;
	}
	
	public String[] validateHeaders(String[] inputHeaders, String[] validHeaders) {
			
			if(inputHeaders == null || inputHeaders.length < 1 || validHeaders == null || validHeaders.length < 1)
				throw new IllegalArgumentException("Either headers in the sheet >> " + inputHeaders  + " or Headers with which to validate >> " + validHeaders);
			
			String[] errors = new String[0];
			
			for(String validHeader :  validHeaders){
				boolean isFound = false;
				for(String input : inputHeaders){
					if(input.equalsIgnoreCase(validHeader)){
						isFound = true;
						break;
					}
				}
				if(!isFound){
					errors = SparrowUtility.initArrayAndInsert(errors, validHeader);
				}
			}
			
			return errors;
	}
	
	public Object getCellValue(Cell cell) {
		HSSFCell hcell = (HSSFCell)cell;
		Object obj = null;
		if(cell != null){
			int type = hcell.getCellType();
			if(type== HSSFCell.CELL_TYPE_STRING){
				obj = hcell.getStringCellValue();
			}else if(type== HSSFCell.CELL_TYPE_NUMERIC){
				obj = hcell.getNumericCellValue();
			}else if(type== HSSFCell.CELL_TYPE_BOOLEAN){
				obj = hcell.getBooleanCellValue();
			}else if(type== HSSFCell.CELL_TYPE_ERROR){
				obj = hcell.getErrorCellValue();
			}else if(type== HSSFCell.CELL_TYPE_BLANK){
				obj = "";
			}
		}
		return obj;
	}
	
	
	public void writeCell(Object object, Cell cell) {
		if(cell != null){
			/*
			 if(object == null){
				String obj = null;
				cell.setCellValue(obj); 
			}else 
			*/
			if(object instanceof Long){
				Long lng = (Long)object;
				Double doubleValue = lng.doubleValue(); 
				cell.setCellValue(doubleValue); 
			}else if(object instanceof Double){
				Double doubleVal = (Double)object;
				cell.setCellValue(doubleVal); 
			}else if(object instanceof String){
				String strVal = String.valueOf(object);
				cell.setCellValue(strVal);
			}else if(object instanceof Integer){
				Integer integer = (Integer)object;
				Double doubleVal = Double.parseDouble(String.valueOf(integer));
				cell.setCellValue(doubleVal);  
			}else if(object instanceof java.util.Date || object instanceof java.sql.Date){
				java.util.Date jDate = null;
				if(object instanceof java.sql.Date){
					java.sql.Date sDate = (java.sql.Date)object;
					Long time = sDate.getTime();
					jDate = new java.util.Date(time);
				}else{
					jDate = (java.util.Date)object;
				}
				cell.setCellValue(jDate); 
			}else if(object instanceof Calendar ){
				Calendar calendar = (Calendar)object;
				cell.setCellValue(calendar);
			}else if(object instanceof RichTextString){
				RichTextString rTxtStr = (RichTextString) object;
				cell.setCellValue(rTxtStr);
			}else if(object instanceof Boolean ){
				Boolean boolean_ = (Boolean)object;
				cell.setCellValue(boolean_);
			}
		}
	}
	
	
 
 
 	
	
	
	

}
