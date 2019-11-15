package org.jax.mgi.mgd.api.model.mrk.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.ChromosomeDAO;
import org.jax.mgi.mgd.api.model.mrk.domain.ChromosomeDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.ChromosomeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ChromosomeService extends BaseService<ChromosomeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ChromosomeDAO chromosomeDAO;

	private ChromosomeTranslator translator = new ChromosomeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ChromosomeDomain> create(ChromosomeDomain object, User user) {
		SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ChromosomeDomain> update(ChromosomeDomain object, User user) {
		SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ChromosomeDomain> delete(Integer key, User user) {
		SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ChromosomeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ChromosomeDomain domain = new ChromosomeDomain();
		if (chromosomeDAO.get(key) != null) {
			domain = translator.translate(chromosomeDAO.get(key));
		}
		chromosomeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ChromosomeDomain> getResults(Integer key) {
        SearchResults<ChromosomeDomain> results = new SearchResults<ChromosomeDomain>();
        results.setItem(translator.translate(chromosomeDAO.get(key)));
        chromosomeDAO.clear();
        return results;
    }

	@Transactional	
	public List<ChromosomeDomain> search(ChromosomeDomain searchDomain) {

		List<ChromosomeDomain> results = new ArrayList<ChromosomeDomain>();

		String cmd = "select * from mrk_chromosome"
				+ "\nwhere _organism_key = " + searchDomain.getOrganismKey()
				+ "\norder by sequenceNum";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ChromosomeDomain domain = new ChromosomeDomain();
				domain = translator.translate(chromosomeDAO.get(rs.getInt("_chromosome_key")));
				chromosomeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
