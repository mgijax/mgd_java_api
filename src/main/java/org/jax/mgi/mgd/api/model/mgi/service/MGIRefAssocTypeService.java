package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIRefAssocTypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIRefAssocTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.MGIRefAssocTypeSearchForm;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIRefAssocTypeTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIRefAssocTypeService extends BaseService<MGIRefAssocTypeDomain> {

	protected Logger log = Logger.getLogger(MGISynonymTypeService.class);

	@Inject
	private MGIRefAssocTypeDAO refAssocTypeDAO;

	private MGIRefAssocTypeTranslator translator = new MGIRefAssocTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<MGIRefAssocTypeDomain> create(MGIRefAssocTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGIRefAssocTypeDomain> update(MGIRefAssocTypeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MGIRefAssocTypeDomain get(Integer key) {
		return translator.translate(refAssocTypeDAO.get(key),1);
	}

    @Transactional
    public SearchResults<MGIRefAssocTypeDomain> getResults(Integer key) {
        SearchResults<MGIRefAssocTypeDomain> results = new SearchResults<MGIRefAssocTypeDomain>();
        results.setItem(translator.translate(refAssocTypeDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<MGIRefAssocTypeDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MGIRefAssocTypeDomain> search(MGIRefAssocTypeSearchForm searchForm) {

		// list of results to be returned
		List<MGIRefAssocTypeDomain> results = new ArrayList<MGIRefAssocTypeDomain>();

		// parameters defined in SearchForm
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		String cmd = "select * from mgi_refassoctype";
		String where = "";
		
		if (params.containsKey("mgiTypeKey")) {
			where = where + "\nwhere _mgitype_key = " + params.get("mgiTypeKey");
		}
		cmd = cmd + "\n" + where + "\norder by _mgitype_key, assoctype";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGIRefAssocTypeDomain domain = new MGIRefAssocTypeDomain();
				domain.setRefAssocTypeKey(rs.getString("_refassoctype_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setAllowOnlyOne(rs.getString("allowonlyone"));
				domain.setAssocType(rs.getString("assoctype"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
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
