package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class OrganismService extends BaseService<OrganismDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private OrganismDAO organismDAO;

	private OrganismTranslator translator = new OrganismTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<OrganismDomain> create(OrganismDomain object, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<OrganismDomain> update(OrganismDomain object, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<OrganismDomain> delete(Integer key, User user) {
		SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public OrganismDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		OrganismDomain domain = new OrganismDomain();
		if (organismDAO.get(key) != null) {
			domain = translator.translate(organismDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<OrganismDomain> getResults(Integer key) {
        SearchResults<OrganismDomain> results = new SearchResults<OrganismDomain>();
        results.setItem(translator.translate(organismDAO.get(key)));
        return results;
    }

	@Transactional	
	public List<OrganismDomain> search() {

		List<OrganismDomain> results = new ArrayList<OrganismDomain>();

		String cmd = "select * from mgi_organism order by _organism_key";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismDomain domain = new OrganismDomain();
				domain = translator.translate(organismDAO.get(rs.getInt("_organism_key")));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
}
