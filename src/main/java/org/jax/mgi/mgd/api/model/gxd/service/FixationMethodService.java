package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.FixationMethodDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.FixationMethodDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.FixationMethod;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class FixationMethodService extends BaseService<FixationMethodDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private FixationMethodDAO fixationDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<FixationMethodDomain> create(FixationMethodDomain domain, User user) {
		SearchResults<FixationMethodDomain> results = new SearchResults<FixationMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<FixationMethodDomain> update(FixationMethodDomain domain, User user) {
		SearchResults<FixationMethodDomain> results = new SearchResults<FixationMethodDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processFixationMethod/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processFixationMethod/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processFixationMethod/nothing to create");
					continue;
				}
				
				FixationMethod entity = new FixationMethod();	
				entity.setFixation(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				fixationDAO.persist(entity);				
				log.info("processFixationMethod/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processFixationMethod delete");
				FixationMethod entity = fixationDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				fixationDAO.remove(entity);
				log.info("processFixationMethod/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processFixationMethod update");
				FixationMethod entity = fixationDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setFixation(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				fixationDAO.update(entity);
				log.info("processFixationMethod/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processFixationMethod/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processFixationMethod/update/returning results");
		results.setItems(search(domain));
		log.info("processFixationMethod/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<FixationMethodDomain> delete(Integer key, User user) {
		SearchResults<FixationMethodDomain> results = new SearchResults<FixationMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public FixationMethodDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		FixationMethodDomain domain = new FixationMethodDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<FixationMethodDomain> getResults(Integer key) {
        SearchResults<FixationMethodDomain> results = new SearchResults<FixationMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<FixationMethodDomain> search(FixationMethodDomain searchDomain) {
		// return FixationMethodDomain which looks like a vocab/term domain

		List<FixationMethodDomain> results = new ArrayList<FixationMethodDomain>();
		FixationMethodDomain adomain = new FixationMethodDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_fixationmethod order by fixation";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_fixation_key"));
				tdomain.setTerm(rs.getString("fixation"));
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
