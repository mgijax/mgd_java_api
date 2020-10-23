package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismMGITypeDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismMGITypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.OrganismMGIType;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.OrganismMGITypeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class OrganismMGITypeService extends BaseService<OrganismMGITypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private OrganismMGITypeDAO organismMGITypeDAO;
	@Inject
	private MGITypeDAO mgiTypeDAO;

	private OrganismMGITypeTranslator translator = new OrganismMGITypeTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<OrganismMGITypeDomain> create(OrganismMGITypeDomain domain, User user) {
		SearchResults<OrganismMGITypeDomain> results = new SearchResults<OrganismMGITypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<OrganismMGITypeDomain> update(OrganismMGITypeDomain domain, User user) {
		SearchResults<OrganismMGITypeDomain> results = new SearchResults<OrganismMGITypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public OrganismMGITypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		OrganismMGITypeDomain domain = new OrganismMGITypeDomain();
		if (organismMGITypeDAO.get(key) != null) {
			domain = translator.translate(organismMGITypeDAO.get(key));
		}
		organismMGITypeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<OrganismMGITypeDomain> getResults(Integer key) {
        SearchResults<OrganismMGITypeDomain> results = new SearchResults<OrganismMGITypeDomain>();
        results.setItem(translator.translate(organismMGITypeDAO.get(key)));
        organismMGITypeDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<OrganismMGITypeDomain> delete(Integer key, User user) {
		SearchResults<OrganismMGITypeDomain> results = new SearchResults<OrganismMGITypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<OrganismMGITypeDomain> search(Integer key) {

		List<OrganismMGITypeDomain> results = new ArrayList<OrganismMGITypeDomain>();

		String cmd = "\nselect _assoc_key from prb_marker where _probe_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				OrganismMGITypeDomain domain = new OrganismMGITypeDomain();	
				domain = translator.translate(organismMGITypeDAO.get(rs.getInt("_assoc_key")));
				organismMGITypeDAO.clear();
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
	public Boolean process(String parentKey, List<OrganismMGITypeDomain> domain, User user) {
		// process organism/mgi type (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processOrganismMGIType");

		if (domain == null || domain.isEmpty()) {
			log.info("processOrganismMGIType/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getMgiTypeKey() == null || domain.get(i).getMgiTypeKey().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processOrganismMGIType/create");
				OrganismMGIType entity = new OrganismMGIType();									
				entity.set_organism_key(Integer.valueOf(parentKey));
				entity.setMgiType(mgiTypeDAO.get(Integer.valueOf(domain.get(i).getMgiTypeKey())));	
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));
				entity.setCreatedBy(user);
				entity.setModifiedBy(user);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());				
				organismMGITypeDAO.persist(entity);				
				log.info("processOrganismMGIType/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processOrganismMGIType/delete");
				if (domain.get(i).getAssocKey() != null && !domain.get(i).getAssocKey().isEmpty()) {
					OrganismMGIType entity = organismMGITypeDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
					organismMGITypeDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processOrganismMGIType/update");
				OrganismMGIType entity = organismMGITypeDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));	
				entity.set_organism_key(Integer.valueOf(parentKey));
				entity.setMgiType(mgiTypeDAO.get(Integer.valueOf(domain.get(i).getMgiTypeKey())));	
				entity.setSequenceNum(Integer.valueOf(domain.get(i).getSequenceNum()));	
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				organismMGITypeDAO.update(entity);
				log.info("processOrganismMGIType/changes processed: " + domain.get(i).getAssocKey());				
				modified = true;
			}
			else {
				log.info("processOrganismMGIType/no changes processed: " + domain.get(i).getAssocKey());
			}           
		}
		
		log.info("processOrganismMGIType/processing successful");
		return modified;
	}
	
}
