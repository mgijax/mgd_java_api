package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymTypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTypeTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISynonymTypeService extends BaseService<MGISynonymTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MGISynonymTypeDAO synonymTypeDAO;

	private MGISynonymTypeTranslator translator = new MGISynonymTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MGISynonymTypeDomain> create(MGISynonymTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGISynonymTypeDomain> update(MGISynonymTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MGISynonymTypeDomain get(Integer key) {
		return translator.translate(synonymTypeDAO.get(key));
	}

    @Transactional
    public SearchResults<MGISynonymTypeDomain> getResults(Integer key) {
        SearchResults<MGISynonymTypeDomain> results = new SearchResults<MGISynonymTypeDomain>();
        results.setItem(translator.translate(synonymTypeDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<MGISynonymTypeDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional	
	public List<MGISynonymTypeDomain> search() {

		List<MGISynonymTypeDomain> results = new ArrayList<MGISynonymTypeDomain>();
		
		String cmd = "select * from mgi_synonymtype"
			+ "\nwhere _organism_key = 1"
			+ "\norder by _mgitype_key, synonymtype";
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
