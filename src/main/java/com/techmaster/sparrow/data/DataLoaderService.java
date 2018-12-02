package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.DataLoaderConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface DataLoaderService {

    void execute();
    List<DataLoaderConfig> getDataLoaderConfigs();
    Workbook getWorkBook (DataLoaderConfig config);
    void saveDataLoaderConfigs(List<DataLoaderConfig> configs);
    Map<String, JSONObject> getJSONs();


}
