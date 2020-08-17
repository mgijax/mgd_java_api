package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyAliasDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyAliasDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyAlias;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyAliasTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyAliasService extends BaseService<AntibodyAliasDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyAliasDAO aliasDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	
	private AntibodyAliasTranslator translator = new AntibodyAliasTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<AntibodyAliasDomain> create(AntibodyAliasDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
		
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AntibodyAliasDomain> update(AntibodyAliasDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AntibodyAliasDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public AntibodyAliasDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyAliasDomain domain = new AntibodyAliasDomain();
		if (aliasDAO.get(key) != null) {
			domain = translator.translate(aliasDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyAliasDomain> getResults(Integer key) {
        SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
        results.setItem(translator.translate(aliasDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyAliasDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
		String cmd = "select count(*) as objectCount from gxd_antibodyalias";
		
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
	public List<AntibodyAliasDomain> search(AntibodyAliasDomain searchDomain) {

		List<AntibodyAliasDomain> results = new ArrayList<AntibodyAliasDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_antibodyalias a";
		String where = "where a._antibodyalias_key is not null";
		String orderBy = "order by a.alias";
		
		//
		// IN PROGRESS, minimal search for now
		//
		
		// if parameter exists, then add to where-clause
		// antibodyName
		if(searchDomain.getAlias() != null && ! searchDomain.getAlias().isEmpty()) {
			where = where + "\n and a.alias ilike '" + searchDomain.getAlias() + "'";
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
				AntibodyAliasDomain domain = new AntibodyAliasDomain();
				domain = translator.translate(aliasDAO.get(rs.getInt("_antibodyalias_key")));				
				aliasDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	@Transactional
	public Boolean process(String parentKey, List<AntibodyAliasDomain> domain, User user) {
		// process antibody aliaess (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAntibodyAlias/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				
				// if alias null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getAlias() == null || domain.get(i).getAlias().isEmpty()) {
					continue;
				}
				
				log.info("processAlias create");

				AntibodyAlias entity = new AntibodyAlias();

				entity.set_antibody_key(Integer.valueOf(parentKey));
				
				entity.setAlias(domain.get(i).getAlias());
				
				if(domain.get(i).getRefsKey() != null && ! domain.get(i).getRefsKey().isEmpty()) {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				aliasDAO.persist(entity);
				
				modified = true;
				log.info("process Alias/create processed: " + entity.get_antibodyalias_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				
				log.info("processAlias delete");
				AntibodyAlias entity = aliasDAO.get(Integer.valueOf(domain.get(i).getAntibodyAliasKey()));
				aliasDAO.remove(entity);
				modified = true;
				log.info("processAlias delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAlias update");
				// IN PROGRESS
				AntibodyAlias entity = aliasDAO.get(Integer.valueOf(domain.get(i).getAntibodyAliasKey()));

				entity.set_antibody_key(Integer.valueOf(parentKey));
				
				entity.setAlias(domain.get(i).getAlias());
				
				//if(domain.get(i).getRefsKey() != null && ! domain.get(i).getRefsKey().isEmpty()) {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				//}
								
				entity.setModification_date(new Date());				
				aliasDAO.update(entity);
				
				modified = true;
				log.info("process Alias/update processed: " + entity.get_antibodyalias_key());					
			}
		
		    log.info("processAlias/processing successful");
		}	
		return modified;
    }
}
