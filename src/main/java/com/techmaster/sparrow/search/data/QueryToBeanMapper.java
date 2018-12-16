package com.techmaster.sparrow.search.data;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.enums.LocationType;
import com.techmaster.sparrow.search.beans.QueryToBeanMapperField;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.xml.XMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryToBeanMapper {

	private static Logger logger = LoggerFactory.getLogger(QueryToBeanMapper.class);
	private QueryToBeanMapper() {}
	private static volatile QueryToBeanMapper instance;
	static {
		if ( instance == null ) {
			synchronized (QueryToBeanMapper.class) {
				instance = new QueryToBeanMapper();
			}
		}
	}
	
	public static QueryToBeanMapper getInstance() {
		return instance;
	}
	
	public <T>List<T> mapForQuery( Class<T> clzz, String query, List<Object> valueList ) {
		
		logger.debug("Mapping for query: " + query);		
		
		SparrowJDBCExecutor executor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
		XMLService xmlService = SparrowCacheUtil.getInstance().getXMLService(SparrowConstants.QUERY_TO_BEAN_MAPPER);
		
		if ( SparrowUtil.notNullNotEmpty(query) ) {
			logger.debug("Retrieved query: " + query);			
			if ( SparrowUtil.notNullNotEmpty( xmlService ) ) {
				List<Map<String, Object>> rowMapList = executor.executeQueryRowMap(query, valueList);
				return this.mapForQuery(clzz, rowMapList, xmlService);
			} else {
				logger.error("No XML service found for query to bean mapper : " + SparrowConstants.QUERY_TO_BEAN_MAPPER );
			}
			
		} else {
			logger.error( "No query found for query: " + query );
		}
		
		
		return null;
	}
	
	public <T>List<T> mapForQuery( Class<T> clzz, List<Map<String, Object>> rowMapList, XMLService xmlService ) {
		List<T> list = new ArrayList<>();
		String mapId = clzz.getSimpleName();
		List<QueryToBeanMapperField> mapperFields = getMapperFields(xmlService, mapId);
		if ( SparrowUtil.isCollNotEmpty(rowMapList) && SparrowUtil.isCollNotEmpty(mapperFields) ) {
			List<T> returnList = new ArrayList<>();
			for( Map<String, Object> rowMap : rowMapList ) {
				T t = getBean( clzz, xmlService, mapId, mapperFields, rowMap );
				returnList.add(t);
			}
			return returnList;
		} else {
			logger.warn( "No data found for the mapId: " + mapId + "\n and values: " ); 
		}
		return list;
	}
	
	public <T>List<T> map( Class<T> clzz, String queryId, List<Object> valueList ){
		logger.debug("Mapping for query ID: " + queryId);		
		SparrowJDBCExecutor executor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
		String query = executor.getQueryForSqlId(queryId);
		return mapForQuery(clzz, query, valueList);
	}
	
	@SuppressWarnings("unchecked")
	private <T>T getBean( Class<T> clzz, XMLService xmlService, String mapId, List<QueryToBeanMapperField> mapperFields, Map<String, Object> rowMap ) { 
		try {
			Object obj = clzz.newInstance();
			for ( QueryToBeanMapperField mapperField : mapperFields ) {
				Object dbValue = rowMap.get(mapperField.getDbField() );
				dbValue = mapperField.isYesNo() ? SparrowUtil.getBooleanForYN(dbValue.toString()) : dbValue;
				this.setFieldValue(clzz, obj, mapperField, dbValue); 
			}
			return (T) obj;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private <T extends Enum<T>> T getEnumVal( String name, Type type ) {
		return Enum.valueOf((Class<T>) type, name);
	}
	
	private List<QueryToBeanMapperField> getMapperFields( XMLService xmlService, String mapId ) {
		String xPath = "maps/map[@id='" + mapId + "']/field";
		NodeList fields = xmlService.getNodeListForPathUsingJavax(xPath);
		if ( SparrowUtil.isNodeListNotEmptpy(fields) ) {
			List<QueryToBeanMapperField> mapperFields = new ArrayList<>();
			for ( int i=0; i<fields.getLength(); i++ ) {
				Node fieldNode = fields.item(i);
				String dbName = SparrowUtil.getNodeAttr(fieldNode, "dbName", String.class);
				String fieldName = SparrowUtil.getNodeAttr(fieldNode, "fieldName", String.class);
				String type = SparrowUtil.getNodeAttr(fieldNode, "type", String.class);
				String subType = SparrowUtil.getNodeAttr(fieldNode, "subType", String.class);
				mapperFields.add( new QueryToBeanMapperField(mapId, dbName, fieldName, subType, type) );
			}
			if ( SparrowUtil.isCollNotEmpty(mapperFields) ) {
				return mapperFields;
			} else {
				logger.debug("No mapping fields found for mapId = " + mapId);
			}
		} else {
			logger.debug("No fields found for mapId = " + mapId); 
		}
		return null;
	}
	
	private void setFieldValue( Class<?> clzz, Object obj, QueryToBeanMapperField mapperField, Object fieldVal ) {
		String fieldName = mapperField.getFieldName();
		String setterMethodName = getSetterMethodName(fieldName);
		try {
			Method setterMethod = clzz.getDeclaredMethod(setterMethodName, getTypeParams(mapperField.getType()));
			fieldVal = mapperField.isYesNo() ? SparrowUtil.getBooleanForYN(fieldVal.toString()) : fieldVal;
			fieldVal = mapperField.isEnum() ? getEnumVal(fieldName, getTypeParams(mapperField.getType())) : fieldVal;
			fieldVal = mapperField.getType().equals("java.lang.String") ? fieldVal != null ? fieldVal.toString() : null : fieldVal;
			fieldVal = mapperField.getType().equals("java.lang.Long") ? SparrowUtil.getLongFromObject(fieldVal) : fieldVal;
			fieldVal = mapperField.getType().equals("java.lang.Float") ? SparrowUtil.getFloatFromObject(fieldVal) : fieldVal;
			fieldVal = mapperField.getType().equals("java.lang.Integer") ? Integer.parseInt(SparrowUtil.getStringOrNullOfObj(fieldVal)) : fieldVal;
			setterMethod.invoke(obj, fieldVal);
		} catch (NoSuchMethodException | SecurityException e) {
			this.logSettingVal(fieldVal, mapperField);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			this.logSettingVal(fieldVal, mapperField);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			this.logSettingVal(fieldVal, mapperField);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			this.logSettingVal(fieldVal, mapperField);
			e.printStackTrace();
		}
		
	}
	
	private void logSettingVal( Object fieldVal, QueryToBeanMapperField mapperField ) {
		logger.info("Setting value : " + ( fieldVal != null ? fieldVal.toString() : " " )  + " to mapper field : " + mapperField.getFieldName() );
	}
	
	private  Class<?> getTypeParams( String type ) {
		try {
			return  Class.forName( type );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getSetterMethodName( String fieldName ) {
		String methodName = "set";
		String firstChar = fieldName.substring(0, 1).toUpperCase();
		String lastPart = fieldName.substring(1, fieldName.length());		
		methodName += firstChar + lastPart; 
		// logger.debug("Created setter method name : " + methodName);
		return methodName;
	}

}
