package org.jax.mgi.mgd.api.model.gxd.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.EmbeddingMethodDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.FixationMethodDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.SpecimenDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Specimen;
import org.jax.mgi.mgd.api.model.gxd.translator.SpecimenTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class SpecimenService extends BaseService<SpecimenDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private SpecimenDAO specimenDAO;
	@Inject
	private EmbeddingMethodDAO embeddingDAO;
	@Inject
	private FixationMethodDAO fixationDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private InSituResultService insituresultService;
	
	private SpecimenTranslator translator = new SpecimenTranslator();				

	@Transactional
	public SearchResults<SpecimenDomain> create(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<SpecimenDomain> update(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<SpecimenDomain> delete(Integer key, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SpecimenDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		SpecimenDomain domain = new SpecimenDomain();
		if (specimenDAO.get(key) != null) {
			domain = translator.translate(specimenDAO.get(key));
		}
		specimenDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<SpecimenDomain> getResults(Integer key) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results.setItem(translator.translate(specimenDAO.get(key)));
		specimenDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(Integer parentKey, List<SpecimenDomain> domain, User user) {
		// process specimens (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processSpecimen/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if specimenLabel is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getSpecimenLabel() == null || domain.get(i).getSpecimenLabel().isEmpty()) {
					continue;
				}
				
				log.info("processSpecimen create");

				Specimen entity = new Specimen();

				entity.set_assay_key(Integer.valueOf(domain.get(i).getAssayKey()));
				entity.setSequenceNum(domain.get(i).getSequenceNum());				
				entity.setSpecimenLabel(domain.get(i).getSpecimenLabel());
				
				//defaults
				
				if (domain.get(i).getEmbeddingKey() == null || domain.get(i).getEmbeddingKey().isEmpty()) {
					entity.setEmbeddingMethod(embeddingDAO.get(-1));
				}
				else {
					entity.setEmbeddingMethod(embeddingDAO.get(Integer.valueOf(domain.get(i).getEmbeddingKey())));
				}

				if (domain.get(i).getFixationKey() == null || domain.get(i).getFixationKey().isEmpty()) {
					entity.setFixationMethod(fixationDAO.get(-1));
				}
				else {
					entity.setFixationMethod(fixationDAO.get(Integer.valueOf(domain.get(i).getFixationKey())));
				}
				
				if (domain.get(i).getGenotypeKey() == null || domain.get(i).getGenotypeKey().isEmpty()) {
					entity.setGenotype(genotypeDAO.get(-1));
				}
				else {
					entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
				}
							
				if (domain.get(i).getSex() == null || domain.get(i).getSex().isEmpty()) {
					entity.setSex("Not Specified");
				}
				else {
					entity.setSex(domain.get(i).getSex());
				}
				
				if (domain.get(i).getHybridization() == null || domain.get(i).getHybridization().isEmpty()) {
					entity.setHybridization("Not Specified");
				}
				else {
					entity.setHybridization(domain.get(i).getHybridization());
				}
				
				if (domain.get(i).getEmbeddingKey() == null || domain.get(i).getEmbeddingKey().isEmpty()) {
					entity.setAge("embryonic day");
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);
				}
				else {
					entity.setAge(domain.get(i).getAge());
					entity.setAgeMin(-1);
					entity.setAgeMax(-1);					
				}
				
				if (domain.get(i).getAgeNote() != null && !domain.get(i).getAgeNote().isEmpty()) {
					entity.setAgeNote(domain.get(i).getAgeNote());
				}
				else {
					entity.setAgeNote(null);					
				}
				
				if (domain.get(i).getSpecimenNote() != null && !domain.get(i).getSpecimenNote().isEmpty()) {
					entity.setSpecimenNote(domain.get(i).getSpecimenNote());
				}
				else {
					entity.setSpecimenNote(null);					
				}
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				specimenDAO.persist(entity);
				
				// process gxd_insituresult
				if (domain.get(i).getSresults() != null && !domain.get(i).getSresults().isEmpty()) {
					if (insituresultService.process(entity.get_specimen_key(), domain.get(i).getSresults(), user)) {
						modified = true;
					}
				}
				
				String cmd = "select count(*) from MGI_resetAgeMinMax ('GXD_Specimen'," + entity.get_specimen_key() + ")";
				log.info("cmd: " + cmd);
				Query query = specimenDAO.createNativeQuery(cmd);
				query.getResultList();
				
				modified = true;
				log.info("processSpecimen/create processed: " + entity.get_specimen_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processSpecimen delete");
				Specimen entity = specimenDAO.get(Integer.valueOf(domain.get(i).getSpecimenKey()));
				specimenDAO.remove(entity);
				modified = true;
				log.info("processSpecimen delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processSpecimen update");

				Specimen entity = specimenDAO.get(Integer.valueOf(domain.get(i).getSpecimenKey()));
			
				entity.set_assay_key(parentKey);
				entity.setEmbeddingMethod(embeddingDAO.get(Integer.valueOf(domain.get(i).getEmbeddingKey())));
				entity.setFixationMethod(fixationDAO.get(Integer.valueOf(domain.get(i).getFixationKey())));
				entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setSpecimenLabel(domain.get(i).getSpecimenLabel());
				entity.setSex(domain.get(i).getSex());
				entity.setHybridization(domain.get(i).getHybridization());
				
				String newAge;
				if (domain.get(i).getAgeStage().isEmpty()) {
					newAge = domain.get(i).getAgePrefix();
				} else {
					newAge = domain.get(i).getAgePrefix() + " " + domain.get(i).getAgeStage();

				}
				entity.setAge(newAge);
				entity.setAgeMin(Integer.valueOf(domain.get(i).getAgeMin()));
				entity.setAgeMax(Integer.valueOf(domain.get(i).getAgeMax()));		

				if (domain.get(i).getAgeNote() != null && !domain.get(i).getAgeNote().isEmpty()) {
					entity.setAgeNote(domain.get(i).getAgeNote());
				}
				else {
					entity.setAgeNote(null);					
				}
				
				if (domain.get(i).getSpecimenNote() != null && !domain.get(i).getSpecimenNote().isEmpty()) {
					entity.setSpecimenNote(domain.get(i).getSpecimenNote());
				}
				else {
					entity.setSpecimenNote(null);					
				}
				
				entity.setModification_date(new Date());

				// process gxd_insituresult
				if (domain.get(i).getSresults() != null && !domain.get(i).getSresults().isEmpty()) {
					if (insituresultService.process(Integer.valueOf(domain.get(i).getSpecimenKey()), domain.get(i).getSresults(), user)) {
						modified = true;
					}
				}
				
				specimenDAO.update(entity);
				
				String cmd = "select count(*) from MGI_resetAgeMinMax ('GXD_Specimen'," + entity.get_specimen_key() + ")";
				log.info("cmd: " + cmd);
				Query query = specimenDAO.createNativeQuery(cmd);
				query.getResultList();
				
				modified = true;
				log.info("processSpecimen/changes processed: " + domain.get(i).getSpecimenKey());	
			}
			else {
				log.info("processSpecimen/no changes processed: " + domain.get(i).getSpecimenKey());
			}
		}
		
		log.info("processSpecimen/processing successful");
		return modified;
	}
	    
}
