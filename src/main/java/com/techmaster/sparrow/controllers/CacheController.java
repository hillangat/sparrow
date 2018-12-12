package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.entities.misc.CacheBean;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.enums.StatusEnum;
import com.techmaster.sparrow.util.SparrowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CacheController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("cache")
    public ResponseEntity<ResponseData> getCacheRecords() {
        List<CacheBean> cacheBeans = SparrowCacheUtil.getInstance().getCacheBeans();
        return ResponseEntity.ok(new ResponseData(cacheBeans, SUCCESS_ACTION_COMPLETION, StatusEnum.SUCCESS.getStatus(), null));
    }

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
                        } else if (!keys.contains("allXMLService")) {
                            keys.add(bean.getKey());
                        }
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
