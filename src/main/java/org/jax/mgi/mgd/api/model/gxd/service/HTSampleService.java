package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;

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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermEMAPADAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class HTSampleService extends BaseService<HTSampleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	//private SQLExecutor sqlExecutor = new SQLExecutor();

	@Inject
	private HTSampleDAO htSampleDAO;
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private TermEMAPADAO termEmapaDAO;	
	@Inject
	private TheilerStageDAO theilerStageDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private NoteService noteService;
	
	private HTSampleTranslator translator = new HTSampleTranslator();

	@Transactional
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		log.info("processHTSample/create");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
		
	@Transactional
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {	
		
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		HTSample entity = htSampleDAO.get(Integer.valueOf(domain.get_sample_key()));
		Boolean modified = true;
//		String cmd;
//		Query query;		
		
		log.info("processHTSample/update");
		
		entity.set_experiment_key(domain.get_experiment_key());
		entity.setOrganism(organismDAO.get(domain.get_organism_key()));
		entity.setRelevance(termDAO.get(domain.get_relevance_key()));
		entity.setSex(termDAO.get(domain.get_sex_key()));
		entity.setGenotype(genotypeDAO.get(domain.get_genotype_key()));

		// not required/may be null
		
		if (domain.getName() == null || domain.getName().isEmpty()) {
			entity.setName(null);
		} else {
			entity.setName(domain.getName());
		}
		
		if (domain.get_stage_key() == null) {
			entity.setTheilerStage(null);
		} else {
			entity.setTheilerStage(theilerStageDAO.get(domain.get_stage_key()));
		}
		
		if (domain.get_emapa_key() == null) {
			entity.setEmapaTerm(null);
		} else {
			entity.setEmapaTerm(termDAO.get(domain.get_emapa_key()));
			entity.setEmapaObject(termEmapaDAO.get(domain.get_emapa_key()));			
		}
		
		if (domain.getAge() == null || domain.getAge().isEmpty()) {
			entity.setAge(null);
			entity.setAgeMin(null);
			entity.setAgeMax(null);				
		} else {
			entity.setAge(domain.getAge());
			entity.setAgeMin(-1);
			entity.setAgeMax(-1);	
		}
		
		// copy getNotes().get(0).getText() into htNotes an duse this for processing changes
		if (domain.getNotes() != null) {
			if (domain.getNotes().get(0).getText().isEmpty()) {
				domain.getHtNotes().setProcessStatus(Constants.PROCESS_DELETE);
			}
			else {
				domain.getHtNotes().setProcessStatus(Constants.PROCESS_UPDATE);
				domain.getHtNotes().setNoteChunk(domain.getNotes().get(0).getText());
			}
			noteService.process(String.valueOf(entity.get_sample_key()), domain.getHtNotes(), "43", user);			
		}
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			htSampleDAO.update(entity);
			log.info("processHTSample/changes processed: " + domain.get_sample_key());
		}
		else {
			log.info("processHTSample/no changes processed: " + domain.get_sample_key());
		}
		
		// return entity translated to domain
		log.info("processHTSample/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processHTSample/update/returned results succsssful");
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
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		log.info("processHTSample/delete");
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}  

}
