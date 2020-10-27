package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.VisualizationMethodDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.VisualizationMethodDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.VisualizationMethod;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class VisualizationMethodService extends BaseService<VisualizationMethodDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private VisualizationMethodDAO visualizationDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<VisualizationMethodDomain> create(VisualizationMethodDomain domain, User user) {
		SearchResults<VisualizationMethodDomain> results = new SearchResults<VisualizationMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<VisualizationMethodDomain> update(VisualizationMethodDomain domain, User user) {
		SearchResults<VisualizationMethodDomain> results = new SearchResults<VisualizationMethodDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processVisualizationMethod/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processVisualizationMethod/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processVisualizationMethod/nothing to create");
					continue;
				}
				
				VisualizationMethod entity = new VisualizationMethod();	
				entity.setVisualization(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				visualizationDAO.persist(entity);				
				log.info("processVisualizationMethod/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processVisualizationMethod delete");
				VisualizationMethod entity = visualizationDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				visualizationDAO.remove(entity);
				log.info("processVisualizationMethod/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processVisualizationMethod update");
				VisualizationMethod entity = visualizationDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setVisualization(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				visualizationDAO.update(entity);
				log.info("processVisualizationMethod/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processVisualizationMethod/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processVisualizationMethod/update/returning results");
		results.setItems(search(domain));
		log.info("processVisualizationMethod/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<VisualizationMethodDomain> delete(Integer key, User user) {
		SearchResults<VisualizationMethodDomain> results = new SearchResults<VisualizationMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public VisualizationMethodDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		VisualizationMethodDomain domain = new VisualizationMethodDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<VisualizationMethodDomain> getResults(Integer key) {
        SearchResults<VisualizationMethodDomain> results = new SearchResults<VisualizationMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<VisualizationMethodDomain> search(VisualizationMethodDomain searchDomain) {
		// return VisualizationMethodDomain which looks like a vocab/term domain

		List<VisualizationMethodDomain> results = new ArrayList<VisualizationMethodDomain>();
		VisualizationMethodDomain adomain = new VisualizationMethodDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_visualizationmethod order by visualization";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_visualization_key"));
				tdomain.setTerm(rs.getString("visualization"));
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
