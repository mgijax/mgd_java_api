package org.jax.mgi.mgd.api.model.mrk.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesRenameDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEiSummaryDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesRenameForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTranslator;
import org.jax.mgi.mgd.api.util.MarkerWithdrawal;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerService extends BaseService<MarkerDomain> implements BaseSearchInterface<MarkerDomain, MarkerSearchForm> {

	protected Logger log = Logger.getLogger(MarkerService.class);

	@Inject
	private MarkerDAO markerDAO;

	private MarkerTranslator translator = new MarkerTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public MarkerDomain create(MarkerDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerDomain update(MarkerDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerDomain get(Integer key) {
		return translator.translate(markerDAO.get(key),3);
	}

	@Transactional
	public MarkerDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MarkerDomain> search(MarkerSearchForm searchForm) {
		SearchResults<Marker> markers;
		if(searchForm.getOrderBy() != null) {
			markers = markerDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			markers = markerDAO.search(searchForm.getSearchFields());
		}
		Iterable<MarkerDomain> newItems = translator.translateEntities(markers.items, searchForm.getSearchDepth());
		return new SearchResults<MarkerDomain>(newItems);
	}


	public MarkerEiSummaryDomain eiSummarySearch(MarkerSearchForm searchForm) {

		// domain object to be JSON-ed
		MarkerEiSummaryDomain markerEiSummaryDomain = new MarkerEiSummaryDomain();
		
		// markerKey-markerSymbol ordered (linked) mapping for summary
		Map<String, String> eiSummaryMarkers = new LinkedHashMap<String, String>();

		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		// formulate sql query
		String cmd = "";
		cmd = cmd + "select _marker_key, symbol "
				+ "from mrk_marker "
				+ "where _organism_key=1 ";
		if (params.containsKey("symbol")) {
			cmd = cmd + "and symbol ilike '" + params.get("symbol")  +"' " ;
		}
		if (params.containsKey("name")) {
			cmd = cmd + "and name ilike '" + params.get("name")  +"' " ;
		}
		if (params.containsKey("chromosome")) {
			cmd = cmd + "and chromosome = '" + params.get("chromosome")  +"' " ;
		}
		cmd = cmd + "order by symbol";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				String markerKey = rs.getString("_marker_key");
				String symbol    = rs.getString("symbol");
				eiSummaryMarkers.put(markerKey , symbol);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		markerEiSummaryDomain.setSummaryMarkers(eiSummaryMarkers);
		return markerEiSummaryDomain;
	}	
	
	public MarkerEIUtilitiesRenameDomain eiUtilitiesRename(MarkerUtilitiesRenameForm searchForm) throws IOException, InterruptedException {
	
		// domain object to be JSON-ed
		MarkerEIUtilitiesRenameDomain markerEIUtilitiesRenameDomain = new MarkerEIUtilitiesRenameDomain();
	
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		MarkerWithdrawal markerWithdrawal = new MarkerWithdrawal();
		
		markerWithdrawal.doWithdrawal(
				(String) params.get("eventKey"),
				(String) params.get("eventReasonKey"),
				(String) params.get("oldKey"),
				(String) params.get("refKey"),
				(String) params.get("addAsSynonym"),
				(String) params.get("newName"),
				(String) params.get("newSymbol"),
				(String) params.get("newKey"));	
		
		return markerEIUtilitiesRenameDomain;
	}
	
}
