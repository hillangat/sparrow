package com.techmaster.sparrow.entities;

import com.techmaster.sparrow.enums.Direction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.awt.event.HierarchyListener;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SearchArg {

    private Direction dir;
    private int pageNo;
    private int pageSize;

}
