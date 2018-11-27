package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymTypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.MGISynonymTypeSearchForm;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTypeTranslator;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGISynonymTypeService extends BaseService<MGISynonymTypeDomain> {

	protected Logger log = Logger.getLogger(MGISynonymTypeService.class);

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
		return translator.translate(synonymTypeDAO.get(key),1);
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

	public List<MGISynonymTypeDomain> search(MGISynonymTypeSearchForm searchForm) {

		// list of results to be returned
		List<MGISynonymTypeDomain> results = new ArrayList<MGISynonymTypeDomain>();

		// parameters defined in SearchForm
		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		String cmd = "select * from mgi_synonymtype";
		String where = "where _organism_key = 1";
		
		if (params.containsKey("mgiTypeKey")) {
			where = where + "\nand _mgitype_key = " + params.get("mgiTypeKey");
		}
		cmd = cmd + "\n" + where + "\norder by _mgitype_key, synonymtype";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISynonymTypeDomain domain = new MGISynonymTypeDomain();
				domain.setSynonymTypeKey(rs.getString("_synonymtype_key"));
				domain.setMgiTypeKey(rs.getString("_mgitype_key"));
				domain.setOrganismKey(rs.getString("_organism_key"));
				domain.setDefinition(rs.getString("definition"));
				domain.setAllowOnlyOne(rs.getString("allowonlyone"));
				domain.setSynonymType(rs.getString("synonymtype"));
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
