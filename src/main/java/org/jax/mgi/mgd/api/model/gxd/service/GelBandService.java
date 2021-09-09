package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GelBandDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GelRowDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.StrengthDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelBandDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelBand;
import org.jax.mgi.mgd.api.model.gxd.translator.GelBandTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GelBandService extends BaseService<GelBandDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GelBandDAO gelBandDAO;
	@Inject
	private GelRowDAO gelRowDAO;
	@Inject
	private StrengthDAO gelStrengthDAO;	
	
	private GelBandTranslator translator = new GelBandTranslator();				

	@Transactional
	public SearchResults<GelBandDomain> create(GelBandDomain domain, User user) {
		SearchResults<GelBandDomain> results = new SearchResults<GelBandDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelBandDomain> update(GelBandDomain domain, User user) {
		SearchResults<GelBandDomain> results = new SearchResults<GelBandDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelBandDomain> delete(Integer key, User user) {
		SearchResults<GelBandDomain> results = new SearchResults<GelBandDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GelBandDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GelBandDomain domain = new GelBandDomain();
		if (gelBandDAO.get(key) != null) {
			domain = translator.translate(gelBandDAO.get(key));
		}
		gelBandDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GelBandDomain> getResults(Integer key) {
		SearchResults<GelBandDomain> results = new SearchResults<GelBandDomain>();
		results.setItem(translator.translate(gelBandDAO.get(key)));
		gelBandDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<GelBandDomain> domain, User user) {
		// process gel band (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processGelBand/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			// gel band row must equal parentKey (gel row key), else skip
			if (!domain.get(i).getGelRowKey().equals(String.valueOf(parentKey))) {
				//log.info("SKIPPING BAND ROW:" + parentKey);
				continue;
			}
			
			log.info("PARENT ROW:" + parentKey);
			log.info("BAND ROW:" + domain.get(i).getGelRowKey());
			
//			// if gel band is null/empty, then skip
//			// pwi has sent a "c" that is empty/not being used
//			if (domain.get(i).getStrengthKey() == null || domain.get(i).getStrengthKey().isEmpty()) {
//				continue;
//			}
//			
//			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
//
//				log.info("processGelBand create");
//
//				GelBand entity = new GelBand();
//
//				entity.setGelRow(gelRowDAO.get(parentKey));								
//				entity.set_gellane_key(Integer.valueOf(domain.get(i).getGelLaneKey()));
//				entity.setStrength(gelStrengthDAO.get(Integer.valueOf(domain.get(i).getStrengthKey())));
//				
//				if (domain.get(i).getBandNote() != null && !domain.get(i).getBandNote().isEmpty()) {
//					entity.setBandNote(domain.get(i).getBandNote());
//				}
//				else {
//					entity.setBandNote(null);					
//				}
//				
//				entity.setCreation_date(new Date());				
//				entity.setModification_date(new Date());				
//				gelBandDAO.persist(entity);
//				
//				modified = true;
//				log.info("processGelBand/create processed: " + entity.get_gelband_key());					
//			}
//			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
//				log.info("processGelBand delete");
//				GelBand entity = gelBandDAO.get(Integer.valueOf(domain.get(i).getGelBandKey()));
//				gelBandDAO.remove(entity);
//				modified = true;
//				log.info("processGelBand delete successful");
//			}
//			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
//				log.info("processGelBand update");
//
//				GelBand entity = gelBandDAO.get(Integer.valueOf(domain.get(i).getGelBandKey()));
//
//				entity.setGelRow(gelRowDAO.get(parentKey));				
//				entity.set_gellane_key(Integer.valueOf(domain.get(i).getGelLaneKey()));
//				entity.setStrength(gelStrengthDAO.get(Integer.valueOf(domain.get(i).getStrengthKey())));
//				
//				if (domain.get(i).getBandNote() != null && !domain.get(i).getBandNote().isEmpty()) {
//					entity.setBandNote(domain.get(i).getBandNote());
//				}
//				else {
//					entity.setBandNote(null);					
//				}
//				
//				entity.setModification_date(new Date());
//
//				gelBandDAO.update(entity);
//				modified = true;
//				log.info("processGelBand/changes processed: " + domain.get(i).getGelBandKey());	
//			}
//			else {
//				log.info("processGelBand/no changes processed: " + domain.get(i).getGelBandKey());
//			}
		}
		
		log.info("processGelBand/processing successful");
		return modified;
	}
	    
}
