package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyPrepDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyPrepDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyPrep;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyPrepTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyPrepService extends BaseService<AntibodyPrepDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyPrepDAO antibodyPrepDAO;
	
	private AntibodyPrepTranslator translator = new AntibodyPrepTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<AntibodyPrepDomain> create(AntibodyPrepDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntibodyPrepDomain> results = new SearchResults<AntibodyPrepDomain>();
		
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AntibodyPrepDomain> update(AntibodyPrepDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntibodyPrepDomain> results = new SearchResults<AntibodyPrepDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AntibodyPrepDomain> delete(Integer key, User user) {
		// get the entity object and delete
		
		SearchResults<AntibodyPrepDomain> results = new SearchResults<AntibodyPrepDomain>();
		AntibodyPrep entity = antibodyPrepDAO.get(key);
		
		results.setItem(translator.translate(antibodyPrepDAO.get(key)));
		antibodyPrepDAO.remove(entity);
		return results;
		
	}
	
/*	@Transactional
	public List<AntibodyPrepDomain> getByAntibodyKey(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyPrepDomain domain = new AntibodyPrepDomain();
		if (antibodyPrepDAO.get(key) != null) {
			domain = translator.translate(antibodyPrepDAO.get(key));
		}
		return domain;
	}*/
	
	@Transactional
	public AntibodyPrepDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyPrepDomain domain = new AntibodyPrepDomain();
		if (antibodyPrepDAO.get(key) != null) {
			domain = translator.translate(antibodyPrepDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyPrepDomain> getResults(Integer key) {
        SearchResults<AntibodyPrepDomain> results = new SearchResults<AntibodyPrepDomain>();
        results.setItem(translator.translate(antibodyPrepDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyPrepDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyPrepDomain> results = new SearchResults<AntibodyPrepDomain>();
		String cmd = "select count(*) as objectCount from gxd_AntibodyPrep";
		
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
	public List<AntibodyPrepDomain> search(AntibodyPrepDomain searchDomain) {

		List<AntibodyPrepDomain> results = new ArrayList<AntibodyPrepDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_AntibodyPrep a";
		String where = "where a._AntibodyPrep_key is not null";
		String orderBy = "order by a._AntibodyPrep_key";
		
		// if parameter exists, then add to where-clause
		// antibodyName
		if(searchDomain.getAntibodyName() != null && ! searchDomain.getAntibodyName().isEmpty()) {
			where = where + "\n and a.antibodyName ilike '" + searchDomain.getAntibodyName() + "'";
		}
		if(searchDomain.getAntibodyKey() != null && ! searchDomain.getAntibodyKey().isEmpty()) {
			where = where + "\n and a._antibody_key = " + searchDomain.getAntibodyKey();
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
				AntibodyPrepDomain domain = new AntibodyPrepDomain();
				domain = translator.translate(antibodyPrepDAO.get(rs.getInt("_antibodyprep_key")));				
				antibodyPrepDAO.clear();
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
