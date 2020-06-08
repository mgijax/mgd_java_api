package org.jax.mgi.mgd.api.model.all.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleMutationDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleMutationDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleMutation;
import org.jax.mgi.mgd.api.model.all.translator.AlleleMutationTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleMutationService extends BaseService<AlleleMutationDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleMutationDAO alleleMutationDAO;
	@Inject
	private TermDAO termDAO;
	
	private AlleleMutationTranslator translator = new AlleleMutationTranslator();				

	@Transactional
	public SearchResults<AlleleMutationDomain> create(AlleleMutationDomain domain, User user) {
		SearchResults<AlleleMutationDomain> results = new SearchResults<AlleleMutationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleMutationDomain> update(AlleleMutationDomain domain, User user) {
		SearchResults<AlleleMutationDomain> results = new SearchResults<AlleleMutationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleMutationDomain> delete(Integer key, User user) {
		SearchResults<AlleleMutationDomain> results = new SearchResults<AlleleMutationDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AlleleMutationDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AlleleMutationDomain domain = new AlleleMutationDomain();
		if (alleleMutationDAO.get(key) != null) {
			domain = translator.translate(alleleMutationDAO.get(key));
		}
		alleleMutationDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AlleleMutationDomain> getResults(Integer key) {
		SearchResults<AlleleMutationDomain> results = new SearchResults<AlleleMutationDomain>();
		results.setItem(translator.translate(alleleMutationDAO.get(key)));
		alleleMutationDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, List<AlleleMutationDomain> domain, User user) {
		// process allele cell line associations (create, delete, update)
		
		Boolean modified = false;

		log.info("processAlleleMutation");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAlleleMutation/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getMutationKey() == null || domain.get(i).getMutationKey().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processAlleleMutation/create");
				AlleleMutation entity = new AlleleMutation();									
				entity.set_allele_key(Integer.valueOf(parentKey));
				entity.setMutation(termDAO.get(Integer.valueOf(domain.get(i).getMutationKey())));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());				
				alleleMutationDAO.persist(entity);				
				log.info("processAlleleMutation/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAlleleMutation/delete");
				if (domain.get(i).getAssocKey() != null && !domain.get(i).getAssocKey().isEmpty()) {
					AlleleMutation entity = alleleMutationDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
					alleleMutationDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAlleleMutation/update");
				AlleleMutation entity = alleleMutationDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));	
				entity.setMutation(termDAO.get(Integer.valueOf(domain.get(i).getMutationKey())));
				entity.setModification_date(new Date());
				alleleMutationDAO.update(entity);
				log.info("processAlleleMutation/changes processed: " + domain.get(i).getAssocKey());				
				modified = true;
			}
			else {
				log.info("processAlleleMutation/no changes processed: " + domain.get(i).getAssocKey());
			}           
		}
		
		log.info("processAlleleMutation/processing successful");
		return modified;
	}
   
}
