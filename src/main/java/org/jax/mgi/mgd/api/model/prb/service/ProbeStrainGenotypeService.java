package org.jax.mgi.mgd.api.model.prb.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainGenotypeDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainGenotypeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainGenotype;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeStrainGenotypeTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeStrainGenotypeService extends BaseService<ProbeStrainGenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private ProbeStrainGenotypeDAO strainGenotypeDAO;
	@Inject
	private GenotypeDAO genotypeDAO;
	@Inject
	private TermDAO termDAO;
	
	private ProbeStrainGenotypeTranslator translator = new ProbeStrainGenotypeTranslator();				

	@Transactional
	public SearchResults<ProbeStrainGenotypeDomain> create(ProbeStrainGenotypeDomain domain, User user) {
		SearchResults<ProbeStrainGenotypeDomain> results = new SearchResults<ProbeStrainGenotypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeStrainGenotypeDomain> update(ProbeStrainGenotypeDomain domain, User user) {
		SearchResults<ProbeStrainGenotypeDomain> results = new SearchResults<ProbeStrainGenotypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeStrainGenotypeDomain> delete(Integer key, User user) {
		SearchResults<ProbeStrainGenotypeDomain> results = new SearchResults<ProbeStrainGenotypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeStrainGenotypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeStrainGenotypeDomain domain = new ProbeStrainGenotypeDomain();
		if (strainGenotypeDAO.get(key) != null) {
			domain = translator.translate(strainGenotypeDAO.get(key));
		}
		strainGenotypeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ProbeStrainGenotypeDomain> getResults(Integer key) {
		SearchResults<ProbeStrainGenotypeDomain> results = new SearchResults<ProbeStrainGenotypeDomain>();
		results.setItem(translator.translate(strainGenotypeDAO.get(key)));
		strainGenotypeDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, List<ProbeStrainGenotypeDomain> domain, User user) {
		// process strain/genotype associations (create, delete, update)
		
		Boolean modified = false;

		log.info("processStrainGenotype");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processStrainGenotype/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getGenotypeKey() == null || domain.get(i).getGenotypeKey().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processStrainGenotype/create");
				ProbeStrainGenotype entity = new ProbeStrainGenotype();									

				entity.set_strain_key(Integer.valueOf(parentKey));
				entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));
				
				if (domain.get(i).getQualifierKey() != null && !domain.get(i).getQualifierKey().isEmpty()) {
					entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				}
				else {
					entity.setQualifier(termDAO.get(706916));
				}
				
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());				
				strainGenotypeDAO.persist(entity);				
				log.info("processStrainGenotype/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processStrainGenotype/delete");
				if (domain.get(i).getStrainGenotypeKey() != null && !domain.get(i).getStrainGenotypeKey().isEmpty()) {
					ProbeStrainGenotype entity = strainGenotypeDAO.get(Integer.valueOf(domain.get(i).getStrainGenotypeKey()));
					strainGenotypeDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processStrainGenotype/update");
				ProbeStrainGenotype entity = strainGenotypeDAO.get(Integer.valueOf(domain.get(i).getStrainGenotypeKey()));	

				entity.set_strain_key(Integer.valueOf(parentKey));
				entity.setGenotype(genotypeDAO.get(Integer.valueOf(domain.get(i).getGenotypeKey())));

				if (domain.get(i).getQualifierKey() != null && !domain.get(i).getQualifierKey().isEmpty()) {
					entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				}
				else {
					entity.setQualifier(termDAO.get(706916));
				}
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());				strainGenotypeDAO.update(entity);
				log.info("processStrainGenotype/changes processed: " + domain.get(i).getStrainGenotypeKey());				
				modified = true;
			}
			else {
				log.info("processStrainGenotype/no changes processed: " + domain.get(i).getStrainGenotypeKey());
			}           
		}
		
		log.info("processStrainGenotype/processing successful");
		return modified;
	}
   
}
