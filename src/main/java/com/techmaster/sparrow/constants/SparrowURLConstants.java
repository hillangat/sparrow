package com.techmaster.sparrow.constants;

public interface SparrowURLConstants {
	
	public static final String COMPUTER_USER_BASE_URL = "C:\\Users\\hillangat\\programming\\intelliJ\\workspaces\\sparrow\\";
	
	public static final String RESOURCE_BASE_PATH = COMPUTER_USER_BASE_URL + "src\\main\\resources\\";
	public static final String RESOURCE_BASE_XML_PATH = RESOURCE_BASE_PATH + "xml\\";

	public static final String QRY_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "Query.xml";
	public static final String UI_MSG_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "UIMessages.xml";

	public static final String JSONS_APTH = SparrowURLConstants.RESOURCE_BASE_PATH + "jsons\\";
	public static final String CACHE_REFRESH_JSON = JSONS_APTH + "cache_refresh.json";
	public static final String DATA_LOAD_CONFIG_JSON = JSONS_APTH + "data_load_config.json";

	public static final String IMPORT_EXCEL_FOLDER = RESOURCE_BASE_PATH + "\\import";
	
}
