package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeReferenceDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeReferenceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReference;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeReferenceTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeReferenceService extends BaseService<ProbeReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeReferenceDAO probeDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private AccessionService accessionService;
	@Inject
	private ProbeAliasService aliasService;

	private ProbeReferenceTranslator translator = new ProbeReferenceTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	private String mgiTypeName = "Segment";
	
	@Transactional
	public SearchResults<ProbeReferenceDomain> create(ProbeReferenceDomain domain, User user) {
		SearchResults<ProbeReferenceDomain> results = new SearchResults<ProbeReferenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeReferenceDomain> update(ProbeReferenceDomain domain, User user) {
		SearchResults<ProbeReferenceDomain> results = new SearchResults<ProbeReferenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ProbeReferenceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeReferenceDomain domain = new ProbeReferenceDomain();
		if (probeDAO.get(key) != null) {
			domain = translator.translate(probeDAO.get(key));
		}
		probeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ProbeReferenceDomain> getResults(Integer key) {
        SearchResults<ProbeReferenceDomain> results = new SearchResults<ProbeReferenceDomain>();
        results.setItem(translator.translate(probeDAO.get(key)));
        probeDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ProbeReferenceDomain> delete(Integer key, User user) {
		SearchResults<ProbeReferenceDomain> results = new SearchResults<ProbeReferenceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ProbeReferenceDomain> search(Integer key) {

		List<ProbeReferenceDomain> results = new ArrayList<ProbeReferenceDomain>();

		String cmd = "\nselect _reference_key from prb_reference where _probe_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ProbeReferenceDomain domain = new ProbeReferenceDomain();	
				domain = translator.translate(probeDAO.get(rs.getInt("_assoc_key")));
				probeDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@Transactional
	public Boolean process(String parentKey, List<ProbeReferenceDomain> domain, User user) {
		// process probe reference (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processProbeReference");
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {

			if (domain == null || domain.isEmpty()) {
				log.info("processProbeReference/nothing to process");
				return modified;
			}
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processProbeReference/create");

	        	if (domain.get(i).getRefsKey() == null || domain.get(i).getRefsKey().isEmpty()) {
	        		return modified;
	        	}
	        	
				ProbeReference entity = new ProbeReference();									
				entity.set_probe_key(Integer.valueOf(parentKey));
				entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				entity.setHasRmap(Integer.valueOf(domain.get(i).getHasRmap()));
				entity.setHasSequence(Integer.valueOf(domain.get(i).getHasSequence()));
				entity.setHasRmap(0);
				entity.setHasSequence(0);				
				entity.setCreatedBy(user);
				entity.setModifiedBy(user);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());				
				probeDAO.persist(entity);				

				// process accession ids
				// set the refsKey in the accession/reference domain
				String refsKey = domain.get(i).getRefsKey();
				for (int j = 0; i < domain.get(i).getAccessionIds().size(); j++) {
					domain.get(i).getAccessionIds().get(j).getReferences().get(0).setRefsKey(refsKey);
				}
				if (accessionService.process(String.valueOf(entity.get_probe_key()), domain.get(i).getAccessionIds(), mgiTypeName, user)) {
					modified = true;
				}
				
				// process alias
				if (aliasService.process(String.valueOf(entity.get_reference_key()), domain.get(i).getAliases(), user)) {
					modified = true;
				}
				
				log.info("processProbeReference/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processProbeReference/delete");
				if (domain.get(i).getReferenceKey() != null && !domain.get(i).getReferenceKey().isEmpty()) {
					ProbeReference entity = probeDAO.get(Integer.valueOf(domain.get(i).getReferenceKey()));
					probeDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processProbeReference/update");
				ProbeReference entity = probeDAO.get(Integer.valueOf(domain.get(i).getReferenceKey()));	
				entity.set_probe_key(Integer.valueOf(parentKey));
				entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				entity.setHasRmap(Integer.valueOf(domain.get(i).getHasRmap()));
				entity.setHasSequence(Integer.valueOf(domain.get(i).getHasSequence()));
				entity.setHasRmap(0);
				entity.setHasSequence(0);
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());

				// process accession ids
				if (accessionService.process(domain.get(i).getProbeKey(), domain.get(i).getAccessionIds(), mgiTypeName, user)) {
					modified = true;
				}
				
				// process alias
				if (aliasService.process(domain.get(i).getReferenceKey(), domain.get(i).getAliases(), user)) {
					modified = true;
				}
				
				probeDAO.update(entity);

				log.info("processProbeReference/changes processed: " + domain.get(i).getReferenceKey());				
				modified = true;
			}
			else {
				log.info("processProbeReference/no changes processed: " + domain.get(i).getReferenceKey());
			}           
		}
		
		log.info("processProbeReference/processing successful");
		return modified;
	}
	
}
