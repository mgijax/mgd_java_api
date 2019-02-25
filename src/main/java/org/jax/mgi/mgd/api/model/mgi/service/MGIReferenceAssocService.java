package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.MGIReferenceAssocDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class MGIReferenceAssocService extends BaseService<MGIReferenceAssocDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private MGIReferenceAssocDAO referenceAssocDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	
	private MGIReferenceAssocTranslator translator = new MGIReferenceAssocTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> create(MGIReferenceAssocDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> update(MGIReferenceAssocDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MGIReferenceAssocDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGIReferenceAssocDomain> getResults(Integer key) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();
		results.setItem(translator.translate(referenceAssocDAO.get(key)));
		return results;
	}
    
	@Transactional
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional	
	public List<MGIReferenceAssocDomain> getByMarker(Integer key) {

		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();

		String cmd = "\nselect _assoc_key from mgi_reference_marker_view where _object_key = " + key;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
				domain = translator.translate(referenceAssocDAO.get(rs.getInt("_assoc_key")),1);
				referenceAssocDAO.clear();
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
	public Boolean process(String parentKey, List<MGIReferenceAssocDomain> domain, String mgiTypeKey, User user) {
		// process reference associations (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processReferenceAssoc/nothing to process");
			return modified;
		}
				
		String cmd = "";
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				// minimum domain info for create:
				// processStatus (‘c’ for create) , mgiTypeKey, parentKey/objectKey, refsKey, refType (string)
				
				log.info("processReferenceAssoc create");

				cmd = "select count(*) from MGI_insertReferenceAssoc ("
							+ user.get_user_key().intValue()
							+ "," + mgiTypeKey
							+ "," + parentKey
							+ "," + domain.get(i).getRefsKey()
							+ ",'" + domain.get(i).getRefAssocType() + "'"
							+ ")";
				log.info("cmd: " + cmd);
				Query query = referenceAssocDAO.createNativeQuery(cmd);
				query.getResultList();
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processReferenceAssoc delete");
				MGIReferenceAssoc entity = referenceAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
				referenceAssocDAO.remove(entity);
				log.info("processReferenceAssoc delete successful");
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processReferenceAssoc update");

				Boolean isUpdated = false;
				MGIReferenceAssoc entity = referenceAssocDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
		
				if (entity.getReference().get_refs_key() != Integer.parseInt(domain.get(i).getRefsKey())) {
					entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
					isUpdated = true;
				}
				
				if (isUpdated) {
					entity.setModification_date(new Date());
					entity.setModifiedBy(user);
					referenceAssocDAO.update(entity);
					modified = true;
					log.info("processReferenceAssoc/changes processed: " + domain.get(i).getAssocKey());
				}
				else {
					log.info("processReferenceAssoc/no changes processed: " + domain.get(i).getAssocKey());
				}
			}
			else {
				log.info("processReferenceAssoc/no changes processed: " + domain.get(i).getAssocKey());
			}
		}
		
		log.info("processReferenceAssoc/processing successful");
		return modified;
	}
	
}
