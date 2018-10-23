package org.jax.mgi.mgd.api.model.mrk.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
public class MarkerService extends BaseService<MarkerDomain> {

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

	public List<MarkerEIResultDomain> eiSearch(MarkerSearchForm searchForm) {

		// list of results to be returned
		List<MarkerEIResultDomain> results = new ArrayList<MarkerEIResultDomain>();

		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		String cmd = "";
		String select = "select m._marker_key, m._marker_type_key, m.symbol";
		String from = "from mrk_marker m";
		String where = "where m._organism_key = 1";
		String orderBy = "order by m._marker_type_key, m.symbol";
		Boolean from_editorNote = false;
		Boolean from_sequenceNote = false;
		Boolean from_revisionNote = false;
		Boolean from_strainNote = false;
		Boolean from_locationNote = false;

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
			where = where + "\nand note1._notetype_key = 1004 and note1.note ilike '" + params.get("editorNote") + "'" ;
			from_editorNote = true;
		}
		if (params.containsKey("sequenceNote")) {
			where = where + "\nand note2._notetype_key = 1009 and note2.note ilike '" + params.get("sequenceNote") + "'" ;
			from_sequenceNote = true;
		}
		if (params.containsKey("revisionNote")) {
			where = where + "\nand note3._notetype_key = 1030 and note3.note ilike '" + params.get("revisionNote") + "'" ;
			from_revisionNote = true;
		}
		if (params.containsKey("strainNote")) {
			where = where + "\nand note4._notetype_key = 1035 and note4.note ilike '" + params.get("strainNote") + "'" ;
			from_strainNote = true;
		}
		if (params.containsKey("locationNote")) {
			where = where + "\nand note5._notetype_key = 1049 and note5.note ilike '" + params.get("locationNote") + "'" ;
			from_locationNote = true;
		}
		
		if (from_editorNote == true) {
			from = from + ", mgi_note_marker_view note1";
			where = where + "\nand m._marker_key = note1._object_key";
		}
		if (from_sequenceNote == true) {
			from = from + ", mgi_note_marker_view note2";
			where = where + "\nand m._marker_key = note2._object_key";
		}
		if (from_revisionNote == true) {
			from = from + ", mgi_note_marker_view note3";
			where = where + "\nand m._marker_key = note3._object_key";
		}
		if (from_strainNote == true) {
			from = from + ", mgi_note_marker_view note4";
			where = where + "\nand m._marker_key = note4._object_key";
		}
		if (from_locationNote == true) {
			from = from + ", mgi_note_marker_view note5";
			where = where + "\nand m._marker_key = note5._object_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerEIResultDomain markerEIResultDomain = new MarkerEIResultDomain();
				markerEIResultDomain.setKey(rs.getString("_marker_key"));
				markerEIResultDomain.setSymbol(rs.getString("symbol"));
				results.add(markerEIResultDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
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
