package com.techmaster.sparrow.constants;

public interface HunterURLConstants {
	
	public static final String COMPUTER_USER_BASE_URL = "C:\\Users\\hillangat\\programming\\intelliJ\\workspaces\\sparrow\\";
	
	public static final String HUNTER_TABLE_CONFIGS_FOLDER = "configs\\hunterTableConfigs\\";
	
	public static final String RESOURCE_BASE_PATH = COMPUTER_USER_BASE_URL + "src\\main\\resources\\";
	public static final String RESOURCE_BASE_IMAGES_PATH = RESOURCE_BASE_PATH + "images\\";
	public static final String RESOURCE_BASE_XML_PATH = RESOURCE_BASE_PATH + "xml\\";
	public static final String RESOURCE_BASE_WORKBOOK_PATH = RESOURCE_BASE_PATH + "workbooks\\";
	
	public static final String QUERY_TO_BEAN_MAPPER = RESOURCE_BASE_XML_PATH + "query_to_bean_mapping.xml";
	public static final String GRID_HEADERS_MAPPINGS = RESOURCE_BASE_XML_PATH + "grid_headers.xml";
	
	public static final String HUNTER_CONFIG_XML_PATH = RESOURCE_BASE_XML_PATH + "ClientConfig.xml";
	public static final String QRY_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "Query.xml";
	public static final String UI_MSG_XML_FL_LOC_PATH = RESOURCE_BASE_XML_PATH + "UIMessages.xml";
	public static final String CLIENT_CONFIG_LOC_PATH = RESOURCE_BASE_XML_PATH + "ClientConfig.xml";
	public static final String RESPONSE_CONFIG_LOC_PATH = RESOURCE_BASE_XML_PATH + "ResponseConfig.xml";
	public static final String EMAIL_TEMPLATES_LOCL_PATH = RESOURCE_BASE_XML_PATH + "emailTemplates.xml";
	public static final String EMAIL_CONFIGS__LOCL_PATH = RESOURCE_BASE_XML_PATH + "emailConfigs.xml";
	public static final String TASK_PROCESS_JOBS_TEMPLATE = RESOURCE_BASE_XML_PATH + "tskPcssJobTemplate.xml";
	public static final String OZEKI_TEST_RSPONSE_XML_LOCL_PATH = RESOURCE_BASE_XML_PATH + "ozekiTestResponse.xml";
	public static final String RESOURCE_TEMPL_FOLDER = COMPUTER_USER_BASE_URL + "src\\main\\webapp\\resources\\tempFolder";
	public static final String POST_LOGIN_URL = "http://localhost:8080/Hunter/hunter/login/after";
	public static final String TESTING_ATTCHMENT_PATH = RESOURCE_BASE_PATH + "Notes\\testing_attachment.txt";
	public static final String LOGIN_DATA_SEE_XML  = RESOURCE_BASE_XML_PATH +  "loginDataSeed.xml";
	public static final String HUNTER_SOCIAL_APP_CONFIG_PATH = RESOURCE_BASE_XML_PATH + "hunterSocialAppConfig.xml";
	public static final String HUNTER_ANGULAR_GRID_HEADERS_PATH = RESOURCE_BASE_XML_PATH + "grid_headers.xml";
	public static final String HUNTER_GRID_QUERY_FIELD_MAPPING = RESOURCE_BASE_XML_PATH + "UiDbFieldMappings.xml";
	
	public static final String JSONS_APTH = HunterURLConstants.RESOURCE_BASE_PATH + "jsons\\";
	public static final String WORKFLOW_TREES_JSONS = JSONS_APTH + "workflow_trees.json";
	public static final String CACHE_REFRESH_JSONS = JSONS_APTH + "cache_refresh.json";
	
	public static final String SELECT_VALUES_RESOURCES_PATH = HUNTER_TABLE_CONFIGS_FOLDER + "selectValues.json";

}
