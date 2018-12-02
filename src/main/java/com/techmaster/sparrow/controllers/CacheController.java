package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowURLConstants;
import com.techmaster.sparrow.util.SparrowUtil;
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
            return new JSONArray( SparrowUtil.convertFileToString(SparrowURLConstants.CACHE_REFRESH_JSON) ).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return SparrowUtil.setJSONObjectForFailure(null, "Application error occurred while loading caches").toString();
        }
    }

    @Produces("application/json")
    @Consumes("application/json")
    @RequestMapping(value="/refresh", method=RequestMethod.POST)
    @ResponseBody
    public String refreshCache( HttpServletRequest request ){
        SparrowUtil.threadSleepFor(500);
        try{
            JSONArray jsonArray = new JSONArray( SparrowUtil.getRequestBodyAsStringSafely(request) );
            if( jsonArray != null && jsonArray.length() > 0 ){
                List<String> keys = new ArrayList<>();
                for( int i = 0; i < jsonArray.length(); i++ ) {
                    JSONObject cache = jsonArray.getJSONObject(i);
                    String key = SparrowUtil.getStringOrNulFromJSONObj(cache, "key");
                    if ( SparrowUtil.notNullNotEmpty(key) ) {
                        if ( key.equals("allXMLService") ) {
                            keys.clear();
                            keys.add(key);
                            break;
                        }
                        keys.add(key);
                    }
                }
                keys.forEach( k -> SparrowCacheUtil.getInstance().refreshCacheService(k) );
                return SparrowUtil.setJSONObjectForSuccess(null, "Successfully refreshed cache!").toString();
            }else{
                return SparrowUtil.setJSONObjectForFailure(null, "No cache service specified in request.").toString();
            }
        }catch(Exception e){
            return SparrowUtil.setJSONObjectForFailure(null, "Applicaiton error : " + e.getMessage()).toString();
        }
    }

}
