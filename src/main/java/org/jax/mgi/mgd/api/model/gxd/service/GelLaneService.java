package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GelRNATypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GelControlDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GelLaneDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;
import org.jax.mgi.mgd.api.model.gxd.translator.GelLaneTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class GelLaneService extends BaseService<GelLaneDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private GelLaneDAO gelLaneDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private GelRNATypeDAO gelRNATypeDAO;
	@Inject
	private GelControlDAO gelControlDAO;
	@Inject
	private GelLaneStructureService structureService;
	
	private GelLaneTranslator translator = new GelLaneTranslator();				

	@Transactional
	public SearchResults<GelLaneDomain> create(GelLaneDomain domain, User user) {
		SearchResults<GelLaneDomain> results = new SearchResults<GelLaneDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelLaneDomain> update(GelLaneDomain domain, User user) {
		SearchResults<GelLaneDomain> results = new SearchResults<GelLaneDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<GelLaneDomain> delete(Integer key, User user) {
		SearchResults<GelLaneDomain> results = new SearchResults<GelLaneDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public GelLaneDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		GelLaneDomain domain = new GelLaneDomain();
		if (gelLaneDAO.get(key) != null) {
			domain = translator.translate(gelLaneDAO.get(key));
		}
		gelLaneDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<GelLaneDomain> getResults(Integer key) {
		SearchResults<GelLaneDomain> results = new SearchResults<GelLaneDomain>();
		results.setItem(translator.translate(gelLaneDAO.get(key)));
		gelLaneDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, Integer assayTypeKey, List<GelLaneDomain> domain, User user) {
		// process gel lanes (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processGelLane/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {

			// if gel lane is null/empty, then skip
			// pwi has sent a "c" that is empty/not being used
			if (domain.get(i).getGelControlKey() == null || domain.get(i).getGelControlKey().isEmpty()) {
				continue;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				log.info("processGelLane create");

				GelLane entity = new GelLane();
					
				entity.set_assay_key(parentKey);
				
				if (domain.get(i).getLaneLabel() == null || domain.get(i).getLaneLabel().isEmpty()) {
					entity.setLaneLabel(null);
				}
				else {
					entity.setLaneLabel(domain.get(i).getLaneLabel());						
				}

				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setGelControl(gelControlDAO.get(Integer.valueOf(domain.get(i).getGelControlKey())));	
				entity.setSampleAmount(domain.get(i).getSampleAmount());					

				// control = No
				if (domain.get(i).getGelControlKey().equals("1")) {
					
					if (domain.get(i).getGenotypeKey() != null && !domain.get(i).getGenotypeKey().isEmpty()) {
						entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));	
					}
					else {
						entity.setGenotype(genotypeDAO.get(-1));						
					}
					
					if (domain.get(i).getSex() != null && !domain.get(i).getSex().isEmpty()) {
						entity.setSex(domain.get(i).getSex());						
					}
					else {
						entity.setSex("Not Specified");						
					}
					
					if (domain.get(i).getAgePrefix() == null || domain.get(i).getAgePrefix().isEmpty()) {
						domain.get(i).setAgePrefix("embryonic day");
					}

					// western blot
					if (assayTypeKey == 8) {
						entity.setGelRNAType(gelRNATypeDAO.get(-2));
					}
					else if (domain.get(i).getGelRNATypeKey() != null && !domain.get(i).getGelRNATypeKey().isEmpty()) { 
						entity.setGelRNAType(gelRNATypeDAO.get(Integer.valueOf(domain.get(i).getGelRNATypeKey())));
					}
					else {
						entity.setGelRNAType(gelRNATypeDAO.get(-1));					
					}					
				}
				// else from domain
				else {
					entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
					entity.setGelRNAType(gelRNATypeDAO.get(Integer.valueOf(domain.get(i).getGelRNATypeKey())));
					entity.setSex(domain.get(i).getSex());						
				}

				String newAge = null;
				if (!domain.get(i).getAgePrefix().isEmpty()) {
					newAge = domain.get(i).getAgePrefix();
				}
				if (domain.get(i).getAgeStage() != null && !domain.get(i).getAgeStage().isEmpty()) {
					newAge = newAge + " " + domain.get(i).getAgeStage();
				}
				entity.setAge(newAge);
				entity.setAgeMin(-1);
				entity.setAgeMax(-1);		

				if (domain.get(i).getAgeNote() != null && !domain.get(i).getAgeNote().isEmpty()) {
					entity.setAgeNote(domain.get(i).getAgeNote());
				}
				else {
					entity.setAgeNote(null);					
				}	
				
				if (domain.get(i).getLaneNote() != null && !domain.get(i).getLaneNote().isEmpty()) {
					entity.setLaneNote(domain.get(i).getLaneNote());
				}
				else {
					entity.setLaneNote(null);					
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				gelLaneDAO.persist(entity);
				
				// set the domain/gellanekey; needed by AssayService/getGelRows
				domain.get(i).setGelLaneKey(String.valueOf(entity.get_gellane_key()));
				
				if (domain.get(i).getStructures() != null && !domain.get(i).getStructures().isEmpty()) {
					modified = structureService.process(entity.get_gellane_key(), domain.get(i).getStructures(), user);
				}
				
				modified = true;
				log.info("processGelLane/create processed: " + entity.get_gellane_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processGelLane delete");
				GelLane entity = gelLaneDAO.get(Integer.valueOf(domain.get(i).getGelLaneKey()));
				gelLaneDAO.remove(entity);
				modified = true;
				log.info("processGelLane delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processGelLane update");

				GelLane entity = gelLaneDAO.get(Integer.valueOf(domain.get(i).getGelLaneKey()));
			
				entity.set_assay_key(parentKey);
				
				if (domain.get(i).getLaneLabel() == null || domain.get(i).getLaneLabel().isEmpty()) {
					entity.setLaneLabel(null);
				}
				else {
					entity.setLaneLabel(domain.get(i).getLaneLabel());						
				}

				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setGelControl(gelControlDAO.get(Integer.valueOf(domain.get(i).getGelControlKey())));	
				entity.setSampleAmount(domain.get(i).getSampleAmount());					

				// control = No
				if (domain.get(i).getGelControlKey().equals("1")) {
					
					if (domain.get(i).getGenotypeKey() != null && !domain.get(i).getGenotypeKey().isEmpty()) {
						entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));	
					}
					else {
						entity.setGenotype(genotypeDAO.get(-1));						
					}
					
					if (domain.get(i).getSex() != null && !domain.get(i).getSex().isEmpty()) {
						entity.setSex(domain.get(i).getSex());						
					}
					else {
						entity.setSex("Not Specified");						
					}
					
					if (domain.get(i).getAgePrefix() == null || domain.get(i).getAgePrefix().isEmpty()) {
						domain.get(i).setAgePrefix("embryonic day");
					}

					// western blot
					if (assayTypeKey == 8) {
						entity.setGelRNAType(gelRNATypeDAO.get(-2));
					}
					else if (domain.get(i).getGelRNATypeKey() != null && !domain.get(i).getGelRNATypeKey().isEmpty()) { 
						entity.setGelRNAType(gelRNATypeDAO.get(Integer.valueOf(domain.get(i).getGelRNATypeKey())));
					}
					else {
						entity.setGelRNAType(gelRNATypeDAO.get(-1));					
					}
				}
				// else from domain
				else {
					entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
					entity.setGelRNAType(gelRNATypeDAO.get(Integer.valueOf(domain.get(i).getGelRNATypeKey())));
					entity.setSex(domain.get(i).getSex());						
				}
				
				String newAge = null;
				if (!domain.get(i).getAgePrefix().isEmpty()) {
					newAge = domain.get(i).getAgePrefix();
				}
				if (domain.get(i).getAgeStage() != null && !domain.get(i).getAgeStage().isEmpty()) {
					newAge = newAge + " " + domain.get(i).getAgeStage();
				}
				entity.setAge(newAge);
				entity.setAgeMin(-1);
				entity.setAgeMax(-1);

				if (domain.get(i).getAgeNote() != null && !domain.get(i).getAgeNote().isEmpty()) {
					entity.setAgeNote(domain.get(i).getAgeNote());
				}
				else {
					entity.setAgeNote(null);					
				}	
				
				if (domain.get(i).getLaneNote() != null && !domain.get(i).getLaneNote().isEmpty()) {
					entity.setLaneNote(domain.get(i).getLaneNote());
				}
				else {
					entity.setLaneNote(null);					
				}
				
				entity.setModification_date(new Date());
	
				if (domain.get(i).getStructures() != null && !domain.get(i).getStructures().isEmpty()) {
					modified = structureService.process(Integer.valueOf(domain.get(i).getGelLaneKey()), domain.get(i).getStructures(), user);
				}
				
				gelLaneDAO.update(entity);
				modified = true;
				log.info("processGelLane/changes processed: " + domain.get(i).getGelLaneKey());	
			}
			else {
				log.info("processGelLane/no changes processed: " + domain.get(i).getGelLaneKey());
			}
		}
		
		log.info("processGelLane/processing successful");
		return modified;
	}
	    
}
