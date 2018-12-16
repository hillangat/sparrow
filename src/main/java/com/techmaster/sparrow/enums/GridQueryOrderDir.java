package com.techmaster.sparrow.enums;

import java.util.Arrays;

public enum GridQueryOrderDir {
	
	asc( "asc", "Ascending" ),
	desc( "desc", "Descending" );
	
	private GridQueryOrderDir(String name, String description ) {
		this.name= name;
		this.description = description;
	}
	
	private String name;
	private String description;
	
	
	public static GridQueryOrderDir getEnumForName(String name ) {
		return name == null ? null : Arrays.stream(values()).filter( g -> g.name.equals(name) ).findFirst().get();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
