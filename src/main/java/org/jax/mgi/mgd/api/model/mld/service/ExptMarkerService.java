package org.jax.mgi.mgd.api.model.mld.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.ExptMarkerDAO;
import org.jax.mgi.mgd.api.model.mld.dao.MappingAssayTypeDAO;
import org.jax.mgi.mgd.api.model.mld.domain.ExptMarkerDomain;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;
import org.jax.mgi.mgd.api.model.mld.translator.ExptMarkerTranslator;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ExptMarkerService extends BaseService<ExptMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ExptMarkerDAO exptMarkerDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private AlleleDAO alleleDAO;
	@Inject
	private MappingAssayTypeDAO assayTypeDAO;
	
	private ExptMarkerTranslator translator = new ExptMarkerTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ExptMarkerDomain> create(ExptMarkerDomain domain, User user) {
		SearchResults<ExptMarkerDomain> results = new SearchResults<ExptMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ExptMarkerDomain> update(ExptMarkerDomain domain, User user) {
		SearchResults<ExptMarkerDomain> results = new SearchResults<ExptMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ExptMarkerDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ExptMarkerDomain domain = new ExptMarkerDomain();
		if (exptMarkerDAO.get(key) != null) {
			domain = translator.translate(exptMarkerDAO.get(key));
		}
		exptMarkerDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ExptMarkerDomain> getResults(Integer key) {
        SearchResults<ExptMarkerDomain> results = new SearchResults<ExptMarkerDomain>();
        results.setItem(translator.translate(exptMarkerDAO.get(key)));
        exptMarkerDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ExptMarkerDomain> delete(Integer key, User user) {
		SearchResults<ExptMarkerDomain> results = new SearchResults<ExptMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ExptMarkerDomain> search(Integer key) {

		List<ExptMarkerDomain> results = new ArrayList<ExptMarkerDomain>();

		String cmd = "\nselect _refs_key from mld_expt_notes where _expt_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ExptMarkerDomain domain = new ExptMarkerDomain();	
				domain = translator.translate(exptMarkerDAO.get(rs.getInt("_expt_key")));
				exptMarkerDAO.clear();
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
	public Boolean process(String parentKey, List<ExptMarkerDomain> domain, User user) {
		// process marker (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processExptMarker");

		if (domain == null || domain.isEmpty()) {
			log.info("processExptMarker/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getMarkerKey() == null || domain.get(i).getMarkerKey().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processExptMarker/create");
				ExptMarker entity = new ExptMarker();									
				entity.set_expt_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
				entity.setAssayType(assayTypeDAO.get(Integer.valueOf(domain.get(i).getAssayTypeKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setMatrixData(0);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());				

				// default = visual phenotype (3)
				if (domain.get(i).getAssayTypeKey() != null && !domain.get(i).getAssayTypeKey().isEmpty()) {
					entity.setAssayType(assayTypeDAO.get(Integer.valueOf(domain.get(i).getAssayTypeKey())));
				}
				else {
					entity.setAssayType(assayTypeDAO.get(3));
				}
				
				if (domain.get(i).getAlleleKey() != null && !domain.get(i).getAlleleKey().isEmpty()) {
					entity.setAllele(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey())));
				}
				else {
					entity.setAllele(null);
				}
				
				if (domain.get(i).getDescription() != null && !domain.get(i).getDescription().isEmpty()) {
					entity.setDescription(domain.get(i).getDescription());
				}
				else {
					entity.setDescription(null);
				}
				
				exptMarkerDAO.persist(entity);				
				log.info("processExptMarker/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processExptMarker/delete");
				if (domain.get(i).getAssocKey() != null && !domain.get(i).getAssocKey().isEmpty()) {
					ExptMarker entity = exptMarkerDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
					exptMarkerDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processExptMarker/update");
				ExptMarker entity = exptMarkerDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));	
				entity.set_expt_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
				entity.setAssayType(assayTypeDAO.get(Integer.valueOf(domain.get(i).getAssayTypeKey())));
				entity.setSequenceNum(domain.get(i).getSequenceNum());
				entity.setMatrixData(0);
				entity.setModification_date(new Date());

				if (domain.get(i).getAlleleKey() != null && !domain.get(i).getAlleleKey().isEmpty()) {
					entity.setAllele(alleleDAO.get(Integer.valueOf(domain.get(i).getAlleleKey())));
				}
				else {
					entity.setAllele(null);
				}
				
				if (domain.get(i).getDescription() != null && !domain.get(i).getDescription().isEmpty()) {
					entity.setDescription(domain.get(i).getDescription());
				}
				else {
					entity.setDescription(null);
				}
				
				exptMarkerDAO.update(entity);
				log.info("processExptMarker/changes processed: " + domain.get(i).getAssocKey());				
				modified = true;
			}
			else {
				log.info("processExptMarker/no changes processed: " + domain.get(i).getAssocKey());
			}           
		}
		
		log.info("processExptMarker/processing successful");
		return modified;
	}		
	
	
}
