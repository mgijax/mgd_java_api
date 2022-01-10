package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.HTSampleDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.TheilerStageDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.gxd.translator.HTSampleTranslator;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class HTSampleService extends BaseService<HTSampleDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private HTSampleDAO htSampleDAO;
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private TheilerStageDAO theilerStageDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private NoteService noteService;
	
	private HTSampleTranslator translator = new HTSampleTranslator();

	@Transactional
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
		
	@Transactional
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {	
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		log.info("processHTSample/delete");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public HTSampleDomain get(Integer key) {
		log.info("processHTSample/get");
		// get the DAO/entity and translate -> domain
		HTSampleDomain domain = new HTSampleDomain();
		HTSample entity = htSampleDAO.get(key);
		if ( entity != null) {
			domain = translator.translate(entity);
		}
		return domain;
	}

    @Transactional
    public SearchResults<HTSampleDomain> getResults(Integer key) {
        SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
        results.setItem(translator.translate(htSampleDAO.get(key)));
        return results;
    } 

	@Transactional
	public Boolean process(Integer parentKey, List<HTSampleDomain> domain, User user) {
		// process ht sample (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processHTSample/nothing to process");
			return modified;
		}
	
		for (int i = 0; i < domain.size(); i++) {
			log.info("--- :" + domain.get(i).getProcessStatus());
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if sample name is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used			
				if (domain.get(i).getName() == null || domain.get(i).getName().isEmpty()) {
					continue;
				} 
				
				log.info("processHTSample create");

				HTSample entity = new HTSample();
				
				entity.set_experiment_key(parentKey);
				entity.setOrganism(organismDAO.get(domain.get(i).get_organism_key()));
				entity.setRelevance(termDAO.get(domain.get(i).get_relevance_key()));
				entity.setSex(termDAO.get(domain.get(i).get_sex_key()));
				entity.setGenotype(genotypeDAO.get(domain.get(i).getGenotype_object().get_genotype_key()));
				entity.setName(domain.get(i).getName());
						
				
				if (domain.get(i).getAge() == null || domain.get(i).getAge().isEmpty()) {
					entity.setAge(null);
					entity.setAgeMin(null);
					entity.setAgeMax(null);				
				} else {
					entity.setAge(domain.get(i).getAge());
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);	
				}

				// use HTEmapsDomain
				if (domain.get(i).getEmaps_object().get_stage_key() == null) {
					entity.setTheilerStage(null);
				} else {
					entity.setTheilerStage(theilerStageDAO.get(domain.get(i).getEmaps_object().get_stage_key()));
				}
				
				if (domain.get(i).getEmaps_object().get_emapa_term_key() == null) {
					entity.setEmapaTerm(null);
				} else {
					entity.setEmapaTerm(termDAO.get(domain.get(i).getEmaps_object().get_emapa_term_key()));
				}

/*				
				// copy getNotes().get(0).getText() -> getHtNotes to use noteService correctly
				// at some point, convert pwi to use getHtNotes format
				if (domain.get(i).getNotes() != null) {
					if (domain.get(i).getNotes().get(0).getText() == null || domain.get(i).getNotes().get(0).getText().isEmpty()) {
						domain.get(i).getHtNotes().setProcessStatus(Constants.PROCESS_DELETE);
						domain.get(i).getHtNotes().setNoteChunk(null);				
					}
					else if (domain.get(i).getHtNotes() != null) {
						domain.get(i).getHtNotes().setProcessStatus(Constants.PROCESS_UPDATE);
						domain.get(i).getHtNotes().setNoteChunk(domain.get(i).getNotes().get(0).getText());
					}
					else {
						NoteDomain newNoteDomain = new NoteDomain();
						newNoteDomain.setProcessStatus(Constants.PROCESS_CREATE);
						newNoteDomain.setNoteKey("");
						newNoteDomain.setObjectKey(String.valueOf(entity.get_sample_key()));
						newNoteDomain.setMgiTypeKey("43");
						newNoteDomain.setNoteTypeKey("1048");	
						newNoteDomain.setNoteChunk(domain.get(i).getNotes().get(0).getText());
						domain.get(i).setHtNotes(newNoteDomain);
					}
					noteService.process(String.valueOf(entity.get_sample_key()), domain.get(i).getHtNotes(), "43", user);			
				}
*/
				entity.setCreation_date(new Date());
				entity.setCreatedBy(user);				
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				htSampleDAO.update(entity);
			
				log.info("processHTSample/create processed: " + entity.get_sample_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processHTSample delete");
				HTSample entity = htSampleDAO.get(domain.get(i).get_sample_key());
				htSampleDAO.remove(entity);
				log.info("processHTSample delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processHTSample update");

				HTSample entity = htSampleDAO.get(domain.get(i).get_sample_key());

				entity.set_experiment_key(parentKey);
				entity.setOrganism(organismDAO.get(domain.get(i).get_organism_key()));
				entity.setRelevance(termDAO.get(domain.get(i).get_relevance_key()));
				entity.setSex(termDAO.get(domain.get(i).get_sex_key()));
				entity.setGenotype(genotypeDAO.get(domain.get(i).getGenotype_object().get_genotype_key()));
				entity.setName(domain.get(i).getName());
						
				if (domain.get(i).getAge() == null || domain.get(i).getAge().isEmpty()) {
					entity.setAge(null);
					entity.setAgeMin(null);
					entity.setAgeMax(null);				
				} else {
					entity.setAge(domain.get(i).getAge());
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);	
				}
				
				// use HTEmapsDomain
				if (domain.get(i).getEmaps_object().get_stage_key() == null) {
					entity.setTheilerStage(null);
				} else {
					entity.setTheilerStage(theilerStageDAO.get(domain.get(i).getEmaps_object().get_stage_key()));
				}
				
				if (domain.get(i).getEmaps_object().get_emapa_term_key() == null) {
					entity.setEmapaTerm(null);
				} else {
					entity.setEmapaTerm(termDAO.get(domain.get(i).getEmaps_object().get_emapa_term_key()));
				}
				
				// copy getNotes().get(0).getText() -> getHtNotes to use noteService correctly
				// at some point, convert pwi to use getHtNotes format
/*
				if (domain.get(i).getNotes() != null) {
					if (domain.get(i).getNotes().get(0).getText() == null || domain.get(i).getNotes().get(0).getText().isEmpty()) {
						domain.get(i).getHtNotes().setProcessStatus(Constants.PROCESS_DELETE);
						domain.get(i).getHtNotes().setNoteChunk(null);				
					}
					else if (domain.get(i).getHtNotes() != null) {
						domain.get(i).getHtNotes().setProcessStatus(Constants.PROCESS_UPDATE);
						domain.get(i).getHtNotes().setNoteChunk(domain.get(i).getNotes().get(0).getText());
					}
					else {
						NoteDomain newNoteDomain = new NoteDomain();
						newNoteDomain.setProcessStatus(Constants.PROCESS_CREATE);
						newNoteDomain.setNoteKey("");
						newNoteDomain.setObjectKey(String.valueOf(entity.get_sample_key()));
						newNoteDomain.setMgiTypeKey("43");
						newNoteDomain.setNoteTypeKey("1048");	
						newNoteDomain.setNoteChunk(domain.get(i).getNotes().get(0).getText());
						domain.get(i).setHtNotes(newNoteDomain);
					}
					noteService.process(String.valueOf(entity.get_sample_key()), domain.get(i).getHtNotes(), "43", user);			
				}
*/	
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				htSampleDAO.update(entity);
				
				log.info("processHTSample/changes processed: " + domain.get(i).get_sample_key());	
			}
			else {
				log.info("processHTSample/no changes processed: " + domain.get(i).get_sample_key());
			}
		}
		
		modified = true;
		log.info("processHTSample/processing successful");
		return modified;
	}
		
}
