package com.techmaster.sparrow.search.beans;


import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class QueryToBeanMapperField {

    private String parentId;
    private String dbField;
    private String fieldName;
    private String subType;
    private String type;

    public boolean isYesNo() {
        return "yes".equalsIgnoreCase(subType) ||
                "no".equalsIgnoreCase(subType);
    }

    public boolean isEnum() {
        return "enum".equalsIgnoreCase(subType);
    }

}
