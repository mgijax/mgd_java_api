package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerHistoryDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerHistorySearchForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerHistoryTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerHistoryService extends BaseService<MarkerHistoryDomain> {

	protected Logger log = Logger.getLogger(MarkerHistoryService.class);

	@Inject
	private MarkerHistoryDAO markerHistoryDAO;

	private MarkerHistoryTranslator translator = new MarkerHistoryTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public MarkerHistoryDomain create(MarkerHistoryDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerHistoryDomain update(MarkerHistoryDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerHistoryDomain get(Integer key) {
		// TODO : has Embedded primary key
		//return translator.translate(markerHistoryDAO.get(key),1);
		return null;
	}
	
	@Transactional
	public SearchResults<MarkerHistoryDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MarkerHistoryDomain> search(MarkerHistorySearchForm searchForm) {

		// list of results to be returned
		List<MarkerHistoryDomain> results = new ArrayList<MarkerHistoryDomain>();

		// parameters defined in SearchForm
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		String cmd = "";
		String select = "select h.*, hm.symbol as historySymbol, e.event, r.eventreason, u1.login as createdBy, u2.login as modifiedBy";
		String from = "from mrk_history h, mrk_marker hm, mrk_event e, mrk_eventreason r, mgi_user u1, mgi_user u2";
		String where = "where h._history_key = hm._marker_key"
				+ "\nand h._marker_event_key = e._marker_event_key"
				+ "\nand h._marker_eventreason_key = r._marker_eventreason_key"
				+ "\nand h._createdby_key = u1._user_key"
				+ "\nand h._modifiedby_key = u2._user_key";
		String orderBy = "\norder by h._marker_key, h.sequencenum";
		
		if (params.containsKey("markerKey")) {
			where = where + "\nand h._marker_key = " + params.get("markerKey");
		}

		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerHistoryDomain markerHistoryDomain = new MarkerHistoryDomain();
				markerHistoryDomain.setMarkerKey(rs.getInt("_marker_key"));
				markerHistoryDomain.setSequenceNum(rs.getInt("sequencenum"));
				markerHistoryDomain.setMarkerEventKey(rs.getInt("_marker_event_key"));
				markerHistoryDomain.setMarkerEvent(rs.getString("event"));
				markerHistoryDomain.setMarkerEventReasonKey(rs.getInt("_marker_eventreason_key"));
				markerHistoryDomain.setMarkerEventReason(rs.getString("eventreason"));
				markerHistoryDomain.setMarkerHistoryKey(rs.getInt("_history_key"));
				markerHistoryDomain.setMarkerHistorySymbol(rs.getString("historySymbol"));
				markerHistoryDomain.setMarkerHistoryName(rs.getString("name"));
				markerHistoryDomain.setRefKey(rs.getInt("_refs_key"));
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
