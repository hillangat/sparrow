package com.techmaster.sparrow.imports.extractors;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.ImportBean;
import com.techmaster.sparrow.entities.Location;
import com.techmaster.sparrow.enums.LocationTypeEnum;
import com.techmaster.sparrow.imports.beans.ImportHelper;
import com.techmaster.sparrow.imports.extraction.AbstractExcelExtractor;
import com.techmaster.sparrow.imports.extraction.ExcelExtractor;
import com.techmaster.sparrow.imports.extraction.ExcelExtractorUtil;
import com.techmaster.sparrow.repositories.ImportBeanRepository;
import com.techmaster.sparrow.repositories.LocationRepository;
import com.techmaster.sparrow.repositories.SparrowDaoFactory;
import com.techmaster.sparrow.util.SparrowUtility;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class LocationExtractor extends AbstractExcelExtractor<Location> {

	public static final String[] validHeaders = new String[]{
			"locationId", "name", "code", "longitude", "latitude",
			"parentId", "locationType", "subLocations"};

	private Logger logger = LoggerFactory.getLogger(LocationExtractor.class);

	public LocationExtractor(Workbook workbook, String originalFileName, String userName) {
		super();
		this.workbook = workbook;
		this.originalFileName = originalFileName;
		this.userName = userName;
	}
	
	@Override
	public Map<Integer, List<String>> validate(Map<Integer, List<Object>> data) {
		
		logger.debug("Validating hunter message receiver data extracted..." ); 
		Map<Integer, List<String>> errors = new HashMap<>();

		List<String> locationIds = data
				.values()
				.stream()
				.map(list -> list.get(0).toString())
				.collect(Collectors.toList());

		for(Map.Entry<Integer, List<Object>> dataRows : data.entrySet()){
			
			Integer dataRowNum = dataRows.getKey();
			List<Object> rowData = dataRows.getValue();
			List<String> rowErrors = new ArrayList<>();

			for(int i=0; i<rowData.size(); i++){
				
				String objStr = rowData.get(i) != null ? rowData.get(i) + "" : null;
				
				// locationId
				if(i==0){
				
					if(objStr == null || objStr.equals("")){
						rowErrors.add("Location ID cannot be blank");
					}

					if (!SparrowUtility.isNumeric(objStr)) {
						rowErrors.add("Location must be a number");
					}
					
				// name
				}else if( i == 1 ){

					if(objStr == null || objStr.equals("")){
						rowErrors.add("Name cannot be blank");
					} else if (SparrowUtility.isNumeric(objStr)){
						rowErrors.add("Name cannot be a number");
					} else if (objStr.length() < 2) {
						rowErrors.add("Name cannot be less than two characters");
					}

				// code
				}else if( i == 2 ){

					/* No validation for code right now */

				// longitude && latitude
				}else if( i == 3 || i == 4 ){

					Long val = objStr != null ? SparrowUtility.getLongFromObject(objStr) : -1;
					if (!SparrowUtility.isNumeric(objStr) || SparrowUtility.isNumeric(objStr) && !( 0 >= val && 180 <= val)) {
						rowErrors.add("Must be a number between 0 and 180");
					}
					
				// parentId
				}else if( i == 5 ){

					if (objStr != null && !SparrowUtility.isNumeric(objStr)) {
						rowErrors.add("Parent ID must be a number");
					} else {
						if (!locationIds.contains(objStr)) {
							rowErrors.add("The parent row of ID: " + objStr + ", not found!");
						}
					}
				
				// locationType
				}else if( i == 6 ){

					boolean valid = !Arrays
							.stream(LocationTypeEnum.values())
							.anyMatch(l -> l.toString().equalsIgnoreCase(objStr));

					if (valid) {
						rowErrors.add("Location Type must be one of : " + Arrays.toString(LocationTypeEnum.values()));
					}
					
				//subLocations
				}else if(i==5){

					if (objStr != null) {
						logger.debug("sub locations provide, will be ignored and auto-generated based on IDs");
					}
					
				}
				
			}
			
			if(!rowErrors.isEmpty()){
				errors.put(dataRowNum, rowErrors);
			}
			
		}
		
		logger.debug("Successfully finished validating Hunter Message Receiver data!");
		
		if(!errors.isEmpty()){
			logger.debug("Data did not pass validation. Number of errors( " + errors.size() + " )");  
		}else{
			logger.debug("Data passed validation!!");
		}
		return errors;
	}

	@Override
	public List<Location> getDataBeans(Map<Integer, List<Object>> data) {
		
		logger.debug("Starting bean construction process for Hunter Message Receiver extractor..."); 
		List<Location> locations = new ArrayList<>();
		
		for(Map.Entry<Integer, List<Object>> dataRows : data.entrySet()){
			
			List<Object> dataRow = dataRows.getValue();
			Location location = new Location();
			
			for(int i=0; i<dataRow.size(); i++){

				Object obj = dataRow.get(i);
				String objStr = obj == null ? null : obj.toString();

				/**
				 * "locationId", "name", "code", "longitude", "latitude",
				 * 			"parentId", "locationType", "subLocations"
				 */

				// locationId
				if(i==0){
					location.setLocationId(Long.valueOf(objStr));
				
				// name
				}else if(i==1){
					location.setName(objStr);
				
				// code
				}else if(i==2){
					location.setCode(objStr);
					
				//longitude
				}else if(i==3){
					location.setLongitude(objStr != null ? Double.valueOf(objStr) : 0);
				
				//latitude
				}else if(i==4){
					location.setLatitude(objStr != null ? Double.valueOf(objStr) : 0);
					
				//parentId
				}else if(i==5){
					location.setParentId(objStr != null ? Long.valueOf(objStr) : 0);
					
				// locationType
				}else if(i==6){
					location.setLocationType(LocationTypeEnum.valueOf(objStr));
				}
				
			}
			
			locations.add(location);
			
		}
		logger.debug("Successfully finished constructing data beans from data extracted! Size ( " + locations.size() + " )");
		return locations;
	}

	@Override
	public Map<String, Object> execute() {
		
		logger.info("Starting extraction execution process for HunterMessageReceiverExtractor..."); 
		Map<String, Object> bundle = new HashMap<String, Object>();
		List<String> sheets = Arrays.asList(new String[]{LOCATION_EXTRACTOR});
		List<String> sheetsMsgs = ExcelExtractorUtil.validateSheets(sheets, workbook);
		
		if(sheetsMsgs != null && sheetsMsgs.size() > 0){
			this.surfaceValid = false;
			for(String sheetMsg : sheetsMsgs){
				surfaceErrors = SparrowUtility.initArrayAndInsert(surfaceErrors, sheetMsg);
			}
			bundle.put(ExcelExtractor.SURFACE_VALIDATION, false);
			bundle.put(ExcelExtractor.ERRORS_STR, surfaceErrors);
			this.bundle = bundle;
			this.success = false;
			logger.debug("Bad sheet! Errors > " + Arrays.toString(surfaceErrors));  
			return bundle;
		}
		
		List<Location> locations = null;
		int lastRowNum = ExcelExtractorUtil.getInstance().getLastRowNumber(LOCATION_EXTRACTOR, workbook);
		String status = null;
		
		String[] headers = extractHeaders(LOCATION_EXTRACTOR, workbook);
		String[] invalidHeaders = validateHeaders(headers, validHeaders);
		
		if(invalidHeaders != null && invalidHeaders.length > 0){
			this.surfaceValid = true;
			for(String header : invalidHeaders){
				surfaceErrors = SparrowUtility.initArrayAndInsert(surfaceErrors, header + " : is not found");
			}
			bundle.put(ExcelExtractor.SURFACE_VALIDATION, false);
			bundle.put(ExcelExtractor.ERRORS_STR, surfaceErrors);
			this.bundle = bundle;
			this.success = false;
			logger.debug("Bad sheet! Errors > " + Arrays.toString(surfaceErrors));
			return bundle;
		}
		
		Map<Integer, List<Object>> data = extractData(LOCATION_EXTRACTOR, this.workbook);
		Map<Integer, List<String>> errors = validate(data);
		
		if(!errors.isEmpty()){
			status = ExcelExtractor.STATUS_FAILED_STR;
			for(Map.Entry<Integer, List<String>> entry : errors.entrySet()){
				Integer rowNum = entry.getKey();
				List<String> rowErrors = entry.getValue();
				Object[] rowErrorsArray = rowErrors.toArray();

				/* create the error cells only if the errors are found for that row. */

				if(rowErrorsArray.length > 0){
					String [] stringArray = SparrowUtility.convertToStringArray(rowErrorsArray);
	 				createErrorCell(LOCATION_EXTRACTOR, workbook, rowNum, stringArray);
				}
			}
			
			bundle.put(RETURNED_WORKBOOK, workbook);
			this.bundle = bundle;
			this.success = false;
			
		} else {
			
			status = ExcelExtractor.STATUS_SUCCESS_STR;
			locations = getDataBeans(data);
			logger.debug("Saving extracted hunter message receiver beans...");
			LocationRepository locationRepository = SparrowDaoFactory.getDaoObject(LocationRepository.class);
			if (locationRepository != null) {
				new LoadCacheWorker().start();
				bundle.put(DATA_BEANS, locations);
				bundle.put(RETURNED_WORKBOOK, workbook);
				bundle.put(STATUS_STR, true);

				this.bundle = bundle;
				this.success = true;
				this.surfaceErrors = null;
			} else {
				logger.error("No location service bean found. locations not loaded to cache!!");
			}
			
		}
		
		createStatusCell(LOCATION_EXTRACTOR, workbook, lastRowNum, status);
		
		logger.debug("Saving excel file to db.."); 
		String stsStr = this.isSuccess() ? SparrowConstants.STATUS_SUCCESS : SparrowConstants.STATUS_FAILED;
		ImportBean importBean = ImportHelper.createImportBeanFromWorkbook(workbook, userName, originalFileName, Location.class.getSimpleName(), stsStr);

		ImportBeanRepository repository = SparrowDaoFactory.getDaoObject(ImportBeanRepository.class);
		if (repository != null) {
			repository.save(importBean);
		} else {
			logger.error("Import Bean Repository bean not found!! Import bean not save to db");
		}

		
		return bundle;
	}
	
	class LoadCacheWorker extends Thread {
		@Override
		public void run() {
			logger.debug("Loading receivers..."); 
			SparrowCacheUtil.getInstance().refreshAllLocations();
			logger.debug("Done loading receivers!!");
		}
	}

	@Override
	public boolean success() {
		return success;
	}
}
