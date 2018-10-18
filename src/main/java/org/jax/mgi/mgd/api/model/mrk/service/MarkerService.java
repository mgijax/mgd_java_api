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
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIResultDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerEIUtilitiesDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerSearchForm;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerUtilitiesForm;
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


	public MarkerEIResultDomain eiSearch(MarkerSearchForm searchForm) {

		// domain object to be JSON-ed
		MarkerEIResultDomain markerEIResultDomain = new MarkerEIResultDomain();
		
		// markerKey-markerSymbol ordered (linked) mapping for summary
		Map<String, String> results = new LinkedHashMap<String, String>();

		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		String cmd = "";
		String select = "select m._marker_key, m._marker_type_key, m.symbol";
		String from = "from mrk_marker m";
		String where = "where m._organism_key = 1";
		String orderBy = "order by m._marker_type_key, m.symbol";
		Boolean from_editornote = false;
		
		if (params.containsKey("symbol")) {
			where = where + "\nand m.symbol ilike '" + params.get("symbol") + "'" ;
		}
		if (params.containsKey("name")) {
			where = where + "\nand m.name ilike '" + params.get("name") + "'" ;
		}
		if (params.containsKey("chromosome")) {
			where = where + "\nand m.chromosome = '" + params.get("chromosome") + "'" ;
		}
		if (params.containsKey("cytogeneticOffset")) {
			where = where + "\nand m.cytogeneticOffset = '" + params.get("cytogeneticOffset") + "'" ;
		}
		if (params.containsKey("cmOffset")) {
			where = where + "\nand m.cmoffset = " + params.get("cmOffset");
		}
		if (params.containsKey("markerStatusKey")) {
			where = where + "\nand m._marker_status_key = " + params.get("markerStatusKey");
		}
		if (params.containsKey("markerTypeKey")) {
			where = where + "\nand m._marker_type_key = " + params.get("markerTypeKey");
		}
		if (params.containsKey("editorNote")) {
			where = where + "\nand enote._notetype_key = 1004 and enote.note ilike '" + params.get("editorNote") + "'" ;
			from_editornote = true;
		}
		
		if (from_editornote == true) {
			from = from + ", mgi_note_marker_view enote";
			where = where + "\nand m._marker_key = enote._object_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				String markerKey = rs.getString("_marker_key");
				String symbol    = rs.getString("symbol");
				results.put(markerKey, symbol);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		markerEIResultDomain.setResults(results);
		return markerEIResultDomain;
	}	
	
	public MarkerEIUtilitiesDomain eiUtilities(MarkerUtilitiesForm searchForm) throws IOException, InterruptedException {
	
		// domain object to be JSON-ed
		MarkerEIUtilitiesDomain markerEIUtilitiesDomain = new MarkerEIUtilitiesDomain();
	
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
		
		return markerEIUtilitiesDomain;
	}
	
}
