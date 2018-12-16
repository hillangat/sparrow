package com.techmaster.sparrow.search.beans;


import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HunterTableConfig {
	
	private boolean sortable;
    private boolean show;
    private boolean currentOrder;
    private boolean actionCol;
    private boolean checkBox;
	private int index;
    private String headerId;
    private String dataId;
    private String displayName;
    private String bootstrapIconName;
    private String dataType;
    private String actionCellType;
    private String actionColIconName;
    private String width;

}
