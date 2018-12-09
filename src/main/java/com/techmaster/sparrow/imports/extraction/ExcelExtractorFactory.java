package com.techmaster.sparrow.imports.extraction;

import com.techmaster.sparrow.imports.extractors.LocationExtractor;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelExtractorFactory {
	
	private static ExcelExtractorFactory  excelExtractorFactory = null;
	
	static{
		if(excelExtractorFactory == null){
			synchronized (ExcelExtractorFactory.class) {
				excelExtractorFactory = new ExcelExtractorFactory();
			}
		}
	}
	
	public static ExcelExtractorFactory getIntance(){
		return excelExtractorFactory;
	}
	
	// private constructor
	private ExcelExtractorFactory(){
		super();
	}
	
	public static ExcelExtractor getExtractor(String jndi, Workbook workbook, String userName, String originalFileName){
		ExcelExtractor extractor = null;
		if(jndi != null && jndi.equalsIgnoreCase(ExcelExtractor.LOCATION_EXTRACTOR)){
			extractor =  new LocationExtractor(workbook, originalFileName, userName);
		}
		return extractor;
	}
	
	

}
