package org.jax.mgi.mgd.api.model.voc.service;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.voc.dao.EvidenceDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;
import org.jax.mgi.mgd.api.model.voc.translator.EvidenceTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class EvidenceService extends BaseService<EvidenceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private EvidenceDAO evidenceDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private EvidencePropertyService propertyService;
	@Inject
	private NoteService noteService;
	
	private EvidenceTranslator translator = new EvidenceTranslator();				

	@Transactional
	public SearchResults<EvidenceDomain> create(EvidenceDomain object, User user) {
		SearchResults<EvidenceDomain> results = new SearchResults<EvidenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<EvidenceDomain> update(EvidenceDomain object, User user) {
		SearchResults<EvidenceDomain> results = new SearchResults<EvidenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<EvidenceDomain> delete(Integer key, User user) {
		SearchResults<EvidenceDomain> results = new SearchResults<EvidenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public EvidenceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		EvidenceDomain domain = new EvidenceDomain();
		if (evidenceDAO.get(key) != null) {
			domain = translator.translate(evidenceDAO.get(key));
		}
		evidenceDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<EvidenceDomain> getResults(Integer key) {
		SearchResults<EvidenceDomain> results = new SearchResults<EvidenceDomain>();
		results.setItem(translator.translate(evidenceDAO.get(key)));
		evidenceDAO.clear();
		return results;
    }
	
	@Transactional
	public Boolean process(String parentKey, List<EvidenceDomain> domain, String annotTypeKey, String mgiTypeKey, User user) {
		// process evidence associations (create, delete, update)
		// note that the processStatus for the evidence 
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processEvidence/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
	
				// if evidence is null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getEvidenceTermKey() == null) {
					continue;
				}
				
				log.info("processEvidence create");

				Evidence entity = new Evidence();
				entity.set_annot_key(Integer.valueOf(parentKey));
				String evidenceTermKey = domain.get(i).getEvidenceTermKey();

				// for MP annotations only, set default evidence to "inferred from experiment"
				if (annotTypeKey.equals("1002")) {
					if (evidenceTermKey ==  null || evidenceTermKey.isEmpty()) {
						log.info("setting default genotype/MP evidence to 'EXP'");
						evidenceTermKey = "52280";
					}
				}
				else if (annotTypeKey.equals("1020") ) {
					if (evidenceTermKey ==  null || evidenceTermKey.isEmpty()) {
						log.info("setting default genotype/DO evidence to 'TAS'" );
						evidenceTermKey = "847168";
					}
				}
				else if (annotTypeKey.equals("1021") ) {
					if (evidenceTermKey ==  null || evidenceTermKey.isEmpty()) {
						log.info("setting default allele/DO evidence to 'TAS'" );
						evidenceTermKey = "8068251";
					}
				}
				else {
					evidenceTermKey = domain.get(i).getEvidenceTermKey();
				}

				entity.setEvidenceTerm(termDAO.get(Integer.valueOf(evidenceTermKey)));
				entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				entity.setInferredFrom(domain.get(i).getInferredFrom());
				entity.setCreatedBy(user);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				entity.setModifiedBy(user);
				
				evidenceDAO.persist(entity);			

				// evidence property
				if (propertyService.process(String.valueOf(entity.get_annotevidence_key()), domain.get(i).getProperties(), annotTypeKey,  user)) 
				{
					log.info("processEvidence/properties successful");					
					modified = true;
				}
				
				// evidence notes
				if (noteService.processAll(String.valueOf(entity.get_annotevidence_key()), domain.get(i).getAllNotes(), mgiTypeKey,  user)) 
				{
					log.info("processEvidence/notes successful");					
					modified = true;
				}
				
				log.info("processEvidence create");
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processEvidence delete");
				Evidence entity = evidenceDAO.get(Integer.valueOf(domain.get(i).getAnnotEvidenceKey()));
				evidenceDAO.remove(entity);
				modified = true;
				log.info("processEvidence delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processEvidence update");

				Boolean isUpdated = false;
				Evidence entity = evidenceDAO.get(Integer.valueOf(domain.get(i).getAnnotEvidenceKey()));
		
				if (!String.valueOf(entity.getEvidenceTerm().get_term_key()).equals(domain.get(i).getEvidenceTermKey())) {
					entity.setEvidenceTerm(termDAO.get(Integer.valueOf(domain.get(i).getEvidenceTermKey())));
					isUpdated = true;
				}
			
				if (!String.valueOf(entity.getReference().get_refs_key()).equals(domain.get(i).getRefsKey())) {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
					modified = true;
				}
			
				if (entity.getInferredFrom() != null && domain.get(i).getInferredFrom() != null) {
					if (!entity.getInferredFrom().equals(domain.get(i).getInferredFrom())) {
						entity.setInferredFrom(domain.get(i).getInferredFrom());
						modified = true;
					}
				}

				// evidence property
				if (propertyService.process(String.valueOf(entity.get_annotevidence_key()), domain.get(i).getProperties(), annotTypeKey,  user)) 
				{
					log.info("processEvidence/properties" + domain.get(i).getAnnotEvidenceKey());					
					modified = true;
				}
				
				// evidence notes
				log.info("processing annotation notes");
				if (noteService.processAll(String.valueOf(entity.get_annotevidence_key()), domain.get(i).getAllNotes(), mgiTypeKey,  user)) 
				{
					log.info("processEvidence/notes" + domain.get(i).getAnnotEvidenceKey());					
					isUpdated = true;
				}
				
				if (isUpdated) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					evidenceDAO.update(entity);
					modified = true;
					log.info("processEvidence/changes processed: " + domain.get(i).getAnnotEvidenceKey());
				}
				else {
					log.info("processEvidence/no changes processed: " + domain.get(i).getAnnotEvidenceKey());
				}
			}
			else {
				log.info("processEvidence/no changes processed: " + domain.get(i).getAnnotEvidenceKey());
			}
		}
		
		log.info("processEvidence/processing successful");
		return modified;
	}
	
}
