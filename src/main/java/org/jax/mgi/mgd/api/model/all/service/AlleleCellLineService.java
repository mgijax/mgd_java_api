package org.jax.mgi.mgd.api.model.all.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDAO;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.model.all.translator.AlleleCellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleCellLineService extends BaseService<AlleleCellLineDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleCellLineDAO alleleCellLineDAO;
	@Inject
	private CellLineDAO cellLineDAO;
	
	private AlleleCellLineTranslator translator = new AlleleCellLineTranslator();				

	@Transactional
	public SearchResults<AlleleCellLineDomain> create(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDomain> update(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDomain> delete(Integer key, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AlleleCellLineDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AlleleCellLineDomain domain = new AlleleCellLineDomain();
		if (alleleCellLineDAO.get(key) != null) {
			domain = translator.translate(alleleCellLineDAO.get(key));
		}
		alleleCellLineDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AlleleCellLineDomain> getResults(Integer key) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setItem(translator.translate(alleleCellLineDAO.get(key)));
		alleleCellLineDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, String alleleTypeKey, String alleleType, List<AlleleCellLineDomain> domain, User user) {
		// process allele cell line (create, delete, update)
		
		Boolean modified = false;

		log.info("processAlleleCellLine");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAlleleCellLine/nothing to process");
			return modified;
		}      
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	log.info("processAlleleCellLine/domain :" + i);
		
        	if (domain.get(i).getMutantCellLine().getCellLineKey().isEmpty()) {
        		return modified;
        	}                 
						
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {			
				AlleleCellLine entity = new AlleleCellLine();									
				entity.set_allele_key(Integer.valueOf(parentKey));
				
				// to-do
				// if no cell line, then create it
				entity.setMutantCellLine(cellLineDAO.get(Integer.valueOf(domain.get(i).getMutantCellLine().getCellLineKey())));				
				
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());				
				alleleCellLineDAO.persist(entity);				
				log.info("processAlleleCellLine/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAlleleCellLine/delete");
				if (domain.get(i).getAssocKey() != null && !domain.get(i).getAssocKey().isEmpty()) {
					AlleleCellLine entity = alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
					alleleCellLineDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAlleleCellLine/update");
				AlleleCellLine entity = alleleCellLineDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));			
				
				// to-do
				// if no cell line, then create it
				entity.setMutantCellLine(cellLineDAO.get(Integer.valueOf(domain.get(i).getMutantCellLine().getCellLineKey())));				
				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				alleleCellLineDAO.update(entity);
				log.info("processAlleleCellLine/changes processed: " + domain.get(i).getAssocKey());				
				modified = true;
			}
			else {
				log.info("processAlleleCellLine/no changes processed: " + domain.get(i).getAssocKey());
			}          
		}
		
		log.info("processAlleleCellLine/processing successful");
		return modified;
	}
   
}
