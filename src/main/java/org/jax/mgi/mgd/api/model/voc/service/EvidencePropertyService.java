package org.jax.mgi.mgd.api.model.voc.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.dao.EvidencePropertyDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.EvidenceProperty;
import org.jax.mgi.mgd.api.model.voc.translator.EvidencePropertyTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class EvidencePropertyService extends BaseService<EvidencePropertyDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private EvidencePropertyDAO propertyDAO;
	@Inject
	private TermDAO termDAO;
	
	private EvidencePropertyTranslator translator = new EvidencePropertyTranslator();				

	@Transactional
	public SearchResults<EvidencePropertyDomain> create(EvidencePropertyDomain object, User user) {
		SearchResults<EvidencePropertyDomain> results = new SearchResults<EvidencePropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<EvidencePropertyDomain> update(EvidencePropertyDomain object, User user) {
		SearchResults<EvidencePropertyDomain> results = new SearchResults<EvidencePropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<EvidencePropertyDomain> delete(Integer key, User user) {
		SearchResults<EvidencePropertyDomain> results = new SearchResults<EvidencePropertyDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public EvidencePropertyDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		EvidencePropertyDomain domain = new EvidencePropertyDomain();
		if (propertyDAO.get(key) != null) {
			domain = translator.translate(propertyDAO.get(key));
		}
		propertyDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<EvidencePropertyDomain> getResults(Integer key) {
		SearchResults<EvidencePropertyDomain> results = new SearchResults<EvidencePropertyDomain>();
		results.setItem(translator.translate(propertyDAO.get(key)));
		propertyDAO.clear();
		return results;
    }
	
	@Transactional
	public Boolean process(String parentKey, List<EvidencePropertyDomain> domain, String annotTypeKey, User user) {
		// process evidence property associations (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processProperty/nothing to process");
			return modified;
		}

		// for MP annotations only, set default sex-specificity to "NA"
		// The property stanza and the sequenceNum will always be 1 
		if (annotTypeKey.equals("1002")) {

			if (domain.isEmpty()) {
				domain.get(0).setAnnotevidenceKey(parentKey);
				domain.get(0).setValue("NA");
				domain.get(0).setPropertyTermKey("8836535");				
			}
			
			// else use domain.get(0).getValue()
			
			domain.get(0).setSequenceNum(Integer.valueOf(1));
			domain.get(0).setStanza(Integer.valueOf(1));			
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if evidence is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getEvidencePropertyKey() == null) {
					continue;
				}
				
				log.info("processProperty create");
				
				EvidenceProperty entity = new EvidenceProperty();
				entity.set_annotEvidence_key(Integer.valueOf(parentKey));
				entity.setPropertyTerm(termDAO.get(Integer.valueOf(domain.get(i).getPropertyTermKey())));
				entity.setValue(domain.get(i).getValue());
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setStanza(domain.get(i).getStanza());
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				
				propertyDAO.persist(entity);			
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processProperty delete");
				EvidenceProperty entity = propertyDAO.get(Integer.valueOf(domain.get(i).getEvidencePropertyKey()));
				propertyDAO.remove(entity);
				modified = true;
				log.info("processProperty delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processProperty update");

				Boolean isUpdated = false;
				EvidenceProperty entity = propertyDAO.get(Integer.valueOf(domain.get(i).getEvidencePropertyKey()));
		
				if (!String.valueOf(entity.getPropertyTerm().get_term_key()).equals(domain.get(i).getPropertyTermKey())) {
					entity.setPropertyTerm(termDAO.get(Integer.valueOf(domain.get(i).getPropertyTermKey())));
					isUpdated = true;
				}

				if (entity.getStanza() != null && domain.get(i).getStanza() != null) {
					if (!entity.getStanza().equals(domain.get(i).getStanza())) {
						entity.setStanza(domain.get(i).getStanza());
						modified = true;
					}
				}
				
				if (entity.getValue() != null && domain.get(i).getValue() != null) {
					if (!entity.getValue().equals(domain.get(i).getValue())) {
						entity.setValue(domain.get(i).getValue());
						modified = true;
					}
				}
				
				if (isUpdated) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					propertyDAO.update(entity);
					modified = true;
					log.info("processProperty/changes processed: " + domain.get(i).getEvidencePropertyKey());
				}
				else {
					log.info("processProperty/no changes processed: " + domain.get(i).getEvidencePropertyKey());
				}
			}
			else {
				log.info("processProperty/no changes processed: " + domain.get(i).getEvidencePropertyKey());
			}
		}
		
		log.info("processProperty/processing successful");
		return modified;
	}
	
}
