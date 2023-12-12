package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymTypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class MGISynonymTypeService extends BaseService<MGISynonymTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MGISynonymTypeDAO synonymTypeDAO;

	private MGISynonymTypeTranslator translator = new MGISynonymTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MGISynonymTypeDomain> create(MGISynonymTypeDomain object, User user) {
		SearchResults<MGISynonymTypeDomain> results = new SearchResults<MGISynonymTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISynonymTypeDomain> update(MGISynonymTypeDomain object, User user) {
		SearchResults<MGISynonymTypeDomain> results = new SearchResults<MGISynonymTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISynonymTypeDomain> delete(Integer key, User user) {
		SearchResults<MGISynonymTypeDomain> results = new SearchResults<MGISynonymTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGISynonymTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGISynonymTypeDomain domain = new MGISynonymTypeDomain();
		if (synonymTypeDAO.get(key) != null) {
			domain = translator.translate(synonymTypeDAO.get(key));
		}
		synonymTypeDAO.clear();
		return domain;	
	}

    @Transactional
    public SearchResults<MGISynonymTypeDomain> getResults(Integer key) {
        SearchResults<MGISynonymTypeDomain> results = new SearchResults<MGISynonymTypeDomain>();
        results.setItem(translator.translate(synonymTypeDAO.get(key)));
        synonymTypeDAO.clear();
        return results;
    }

	@Transactional	
	public List<MGISynonymTypeDomain> search(MGISynonymTypeDomain searchDomain) {

		List<MGISynonymTypeDomain> results = new ArrayList<MGISynonymTypeDomain>();
		
		String cmd = "select * from mgi_synonymtype"
			+ "\nwhere _mgitype_key = " + searchDomain.getMgiTypeKey();
		
		if (searchDomain.getOrganismKey() != null && !searchDomain.getOrganismKey().isEmpty()) {
			cmd = cmd + "\nand _organism_key = " + searchDomain.getOrganismKey();
		}
		
		cmd = cmd + "\norder by _mgitype_key, synonymtype";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISynonymTypeDomain domain = new MGISynonymTypeDomain();
				domain = translator.translate(synonymTypeDAO.get(rs.getInt("_synonymtype_key")));
				synonymTypeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
