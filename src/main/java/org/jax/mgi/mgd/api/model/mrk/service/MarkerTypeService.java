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
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerTypeService extends BaseService<MarkerTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MarkerTypeDAO markerTypeDAO;

	private MarkerTypeTranslator translator = new MarkerTypeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerTypeDomain> create(MarkerTypeDomain object, User user) {
		SearchResults<MarkerTypeDomain> results = new SearchResults<MarkerTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MarkerTypeDomain> update(MarkerTypeDomain object, User user) {
		SearchResults<MarkerTypeDomain> results = new SearchResults<MarkerTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<MarkerTypeDomain> delete(Integer key, User user) {
		SearchResults<MarkerTypeDomain> results = new SearchResults<MarkerTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MarkerTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MarkerTypeDomain domain = new MarkerTypeDomain();
		if (markerTypeDAO.get(key) != null) {
			domain = translator.translate(markerTypeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<MarkerTypeDomain> getResults(Integer key) {
        SearchResults<MarkerTypeDomain> results = new SearchResults<MarkerTypeDomain>();
        results.setItem(translator.translate(markerTypeDAO.get(key)));
        return results;
    }

	@Transactional	
	public List<MarkerTypeDomain> search() {

		List<MarkerTypeDomain> results = new ArrayList<MarkerTypeDomain>();

		String cmd = "select * from mrk_types order by name";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerTypeDomain domain = new MarkerTypeDomain();
				domain = translator.translate(markerTypeDAO.get(rs.getInt("_marker_type_key")));
				markerTypeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
