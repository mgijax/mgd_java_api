package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyTypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyTypeService extends BaseService<AntibodyTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyTypeDAO typeDAO;
	
	private AntibodyTypeTranslator translator = new AntibodyTypeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<AntibodyTypeDomain> create(AntibodyTypeDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
		
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AntibodyTypeDomain> update(AntibodyTypeDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AntibodyTypeDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public AntibodyTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyTypeDomain domain = new AntibodyTypeDomain();
		if (typeDAO.get(key) != null) {
			domain = translator.translate(typeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyTypeDomain> getResults(Integer key) {
        SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
        results.setItem(translator.translate(typeDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyTypeDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyTypeDomain> results = new SearchResults<AntibodyTypeDomain>();
		String cmd = "select count(*) as objectCount from gxd_AntibodyType";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional
	public List<AntibodyTypeDomain> search(AntibodyTypeDomain searchDomain) {

		List<AntibodyTypeDomain> results = new ArrayList<AntibodyTypeDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_AntibodyType a";
		String where = "where a._AntibodyType_key is not null";
		String orderBy = "order by a.antibodyType";
		
		//
		// IN PROGRESS, minimal search for now
		//
		
		// if parameter exists, then add to where-clause
		// antibodyName
		if(searchDomain.getAntibodyType() != null && ! searchDomain.getAntibodyType().isEmpty()) {
			where = where + "\n and a.antibodytype ilike '" + searchDomain.getAntibodyType() + "'";
		}
		
		// create/mod date (no 'by')
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", null, null, searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AntibodyTypeDomain domain = new AntibodyTypeDomain();
				domain = translator.translate(typeDAO.get(rs.getInt("_AntibodyType_key")));				
				typeDAO.clear();
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
