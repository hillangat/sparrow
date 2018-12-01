package com.techmaster.sparrow.data;

import com.techmaster.sparrow.entities.DataLoaderConfig;
import com.techmaster.sparrow.location.LocationService;
import com.techmaster.sparrow.repositories.DataLoaderConfigRepository;
import com.techmaster.sparrow.repositories.LocationRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class DataLoaderServiceImpl implements DataLoaderService {

    @Autowired private DataLoaderConfigRepository configRepository;

    @Override
    public void execute() {

    }

    @Override
    public List<DataLoaderConfig> getDataLoaderConfigs() {
        return null;
    }

    @Override
    public Map<String, Workbook> getWorkBooks() {
        return null;
    }

    @Override
    public Map<String, JSONObject> getJSONs() {
        return null;
    }

    @Override
    public void saveDataLoaderConfigs(List<DataLoaderConfig> configs) {

    }
}
