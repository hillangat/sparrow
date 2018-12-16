package com.techmaster.sparrow.enums;

import java.util.Arrays;

public enum GridQueryOperation {

	// gt,lt,equals,before,after,contains,begins,ends;

	gt( "gt", "Greater Than", " > " ),
	lt( "lt", "Less Than", " < " ),
	equals( "equals", "Equals", " = " ),
	notEquals( "notEquals", "Equals", " != " ),
	before( "before", "Before", " < " ),
	after( "after", "After", " > " ),
	contains( "contains", "Contains", " IN " ),
	begins( "begins", "Begins With", " LIKE " ),
	ends( "ends", "Ends With", " ENDS WITH " ),
	notContains( "notContains", "Does not contain", " NOT IN " ),
	isNull( "isNull", "Is Null", " IS NULL " ),
	isNotNull( "isNotNull", "Is Not Null", " IS NOT NULL " ),
	isEmpty( "isEmpty", "Is Empty", " IS NULL OR LENGTH( TRIM( :replaceField ) ) = 0 " ),
	isNotEmpty( "isNotEmpty", "Is Not Empty", " IS NOT NULL AND LENGTH( TRIM( :replaceField ) ) > 0  " );

	
	private GridQueryOperation(String uiName, String uiDesc, String dbName) {
		this.uiName = uiName;
		this.uiDesc = uiDesc;
		this.dbName = dbName;
	}
	
	private final String uiName;
	private final String uiDesc;
	private final String dbName;
	
	public String getUiName() {
		return uiName;
	}
	public String getUiDesc() {
		return uiDesc;
	}
	public String getDbName() {
		return dbName;
	}
	
	public static GridQueryOperation getEnumForName(String uiName ) {
		return uiName == null ? null : Arrays.stream(values()).filter( g -> g.uiName.equals(uiName) ).findFirst().get();
	}
	
	public static String getSqlFragment( String fieldAlias, String dbName, String val, String uiName ) {
		switch ( getEnumForName(uiName) ) {
			case lt: 			return fieldAlias + "." + dbName + " < " + val;
			case gt: 			return fieldAlias + "." + dbName + " > " + val;
			case equals: 		return fieldAlias + "." + dbName + " = '" + val + "'";
			case notEquals: 	return fieldAlias + "." + dbName + " != '" + val + "'";
			case contains:		return fieldAlias + "." + dbName + " LIKE '%" + val + "%'";
			case before: 		return fieldAlias + "." + dbName + " < " + val;
			case after: 		return fieldAlias + "." + dbName + " > " + val;
			case begins: 		return fieldAlias + "." + dbName + " LIKE '" + val + "%'";
			case ends: 			return fieldAlias + "." + dbName + " LIKE '%" + val + "'";
			case notContains: 	return fieldAlias + "." + dbName +  " NOT LIKE " + "'%" + val + "%'";
			case isNull: 		return fieldAlias + "." + dbName +  " IS NULL ";
			case isNotNull: 	return fieldAlias + "." + dbName +  " IS NOT NULL ";
			case isEmpty: 		return fieldAlias + "." + dbName +  " IS NULL OR LENGTH( TRIM( " + fieldAlias + "." + dbName + " ) ) = 0 ";
			case isNotEmpty: 	return fieldAlias + "." + dbName +  " IS NOT NULL AND LENGTH( TRIM( " + fieldAlias + "." + dbName + " ) ) > 0 ";
			default : 			return null;
		}
	}
	
	
}
