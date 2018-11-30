
package com.techmaster.sparrow.constants;

public class SparrowConstants {
	
	//http://localhost:9998/Login.aspx
	//1000:QaBdjB/so2UtXtQTTL4v0/3Kki2ajvtC:m62HrhKB/2MD6+gh+EnpRDxLEBZLEToh
	
	// Hunter specific
	public static final String HUNTER_ADMIN_USER_NAME = "hunterAdmin";
	public static final String HUNTER_ADMIN_PSSWRD = "hunterAdmin.1900";
	public static final String HUNTER_ADMIN_EMAIL_USER_NAM = "techmaster.hunter.mail@gmail.com";
	public static final String HUNTER_ADMIN_EMAIL_PSSWRD = "hunter.admin.1900";
	public static final String HUNTER_ADMIN_POSTGRESS_SQL_USRNAM = "postgres";
	public static final String HUNTER_ADMIN_POSTGRESS_SQL_PSSCDE = "hunter.admin.90"; /** system >> hunter.admin.1900 */
	public static final String HUNTER_TWITTER_PSSWRD = "techmaster.hunter.admin.1990";
	public static final String HUNTER_FB_PSSWRD = "techmaster.hunter.admin.1990";
	public static final String HUNTER_SYSTEM_USER_NAME = "hunterSystem";
	
	// Methods
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	
	// Photo Categories
	public static final String PHOTO_CAT_USER_PROFILE = "USER_PROFILE";
	
	// Photo Size Formats
	public static final String FORMAT_BYTES = "byte";
	
	// Credentials
	public static final String SECURITY_USER_NAME_STR = "userName";
	public static final String SECURITY_USER_PSWRD_STR = "password";
	
	//Excel Sheets
	public static final String DOT_XLS = ".xls";
	public static final String DOT_XLSX = ".xlsx";
	
	// Hunter user types
	public static final String HUNTER_EMPL_USER = "1000";
	public static final String HUNTER_SUPER_ADMIN_USER = "1001";
	public static final String HUNTER_TECH_ADMIN_USER = "1002";
	public static final String RECEIVER_USER = "2000";
	public static final String HUNTER_CLIENT_USER = "2001";
	public static final String HUNTER_RAW_RECEIVER_USER = "2002";
	
	// Message delivery status
	public static final String STATUS_STRING = "status";
	public static final String STATUS_STARTED = "Started";
	public static final String STATUS_CONCEPTUAL = "Conceptual";
	public static final String STATUS_PROCESSED = "Processed";
	public static final String STATUS_PARTIAL = "Partial";
	public static final String STATUS_SENT = "Sent";
	public static final String STATUS_DELEIVERED = "Delivered";
	public static final String STATUS_PENDING = "Pending";
	public static final String STATUS_SUCCESS = "Success";
	public static final String STATUS_FAILED = "Failed";
	
	//GateWayMessage status types
	
	public static final String STATUS_TYPE_CLIENT = "respStatus";
	public static final String STATUS_TYPE_DELIVERY = "deliveryStatus";
	public static final String STATUS_TYPE_LIFE = "lifeStatus";
	
	// message type audio, message, email, etc.
	public static final String MESSAGE_TYPE_AUDIO = "Audio";
	public static final String MESSAGE_TYPE_EMAIL = "Email";
	public static final String MESSAGE_TYPE_SOCIAL = "Social";
	public static final String MESSAGE_TYPE_TEXT = "Text";
	public static final String MESSAGE_TYPE_PHONE_CALL = "Call";
	public static final String MESSAGE_TYPE_VOICE_MAIL = "Voice Mail";
	
	// Contact type 
	public static final String CONTACT_TYPE_TEXT = "TEXT";
	public static final String CONTACT_TYPE_PHONE = "PHONE";
	public static final String CONTACT_TYPE_EMAIL = "EMAIL";
	public static final String CONTACT_TYPE_VOICE_MAIL = "VOICE_MAIL";
	
	// payment card types
	public static final String CARD_TYPE_VISA = "Visa";
	public static final String CARD_TYPE_MASTER_CARD = "Master Card";
	public static final String CARD_TYPE_DISCOVER = "Dicover";
	public static final String CARD_TYPE_AMERICAN_EXPRESS = "American Express";
	
	//JMS types
	public static final String JSMS_TYPE_POINT_TO_POINT = "POINT_TO_POINT";
	public static final String JSMS_TYPE_PUBLISH = "PUBLISH";
	
	// hunter default phone number
	public static final String HUNTER_DEFAULT_PHONE_NUMBER = "0017324704894"; 
	
	
	// message life cycle
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REVIEW = "Review";
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_COMPLETED = "Completed";
	public static final String STATUS_REJECTED = "Rejected";
	
	
	// task types 
	public static final String TASK_TYPE_POLITICAL = "Political";
	public static final String TASK_TYPE_SOCIAL = "Social";
	public static final String TASK_TYPE_CORPORATE = "Corporate";
	public static final String TASK_TYPE_EDUCATIONAL = "Educational";
	public static final String TASK_TYPE_TESTING = "Testing";
	
	
	// receiver region level types 
	public static final String HUNTER_REGION_COUNTRY = "Country";
	public static final String HUNTER_REGION_COUNTY = "County";
	public static final String HUNTER_REGION_CNSTNCY = "Constituency";
	public static final String HUNTER_REGION_WARD = "Ward";
	public static final String HUNTER_REGION_CITY = "City";
	public static final String HUNTER_REGION_VILLAGE = "Village";
	
	public static final String REGION_LEVEL_NAME = "regionLevelName";
	public static final String REGION_LEVEL_Type = "regionLevelType";
	
	
	// date 
	public static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	
	public static final String YEAR 	= "YEAR";
	public static final String MONTH 	= "MONTH";
	public static final String DAY 		= "DAY";
	public static final String HOUR 	= "HOUR";
	public static final String MINUTE 	= "MINUTE";
	public static final String SECOND 	= "SECOND";
	public static final String WEEK 	= "WEEK";
	
	
	// Media types 
	public static final String MEDIA_TYPE_PDF = "PDF";
	public static final String MEDIA_TYPE_PNG = "PNG";
	public static final String MEDIA_TYPE_JPG = "JPG";
	public static final String MEDIA_TYPE_AV = "AV";
	
	// Address type 
	public static final String ADDRESS_TYPE_BILLING = "Billing";
	public static final String ADDRESS_TYPE_SHIPPING = "Shipping";
	
	
	// Receiver levels
	public static final String RECEIVER_LEVEL_COUNTRY = "Country";
	public static final String RECEIVER_LEVEL_STATE = "State";
	public static final String RECEIVER_LEVEL_COUNTY = "County";
	public static final String RECEIVER_LEVEL_CONSITUENCY = "Constituency";
	public static final String RECEIVER_LEVEL_WARD = "Ward";
	public static final String RECEIVER_LEVEL_CITY = "City";
	public static final String RECEIVER_LEVEL_VILLAGE = "Village";
	
	//Receiver Types 
	public static final String RECEIVER_TYPE_EMAIL = "Email";
	public static final String RECEIVER_TYPE_TEXT = "Text";
	public static final String RECEIVER_TYPE_VOICE_MAIL = "Voice";
	public static final String RECEIVER_TYPE_CALL = "Call";
	public static final String RECEIVER_TYPE_SOCIAL = "Social";
	
	// Clients business names
	public static final String CLIENT_CM = "CM";
	public static final String CLIENT_OZEKI = "OZEKI";
	public static final String CLIENT_SAFARICOM = "SAFARICOM";
	public static final String CLIENT_AIRTEL = "AIRTELL";
	public static final String CLIENT_TECH_MASTERS = "TECH MASTERS";
	public static final String CLIENT_HUNTER_EMAIL = "Hunter Email";
	public static final String CLIENT_HUNTER_SOCIAL = "Hunter Social";
	
	// Client Configurations Element Names

	public static final String CONFIG_USER_NAME = "userName";
	public static final String CONFIG_PASSWORD = "password";
	public static final String CONFIG_ACTIVE_METHOD = "actvMethod";
	public static final String CONFIG_ACTIVE_METHOD_URL = "actvURL";
	public static final String CONFIG_DENOMINATION = "denomination";
	public static final String CONFIG_UUID = "UUID";
	public static final String CONFIG_TXT_PRICE = "txtPrice";
	public static final String CONFIG_MEDIA_PRICE = "mediaPrice";
	public static final String CONFIG_VOICE_PRICE = "voicedPrice";
	public static final String CONFIG_CALL_PRICE = "callPriceS";
	public static final String CONFIG_HUNTER_BALANCE = "hunterBalance";
	public static final String CONFIG_HUNTER_MSG_BAL_COUNT = "blncMsgCnt";
	public static final String CONFIG_HUNTER_RATING = "hunterRating";
	public static final String CONFIG_SENDER = "TechMasters";
	public static final String CONFIG_SENDER_KEY = "sender";
	
	public static final String CONFIG_HUNTER_DEFAULT_EMAIL_CONFIG_NAME = "hunterClientDefaultConfigs";
	public static final String CONFIG_TEXT_HTML_UTF8 = "text/html; charset=utf-8";
	public static final String CONFIG_TEXT_HTML_UTF8_KEY = "contentTypeAndCharset";
	
	public static final String BASE_URL = "GET_BASE_URL";
	public static final String TOKEN = "producttoken";
	public static final String BODY = "body";
	public static final String TO = "to";
	public static final String FROM = "from";
	public static final String  REFERENCE = "reference";
	
	// Hunter client Data format with seconds
	public static final String HUNTER_DATE_FORMAT_MILIS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String HUNTER_DATE_FORMAT_SECS = "yyyy-MM-dd HH:mm:ss";
	public static final String HUNTER_DATE_FORMAT_MIN = "yyyy-MM-dd HH:mm";
	
	
	// Session Security constants
	public static final String HNTR_SEC_SESS_BN = "HNTR_SEC_SESS_BN";
	public static final String HNTR_SESS_ADT_INFO = "HNTR_SESS_ADT_INFO";
	
	// Hunter Security Context
	public static final String USR_SEC_CNTXT_BN = "userSecurityContextBean";
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String ROLES = "ROLES";
	public static final long ROLE_USER = 2;
	public static final long ROLE_ADMIN = 1;
	public static final long ROLE_EXT_APP = 3;
	public static final long ROLE_TASK_APPROVER = 4;
	public static final long ROLE_TASK_PROCESSOR = 5;
	
	// Message string to return to client.
	public static final String MESSAGE_STRING = "message"; 
	
	// Mail Type.
	public static final String MAIL_TYPE_TASK_PROCESS_NOTIFICATION = "taskpProcessRequestNotification";
	public static final String MAIL_TYPE_ASK_PROCESS_OVERDUE_NOTIFICATION = "taskProcessOverdueNotification";
	public static final String MAIL_TYPE_TASK_PROCESS_INCOMPLETE_NOTIFICATION = "taskProcessIncompleteNotification";
	public static final String MAIL_TYPE_USER_SIGN_UP_NOTIFICATION = "userSignUpNotification";
	public static final String MAIL_TYPE_TASK_EMAIL_CLIENT = "taskEmailClient";
	
	
	public static final String[] MAIL_TYPES_NAMES = new String[]{
		MAIL_TYPE_TASK_PROCESS_NOTIFICATION,
		MAIL_TYPE_ASK_PROCESS_OVERDUE_NOTIFICATION,
		MAIL_TYPE_TASK_PROCESS_INCOMPLETE_NOTIFICATION,
		MAIL_TYPE_USER_SIGN_UP_NOTIFICATION,
		MAIL_TYPE_TASK_EMAIL_CLIENT
	};
	
	//Email Templates
	public static final String BLUE_WAVE_TEMPLATE = "blueWaveTemplate";
	
	
	// XML Cached Services
	public static final String ANGULAR_HEADERS_CONFIG_CACHED_SERVICE = "ANGULAR_HEADERS_CONFIG_CACHED_SERVICE";
	public static final String QUERY_XML_CACHED_SERVICE = "QUERY_XML_CACHED_SERVICE";
	public static final String CLIENT_CONFIG_XML_CACHED_SERVICE = "CLIENT_CONFIG_XML_CACHED_SERVICE";
	public static final String RESPONSE_CONFIG_CACHED_SERVICE = "RESPONSE_CONFIG_CACHED_SERVICE";
	public static final String UI_MSG_CACHED_SERVICE = "UI_MSG_CACHED_SERVICE";
	public static final String EMAIL_TEMPLATES_CACHED_SERVICE = "EMAIL_TEMPLATES_CACHED_SERVICE";
	public static final String EMAIL_CONFIG_CACHED_SERVICE = "EMAIL_CONFIG_CACHED_SERVICE";
	public static final String TASK_PROCESS_JOBS_TEMPLATE = "TASK_PROCESS_JOBS_TEMPLATE";
	public static final String LOGIN_DATA_SEED_XML = "LOGIN_DATA_SEE_XML";
	public static final String QUERY_TO_BEAN_MAPPER = "QUERY_TO_BEAN_MAPPER";
	public static final String QUERY_GRID_FIELD_MAPPER = "QUERY_GRID_FIELD_MAPPER";
	
	//Hunter Cache service names 
	public static final String EMAIL_TEMPLATES_CACHE_SERVICE = "emailTemplateCacheService";
	public static final String ALL_XML_SERVICES = "allXMLServices";
	
	public static final String UI_MSG_CACHED_BEANS = "UI_MSG_CACHED_BEANS";
	
	//Email content params constants
	public static final String TO_LIST = "toList";
	public static final String FROM_EMAIL = "fromEmail";
	public static final String CC_LIST = "ccList";
	public static final String FOOTER = "footer";
	
	public static final String HUNTER_EMAIL_RESPONSE_CODE = "100";
	
	//Hunter Cache Keys
	public static final String EXISTENT_EMAIL_TEMPLATES = "EXISTENT_EMAIL_TEMPLATES";
	public static final String EMAIL_TEMPLATE_BEANS = "EMAIL_TEMPLATE_BEANS";
	public static final String COUNTRIES = "COUNTRIES";
	public static final String RECEIVERS = "RECEIVERS";
	public static final String DEFAULT_EMAIL_TEMPLATE = "DEFAULT_EMAIL_TEMPLATE";
	
	//HUNTER PROCESS JSON SERIALIZED FILE NAME
	public static final String HUNTER_PRCESS_SERIAL_PREF_KEY = "hntr_proc_srl_ref_key_";
	
	
	//Hunter Social Constants.
	public static final String MEDIA_TYPE_PHOTO = "mediaPhoto";
	public static final String MEDIA_TYPE_SOUND = "mediaSound";
	public static final String MEDIA_TYPE_VIDEO = "mediaVideo";
	public static final String MEDIA_TYPE_GIF = "mediaGIF";
	
	public static final String SOCIAL_TYPE_FACEBOOK = "Facebook";
	public static final String SOCIAL_TYPE_INSTAGRAM = "Instagram";
	public static final String SOCIAL_TYPE_TWITTER = "Twitter";
	public static final String SOCIAL_TYPE_BLOG = "Blog";
	
	public static final String SOCIAL_POST_TYPE_LINK_NEWS = "Post Link News";
	public static final String SOCIAL_POST_TYPE_LINK_OTHER = "Post Link Other";
	public static final String SOCIAL_POST_TYPE_TEXT = "Post Text";
	public static final String SOCIAL_POST_TYPE_IMAGE = "Post Image";
	public static final String SOCIAL_POST_TYPE_VIDEO = "Post Video";
	public static final String SOCIAL_POST_TYPE_AUDIO = "Post Audio";
	
	public static final String SOCIAL_POST_ACTION_TO_GROUP 	  = "Post To Group";
	public static final String SOCIAL_POST_ACTION_AS_GROUP 	  = "Post As Group";
	public static final String SOCIAL_POST_ACTION_TO_TIMELINE = "Post To Timeline";
	
	public static final String SOCIAL_MEDIA_IN_USER_SESSION = "socialMediaInUserSession";
	
	public static final int DEFAULT_NUMBER_OF_YEARS_TASK_IS_ACTIVE = 3;
	
	public static final String ALLOWED_CORS_ORIGINS = "http://localhost:4200";
	
	public static final String MAPPER_FIELD_HUNTER_USER_JSON = "MAPPER_FIELD_HUNTER_USER_JSON";
	public static final String GRID_HEADERS_MAPPINGS = "GRID_HEADERS_MAPPINGS";
	
}

