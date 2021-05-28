package org.jax.mgi.mgd.api.model.acc.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.ActualDBDAO;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDbDomain;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;
import org.jax.mgi.mgd.api.model.acc.translator.ActualDbTranslator;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ActualDbService extends BaseService<ActualDbDomain> {
	
	protected Logger log = Logger.getLogger(getClass());
		
	@Inject
	private ActualDBDAO actualDBDAO;
	@Inject
	private OrganismDAO organismDAO;
	
	private ActualDbTranslator translator = new ActualDbTranslator();	

	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<ActualDbDomain> create(ActualDbDomain domain, User user) {
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<ActualDbDomain> update(ActualDbDomain object, User user) {
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ActualDbDomain> delete(Integer key, User user) {
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ActualDbDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ActualDbDomain domain = new ActualDbDomain();
		if (actualDBDAO.get(key) != null) {
			domain = translator.translate(actualDBDAO.get(key));
		}
		actualDBDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ActualDbDomain> getResults(Integer key) {
        SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
        log.info("Calling ldb translator");
        results.setItem(translator.translate(actualDBDAO.get(key)));
        log.info("Done calling ldb translator");
		actualDBDAO.clear();
        return results;
    }
    
	public Boolean process(String parentKey, List<ActualDbDomain> domains, User user) {
		
		log.info("ActualDbService process");
		
		Boolean modified = false;
		
		if(domains == null || domains.isEmpty()) {
			log.info("processActualDB/nothing to process");
			return modified;
		}
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		for (int i = 0; i < domains.size(); i++) {
			
			if (domains.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
		ActualDB entity = new ActualDB();
		
		if(domain.getName() == null || domain.getName().isEmpty()) {
			results.setError("Failed : ActualDb error", "Missing name: " + domain.getName(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		if(domain.getUrl() == null || domain.getUrl().isEmpty()) {
			results.setError("Failed : ActualDb error", "Missing URL: " + domain.getUrl(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		entity.set_logicaldb_key(Integer.valueOf(domain.getLogicalDBKey()));
		entity.setName(domain.getName());
		entity.setUrl(domain.getUrl());
		
		entity.setName(domain.getName());
		entity.setUrl(domain.getUrl());
		if (domain.getActive() == null || domain.getActive().isEmpty()) {
			entity.setActive(0);
		}
		else {
			entity.setActive(Integer.valueOf(domain.getActive()));
		}
		
		if (domain.getAllowsMultiple() == null || domain.getAllowsMultiple().isEmpty()) {
			entity.setAllowsMultiple(0);
		}
		else {
			entity.setAllowsMultiple(Integer.valueOf(domain.getAllowsMultiple()));
		}

		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		log.info("ActualDbService persisting ActualDb");
		// execute persist/insert/send to database
		ActualDbDAO.persist(entity);
		
		// get the key that has now been generated for use in creating the actualDB
		Integer actualDbKey = entity.get_actualdb_key(); 
		log.info("Persisted actualDbKey: " + actualDbKey);
		// Is there an actualDB to create?
		if (domain.getActualDBs() != null && !domain.getActualDBs().isEmpty()) {
			List<ActualDbDomain> adbList = domain.getActualDBs();
			
			for (int i = 0; i < adbList.size(); i++) {
				ActualDbDomain adbDomain = adbList.get(i);
				// if actualDB name or url are empty set Error message in results
				if (adbDomain.getName()!= null && !adbDomain.getName().isEmpty() ) {
					if (adbDomain.getUrl() == null || adbDomain.getUrl().isEmpty()) {
						results.setError("Failed : ActualDB error", "Missing Url", Constants.HTTP_SERVER_ERROR);
					    return results;
				    }
				    
				}
				else { 
				    results.setError("Failed : Actual DB error", "Missing Name", Constants.HTTP_SERVER_ERROR);
				    return results;
			    }
				ActualDB adbEntity = new ActualDB();
				adbEntity.set_ActualDb_key(ActualDbKey);
				adbEntity.setName(adbDomain.getName());
				adbEntity.setUrl(adbDomain.getUrl());
				if (adbDomain.getActive() == null || adbDomain.getActive().isEmpty()) {
					adbEntity.setActive(0);
				}
				else {
					adbEntity.setActive(Integer.valueOf(adbDomain.getActive()));
				}
				
				if (adbDomain.getAllowsMultiple() == null || adbDomain.getAllowsMultiple().isEmpty()) {
					adbEntity.setAllowsMultiple(0);
				}
				else {
					adbEntity.setAllowsMultiple(Integer.valueOf(adbDomain.getAllowsMultiple()));
				}
				// add creation/modification 
				adbEntity.setCreatedBy(user);
				adbEntity.setCreation_date(new Date());
				adbEntity.setModifiedBy(user);
				adbEntity.setModification_date(new Date());
				
				// execute persist/insert/send to database
				
				actualDBDAO.persist(adbEntity);
				
				log.info("Service create persisted actualDB attributes: ");
				log.info("Service create actualDbKey: " + adbEntity.get_actualdb_key());
				log.info("name" + adbEntity.getName());
				log.info("url" + adbEntity.getUrl());
				log.info("ldbKey: " + adbEntity.get_ActualDb_key());
				log.info("active: " + adbEntity.getActive());
				log.info("multiple: " + adbEntity.getAllowsMultiple());
				log.info("delimiter: "+ adbEntity.getDelimiter());
				
			}
			
		}
		// now get the results from the database
		log.info("calling getResults(ActualDbKey)");
		//results = getResults(ActualDbKey);
		
		// I believe this does the same thing as above:
		results.setItem(translator.translate(ActualDbDAO.get(ActualDbKey)));
		return results;
			}
	}

	
    
	@Transactional
	public SearchResults<ActualDbDomain> deletes(Integer key, User user) {
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
		//results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		//ActualDb entity = ActualDbDAO.get(key);
		// this returns the correct ldbKey from the adb.
		log.info("ActualDbService.delete actualDB.ActualDbKey: " + ActualDbDAO.get(key).getActualDBs().get(0).get_ActualDb_key());
		ActualDbDomain domain = translator.translate(ActualDbDAO.get(key));
		log.info("ActualDbService.delete actualDB.ActualDbKey after translate: " + domain.getActualDBs().get(0).getActualDbKey());
		results.setItem(domain);
		ActualDbDAO.remove(ActualDbDAO.get(key));
		
		return results;
	}
	


    @Transactional	
	public SearchResults<ActualDbDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();
		String cmd = "select count(*) as objectCount from acc_ActualDb";
		
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
	public SearchResults<ActualDbDomain> search(ActualDbDomain searchDomain) {	
					
		SearchResults<ActualDbDomain> results = new SearchResults<ActualDbDomain>();

		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		
		String select = "select distinct ldb.*";
		String from = "from acc_ActualDb ldb";
		String where = "where ldb._ActualDb_key is not null";
		String orderBy = "order by ldb.name";

		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand ldb.name ilike '" + searchDomain.getName() + "'";
		}
		if (searchDomain.getDescription() != null && !searchDomain.getDescription().isEmpty()) {
			where = where +  "\nand ldb.description ilike '" + searchDomain.getDescription() + "'";
		}
		if (searchDomain.getCommonName() != null && !searchDomain.getCommonName().isEmpty()) {
			from = from + ", mgi_organism o";
			where = where + "\nand ldb._organism_key = o._organism_key\nand o.commonName ilike '" + searchDomain.getCommonName() + "'";
		}
		if (searchDomain.getOrganismKey() != null && !searchDomain.getOrganismKey().isEmpty()) {
			where = where + "\nand ldb._organism_key = " + searchDomain.getOrganismKey().toString();
		}
		String cmResults[] = DateSQLQuery.queryByCreationModification("ldb", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
			}
		
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);		
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			List<ActualDbDomain> domainList = new ArrayList<ActualDbDomain>();
			while (rs.next()) {					
				ActualDbDomain domain = new ActualDbDomain();									
				domain = translator.translate(ActualDbDAO.get(rs.getInt("_ActualDb_key")));
				log.info("ldb name: " + domain.getName());
				domainList.add(domain);
				ActualDbDAO.clear();
			}
			results.setItems(domainList);
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
