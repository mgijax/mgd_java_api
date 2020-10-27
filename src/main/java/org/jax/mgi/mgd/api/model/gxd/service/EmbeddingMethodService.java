package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.EmbeddingMethodDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.EmbeddingMethodDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.EmbeddingMethod;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class EmbeddingMethodService extends BaseService<EmbeddingMethodDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private EmbeddingMethodDAO embeddingDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<EmbeddingMethodDomain> create(EmbeddingMethodDomain domain, User user) {
		SearchResults<EmbeddingMethodDomain> results = new SearchResults<EmbeddingMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<EmbeddingMethodDomain> update(EmbeddingMethodDomain domain, User user) {
		SearchResults<EmbeddingMethodDomain> results = new SearchResults<EmbeddingMethodDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processEmbeddingMethod/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processEmbeddingMethod/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processEmbeddingMethod/nothing to create");
					continue;
				}
				
				EmbeddingMethod entity = new EmbeddingMethod();	
				entity.setEmbeddingMethod(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				embeddingDAO.persist(entity);				
				log.info("processEmbeddingMethod/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processEmbeddingMethod delete");
				EmbeddingMethod entity = embeddingDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				embeddingDAO.remove(entity);
				log.info("processEmbeddingMethod/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processEmbeddingMethod update");
				EmbeddingMethod entity = embeddingDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setEmbeddingMethod(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				embeddingDAO.update(entity);
				log.info("processEmbeddingMethod/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processEmbeddingMethod/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processEmbeddingMethod/update/returning results");
		results.setItems(search(domain));
		log.info("processEmbeddingMethod/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<EmbeddingMethodDomain> delete(Integer key, User user) {
		SearchResults<EmbeddingMethodDomain> results = new SearchResults<EmbeddingMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public EmbeddingMethodDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		EmbeddingMethodDomain domain = new EmbeddingMethodDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<EmbeddingMethodDomain> getResults(Integer key) {
        SearchResults<EmbeddingMethodDomain> results = new SearchResults<EmbeddingMethodDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<EmbeddingMethodDomain> search(EmbeddingMethodDomain searchDomain) {
		// return EmbeddingMethodDomain which looks like a vocab/term domain

		List<EmbeddingMethodDomain> results = new ArrayList<EmbeddingMethodDomain>();
		EmbeddingMethodDomain adomain = new EmbeddingMethodDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_embeddingmethod order by embeddingmethod";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_embedding_key"));
				tdomain.setTerm(rs.getString("embeddingmethod"));
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
