package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.InSituResultCellTypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultCellTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultCellType;
import org.jax.mgi.mgd.api.model.gxd.translator.InSituResultCellTypeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class InSituResultCellTypeService extends BaseService<InSituResultCellTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private InSituResultCellTypeDAO insitucelltypeDAO;
	@Inject
	private TermDAO termDAO;

	private InSituResultCellTypeTranslator translator = new InSituResultCellTypeTranslator();				

	@Transactional
	public SearchResults<InSituResultCellTypeDomain> create(InSituResultCellTypeDomain domain, User user) {
		SearchResults<InSituResultCellTypeDomain> results = new SearchResults<InSituResultCellTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultCellTypeDomain> update(InSituResultCellTypeDomain domain, User user) {
		SearchResults<InSituResultCellTypeDomain> results = new SearchResults<InSituResultCellTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<InSituResultCellTypeDomain> delete(Integer key, User user) {
		SearchResults<InSituResultCellTypeDomain> results = new SearchResults<InSituResultCellTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public InSituResultCellTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		InSituResultCellTypeDomain domain = new InSituResultCellTypeDomain();
		if (insitucelltypeDAO.get(key) != null) {
			domain = translator.translate(insitucelltypeDAO.get(key));
		}
		insitucelltypeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<InSituResultCellTypeDomain> getResults(Integer key) {
		SearchResults<InSituResultCellTypeDomain> results = new SearchResults<InSituResultCellTypeDomain>();
		results.setItem(translator.translate(insitucelltypeDAO.get(key)));
		insitucelltypeDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<InSituResultCellTypeDomain> domain, User user) {
		// process in situ result cell types (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processInSituCellTypes/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// if result is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
			if (domain.get(i).getCelltypeTerm() == null || domain.get(i).getCelltypeTerm().isEmpty()) {
				continue;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {			
				log.info("processInSituCellTypes create");

				InSituResultCellType entity = new InSituResultCellType();

				entity.set_result_key(parentKey);
				entity.setCelltypeTerm(termDAO.get(Integer.valueOf(domain.get(i).getCelltypeTermKey())));
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());
				
				insitucelltypeDAO.persist(entity);				
				modified = true;
				log.info("processInSituCellTypes/create processed: " + entity.get_resultcelltype_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				// ignore/do nothing
				if (domain.get(i).getResultCelltypeKey() == "" || domain.get(i).getResultCelltypeKey().isEmpty()) {
					continue;
				}
				log.info("processInSituCellTypes delete");
				InSituResultCellType entity = insitucelltypeDAO.get(Integer.valueOf(domain.get(i).getResultCelltypeKey()));
				insitucelltypeDAO.remove(entity);
				modified = true;
				log.info("processInSituCellTypes delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processInSituCellTypes update");

				InSituResultCellType entity = insitucelltypeDAO.get(Integer.valueOf(domain.get(i).getResultCelltypeKey()));
				
				entity.set_result_key(parentKey);
				entity.setCelltypeTerm(termDAO.get(Integer.valueOf(domain.get(i).getCelltypeTermKey())));
				entity.setModification_date(new Date());
				
				insitucelltypeDAO.update(entity);
				modified = true;
				log.info("processInSituCellTypes/changes processed: " + domain.get(i).getResultCelltypeKey());	
			}
			else {
				log.info("processInSituCellTypes/no changes processed: " + domain.get(i).getResultCelltypeKey());
			}
		}
		
		log.info("processInSituCellTypes/processing successful");
		return modified;
	}
	    
}
