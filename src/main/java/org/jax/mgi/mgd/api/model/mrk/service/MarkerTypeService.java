package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerTypeDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerTypeDomain;
import org.jax.mgi.mgd.api.model.mrk.search.MarkerTypeSearchForm;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTypeTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerTypeService extends BaseService<MarkerTypeDomain> {

	protected Logger log = Logger.getLogger(MarkerTypeService.class);

	@Inject
	private MarkerTypeDAO markerTypeDAO;

	private MarkerTypeTranslator translator = new MarkerTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerTypeDomain> create(MarkerTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MarkerTypeDomain> update(MarkerTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerTypeDomain get(Integer key) {
		return translator.translate(markerTypeDAO.get(key));
	}

        @Transactional
        public SearchResults<MarkerTypeDomain> getResults(Integer key) {
                SearchResults<MarkerTypeDomain> results = new SearchResults<MarkerTypeDomain>();
                results.setItem(translator.translate(markerTypeDAO.get(key)));
                return results;
        }
    
	@Transactional
	public SearchResults<MarkerTypeDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MarkerTypeDomain> search(MarkerTypeSearchForm searchForm) {

		// list of results to be returned
		List<MarkerTypeDomain> results = new ArrayList<MarkerTypeDomain>();

		String cmd = "select * from mrk_types";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerTypeDomain markerTypeDomain = new MarkerTypeDomain();
				markerTypeDomain.setMarkerTypeKey(rs.getInt("_marker_type_key"));
				markerTypeDomain.setMarkerType(rs.getString("name"));
				markerTypeDomain.setCreation_date(rs.getString("creation_date"));
				markerTypeDomain.setModification_date(rs.getString("modification_date"));
				results.add(markerTypeDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
	
}
