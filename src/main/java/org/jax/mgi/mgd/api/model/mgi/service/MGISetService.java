package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISetDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISetTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISetService extends BaseService<MGISetDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGISetDAO setDAO;
	
	private MGISetTranslator translator = new MGISetTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGISetDomain> create(MGISetDomain object, User user) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetDomain> update(MGISetDomain domain, User user) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGISetDomain> delete(Integer key, User user) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGISetDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGISetDomain domain = new MGISetDomain();
		if (setDAO.get(key) != null) {
			domain = translator.translate(setDAO.get(key));
		}
		setDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<MGISetDomain> getResults(Integer key) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results.setItem(translator.translate(setDAO.get(key)));
		setDAO.clear();
		return results;
    }

	@Transactional	
	public List<MGISetDomain> getBySetUser(MGISetDomain searchDomain) {
		// return all set members by _set_key, _createdby_key
		
		List<MGISetDomain> results = new ArrayList<MGISetDomain>();

		String cmd = "\nselect * from mgi_setmember"
				+ "\nwhere _set_key = " + searchDomain.getSetKey()
				+ "\nand _createdby_key = " + searchDomain.getCreatedByKey();
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetDomain domain = new MGISetDomain();
				domain = translator.translate(setDAO.get(rs.getInt("_set_key")));
				setDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}
