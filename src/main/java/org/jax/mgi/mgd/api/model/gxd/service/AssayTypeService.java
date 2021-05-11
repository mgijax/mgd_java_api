package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayTypeDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AssayType;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AssayTypeService extends BaseService<AssayTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AssayTypeDAO assayTypeDAO;
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AssayTypeDomain> create(AssayTypeDomain domain, User user) {
		SearchResults<AssayTypeDomain> results = new SearchResults<AssayTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AssayTypeDomain> update(AssayTypeDomain domain, User user) {
		SearchResults<AssayTypeDomain> results = new SearchResults<AssayTypeDomain>();

		List<TermDomain> termdomain = domain.getTerms();
				
		log.info("processAssayType/update");
				
		// iterate thru the list of domains
		// for each domain, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < termdomain.size(); i++) {

			if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processAssayType/create");

				if (termdomain.get(i).getTerm() == null || termdomain.get(i).getTerm().isEmpty()) {
					log.info("processAssayType/nothing to create");
					continue;
				}
				
				AssayType entity = new AssayType();	
				entity.setAssayType(termdomain.get(i).getTerm());
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				assayTypeDAO.persist(entity);				
				log.info("processAssayType/create processed");												
			}
			
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAssayType delete");
				AssayType entity = assayTypeDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				assayTypeDAO.remove(entity);
				log.info("processAssayType/delete processed");
			} 
			else if (termdomain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAssayType update");
				AssayType entity = assayTypeDAO.get(Integer.valueOf(termdomain.get(i).getTermKey()));
				entity.setAssayType(termdomain.get(i).getTerm());
				entity.setModification_date(new Date());
				assayTypeDAO.update(entity);
				log.info("processAssayType/changes processed: " + termdomain.get(i).getTermKey());								
			}
			else {
				log.info("processAssayType/no changes processed: " + termdomain.get(i).getTermKey());
			} 
		}
			
		log.info("processAssayType/update/returning results");
		results.setItems(search(domain));
		log.info("processAssayType/update/returned results succsssful");
		
		return results;
	}

	@Transactional
	public SearchResults<AssayTypeDomain> delete(Integer key, User user) {
		SearchResults<AssayTypeDomain> results = new SearchResults<AssayTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public AssayTypeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AssayTypeDomain domain = new AssayTypeDomain();
		// do not implement
		return domain;
	}

    @Transactional
    public SearchResults<AssayTypeDomain> getResults(Integer key) {
        SearchResults<AssayTypeDomain> results = new SearchResults<AssayTypeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
        return results;
    } 
	
	@Transactional
	public List<AssayTypeDomain> search(AssayTypeDomain searchDomain) {
		// return AssayTypeDomain which looks like a vocab/term domain

		List<AssayTypeDomain> results = new ArrayList<AssayTypeDomain>();
		AssayTypeDomain adomain = new AssayTypeDomain();
		List<TermDomain> termresults = new ArrayList<TermDomain>();
		Integer sequenceNum = 1;
		
		String cmd = "select * from gxd_assaytype order by assaytype";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				TermDomain tdomain = new TermDomain();
				tdomain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				tdomain.setVocabKey(adomain.getVocabKey());
				tdomain.setTermKey(rs.getString("_assaytype_key"));
				tdomain.setTerm(rs.getString("assaytype"));
				tdomain.setIsObsolete("0");
				tdomain.setSequenceNum(String.valueOf(sequenceNum));
				termresults.add(tdomain);
				sequenceNum = sequenceNum + 1;
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		adomain.setTerms(termresults);
		results.add(adomain);
		
		return results;
	}
}
