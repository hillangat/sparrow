package com.techmaster.sparrow.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SparrowCache implements Serializable{

	private static final long serialVersionUID = 1L;
	private static SparrowCache instance;
	private final static Map<String, Object> hunterCache = new HashMap<>();
	private SparrowCache(){};
	private static Logger logger = LoggerFactory.getLogger(SparrowCache.class);
	
	static{
		if(instance == null){
			synchronized (SparrowCache.class) {
				instance = new SparrowCache();
			}
		}
		loadHunterCache();
	}
	
	public static SparrowCache getInstance(){
		return instance;
	}
	
	private static void loadHunterCache() {
		logger.debug("Refreshing hunter cache...");
		SparrowCacheUtil util = SparrowCacheUtil.getInstance();
		util.refreshAllXMLServices();
		util.populateUIMessages();
		logger.debug("Finished refreshing hunter cache...");
	}

	public boolean containsKey(String key){
		return hunterCache.containsKey(key);
	}
	
	public Object get(String key){
		if(containsKey(key)){
			return hunterCache.get(key);
		}
		return null;
	}
	
	public boolean isEmpty(){
		return hunterCache.isEmpty();
	}
	
	public Object put(String key, Object value){
		return hunterCache.put(key, value);
	}
	
	public Set<String> keySet(){
		return hunterCache.keySet();
	}
	
	public Object remove(String key){
		return hunterCache.remove(key);
	}
	
	public int size(){
		return hunterCache.size();
	}
	
	
	
	

}
