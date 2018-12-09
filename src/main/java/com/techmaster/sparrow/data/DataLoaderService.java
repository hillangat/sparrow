package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.DataLoaderConfig;
import com.techmaster.sparrow.entities.MediaObj;
import com.techmaster.sparrow.entities.email.EmailReceiver;
import com.techmaster.sparrow.entities.email.EmailTemplate;
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
    List<EmailTemplate> loadEmailTemplates(List<MediaObj> mediaObjs);
    List<EmailReceiver> loadEmailReceivers();
    List<MediaObj> loadMediaObjects();


}
