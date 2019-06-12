package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIRefAssocTypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIRefAssocTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIRefAssocTypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIRefAssocTypeService extends BaseService<MGIRefAssocTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private MGIRefAssocTypeDAO refAssocTypeDAO;

	private MGIRefAssocTypeTranslator translator = new MGIRefAssocTypeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MGIRefAssocTypeDomain> create(MGIRefAssocTypeDomain object, User user) {
		SearchResults<MGIRefAssocTypeDomain> results = new SearchResults<MGIRefAssocTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGIRefAssocTypeDomain> update(MGIRefAssocTypeDomain object, User user) {
		SearchResults<MGIRefAssocTypeDomain> results = new SearchResults<MGIRefAssocTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<MGIRefAssocTypeDomain> delete(Integer key, User user) {
		SearchResults<MGIRefAssocTypeDomain> results = new SearchResults<MGIRefAssocTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public MGIRefAssocTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		MGIRefAssocTypeDomain domain = new MGIRefAssocTypeDomain();
		if (refAssocTypeDAO.get(key) != null) {
			domain = translator.translate(refAssocTypeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<MGIRefAssocTypeDomain> getResults(Integer key) {
        SearchResults<MGIRefAssocTypeDomain> results = new SearchResults<MGIRefAssocTypeDomain>();
        results.setItem(translator.translate(refAssocTypeDAO.get(key)));
        return results;
    }

	@Transactional	
	public List<MGIRefAssocTypeDomain> search() {

		List<MGIRefAssocTypeDomain> results = new ArrayList<MGIRefAssocTypeDomain>();

		String cmd = "select * from mgi_refassoctype order by _mgitype_key, assoctype";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGIRefAssocTypeDomain domain = new MGIRefAssocTypeDomain();
				domain = translator.translate(refAssocTypeDAO.get(rs.getInt("_refassoctype_key")));
				refAssocTypeDAO.clear();
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
