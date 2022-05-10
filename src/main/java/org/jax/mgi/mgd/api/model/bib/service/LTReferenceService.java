package org.jax.mgi.mgd.api.model.bib.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.exception.FatalAPIException;
import org.jax.mgi.mgd.api.exception.NonFatalAPIException;
import org.jax.mgi.mgd.api.model.bib.dao.LTReferenceDAO;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.repository.LTReferenceRepository;
import org.jax.mgi.mgd.api.model.bib.translator.LTReferenceTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class LTReferenceService {

	@Inject
	private LTReferenceDAO referenceDAO;
	@Inject
	private LTReferenceRepository repo;	
	
	LTReferenceTranslator translator = new LTReferenceTranslator();

	private Logger log = Logger.getLogger(getClass());
	
	private static int maxRetries = 10;		// maximum number of retries for non-fatal exceptions on update operations
	private static int retryDelay = 200;	// number of ms to wait before retrying update operation after non-fatal exception

	public SearchResults<LTReferenceDomain> getReference(String refsKey) throws APIException {
		SearchResults<LTReferenceDomain> results = new SearchResults<LTReferenceDomain>();
		results.setItem(translator.translate(referenceDAO.get(Integer.valueOf(refsKey))));
		referenceDAO.clear();
		return results;
	}
	
	/* Update the reference entity corresponding to the given domain object (and updates citation cache).  Returns
	 * domain object if successful or throws APIException if not.
	 */
	@Transactional
	public LTReferenceDomain updateReference(LTReferenceDomain domain, User user) throws FatalAPIException, NonFatalAPIException, APIException {
		log.info("LTReferenceService:updateReference()");
				
		LTReference entity = referenceDAO.get(Integer.valueOf(domain.getRefsKey()));
		repo.applyChanges(entity, domain, user);
		referenceDAO.persist(entity);				

		Query query = referenceDAO.createNativeQuery("select count(*) from BIB_reloadCache(" + domain.getRefsKey() + ")");
		query.getResultList();
		
		return repo.get(domain.refsKey);
	}

	/* returns true if references were updated, false if not; does not update citation cache, as
	 * only workflow tags are processed currently
	 */
	@Transactional
	public void updateReferencesInBulk(List<String> listOfRefsKey, String workflowTag, String workflow_tag_operation, User user) throws APIException {
		log.info("updateReferenceInBulk()");
		
		// if no references or no tags, just bail out as a no-op
		if ((listOfRefsKey == null) || (listOfRefsKey.size() == 0) || (workflowTag == null) || (workflowTag.length() == 0)) {
			return; 
		}

		// if no workflow tag operation is specified, default to 'add'
		if ((workflow_tag_operation == null) || workflow_tag_operation.equals("")) {
			workflow_tag_operation = Constants.OP_ADD_WORKFLOW;
		} else if (!workflow_tag_operation.equals(Constants.OP_ADD_WORKFLOW) && !workflow_tag_operation.equals(Constants.OP_REMOVE_WORKFLOW)) {
			throw new FatalAPIException("Invalid workflow_tag_operation: " + workflow_tag_operation);
		}

		/* for each reference:
			* 1. if the operation fails in a fatal manner, rethrow that exception immediately.
			* 2. if the operation fails in a non-fatal manner, wait briefly and try again up to maxRetries times.
		*/
		for (String refsKey : listOfRefsKey) {
			LTReference entity = referenceDAO.get(Integer.valueOf(refsKey));
			
			if (entity== null) {
				throw new FatalAPIException("Unknown reference key: " + refsKey);				
			}
			
			int retries = 0;
			boolean succeeded = false;

			while (!succeeded) {
				try {
					if (workflow_tag_operation.equals(Constants.OP_ADD_WORKFLOW)) {
						repo.addTag(entity, workflowTag, user);
					} else {
						repo.removeTag(entity, workflowTag, user);
					}
					succeeded = true;
				} catch (FatalAPIException fe) {
					throw fe;
				} catch (APIException ae) {
					retries++;
					if (retries > maxRetries) {
						throw ae;
					}
					try {
						Thread.sleep(retryDelay);
						log.info("UpdateBulk: Retry #" + retries + " for " + entity.getMgiid());
					} catch (InterruptedException ie) {
						throw new FatalAPIException("Operation cancelled - system interrupted");
					}
				}
			}
		}
	}

}
