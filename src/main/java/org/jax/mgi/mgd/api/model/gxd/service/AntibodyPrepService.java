package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyPrepDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyPrepDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyPrep;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyPrepTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.service.TermService;
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
	@Inject
	private AntibodyDAO antibodyDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private TermService termService;
	
	private AntibodyPrepTranslator translator = new AntibodyPrepTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<AntibodyPrepDomain> create(AntibodyPrepDomain domain, User user) {
		SearchResults<AntibodyPrepDomain> results = new SearchResults<AntibodyPrepDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;	
	}

	@Transactional
	public SearchResults<AntibodyPrepDomain> update(AntibodyPrepDomain domain, User user) {
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

	@Transactional
	public Integer process(Integer parentKey, AntibodyPrepDomain domain, User user) {
		// process antibodyprep (create, delete, update)
				
		if (domain == null) {
			log.info("processAntibodyPrep/nothing to process");
			return(0);
		}
		
		TermDomain termDomain = new TermDomain();
		
		// vocabulary keys/secondary		
		termDomain.setVocabKey("160");
		termDomain.setTerm("Not Specified");
		int secondaryNS = termService.searchByTerm(termDomain);
		
		// vocabulary keys/secondary		
		termDomain.setVocabKey("152");
		termDomain.setTerm("Not Specified");
		int labelNS = termService.searchByTerm(termDomain);
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
						
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {					
			log.info("processAntibodyPrep create");							
			AntibodyPrep entity = new AntibodyPrep();
			entity.setAntibody(antibodyDAO.get(Integer.valueOf(domain.getAntibodyKey())));
			
			// Not Specified
			if (domain.getSecondaryKey() == null || domain.getSecondaryKey().isEmpty()) {
				entity.setSecondary(termDAO.get(secondaryNS));
			}
			else {
				entity.setSecondary(termDAO.get(Integer.valueOf(domain.getSecondaryKey())));
			}
			
			// Not Specified
			if (domain.getLabelKey() == null || domain.getLabelKey().isEmpty()) {
				entity.setLabel(termDAO.get(labelNS));
			}
			else {
				entity.setLabel(termDAO.get(Integer.valueOf(domain.getLabelKey())));
			}			
			
			entity.setCreation_date(new Date());
			entity.setModification_date(new Date());
			antibodyPrepDAO.persist(entity);
			log.info("processAntibodyPrep/create processed: " + entity.get_antibodyprep_key());	
			return(entity.get_antibodyprep_key());
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processAntibodyPrep delete");
			AntibodyPrep entity = antibodyPrepDAO.get(Integer.valueOf(domain.getAntibodyPrepKey()));
			antibodyPrepDAO.remove(entity);
			log.info("processAntibodyPrep delete successful");
			return(1);
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processAntibodyPrep update");
			AntibodyPrep entity = antibodyPrepDAO.get(Integer.valueOf(domain.getAntibodyPrepKey()));		
			entity.setAntibody(antibodyDAO.get(Integer.valueOf(domain.getAntibodyKey())));

			// Not Specified
			if (domain.getSecondaryKey() == null || domain.getSecondaryKey().isEmpty()) {
				entity.setSecondary(termDAO.get(secondaryNS));
			}
			else {
				entity.setSecondary(termDAO.get(Integer.valueOf(domain.getSecondaryKey())));
			}
			
			// Not Specified
			if (domain.getLabelKey() == null || domain.getLabelKey().isEmpty()) {
				entity.setLabel(termDAO.get(labelNS));
			}
			else {
				entity.setLabel(termDAO.get(Integer.valueOf(domain.getLabelKey())));
			}	
			
			entity.setModification_date(new Date());			
			antibodyPrepDAO.update(entity);
			log.info("processAntibodyPrep/changes processed: " + domain.getAntibodyPrepKey());
			return(1);
		}
		else {
			log.info("processAntibodyPrep/no changes processed: " + domain.getAntibodyPrepKey());
			return(0);
		}
	}
	
}
