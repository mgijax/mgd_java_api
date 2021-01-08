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
import org.jax.mgi.mgd.api.model.acc.dao.LogicalDBDAO;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDbDomain;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDbDomain;
import org.jax.mgi.mgd.api.model.acc.entities.ActualDB;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.acc.translator.LogicalDbTranslator;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class LogicalDbService extends BaseService<LogicalDbDomain> {
	
	protected Logger log = Logger.getLogger(getClass());
		
	@Inject
	private LogicalDBDAO logicalDBDAO;
	@Inject
	private ActualDBDAO actualDBDAO;
	@Inject
	private OrganismDAO organismDAO;
	
	private LogicalDbTranslator translator = new LogicalDbTranslator();	

	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<LogicalDbDomain> create(LogicalDbDomain domain, User user) {
		log.info("LogicalDBService create");
		SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
		LogicalDB entity = new LogicalDB();
		
		if(domain.getName() == null || domain.getName().isEmpty()) {
			results.setError("Failed : LogicalDB error", "Missing name: " + domain.getName(), Constants.HTTP_SERVER_ERROR);
			return results;
		}
		
		entity.setName(domain.getName());
		entity.setDescription(domain.getDescription());
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		log.info("LogicalDbService persisting LogicalDB");
		// execute persist/insert/send to database
		logicalDBDAO.persist(entity);
		
		// get the key that has now been generated for use in creating the actualDB
		Integer logicalDBKey = entity.get_logicaldb_key();
		log.info("Persisted logicalDbKey: " + logicalDBKey); //XX 276
		
		// Is there an actualDB to create?
		if (domain.getActualDBs() != null && !domain.getActualDBs().isEmpty()) {
			List<ActualDbDomain> adbList = domain.getActualDBs();
			
			for (int i = 0; i < adbList.size(); i++) {
				ActualDbDomain adbDomain = adbList.get(i);
				
				//**********
				// if name and url are empty, skip - this is a blank line we want to ignore
				if ((adbDomain.getName() == null || adbDomain.getName().isEmpty()) && (adbDomain.getUrl()== null || adbDomain.getUrl().isEmpty())) {
					continue;
				}
				// if actualDB name OR url are empty set Error message in results
				else if (adbDomain.getName()== null || adbDomain.getName().isEmpty() ) {
					results.setError("Failed : ActualDB error", "Missing Name", Constants.HTTP_SERVER_ERROR);
					return results;
				}
					
				else if (adbDomain.getUrl() == null || adbDomain.getUrl().isEmpty()) {
					results.setError("Failed : ActualDB error", "Missing Url", Constants.HTTP_SERVER_ERROR);
				    return results;
			    }
				    
				//****************
				
				ActualDB adbEntity = new ActualDB();
				adbEntity.set_logicaldb_key(logicalDBKey);
				adbEntity.setName(adbDomain.getName());
				adbEntity.setUrl(adbDomain.getUrl());
				adbEntity.setDelimiter(adbDomain.getDelimiter());
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
				log.info("Service create actualDbKey: " + adbEntity.get_actualdb_key()); //XXX185
				log.info("name: " + adbEntity.getName()); //XXX A new ADB
				log.info("url: " + adbEntity.getUrl()); //XXX A new ADB url
				log.info("ldbKey: " + adbEntity.get_logicaldb_key()); //XXX 276
				log.info("active: " + adbEntity.getActive()); //XXX 1
				log.info("multiple: " + adbEntity.getAllowsMultiple()); //XXX 0
				log.info("delimiter: "+ adbEntity.getDelimiter()); //XXX null
				
			}
			
		}
		
		log.info("Service calling translator.translate(entity)");
		results.setItem(translator.translate(entity));
		//logicalDBDAO.clear();

		return results;
	}

	@Transactional
	public SearchResults<LogicalDbDomain> update(LogicalDbDomain domain, User user) {
		SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>(); 
		LogicalDB entity = logicalDBDAO.get(Integer.valueOf(domain.getLogicalDBKey()));
		Boolean modified = false;
		String logicalDBKey = domain.getLogicalDBKey();
		log.info("LogicalDB/update");
		if(!entity.getName().equals(domain.getName())) {
			log.info("process name");
			entity.setName(domain.getName());
			modified = true;
		}
		// may be null coming from entity
		if(entity.getDescription() == null) {
			if(domain.getDescription() != null && !domain.getDescription().isEmpty()) {
				entity.setDescription(domain.getDescription());
				modified = true;
			}
		}
		// may be null coming from domain
		else if (domain.getDescription() == null) {
			entity.setDescription(null);
			modified = true;
		}
		// if not entity/null and not domain/empty, then check if equivalent
		else if (!entity.getDescription().equals(domain.getDescription())) {
			entity.setDescription(domain.getDescription());
			modified = true;
		}
		// may be null coming from entity
		if(entity.getOrganism() == null) {
			if(domain.getOrganismKey() != null) {
				entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
				modified = true;
			}
		}
		// may be null coming from domain
		else if (domain.getOrganismKey() == null) {
			entity.setOrganism(null);
			modified = true;
		}
		// if not entity/null and not domain/empty, then check if equivalent
		else if (!String.valueOf(entity.getOrganism().get_organism_key()).equals(domain.getOrganismKey())) {
			log.info("updating to organismKey: " + domain.getOrganismKey()); 
			entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
			modified = true;
		}
		// do we need a actualDB service with a process method? doing it this way for now
		log.info("actualDBs: " + domain.getActualDBs());
		// if the adb list is neither null nor empty
		if (domain.getActualDBs() != null && !domain.getActualDBs().isEmpty()) {
			log.info("have actualDB domain");
			List<ActualDbDomain> adbList = domain.getActualDBs();
			Boolean adbModified = false;

			for (int i = 0; i < adbList.size(); i++) {
				
				ActualDbDomain adbDomain = adbList.get(i);
				log.info("key: " + adbDomain.getLogicalDBKey() + " name: " + adbDomain.getName() + " url: " + adbDomain.getUrl());
				
				// This is new ADB for existing ldb if no adb key
				if (adbDomain.getActualDBKey() == null || adbDomain.getActualDBKey().isEmpty()) {
					
					// if name and url are empty, skip - this is a blank line we want to ignore
					if ((adbDomain.getName() == null || adbDomain.getName().isEmpty()) && (adbDomain.getUrl()== null || adbDomain.getUrl().isEmpty())) {
						continue;
					}
					// if actualDB name OR url are empty set Error message in results
					else if (adbDomain.getName()== null || adbDomain.getName().isEmpty() ) {
						results.setError("Failed : ActualDB error", "Missing Name", Constants.HTTP_SERVER_ERROR);
						return results;
					}
						
					else if (adbDomain.getUrl() == null || adbDomain.getUrl().isEmpty()) {
						results.setError("Failed : ActualDB error", "Missing Url", Constants.HTTP_SERVER_ERROR);
					    return results;
				    }
					    
					// we have a new actual db, copy the attributes from domain to entity
					ActualDB adbEntity = new ActualDB();
					adbEntity.set_logicaldb_key(Integer.valueOf(logicalDBKey));
					adbEntity.setName(adbDomain.getName());
					adbEntity.setUrl(adbDomain.getUrl());
					adbEntity.setDelimiter(adbDomain.getDelimiter());
					
					// set defaults if active or multiple null/empty
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
					
					log.info("Service update persisted new actualDB attributes: ");
					log.info("Service update created actualDbKey: " + adbEntity.get_actualdb_key());
					log.info("name: " + adbEntity.getName());
					log.info("url: " + adbEntity.getUrl());
					log.info("ldbKey: " + adbEntity.get_logicaldb_key());
					log.info("active: " + adbEntity.getActive());
					log.info("multiple: " + adbEntity.getAllowsMultiple());
					log.info("delimiter: "+ adbEntity.getDelimiter());
					
				}
				else { // has adb key so do an update
		
					ActualDB adbEntity = actualDBDAO.get(Integer.valueOf(adbDomain.getActualDBKey()));
					
					log.info("adb name: " + adbDomain.getName());
					if (adbDomain.getName()!= null && !adbDomain.getName().isEmpty() ) {
						if (!adbEntity.getName().equals(adbDomain.getName())) {
							adbEntity.setName(adbDomain.getName());
							adbModified = true;
						}
					    
					}
					log.info("adb url: " + adbDomain.getUrl());
					if (adbDomain.getUrl() != null && !adbDomain.getUrl().isEmpty()) {
						if(!adbEntity.getUrl().equals(adbDomain.getUrl())) {
							adbEntity.setUrl(adbDomain.getUrl());
							adbModified = true;
						}
					}
					log.info("adb active: " + adbDomain.getActive());
					if (adbDomain.getActive() != null && !adbDomain.getActive().isEmpty()) {
						if (!Integer.valueOf(adbEntity.getActive()).equals(Integer.valueOf(adbDomain.getActive()))) {
						    adbEntity.setActive(Integer.valueOf(adbDomain.getActive()));
						    adbModified = true;
						}
					}
					log.info("adb multiple: " + adbDomain.getAllowsMultiple());				
					if (adbDomain.getAllowsMultiple() != null && !adbDomain.getAllowsMultiple().isEmpty()) {
						if (!Integer.valueOf(adbEntity.getAllowsMultiple()).equals(Integer.valueOf(adbDomain.getAllowsMultiple()))) {
							adbEntity.setAllowsMultiple(Integer.valueOf(adbDomain.getAllowsMultiple()));
							adbModified = true;
						}
					}
					log.info("adb delimiter: " + adbDomain.getDelimiter());
					if (adbDomain.getDelimiter() != null && !adbDomain.getDelimiter().isEmpty()) {
						if (!adbEntity.getDelimiter().equals(adbDomain.getDelimiter())) {
							adbEntity.setDelimiter(adbDomain.getDelimiter());
							adbModified = true;
						}
					}
					
					if (adbModified == true) {
																
						adbEntity.setModification_date(new Date());
						adbEntity.setModifiedBy(user);
						// execute persist/insert/send to database
						actualDBDAO.persist(adbEntity);
						
						log.info("Service update persisted actualDB attributes: ");
						log.info("Service update actualDbKey: " + adbEntity.get_actualdb_key());
						log.info("name: " + adbEntity.getName());
						log.info("url: " + adbEntity.getUrl());
						log.info("ldbKey: " + adbEntity.get_logicaldb_key());
						log.info("active: " + adbEntity.getActive());
						log.info("multiple: " + adbEntity.getAllowsMultiple());
						log.info("delimiter: "+ adbEntity.getDelimiter());
					}
				}
			}
			
		}
		
		log.info(" modified: " + modified);
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			log.info("persisting ldb");
			logicalDBDAO.persist(entity);
			log.info("done persisting ldb");
		}
		else {
			log.info("update LDB/no changes processed: " + domain.getLogicalDBKey());
		}
				
		// return entity translated to domain
		log.info("process Logical DB update adding to results");
		results.setItem(translator.translate(entity));
		
		log.info("process Logical DB update returning results");
		//logicalDBDAO.clear();
		return results;

	}
    
	@Transactional
	public SearchResults<LogicalDbDomain> delete(Integer key, User user) {
		SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
		
		// this returns the correct ldbKey from the adb.
		//log.info("LogicalDbService.delete actualDB.logicalDBKey: " + logicalDBDAO.get(key).getActualDBs().get(0).get_logicaldb_key());
		LogicalDB entity = logicalDBDAO.get(key);
		results.setItem(translator.translate(entity));
		logicalDBDAO.remove(entity);
		
		return results;
	}

	
	@Transactional
	public LogicalDbDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		LogicalDbDomain domain = new LogicalDbDomain();
		if (logicalDBDAO.get(key) != null) {
			domain = translator.translate(logicalDBDAO.get(key));
		}
		logicalDBDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<LogicalDbDomain> getResults(Integer key) {
        SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
        log.info("Calling ldb translator");
        results.setItem(translator.translate(logicalDBDAO.get(key)));
        log.info("Done calling ldb translator");
        logicalDBDAO.clear();
        return results;
    }

    @Transactional	
	public SearchResults<LogicalDbDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
		String cmd = "select count(*) as objectCount from acc_logicaldb";
		
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
	public List<LogicalDbDomain> search(LogicalDbDomain searchDomain) {	
					
		//SearchResults<LogicalDbDomain> results = new SearchResults<LogicalDbDomain>();
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		
		String select = "select distinct ldb.*";
		String from = "from acc_logicaldb ldb";
		String where = "where ldb._logicaldb_key is not null";
		String orderBy = "order by ldb.name";

		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand ldb.name ilike '" + searchDomain.getName() + "'";
		}
		if (searchDomain.getDescription() != null && !searchDomain.getDescription().isEmpty()) {
			where = where +  "\nand ldb.description ilike '" + searchDomain.getDescription() + "'";
		}
	        // translate the 'Not Specified' value back to null as that is what it is in the database -
                // see LogicalDbTranslator.
                if (searchDomain.getCommonName() == "Not Specified") {
                        searchDomain.setCommonName(null);
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
			//List<LogicalDbDomain> domainList = new ArrayList<LogicalDbDomain>();
			while (rs.next()) {					
				LogicalDbDomain domain = new LogicalDbDomain();									
				domain = translator.translate(logicalDBDAO.get(rs.getInt("_logicaldb_key")));
				log.info("ldb name: " + domain.getName());
				results.add(domain);
				logicalDBDAO.clear();
			}
			//results.setItems(domainList);
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//return results;
		return results;
	}

	@Transactional
	public List<LogicalDbDomain> searchMCLSet() {
		// get mutant cell line logical db list
		// returns list of logical db domain

		//		BayGenomics                 |             95
		//		CMHD                        |             99
		//		CSD-KOMP                    |            108
		//		EGTC                        |            101
		//		ESDB                        |            100
		//		EUCOMM                      |            137
		//		EUCOMM-GTcellline           |            128
		//		FHCRC                       |            104
		//		GGTC                        |            102
		//		IGTC                        |             66
		//		Ishida Y Cell Line          |            152
		//		Lexicon                     |             96
		//		mirKO                       |            165
		//		NorCOMM                     |            142
		//		Pleiades Promoter Cell Line |            181
		//		Regeneron-KOMP              |            109
		//		Ruley HE Cell Line          |            150
		//		SIGTR                       |            103
		//		TIGEM                       |             98
		//		TIGM                        |             97
		//		TIGM Cell Line              |            121	
		
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();

		String cmd = "select _logicaldb_key, name from acc_logicaldb "
				+ "\nwhere _logicaldb_key in (95,99,108,101,100,137,128,104,102,66,152,96,165,142,181,109,150,103,98,97,121)"
				+ "\norder by name";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				LogicalDbDomain domain = new LogicalDbDomain();									
				domain = translator.translate(logicalDBDAO.get(rs.getInt("_logicaldb_key")));
				results.add(domain);
				logicalDBDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public List<LogicalDbDomain> searchProbeSet() {
		// get probe logical db list
		// returns list of logical db domain
		
		//        9 | Sequence DB (aka Nucleotide Sequence)
		//       12 | ATCC
		//       82 | BROAD
		//       17 | IMAGE
		//       44 | MGC
		//       49 | NIA
		//       46 | NIA 15K
		//       50 | NIA 7.4K
		//       51 | RIKEN
		//       25 | RIKEN Cluster
		//       26 | RIKEN (FANTOM)
		//       48 | RPCI-23
		//       52 | RPCI-24
		//       80 | UniSTS
		//       16 | WashU		
		
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();

		String cmd = "select _logicaldb_key, name, 0 as org"
				+ "\nfrom acc_logicaldb"
				+ "\nwhere _logicaldb_key in (9)"
				+ "\nunion"
				+ "\nselect _logicaldb_key, name, 1 as org"
				+ "\nfrom acc_logicaldb"
				+ "\nwhere _logicaldb_key in (12,82,17,44,49,46,50,51,25,26,48,52,80,16)"
				+ "\norder by org, name";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				LogicalDbDomain domain = new LogicalDbDomain();									
				domain = translator.translate(logicalDBDAO.get(rs.getInt("_logicaldb_key")));
				domain.setName(domain.getName().replace("Sequence DB", "Nucleotide"));
				results.add(domain);
				logicalDBDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public List<LogicalDbDomain> searchProbeSourceSet() {
		// get probe source logical db list
		// returns list of logical db domain	

        // 43 | IMAGE Clone Libraries
        // 18 | dbEST Libraries
        // 46 | NIA 15K
		
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();

		String cmd = "select _logicaldb_key, name"
				+ "\nfrom acc_logicaldb"
				+ "\nwhere _logicaldb_key in (18, 43, 46)"
				+ "\norder by name";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				LogicalDbDomain domain = new LogicalDbDomain();									
				domain = translator.translate(logicalDBDAO.get(rs.getInt("_logicaldb_key")));
				results.add(domain);
				logicalDBDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional
	public List<LogicalDbDomain> searchProbeStrainSet() {
		// get probe strain logical db list
		// returns list of logical db domain	
		
		List<LogicalDbDomain> results = new ArrayList<LogicalDbDomain>();

		String cmd = "select _logicaldb_key, name, 0 as org"
				+ "\nfrom acc_logicaldb"
				+ "\nwhere _logicaldb_key in (22)"
				+ "\nunion"
				+ "\nselect _logicaldb_key, name, 1 as org"
				+ "\nfrom acc_logicaldb"
				+ "\nwhere _logicaldb_key in (38)"				
				+ "\nunion"
				+ "\nselect _logicaldb_key, name, 2 as org"
				+ "\nfrom acc_logicaldb"
				+ "\nwhere _logicaldb_key in (37,39,40,54,56,57,58,70,71,83,87,90,91,92,93,94,154,161,177,184,188,200,206,207,208,213,215,216,217,219,220)"
				+ "\norder by org, name";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {					
				LogicalDbDomain domain = new LogicalDbDomain();									
				domain = translator.translate(logicalDBDAO.get(rs.getInt("_logicaldb_key")));
				results.add(domain);
				logicalDBDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
