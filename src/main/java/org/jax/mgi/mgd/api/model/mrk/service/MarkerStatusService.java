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
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MarkerStatusService extends BaseService<MarkerStatusDomain> {

	protected Logger log = Logger.getLogger(MarkerStatusService.class);

	@Inject
	private MarkerStatusDAO markerStatusDAO;

	private MarkerStatusTranslator translator = new MarkerStatusTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MarkerStatusDomain> create(MarkerStatusDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MarkerStatusDomain> update(MarkerStatusDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MarkerStatusDomain get(Integer key) {
		return translator.translate(markerStatusDAO.get(key));
	}

        @Transactional
        public SearchResults<MarkerStatusDomain> getResults(Integer key) {
                SearchResults<MarkerStatusDomain> results = new SearchResults<MarkerStatusDomain>();
                results.setItem(translator.translate(markerStatusDAO.get(key)));
                return results;
        }

	@Transactional
	public SearchResults<MarkerStatusDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MarkerStatusDomain> search() {

		// list of results to be returned
		List<MarkerStatusDomain> results = new ArrayList<MarkerStatusDomain>();

		String cmd = "select * from mrk_status";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MarkerStatusDomain domain = new MarkerStatusDomain();
				domain.setMarkerStatusKey(rs.getInt("_marker_status_key"));
				domain.setMarkerStatus(rs.getString("status"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}	
	
}
