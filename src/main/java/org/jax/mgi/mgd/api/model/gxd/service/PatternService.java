package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.PatternDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.PatternDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Pattern;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class PatternService extends BaseService<PatternDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private PatternDAO patternDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<PatternDomain> create(PatternDomain domain, User user) {
		SearchResults<PatternDomain> results = new SearchResults<PatternDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<PatternDomain> update(PatternDomain domain, User user) {
		SearchResults<PatternDomain> results = new SearchResults<PatternDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processPattern/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processPattern/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processPattern/nothing to create");
					continue;
				}
				
				Pattern entity = new Pattern();	
				entity.setPattern(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				patternDAO.persist(entity);				
				log.info("processPattern/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processPattern delete");
				Pattern entity = patternDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				patternDAO.remove(entity);
				log.info("processPattern/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processPattern update");
				Pattern entity = patternDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setPattern(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				patternDAO.update(entity);
				log.info("processPattern/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processPattern/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processPattern/update/returning results");
		results.setItems(search(domain));
		log.info("processPattern/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<PatternDomain> delete(Integer key, User user) {
		SearchResults<PatternDomain> results = new SearchResults<PatternDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public PatternDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		PatternDomain domain = new PatternDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<PatternDomain> getResults(Integer key) {
        SearchResults<PatternDomain> results = new SearchResults<PatternDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<PatternDomain> search(PatternDomain searchDomain) {
		// return PatternDomain which looks like a vocab/term domain

		List<PatternDomain> results = new ArrayList<PatternDomain>();
		PatternDomain adomain = new PatternDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_pattern order by pattern";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_pattern_key"));
				tdomain.setTerm(rs.getString("pattern"));
				tdomain.setIsObsolete("0");
				tdomain.setSequenceNum(String.valueOf(sequenceNum));
				termresults.add(tdomain);
				sequenceNum = sequenceNum + 1;
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		adomain.setTerms(termresults);
		results.add(adomain);
		
		return results;
	}
}
