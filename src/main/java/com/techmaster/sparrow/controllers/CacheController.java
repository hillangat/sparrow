package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.entities.CacheBean;
import com.techmaster.sparrow.entities.ResponseData;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.util.SparrowUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CacheController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Produces("application/json")
    @GetMapping("cache")
    public ResponseEntity<ResponseData> getCacheRecords() {
        List<CacheBean> cacheBeans = SparrowCacheUtil.getInstance().getCacheBeans();
        return ResponseEntity.ok(new ResponseData(cacheBeans, SUCCESS_RETRIEVAL_MSG, StatusEnum.SUCCESS.getStatus(), null));
    }

    @Produces("application/json")
    @Consumes("application/json")
    @PostMapping("cache")
    public ResponseEntity<ResponseData> refreshCache(@RequestBody List<CacheBean> cacheBeans){
        SparrowUtil.threadSleepFor(500);
        String message = null;
        String status = StatusEnum.SUCCESS.getStatus();
        try{
            if( cacheBeans != null && !cacheBeans.isEmpty() ){
                List<String> keys = new ArrayList<>();
                cacheBeans.forEach(bean -> {
                    if ( SparrowUtil.notNullNotEmpty(bean.getKey()) ) {
                        if ( bean.getKey().equals("allXMLService") ) {
                            keys.clear();
                            keys.add(bean.getKey());
                        }
                        keys.add(bean.getKey());
                    }
                });
                keys.forEach( k -> SparrowCacheUtil.getInstance().refreshCacheService(k) );
                message = "Successfully refreshed cache!";
            }else{
                message = "No cache service specified in request.";
                status = StatusEnum.FAILED.getStatus();
            }
        }catch(Exception e){
            logger.error("Error occurred while trying to refresh cache!!");
            message = APPLICATION_ERROR_OCCURRED;
            status = StatusEnum.FAILED.getStatus();
        }

        return ResponseEntity.ok(new ResponseData(null, message, status, null));
    }

}
