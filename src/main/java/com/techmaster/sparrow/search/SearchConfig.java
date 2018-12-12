package com.techmaster.sparrow.search;

import com.techmaster.sparrow.entities.misc.SearchArg;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class SearchConfig<T> {

    protected Map<String, String> fieldToColMappings = new HashMap<>();
    protected SearchArg searchArg;
    protected Class<T> clzz;
    protected List<String> fields = new ArrayList<>();

}
