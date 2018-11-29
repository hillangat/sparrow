package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.HunterURLConstants;
import com.techmaster.sparrow.util.SparrowUtility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cache")
public class CacheController extends BaseController {

    // private Logger logger = Logger.getLogger(this.getClass());

    @Produces("application/json")
    @RequestMapping(value="/read", method= RequestMethod.GET)
    @ResponseBody
    public String refreshCache() {
        try {
            return new JSONArray( SparrowUtility.convertFileToString(HunterURLConstants.CACHE_REFRESH_JSONS) ).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return SparrowUtility.setJSONObjectForFailure(null, "Application error occurred while loading caches").toString();
        }
    }

    @Produces("application/json")
    @Consumes("application/json")
    @RequestMapping(value="/refresh", method=RequestMethod.POST)
    @ResponseBody
    public String refreshCache( HttpServletRequest request ){
        SparrowUtility.threadSleepFor(500);
        try{
            JSONArray jsonArray = new JSONArray( SparrowUtility.getRequestBodyAsStringSafely(request) );
            if( jsonArray != null && jsonArray.length() > 0 ){
                List<String> keys = new ArrayList<>();
                for( int i = 0; i < jsonArray.length(); i++ ) {
                    JSONObject cache = jsonArray.getJSONObject(i);
                    String key = SparrowUtility.getStringOrNulFromJSONObj(cache, "key");
                    if ( SparrowUtility.notNullNotEmpty(key) ) {
                        if ( key.equals("allXMLService") ) {
                            keys.clear();
                            keys.add(key);
                            break;
                        }
                        keys.add(key);
                    }
                }
                keys.forEach( k -> SparrowCacheUtil.getInstance().refreshCacheService(k) );
                return SparrowUtility.setJSONObjectForSuccess(null, "Successfully refreshed cache!").toString();
            }else{
                return SparrowUtility.setJSONObjectForFailure(null, "No cache service specified in request.").toString();
            }
        }catch(Exception e){
            return SparrowUtility.setJSONObjectForFailure(null, "Applicaiton error : " + e.getMessage()).toString();
        }
    }

}
