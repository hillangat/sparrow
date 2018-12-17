package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.CacheBean;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.entities.misc.SearchResult;
import com.techmaster.sparrow.enums.Status;
import com.techmaster.sparrow.rules.abstracts.RuleResultBean;
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
        return getResponse(true, cacheBeans, null);
    }

    @PostMapping("cache")
    public ResponseEntity<ResponseData> refreshCache(@RequestBody List<CacheBean> cacheBeans){

        RuleResultBean resultBean = new RuleResultBean();
        SparrowUtil.threadSleepFor(500);
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
            }else{
                resultBean.setError("cacheService", "No cache service specified in request.");
            }
        }catch(Exception e){
            logger.error("Error occurred while trying to refresh cache!!");
            resultBean.setError(SparrowConstants.APPLICATION_ERROR_KEY, "Error occurred while trying to refresh cache!!");
        }

        return getResponse(false, null, resultBean);
    }

}
