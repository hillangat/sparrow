package com.techmaster.sparrow.constants;

public interface SparrowURLConstants {
	
	String COMPUTER_USER_BASE_URL = "C:\\Users\\hillangat\\programming\\intelliJ\\workspaces\\sparrow\\";
	
	String RESOURCE_BASE_PATH = COMPUTER_USER_BASE_URL + "src\\main\\resources\\";
	String RESOURCE_BASE_XML_PATH = RESOURCE_BASE_PATH + "xml\\";

	String QRY_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "Query.xml";
	String UI_MSG_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "UIMessages.xml";

	String JSONS_APTH = SparrowURLConstants.RESOURCE_BASE_PATH + "jsons\\";
	String CACHE_REFRESH_JSON = JSONS_APTH + "cache_refresh.json";
	String DATA_LOAD_CONFIG_JSON = JSONS_APTH + "data_load_config.json";
	String RULE_TYPES_JSON_PATH = RESOURCE_BASE_PATH + "rules\\ruleTypes\\ruleTypes.json";
	String DROOL_DRL_FILES = RESOURCE_BASE_PATH + "rules\\files";
	String EMAIL_TEMPLATES_PATH = RESOURCE_BASE_XML_PATH + "templates\\";

	String IMPORT_EXCEL_FOLDER = RESOURCE_BASE_PATH + "\\import";
	String MEDIA_FILES_FOLDER = RESOURCE_BASE_PATH + "media";
	
}
