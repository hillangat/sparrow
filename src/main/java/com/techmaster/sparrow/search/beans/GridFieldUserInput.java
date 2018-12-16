package com.techmaster.sparrow.search.beans;

import com.techmaster.sparrow.enums.GridQueryOperation;
import com.techmaster.sparrow.enums.GridQueryOrderDir;
import lombok.*;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GridFieldUserInput {

	private String fieldName;
	private String dbName;
	private String fieldAlias;
    private String userInput;
    private GridQueryOperation operation;
    private GridQueryOrderDir dir;

}
