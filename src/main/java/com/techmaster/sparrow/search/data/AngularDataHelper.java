package com.techmaster.sparrow.search.data;

import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.util.SparrowUtil;

import java.util.List;

public class AngularDataHelper {
	
	private static volatile AngularDataHelper instance;
	
	static {
		if ( instance == null ) {
			synchronized (AngularDataHelper.class) {
				instance = new AngularDataHelper();
			}
		}
	}
	
	/**
	 * Prevent initialization
	 */
	private AngularDataHelper() {  }
	
	
	public static AngularDataHelper getIntance() {
		return instance;
	}

	public ResponseData getDataBean(Object data) {
		ResponseData angularData = new ResponseData();
		try {
			angularData.setData(data);
			angularData.setMessage(null);
			angularData.setStatus(SparrowConstants.STATUS_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			angularData.setMessage( SparrowConstants.APPLICATION_ERROR_OCCURRED );
			angularData.setStatus(SparrowConstants.STATUS_FAILED);
		}
		return angularData;
	}
	
	public <T> ResponseData getBeanForQuery( Class<T> clss, String queryId, List<Object> values) {
		ResponseData responseData = new ResponseData();
		try {
			List<T> data = QueryToBeanMapper.getInstance().map(clss, queryId, values);
			return getDataBean(data);
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage( SparrowConstants.APPLICATION_ERROR_OCCURRED );
			responseData.setStatus(SparrowConstants.STATUS_FAILED);
		}
		return responseData;
	}
	
	public ResponseData getBeanForMsgAndSts( String message, String status, Object data ) {
		ResponseData responseData = new ResponseData();
		responseData.setData(data);
		try {
			responseData.setMessage( message );
			responseData.setStatus( status );
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setMessage( SparrowConstants.APPLICATION_ERROR_OCCURRED );
			responseData.setStatus(SparrowConstants.STATUS_FAILED);
		}
		return responseData;
	}
	
}
