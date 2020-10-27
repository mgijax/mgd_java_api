package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GelControlDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelControlDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelControl;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GelControlService extends BaseService<GelControlDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private GelControlDAO gelControlDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<GelControlDomain> create(GelControlDomain domain, User user) {
		SearchResults<GelControlDomain> results = new SearchResults<GelControlDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<GelControlDomain> update(GelControlDomain domain, User user) {
		SearchResults<GelControlDomain> results = new SearchResults<GelControlDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processGelControl/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processGelControl/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processGelControl/nothing to create");
					continue;
				}
				
				GelControl entity = new GelControl();	
				entity.setGelLaneContent(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				gelControlDAO.persist(entity);				
				log.info("processGelControl/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGelControl delete");
				GelControl entity = gelControlDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				gelControlDAO.remove(entity);
				log.info("processGelControl/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGelControl update");
				GelControl entity = gelControlDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setGelLaneContent(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				gelControlDAO.update(entity);
				log.info("processGelControl/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processGelControl/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processGelControl/update/returning results");
		results.setItems(search(domain));
		log.info("processGelControl/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<GelControlDomain> delete(Integer key, User user) {
		SearchResults<GelControlDomain> results = new SearchResults<GelControlDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public GelControlDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GelControlDomain domain = new GelControlDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<GelControlDomain> getResults(Integer key) {
        SearchResults<GelControlDomain> results = new SearchResults<GelControlDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<GelControlDomain> search(GelControlDomain searchDomain) {
		// return GelControlDomain which looks like a vocab/term domain

		List<GelControlDomain> results = new ArrayList<GelControlDomain>();
		GelControlDomain adomain = new GelControlDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_gelcontrol order by gellanecontent";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_gelcontrol_key"));
				tdomain.setTerm(rs.getString("gellanecontent"));
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
