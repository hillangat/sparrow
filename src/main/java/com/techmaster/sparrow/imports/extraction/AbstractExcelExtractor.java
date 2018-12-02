package com.techmaster.sparrow.imports.extraction;

import com.techmaster.sparrow.util.SparrowUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public abstract class AbstractExcelExtractor<E> implements ExcelExtractor<E> {

	protected boolean success = false;
	protected String[] surfaceErrors = null;
	protected boolean surfaceValid = false;
	protected Map<String, Object> bundle = new HashMap<>();
	protected Workbook workbook;
	protected String originalFileName;
	protected String userName;
	protected List<E> listBean = new ArrayList<>();

	public boolean isSuccess() {
		return success;
	}

	public String[] getSurfaceErrors() {
		return surfaceErrors;
	}

	public boolean isSurfaceValid() {
		return surfaceValid;
	}

	public Map<String, Object> getBundle() {
		return bundle;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public List<E> getBeanList() {
		return listBean;
	}

	@Override
	public String[] extractHeaders(String sheetName, Workbook workbook) {
		String[] headers = ExcelExtractorUtil.getInstance().extractHeaders(sheetName, workbook);
		return headers;
	}

	@Override
	public String[] validateHeaders(String[] inputHeaders, String[] validHeaders) {
		String[] errorHeaders = ExcelExtractorUtil.getInstance().validateHeaders(inputHeaders, validHeaders);
		return errorHeaders;
	}
	

	@Override
	public Map<Integer, List<Object>> extractData(String sheetName, Workbook workbook) {
		
		if(sheetName == null || sheetName.trim().equalsIgnoreCase(""))
			throw new IllegalArgumentException("Sheet name provided is not valid. Name >> " + sheetName);
		
		Sheet sheet = workbook.getSheet(sheetName);
		if(sheet == null) throw new IllegalArgumentException("No sheet found >> " + sheetName);
		
		Map<Integer, List<Object>> rowDataMap = new HashMap<Integer, List<Object>>();
		
		int lastRowNum = sheet.getLastRowNum();
		
		for(int i=1; i<=lastRowNum; i++){
			Row row = sheet.getRow(i);
			List<Object> rowData = new ArrayList<Object>();
			int lastCellNum = row.getLastCellNum();
			for(int j=0; j< lastCellNum ; j++){
				Cell cell = row.getCell(j);
				Object value = getCellValue(cell);	
				rowData.add(value);
			}
			rowDataMap.put(i, rowData);
		}
		
		return rowDataMap;
	}

	@Override
	public void createErrorCell(String sheetName, Workbook workbook, Integer rowNum, String[] errors) {
		
		if(sheetName == null || sheetName.trim().equalsIgnoreCase(""))
			throw new IllegalArgumentException("Sheet name provided is not valid. Name >> " + sheetName);
		Sheet sheet = workbook.getSheet(sheetName);
		if(sheet == null) throw new IllegalArgumentException("No sheet found >> " + sheetName);
		
		String[] headers = extractHeaders(sheetName,workbook);
		boolean errorHeadFound = false;
		
		for(String header : headers){
			if(header.equals(ExcelExtractor.ERRORS_STR)){
				errorHeadFound = true;
				break;
			}
		}
		
		int errorCellNum = 0;
		int firstRow = sheet.getFirstRowNum();
		Row headerRow = sheet.getRow(firstRow);
		
		if(!errorHeadFound){
			errorCellNum = headerRow.getLastCellNum();
			Cell cell = headerRow.createCell(errorCellNum);
			cell.setCellValue(ExcelExtractor.ERRORS_STR);
		}else{
			Iterator<Cell> headerItr = headerRow.cellIterator();
			while(headerItr.hasNext()){
				Cell cell = headerItr.next();
				String name = String.valueOf(getCellValue(cell));
				if(name != null && !name.trim().equals("") && name.equals(ExcelExtractor.ERRORS_STR)){
					errorCellNum = cell.getColumnIndex();
				}
			}
		}
		
		Row dataRow = sheet.getRow(rowNum);
		Cell errorCell = dataRow.createCell(errorCellNum);
		String errorsStr = SparrowUtil.getCommaDelimitedStrings(errors);
		errorCell.setCellValue(errorsStr); 
		
	}

	@Override
	public void createStatusCell(String sheetName, Workbook workbook, Integer rowNum, String status) {
		if(sheetName == null || sheetName.trim().equalsIgnoreCase(""))
			throw new IllegalArgumentException("Sheet name provided is not valid. Name >> " + sheetName);
		Sheet sheet = workbook.getSheet(sheetName);
		if(sheet == null) throw new IllegalArgumentException("No sheet found >> " + sheetName);
		
		String[] headers = extractHeaders(sheetName,workbook);
		boolean statusHeaderFound = false;
		
		for(String header : headers){
			if(header.equals(ExcelExtractor.STATUS_STR)){
				statusHeaderFound = true;
				break;
			}
		}
		
		int errorCellNum = 0;
		int firstRow = sheet.getFirstRowNum();
		Row headerRow = sheet.getRow(firstRow);
		
		if(!statusHeaderFound){
			errorCellNum = headerRow.getLastCellNum();
			Cell cell = headerRow.createCell(errorCellNum);
			cell.setCellValue(ExcelExtractor.STATUS_STR);
		}else{
			Iterator<Cell> headerItr = headerRow.cellIterator();
			while(headerItr.hasNext()){
				Cell cell = headerItr.next();
				String name = String.valueOf(getCellValue(cell));
				if(name != null && !name.trim().equals("") && name.equals(ExcelExtractor.STATUS_STR)){
					errorCellNum = cell.getColumnIndex();
				}
			}
		}
		
		Row lastRow = sheet.getRow(1);
		Cell errorCell = lastRow.createCell(errorCellNum);
		
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(new HSSFColor.RED().getIndex());
		cellStyle.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());
		errorCell.setCellStyle(cellStyle); 
		
		errorCell.setCellValue(status);
		
		int statusRow = extractHeaders(sheetName, workbook).length - 1;
		CellRangeAddress cRngAddrs = new CellRangeAddress(1, rowNum, statusRow, statusRow);
		sheet.addMergedRegion(cRngAddrs);
	}

	@Override
	public Object getCellValue(Cell cell) {
		Cell hcell = (Cell)cell;
		return ExcelExtractorUtil.getInstance().getCellValue(cell);
	}

	@Override
	public void saveBeanList(CrudRepository<E, ?> repository) {
		repository.saveAll(this.listBean);
	}
}
