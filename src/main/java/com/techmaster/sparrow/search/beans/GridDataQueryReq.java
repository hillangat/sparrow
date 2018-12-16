package com.techmaster.sparrow.search.beans;

import lombok.*;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GridDataQueryReq {
	
	private GridFieldUserInput filterBy[];
	private GridFieldUserInput orderBy[];
	private int pageNo;
	private int pageSize;
	private String reference;
	private String[] colSensitiveCols;
	

}
