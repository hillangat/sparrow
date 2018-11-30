package com.techmaster.sparrow.cache;

import com.techmaster.sparrow.constants.HunterURLConstants;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.util.SparrowUtility;
import com.techmaster.sparrow.xml.XMLService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.*;


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

	public void refreshAllXMLServices(){
		String path = HunterURLConstants.CACHE_REFRESH_JSONS;
		File cacheRefresh = new File( path );
		String cacheContents = SparrowUtility.getStringOfFile(cacheRefresh);
		if ( SparrowUtility.notNullNotEmpty(cacheContents) ) {
			try {
				JSONArray cacheRefreshes = new JSONArray( cacheContents );
				for( int i = 0; i < cacheRefreshes.length(); i++ ) {
					JSONObject cache = cacheRefreshes.getJSONObject(i);
					String key = SparrowUtility.getStringOrNulFromJSONObj(cache, "key");
					boolean active = Boolean.valueOf(SparrowUtility.getStringOrNulFromJSONObj(cache, "active"));
					if ( active && SparrowUtility.notNullNotEmpty(key) && !key.equals("allXMLService") ) {
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
			XMLService queryService = SparrowUtility.getXMLServiceForFileLocation(HunterURLConstants.QRY_XML_FL_LOC_PATH);
			SparrowCache.getInstance().put(SparrowConstants.QUERY_XML_CACHED_SERVICE, queryService);
			logger.debug("Done caching xmlQuery!!");
			break;
		case "uiMsgXML":
			logger.debug("Caching ui message xml...");
			XMLService uiMsgService = SparrowUtility.getXMLServiceForFileLocation(HunterURLConstants.UI_MSG_XML_FL_LOC_PATH);
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
				String id = SparrowUtility.getNodeAttr(message, "id", String.class);
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
		if ( msg != null && SparrowUtility.isMapNotEmpty( params ) ) {
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
	
}
