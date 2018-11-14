package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerHistoryKey;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerHistorySearchForm;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerHistoryService extends BaseService<MarkerHistoryDomain> {

	protected Logger log = Logger.getLogger(MarkerHistoryService.class);

	//@Inject
	//private MarkerHistoryDAO markerHistoryDAO;

	//private MarkerHistoryTranslator translator = new MarkerHistoryTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerHistoryDomain> create(MarkerHistoryDomain domain, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MarkerHistoryDomain> update(MarkerHistoryDomain domain, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerHistoryDomain get(Integer key) {
		// this table contains a compound primary key
		//return translator.translate(markerHistoryDAO.get(key));
		return null;
	}
	
    @Transactional
    public SearchResults<MarkerHistoryDomain> getResults(Integer key) {
		// this table contains a compound primary key
        //SearchResults<MarkerHistoryDomain> results = new SearchResults<MarkerHistoryDomain>();
        //results.setItem(translator.translate(markerHistoryDAO.get(key)));
        //return results;
    	return null;
    }
    
	@Transactional
	public SearchResults<MarkerHistoryDomain> delete(Integer key, User user) {
		// this table contains a compound primary key
		// deletes to this table are implemented in the parent's "update" method
		return null;
	}

	public List<MarkerHistoryDomain> search(MarkerHistorySearchForm searchForm) {

		// list of results to be returned
		List<MarkerHistoryDomain> results = new ArrayList<MarkerHistoryDomain>();

		// parameters defined in SearchForm
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		String cmd = "";
		String select = "select h.*";
		String from = "from mrk_history_view h";
		String where = "where ";
		String orderBy = "\norder by h._marker_key, h.sequencenum";
		
		if (params.containsKey("markerKey")) {
			where = where + "h._marker_key = " + params.get("markerKey");
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				MarkerHistoryDomain markerHistoryDomain = new MarkerHistoryDomain();
				
				// store embedded/compound primary key values
				MarkerHistoryKey markerHistoryKey = new MarkerHistoryKey();
				markerHistoryKey.set_marker_key(rs.getInt("_marker_key"));
				markerHistoryKey.setSequenceNum(rs.getInt("sequencenum"));
				markerHistoryDomain.setMarkerHistoryKey(markerHistoryKey);
				//markerHistoryDomain.setMarkerKey(rs.getInt("_marker_key"));
				//markerHistoryDomain.setSequenceNum(rs.getInt("sequencenum"));
				
				markerHistoryDomain.setMarkerEventKey(rs.getInt("_marker_event_key"));
				markerHistoryDomain.setMarkerEvent(rs.getString("event"));
				markerHistoryDomain.setMarkerEventReasonKey(rs.getInt("_marker_eventreason_key"));
				markerHistoryDomain.setMarkerEventReason(rs.getString("eventreason"));
				markerHistoryDomain.setMarkerHistorySymbolKey(rs.getString("_history_key"));
				markerHistoryDomain.setMarkerHistorySymbol(rs.getString("history"));
				markerHistoryDomain.setMarkerHistoryName(rs.getString("name"));
				markerHistoryDomain.setRefKey(rs.getInt("_refs_key"));
				markerHistoryDomain.setJnumid(rs.getString("jnumid"));
				markerHistoryDomain.setShort_citation(rs.getString("short_citation"));
				markerHistoryDomain.setEvent_date(rs.getDate("event_date"));
				markerHistoryDomain.setCreatedByKey(rs.getInt("_createdby_key"));
				markerHistoryDomain.setCreatedBy(rs.getString("createdBy"));
				markerHistoryDomain.setModifiedByKey(rs.getInt("_modifiedby_key"));
				markerHistoryDomain.setModifiedBy(rs.getString("modifiedBy"));
				markerHistoryDomain.setCreation_date(rs.getDate("creation_date"));
				markerHistoryDomain.setModification_date(rs.getDate("modification_date"));
				
				results.add(markerHistoryDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
	
}
