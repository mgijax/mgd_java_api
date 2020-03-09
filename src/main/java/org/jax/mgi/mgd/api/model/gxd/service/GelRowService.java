package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GelUnitDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GelRowDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelRowDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelRow;
import org.jax.mgi.mgd.api.model.gxd.translator.GelRowTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GelRowService extends BaseService<GelRowDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GelRowDAO gelRowDAO;
	@Inject
	private GelUnitDAO gelUnitDAO;
	
	private GelRowTranslator translator = new GelRowTranslator();				

	@Transactional
	public SearchResults<GelRowDomain> create(GelRowDomain domain, User user) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelRowDomain> update(GelRowDomain domain, User user) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelRowDomain> delete(Integer key, User user) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GelRowDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GelRowDomain domain = new GelRowDomain();
		if (gelRowDAO.get(key) != null) {
			domain = translator.translate(gelRowDAO.get(key));
		}
		gelRowDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GelRowDomain> getResults(Integer key) {
		SearchResults<GelRowDomain> results = new SearchResults<GelRowDomain>();
		results.setItem(translator.translate(gelRowDAO.get(key)));
		gelRowDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<GelRowDomain> domain, User user) {
		// process gel row (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processGelRow/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if gel row is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getGelUnits() == null || domain.get(i).getGelUnits().isEmpty()) {
					continue;
				}
				
				log.info("processGelRow create");

				GelRow entity = new GelRow();

				entity.set_assay_key(Integer.valueOf(domain.get(i).getAssayKey()));
				entity.setGelUnits(gelUnitDAO.get(Integer.valueOf(domain.get(i).getGelUnitsKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setSize(domain.get(i).getSize());
				
				if (domain.get(i).getRowNote() != null && domain.get(i).getRowNote().isEmpty()) {
					entity.setRowNote(domain.get(i).getRowNote());
				}
				else {
					entity.setRowNote(null);					
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				gelRowDAO.persist(entity);
				
				modified = true;
				log.info("processGelRow/create processed: " + entity.get_gelrow_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGelRow delete");
				GelRow entity = gelRowDAO.get(Integer.valueOf(domain.get(i).getGelRowKey()));
				gelRowDAO.remove(entity);
				modified = true;
				log.info("processGelRow delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGelRow update");

				GelRow entity = gelRowDAO.get(Integer.valueOf(domain.get(i).getGelRowKey()));
			
				entity.set_assay_key(Integer.valueOf(domain.get(i).getAssayKey()));
				entity.setGelUnits(gelUnitDAO.get(Integer.valueOf(domain.get(i).getGelUnitsKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setSize(domain.get(i).getSize());
				
				if (domain.get(i).getRowNote() != null && domain.get(i).getRowNote().isEmpty()) {
					entity.setRowNote(domain.get(i).getRowNote());
				}
				else {
					entity.setRowNote(null);					
				}
				
				entity.setModification_date(new Date());

				gelRowDAO.update(entity);
				modified = true;
				log.info("processGelRow/changes processed: " + domain.get(i).getGelRowKey());	
			}
			else {
				log.info("processGelRow/no changes processed: " + domain.get(i).getGelRowKey());
			}
		}
		
		log.info("processGelRow/processing successful");
		return modified;
	}
	    
}
