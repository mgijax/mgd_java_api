package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerStatusDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerStatusDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.MarkerStatusTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerStatusService extends BaseService<MarkerStatusDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MarkerStatusDAO markerStatusDAO;

	private MarkerStatusTranslator translator = new MarkerStatusTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerStatusDomain> create(MarkerStatusDomain object, User user) {
		SearchResults<MarkerStatusDomain> results = new SearchResults<MarkerStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MarkerStatusDomain> update(MarkerStatusDomain object, User user) {
		SearchResults<MarkerStatusDomain> results = new SearchResults<MarkerStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MarkerStatusDomain> delete(Integer key, User user) {
		SearchResults<MarkerStatusDomain> results = new SearchResults<MarkerStatusDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MarkerStatusDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MarkerStatusDomain domain = new MarkerStatusDomain();
		if (markerStatusDAO.get(key) != null) {
			domain = translator.translate(markerStatusDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<MarkerStatusDomain> getResults(Integer key) {
        SearchResults<MarkerStatusDomain> results = new SearchResults<MarkerStatusDomain>();
        results.setItem(translator.translate(markerStatusDAO.get(key)));
        return results;
    }

	@Transactional	
	public List<MarkerStatusDomain> search() {

		List<MarkerStatusDomain> results = new ArrayList<MarkerStatusDomain>();

		String cmd = "select * from mrk_status order by status";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerStatusDomain domain = new MarkerStatusDomain();
				domain = translator.translate(markerStatusDAO.get(rs.getInt("_marker_status_key")));
				markerStatusDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
