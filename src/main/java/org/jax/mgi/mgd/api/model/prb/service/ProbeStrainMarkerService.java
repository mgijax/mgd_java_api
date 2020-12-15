package org.jax.mgi.mgd.api.model.prb.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainMarkerDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainMarker;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeStrainMarkerTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeStrainMarkerService extends BaseService<ProbeStrainMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private ProbeStrainMarkerDAO strainMarkerDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private AlleleDAO alleleDAO;
	@Inject
	private TermDAO termDAO;
	
	private ProbeStrainMarkerTranslator translator = new ProbeStrainMarkerTranslator();				

	@Transactional
	public SearchResults<ProbeStrainMarkerDomain> create(ProbeStrainMarkerDomain domain, User user) {
		SearchResults<ProbeStrainMarkerDomain> results = new SearchResults<ProbeStrainMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeStrainMarkerDomain> update(ProbeStrainMarkerDomain domain, User user) {
		SearchResults<ProbeStrainMarkerDomain> results = new SearchResults<ProbeStrainMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeStrainMarkerDomain> delete(Integer key, User user) {
		SearchResults<ProbeStrainMarkerDomain> results = new SearchResults<ProbeStrainMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeStrainMarkerDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeStrainMarkerDomain domain = new ProbeStrainMarkerDomain();
		if (strainMarkerDAO.get(key) != null) {
			domain = translator.translate(strainMarkerDAO.get(key));
		}
		strainMarkerDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ProbeStrainMarkerDomain> getResults(Integer key) {
		SearchResults<ProbeStrainMarkerDomain> results = new SearchResults<ProbeStrainMarkerDomain>();
		results.setItem(translator.translate(strainMarkerDAO.get(key)));
		strainMarkerDAO.clear();
		return results;
    }

	@Transactional
	public Boolean process(String parentKey, List<ProbeStrainMarkerDomain> domain, User user) {
		// process strain/marker associations (create, delete, update)
		
		Boolean modified = false;

		log.info("processStrainMarker");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processStrainMarker/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getMarkerKey() == null || domain.get(i).getMarkerKey().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processStrainMarker/create");
				ProbeStrainMarker entity = new ProbeStrainMarker();									
				entity.set_strain_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
				
				if (domain.get(i).getAlleleKey() != null && !domain.get(i).getAlleleKey().isEmpty()) {
					entity.setAllele(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey())));
				}
				else {
					entity.setAllele(null);
				}
				
				if (domain.get(i).getQualifierKey() != null && !domain.get(i).getQualifierKey().isEmpty()) {
					entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				}
				else {
					entity.setQualifier(termDAO.get(615427));
				}
				
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				
				strainMarkerDAO.persist(entity);				
				log.info("processStrainMarker/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processStrainMarker/delete");
				if (domain.get(i).getStrainMarkerKey() != null && !domain.get(i).getStrainMarkerKey().isEmpty()) {
					ProbeStrainMarker entity = strainMarkerDAO.get(Integer.valueOf(domain.get(i).getStrainMarkerKey()));
					strainMarkerDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processStrainMarker/update");
				ProbeStrainMarker entity = strainMarkerDAO.get(Integer.valueOf(domain.get(i).getStrainMarkerKey()));	
				entity.set_strain_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));

				if (domain.get(i).getAlleleKey() != null && !domain.get(i).getAlleleKey().isEmpty()) {
					entity.setAllele(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey())));
				}
				else {
					entity.setAllele(null);
				}
				
				if (domain.get(i).getQualifierKey() != null && !domain.get(i).getQualifierKey().isEmpty()) {
					entity.setQualifier(termDAO.get(Integer.valueOf(domain.get(i).getQualifierKey())));
				}
				else {
					entity.setQualifier(termDAO.get(615427));
				}
				
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				
				strainMarkerDAO.update(entity);
				log.info("processStrainMarker/changes processed: " + domain.get(i).getStrainMarkerKey());				
				modified = true;
			}
			else {
				log.info("processStrainMarker/no changes processed: " + domain.get(i).getStrainMarkerKey());
			}           
		}
		
		log.info("processStrainMarker/processing successful");
		return modified;
	}
   
}
