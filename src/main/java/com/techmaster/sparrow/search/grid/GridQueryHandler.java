package com.techmaster.sparrow.search.grid;

import com.techmaster.sparrow.cache.SparrowCacheUtil;
import com.techmaster.sparrow.constants.SparrowConstants;
import com.techmaster.sparrow.entities.misc.ResponseData;
import com.techmaster.sparrow.search.beans.GridDataQueryReq;
import com.techmaster.sparrow.search.beans.GridFieldUserInput;
import com.techmaster.sparrow.enums.GridQueryOperation;
import com.techmaster.sparrow.enums.GridQueryOrderDir;
import com.techmaster.sparrow.repositories.SparrowBeanContext;
import com.techmaster.sparrow.repositories.SparrowJDBCExecutor;
import com.techmaster.sparrow.search.data.AngularDataHelper;
import com.techmaster.sparrow.search.data.QueryToBeanMapper;
import com.techmaster.sparrow.util.SparrowUtil;
import com.techmaster.sparrow.xml.XMLService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class GridQueryHandler {

	private static GridQueryHandler instance;
	private GridQueryHandler() {}
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	static {
		if ( instance == null ) {
			synchronized( GridQueryHandler.class ) {
				instance = new GridQueryHandler();
			}
		}
	}
	
	public <T> ResponseData executeForAngularData(Class<T> clzz, HttpServletRequest request, String headerId, List<Object> valueList  ) {
		
		GridDataQueryReq req = getReqBeanFromRequest(request);
		SparrowJDBCExecutor executor = SparrowBeanContext.getBean(SparrowJDBCExecutor.class);
		String query = createQueryFromReq(request, req, valueList);
		XMLService xmlService = SparrowCacheUtil.getInstance().getXMLService(SparrowConstants.QUERY_TO_BEAN_MAPPER);
		List<Map<String, Object>> rowMapList = executor.executeQueryRowMap(query, valueList);
		
		int total = 0;
		
		if ( SparrowUtil.isCollNotEmpty( rowMapList ) && SparrowUtil.notNullNotEmpty( rowMapList.get(0).get(SparrowConstants.GRID_DATA_COUNT) ) ) {
			total = Integer.valueOf(rowMapList.get(0).get(SparrowConstants.GRID_DATA_COUNT).toString());
			if ( req.getPageNo() * req.getPageSize() > total ) {
				req.setPageNo(1);
				req.setPageSize(total);
			}
			// remove the count place holding column.
			rowMapList.forEach( map -> map.remove(SparrowConstants.GRID_DATA_COUNT) );
		}
		
		List<T> angularRecs = QueryToBeanMapper.getInstance().mapForQuery(clzz, rowMapList, xmlService);
		ResponseData angData = AngularDataHelper.getIntance().getDataBean(angularRecs);
		angData.setTotal(total);
		return angData;
	}
	
	public <T>List<T> executeQridQueryForRequest(Class<T> clzz, HttpServletRequest request, GridDataQueryReq req, List<T> valueList ) {
		String finalQuery = createQueryFromReq( request, req, valueList );
		List<T> ts = QueryToBeanMapper.getInstance().mapForQuery( clzz, finalQuery, null );
		return ts;
	}
	
	public static GridQueryHandler getInstance() {
		return instance;
	}
	
	public String replaceQuestionMarks(String query, Object value) {
		int index = query.indexOf("?");
		logger.debug("Replacing next parameter parameter: " + value.toString() + ", on the query: \n " + query);
		if (index >= 0) {
			String part1 = query.substring(0,  index - 1);
			String part2 = query.substring(index + 1,  query.length());
			String finalQuery = part1 + ( SparrowUtil.isNumeric(value) ? value.toString() : " '" + value.toString() + "' " ) + part2;
			logger.debug("After replacement: \n " + finalQuery);
			return finalQuery;
		} else {
			return query;
		}
	}
	
	public <T> String createQueryFromReq(HttpServletRequest request, GridDataQueryReq req, List<T> valueList ) {
		String queryId = getReferenceQueryId( req );
		String query = SparrowBeanContext.getBean(SparrowJDBCExecutor.class).getQueryForSqlId( queryId );
		if (SparrowUtil.isCollNotEmpty(valueList) && replaceValuesFirst(req)) {
			for( Object value : valueList ) {
				query = replaceQuestionMarks(query, value);
			}
			valueList.clear(); // so it does not cause invalid column index exception
		}
		String whereAndGroupByClause = createWhereAndGroupClause(req, query );
		query = query + " " + whereAndGroupByClause;
		String finalQuery = addWithClause(query, valueList, req);
		this.logger.debug("finalQuery >>> " + finalQuery );
		return finalQuery;
	}
	
	private <T> String addWithClause( String query, List<T> valueList, GridDataQueryReq req) {
		String
		part1 = " WITH DATA_RECORDS_COUNT AS (  SELECT COUNT(*) AS DATA_COUNT_VAL ",
		part2 = getCountFrag(query, valueList, req),
		part3 = " ), DATA_RECORDS_ROWS AS ( " + query + " )",
		part4 = " SELECT dr.*, ( SELECT dc.DATA_COUNT_VAL FROM DATA_RECORDS_COUNT dc ) AS " + SparrowConstants.GRID_DATA_COUNT + " FROM DATA_RECORDS_ROWS dr",
		finl  = part1 + part2 + part3 + part4;
		return finl;
	}
	
	private <T> String replaceCountSensitiveCols( String query, GridDataQueryReq req, List<T> valueList ) {
		query.toLowerCase();
		while( query.contains("= ") || query.contains(" =") || query.contains("=\\t") || query.contains("\\t=") ) {
			query = query.replaceAll("= ", "=");
			query = query.replaceAll(" =", "=");
			query = query.replaceAll("=\\t", "=");
			query = query.replaceAll("\\t=", "=");
		}
		if ( SparrowUtil.isArrNotEmpty(req.getColSensitiveCols()) ) {
			for( int i = 0; i < req.getColSensitiveCols().length; i++ ) {
				String col = req.getColSensitiveCols()[i];
				String valAndParam = col + "=" + "?";
				while( query.contains( valAndParam ) ) {
					query = SparrowUtil.replaceWord(query, valAndParam, SparrowUtil.singleQuote(valueList.get(i)));
				}
			}
		}
		return query;
	}
	
	private <T> String getCountFrag( String query, List<T> valueList, GridDataQueryReq req ) {
		String from	= query.substring(query.toLowerCase().lastIndexOf("from"), query.length());
		if ( from.toUpperCase().contains("OFFSET") ) {
			from = from.substring(0, from.toUpperCase().indexOf("OFFSET"));
		}
		from = this.replaceCountSensitiveCols(from, req, valueList);
		return from;
	}
	
	public GridDataQueryReq getReqBeanFromRequest( HttpServletRequest request ) {
		String reqStr = SparrowUtil.getRequestBodyAsStringSafely(request);
		JSONObject reqObj = reqStr != null ? new JSONObject( reqStr ) : null;
		return getReqBeanFromJson(reqObj);
	}
	
	private String getPaginationSqlFrag( GridDataQueryReq req ) {
		StringBuilder builder = new StringBuilder();
		if ( req != null && !( req.getPageNo() == 0 && req.getPageSize() == 0  )  ) {
			req.setPageNo( req.getPageNo() == 0 ? 1 : req.getPageNo() );
			int offset = ( req.getPageNo() - 1 ) * req.getPageSize();
			builder.append(" OFFSET ").append( offset ).append( " ROWS FETCH NEXT " ).append( req.getPageSize() ).append(" ROWS ONLY ");
		}
		return builder.toString();
	}
	
	private String getValFrag( GridFieldUserInput filter ) {
		return GridQueryOperation.getSqlFragment(filter.getFieldAlias(), filter.getDbName(), filter.getUserInput(), filter.getOperation().getUiName() );
	}
	
	private boolean addWhereClause( String query, GridFieldUserInput filters[] ) {
		if ( !SparrowUtil.isArrNotEmpty(filters) ) {
			return false;
		}
		int whereMarkIndex = query.toLowerCase().lastIndexOf("where");
		int fromMarkIndex = query.toLowerCase().lastIndexOf("from");
		if ( fromMarkIndex > -1 && whereMarkIndex > -1 && whereMarkIndex > fromMarkIndex  ) {
			return false;
		}
		String whereClause = query.substring(whereMarkIndex, query.length() );
		if ( whereMarkIndex >  -1 ) {
			if ( whereClause.toLowerCase().contains(filters[0].getDbName().toLowerCase()) ) {
				return false;
			}
		}
		return true;
	}
	
	public String createWhereAndGroupClause( GridDataQueryReq req, String query ) {
		StringBuilder builder = new StringBuilder();
		if ( req != null ) {
			if ( SparrowUtil.isArrNotEmpty(req.getFilterBy()) ) {
				boolean addWhereClause =  addWhereClause(query, req.getFilterBy());
				if ( addWhereClause ) {
					builder.append(" WHERE ");
				}
				for( int i = 0; req.getFilterBy() != null && i < req.getFilterBy().length; i++ ) {
					GridFieldUserInput filter = req.getFilterBy()[i];
					String valFrag = getValFrag(filter);
					if ( !addWhereClause ) {
						builder.append(" AND ").append( valFrag ).append(" ");
					} else {
						builder.append(" ").append( valFrag ).append(" ");
						if ( i < req.getFilterBy().length - 1 && req.getFilterBy().length > 1 ) {
							builder.append(" AND ");
						}
					}
				}
			}
			if ( SparrowUtil.isArrNotEmpty(req.getOrderBy()) ) {
				builder.append(" ORDER BY ");
				for( int i = 0; req.getOrderBy() != null && i < req.getOrderBy().length; i++  ) {
					GridFieldUserInput orderBy = req.getOrderBy()[i];
					builder.append(orderBy.getFieldAlias()).append(".").append( orderBy.getDbName() ).append(" ").append(orderBy.getDir().getName().toUpperCase() ).append(" ");
					if ( i < req.getOrderBy().length - 1 && req.getOrderBy().length > 1 ) {
						builder.append(", ");
					}
				}
			}
			String pagination = this.getPaginationSqlFrag(req);
			if ( SparrowUtil.notNullNotEmpty(pagination) ) {
				builder.append(pagination);	
			}
		}
		String whereStr = builder.toString();
		return whereStr;
	}
	
	private String getReferenceQueryId( GridDataQueryReq req ) {
		NodeList references = getReferences(req);
		if ( SparrowUtil.isNodeListNotEmptpy(references) ) {
			Node reference = references.item(0);
			return SparrowUtil.getNodeAttr(reference, "queryId", String.class);
		}
		return null;
	}
	
	private boolean replaceValuesFirst( GridDataQueryReq req ) {
		NodeList references = getReferences(req);
		if ( SparrowUtil.isNodeListNotEmptpy(references) ) {
			Node reference = references.item(0);
			return SparrowUtil.getNodeAttr(reference, "replaceValuesFirst", Boolean.class);
		}
		return false;
	}
	
	private NodeList getReferences( GridDataQueryReq req ) {
		XMLService mappings = SparrowCacheUtil.getInstance().getXMLService(SparrowConstants.QUERY_GRID_FIELD_MAPPER);
		String xPath = "mappings/references/reference[@name='"+ req.getReference() + "']";
		NodeList references = mappings.getNodeListForPathUsingJavax(xPath);
		return references;
	}
	
	private void setCntSnstvCols( GridDataQueryReq req, Node reference ) {
		String cntSnstvColsStr = SparrowUtil.getNodeAttr(reference, "countSensitiveCols", String.class);
		req.setColSensitiveCols( null == cntSnstvColsStr ? null : cntSnstvColsStr.split(",") ); 
	}
	
	public GridDataQueryReq setDbNamesAndCntSnstvCols( GridDataQueryReq req ) {
		NodeList references = getReferences(req);
		if ( SparrowUtil.isNodeListNotEmptpy(references) ) {
			for( int i = 0 ; i < references.getLength(); i++ ) {
				Node reference = references.item(i);
				setCntSnstvCols(req, reference);
				String tableName = SparrowUtil.getNodeAttr(reference, "table", String.class);
				String alias = SparrowUtil.getNodeAttr(reference, "alias", String.class);
				this.logger.debug("tableName = " + tableName + ", alias = " + alias);
				NodeList fields = reference.getChildNodes();
				if ( SparrowUtil.isNodeListNotEmptpy(references) ) {
					for( int j = 0 ; j < fields.getLength(); j++ ) {
						Node field = fields.item(j);
						if ( !SparrowUtil.isTextNode(field) ) {
							String uiName = SparrowUtil.getNodeAttr(field, "ui", String.class);
							String dbName = SparrowUtil.getNodeAttr(field, "db", String.class);
							for ( GridFieldUserInput filter : req.getFilterBy() ) {
								if ( uiName.equals(filter.getFieldName()) ) {
									filter.setDbName(dbName);
								}
							}
							for ( GridFieldUserInput orderBy : req.getOrderBy() ) {
								if ( uiName.equals(orderBy.getFieldName()) ) {
									orderBy.setDbName(dbName);
								}
							}
						}
					}
				}
			}
		}
		return req;
	}
	
	public GridDataQueryReq getReqBeanFromJson( JSONObject obj ) {
		
		if ( obj == null ) {
			return null;
		}
		
		GridDataQueryReq  req = new GridDataQueryReq();
		
		JSONArray filterByArr = getArray( "filterBy", obj );
		GridFieldUserInput filterBy[] = createUserInputs(filterByArr);
		req.setFilterBy(filterBy);
		
		
		JSONArray orderByArr = getArray( "orderBy", obj );
		GridFieldUserInput orderBy[] = createUserInputs(orderByArr);
		req.setOrderBy(orderBy);
		
		String pageNoStr = getNullOrVal(obj, "pageNo");
		req.setPageNo( Integer.parseInt( pageNoStr != null ? pageNoStr : "0" ) );
		
		String pagSizeStr = getNullOrVal(obj, "pageSize");
		req.setPageSize( Integer.parseInt( pagSizeStr != null ? pagSizeStr : "0" ) );
		
		String reference = getNullOrVal(obj, "reference");
		req.setReference( reference );
		
		req = setFieldAliases(req);
		req = setDbNamesAndCntSnstvCols(req);
		
		return req;
	}
	
	public GridFieldUserInput[] createUserInputs( JSONArray array ) {
		if ( array != null && array.length() > 0 ) {
			GridFieldUserInput [] inputs = new GridFieldUserInput[array.length()];
			for( int i = 0; i < inputs.length; i++ ) {
				JSONObject inputObj = array.getJSONObject(i);
				GridFieldUserInput input = new GridFieldUserInput();
				input.setDir( GridQueryOrderDir.getEnumForName( getNullOrVal(inputObj, "dir") )  );
				input.setFieldName( getNullOrVal(inputObj, "fieldName") );
				input.setOperation( GridQueryOperation.getEnumForName(getNullOrVal(inputObj, "operation")) );
				input.setUserInput( getNullOrVal(inputObj, "userInput"));
				inputs[i] = input;
			}
			return inputs;
		}
		return new GridFieldUserInput[0];
	}
	
	public GridDataQueryReq setFieldAliases( GridDataQueryReq req ) {
		NodeList references = getReferences(req);
		Node reference = SparrowUtil.isNodeListNotEmptpy(references) ? references.item(0) : null;
		setAliasForInputs(req.getFilterBy(), reference);
		setAliasForInputs(req.getOrderBy(), reference);
		return req;
	}
	
	public void setAliasForInputs( GridFieldUserInput inputs[], Node reference ) {
		for( int i = 0 ; i < inputs.length && inputs != null  && reference != null; i++ ) {
			GridFieldUserInput filter = inputs[i];
			String fieldName = filter.getFieldName();
			NodeList fields = reference.getChildNodes();
			for( int j= 0 ; fields != null && j < fields.getLength(); j++ ) {
				Node field = fields.item(j);
				if ( !field.getNodeName().equals("#text") ) {
					String uiName = SparrowUtil.getNodeAttr(field, "ui", String.class);
					String alias = SparrowUtil.getNodeAttr(field, "alias", String.class);
					if ( fieldName.equals(uiName) ) {
						filter.setFieldAlias(alias);
					}
				}
			}
		}
	}
	
	private String getNullOrVal(JSONObject obj, String key ) {
		return SparrowUtil.getStringOrNulFromJSONObj(obj, key);
	}
	
	public JSONArray getArray(String key, JSONObject obj ) {
		if ( obj.has(key) && obj.get( key ) != null ) {
			return obj.getJSONArray(key);
		} else {
			return new JSONArray();
		}
	}

}
