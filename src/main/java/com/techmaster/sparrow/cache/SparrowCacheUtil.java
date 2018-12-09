package com.techmaster.sparrow.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.CacheBean;
import com.techmaster.sparrow.entities.Location;
import com.techmaster.sparrow.location.LocationService;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.rules.abstracts.RuleTypeBean;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.xml.XMLService;
import org.h2.bnf.Rule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class SparrowCacheUtil {

	private static SparrowCacheUtil instance;
	private static Logger logger = LoggerFactory.getLogger(SparrowCacheUtil.class);
	private SparrowCacheUtil(){};
	
	static{
		if(instance == null){
			synchronized (SparrowCache.class) {
				instance = new SparrowCacheUtil();
			}
		}
	}
	
	public static SparrowCacheUtil getInstance(){
		return instance;
	}

	public void loadHunterCache() {
		logger.debug("Refreshing hunter cache...");
		refreshAllXMLServices();
		populateUIMessages();
		refreshAllLocations();
		cacheRuleTypes();
		cacheDroolDrlFiles();
		logger.debug("Finished refreshing hunter cache...");
	}

	public List<Location> refreshAllLocations() {
		LocationService locationService = SparrowBeanContext.getBean(LocationService.class);
		List<Location> locations = null;
		if (locationService != null) {
			locations = locationService.getLocationHierarchies();
			SparrowCache.getInstance().put(SparrowConstants.LOCATIONS_CACHE_KEY, locations);
			logger.debug("Locations loaded to cache successfully. Size: " + locations.size());
		} else {
			logger.error("No location service bean found. locations not loaded to cache!!");
		}
		return locations;
	}

	public List<Location> getLocationHierarchies() {
		List<Location> locations = (List<Location>)SparrowCache.getInstance().get(SparrowConstants.LOCATIONS_CACHE_KEY);
		locations = locations == null || locations.isEmpty() ? refreshAllLocations() : locations;
		return locations;
	}

	public void refreshAllXMLServices(){
		String path = SparrowURLConstants.CACHE_REFRESH_JSON;
		File cacheRefresh = new File( path );
		String cacheContents = SparrowUtil.getStringOfFile(cacheRefresh);
		if ( SparrowUtil.notNullNotEmpty(cacheContents) ) {
			try {
				JSONArray cacheRefreshes = new JSONArray( cacheContents );
				for( int i = 0; i < cacheRefreshes.length(); i++ ) {
					JSONObject cache = cacheRefreshes.getJSONObject(i);
					String key = SparrowUtil.getStringOrNulFromJSONObj(cache, "key");
					boolean active = Boolean.valueOf(SparrowUtil.getStringOrNulFromJSONObj(cache, "active"));
					if ( active && SparrowUtil.notNullNotEmpty(key) && !key.equals("allXMLService") ) {
						this.refreshCacheService( key );
					}
				}
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public void refreshCacheService(String xmlName){
		switch (xmlName) {
		case "queryXML":
			logger.debug("Caching xmlQuery..."); 
			XMLService queryService = SparrowUtil.getXMLServiceForFileLocation(SparrowURLConstants.QRY_XML_FL_LOC_PATH);
			SparrowCache.getInstance().put(SparrowConstants.QUERY_XML_CACHED_SERVICE, queryService);
			logger.debug("Done caching xmlQuery!!");
			break;
		case "uiMsgXML":
			logger.debug("Caching ui message xml...");
			XMLService uiMsgService = SparrowUtil.getXMLServiceForFileLocation(SparrowURLConstants.UI_MSG_XML_FL_LOC_PATH);
			SparrowCache.getInstance().put(SparrowConstants.UI_MSG_CACHED_SERVICE, uiMsgService);
			logger.debug("Done caching ui message xml!!");
			break;
		default:
			break;
		}
	}
	
	public XMLService getXMLService(String xmlName) {
		XMLService service = (XMLService) SparrowCache.getInstance().get(xmlName);
		if(service == null) logger.debug("Service ( " + xmlName + " ) cannot be found!!") ; 
		return service;
	}
	
	public void populateUIMessages(){
		Map<String,String> uiMessages = new HashMap<>();
		logger.debug("populating ui messages..." ); 
		XMLService service = getXMLService(SparrowConstants.UI_MSG_CACHED_SERVICE);
		NodeList messages = service.getNodeListForXPath("//message");
		for(int i=0; i<messages.getLength(); i++){
			Node message = messages.item(i);
			if(message.getNodeName().equals("message")){
				String id = SparrowUtil.getNodeAttr(message, "id", String.class);
				NodeList metadata = message.getChildNodes();
				String desc = null;
				String text = null;
				for(int j=0; j<metadata.getLength(); j++){
					Node datum = metadata.item(j);
					if(datum.getNodeName().equals("desc"))
						desc = datum.getTextContent();
					else if(datum.getNodeName().equals("text")){
						text = datum.getTextContent();
					}
				}
				uiMessages.put(id+"_DESC", desc);
				uiMessages.put(id+"_TEXT", text);
			}
		}
		SparrowCache.getInstance().put(SparrowConstants.UI_MSG_CACHED_BEANS, uiMessages);
		logger.debug("Succesfully populated ui messages!!" );
	}
	
	public String getUIMsgTxtForMsgId(String msgId){
		return getUIMsgMap().get(msgId+"_TEXT");
	}
	
	public String getUIMsgDescForMsgId(String msgId){
		return getUIMsgMap().get(msgId+"_DESC");
	}
	
	public String getUIMsgTxtAndReplace( String msgId, Map<String, String> params ){
		String msg = getUIMsgMap().get(msgId+"_TEXT");
		if ( msg != null && SparrowUtil.isMapNotEmpty( params ) ) {
			for ( Map.Entry<String, String> entry : params.entrySet() ) {
				msg = msg.replaceAll(entry.getKey(), entry.getValue());
			}
		}
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getUIMsgMap() {
		return (Map<String, String>) SparrowCache.getInstance().get(SparrowConstants.UI_MSG_CACHED_BEANS);
	}

	public List<RuleTypeBean> cacheRuleTypes() {
		List<RuleTypeBean> ruleTypeBeans = new ArrayList<>();
		String path = SparrowURLConstants.RULE_TYPES_JSON_PATH;
		File file = new File(SparrowURLConstants.RULE_TYPES_JSON_PATH);
		if (file.exists()) {
			String fileStr = SparrowUtil.getStringOfFile(file);
			JSONArray array = new JSONArray(fileStr);
			for( int i = 0; i < array.length(); i++ ) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					RuleTypeBean bean = objectMapper.readValue(array.getJSONObject(i).toString(), RuleTypeBean.class);
					ruleTypeBeans.add(bean);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			SparrowCache.getInstance().put(SparrowConstants.RULE_TYPES_KEY, ruleTypeBeans);
			logger.debug("Finished caching rule type beans: Size = " + ruleTypeBeans.size());
		} else {
			logger.error("No rule type file found. Rule types not cached!!!!, path: " + file.getAbsolutePath());
		}
		return ruleTypeBeans;
	}

	public List<RuleTypeBean> getRuleTypeBeans(String name) {
		List<RuleTypeBean> ruleTypeBeans = (List<RuleTypeBean> )SparrowCache.getInstance().get(SparrowConstants.RULE_TYPES_KEY);
		ruleTypeBeans = ruleTypeBeans == null || ruleTypeBeans.isEmpty() ? cacheRuleTypes() : ruleTypeBeans;
		return ruleTypeBeans
                .stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .sorted(Comparator.comparingInt(RuleTypeBean::getIndex))
                .collect(Collectors.toList());
	}

	private Map<String, String> getAllFiles ( String folder ) {
		Map<String, String> files = new HashMap<>();
		File fFolder = new File(folder);
		if (fFolder.exists() && fFolder.isDirectory()) {
			for( final File file : fFolder.listFiles() ) {
				files.put(file.getName(), SparrowUtil.getStringOfFile(file));
			}
		}
		return files;
	}

	public Map<String, String> cacheDroolDrlFiles() {
		String path = SparrowURLConstants.DROOL_DRL_FILES;
		Map<String, String>  files = getAllFiles(path);
		SparrowCache.getInstance().put(SparrowConstants.DRL_FILES_KEY, files);
		return files;
	}

	public Map<String, String> getCacheDroolDrlFiles() {
		Map<String, String> files = (Map<String, String>)SparrowCache.getInstance().get(SparrowConstants.DRL_FILES_KEY);
		files = files == null || files.isEmpty() ? cacheDroolDrlFiles() : files;
		return files;
	}

	public List<CacheBean> cacheCacheBeans() {
		List<CacheBean> cacheBeans = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		JSONArray data = new JSONArray( SparrowUtil.convertFileToString(SparrowURLConstants.CACHE_REFRESH_JSON) );
		if (data != null) {
			for(int i = 0; i <  data.length(); i++) {
				JSONObject obj = data.getJSONObject(i);
				try {
					cacheBeans.add(objectMapper.readValue(obj.toString(), CacheBean.class));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("Could not find cache refresh json file. No refresh done for the file!!!");
		}
		SparrowCache.getInstance().put(SparrowConstants.CACHE_BEANS_KEY, cacheBeans);
		return cacheBeans;
	}

	public List<CacheBean> getCacheBeans() {
		List<CacheBean> cacheBeans = (List<CacheBean>)SparrowCache.getInstance().get(SparrowConstants.CACHE_BEANS_KEY);
		cacheBeans = cacheBeans == null || cacheBeans.isEmpty() ? cacheCacheBeans() : cacheBeans;
		return cacheBeans;
	}
	
}
