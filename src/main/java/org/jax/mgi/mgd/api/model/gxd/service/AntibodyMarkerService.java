package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyMarkerDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyMarkerDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyMarker;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyMarkerTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyMarkerService extends BaseService<AntibodyMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyMarkerDAO antibodyMarkerDAO;
	@Inject
	private MarkerDAO markerDAO;
	
	private AntibodyMarkerTranslator translator = new AntibodyMarkerTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<AntibodyMarkerDomain> create(AntibodyMarkerDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key

		SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
		
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AntibodyMarkerDomain> update(AntibodyMarkerDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AntibodyMarkerDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public AntibodyMarkerDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyMarkerDomain domain = new AntibodyMarkerDomain();
		if (antibodyMarkerDAO.get(key) != null) {
			domain = translator.translate(antibodyMarkerDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyMarkerDomain> getResults(Integer key) {
        SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
        results.setItem(translator.translate(antibodyMarkerDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyMarkerDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyMarkerDomain> results = new SearchResults<AntibodyMarkerDomain>();
		String cmd = "select count(*) as objectCount from gxd_antibodymarker";
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.total_count = rs.getInt("objectCount");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
	
	@Transactional
	public List<AntibodyMarkerDomain> search(AntibodyMarkerDomain searchDomain) {

		List<AntibodyMarkerDomain> results = new ArrayList<AntibodyMarkerDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_AntibodyMarker a";
		String where = "where a._AntibodyMarker_key is not null";
		String orderBy = "order by a._Antibody_key";
		
		//
		// IN PROGRESS, minimal search for now
		//
		
		// if parameter exists, then add to where-clause
		// antibodyName
		if(searchDomain.getMarkerKey() != null && ! searchDomain.getMarkerKey().isEmpty()) {
			where = where + "\n and a._marker_key =  '" + searchDomain.getMarkerKey() + "'";
		}
		
		// create/mod date (no 'by')
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", null, null, searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AntibodyMarkerDomain domain = new AntibodyMarkerDomain();
				domain = translator.translate(antibodyMarkerDAO.get(rs.getInt("_AntibodyMarker_key")));				
				antibodyMarkerDAO.clear();
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
	public Boolean process(String parentKey, List<AntibodyMarkerDomain> domain, User user) {
		// process antibody markers (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null || domain.isEmpty()) {
			log.info("processAntibodyMarker/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
				
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				//IN PROGRESS _ START HERE.
				// if markerKey null/empty, then skip
				// pwi has sent a "c" that is empty/not being used
				if (domain.get(i).getMarkerKey() == null || domain.get(i).getMarkerKey().isEmpty()) {
					continue;
				}
				
				log.info("processAntibodyMarker create");

				AntibodyMarker entity = new AntibodyMarker();

				entity.set_antibody_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
				
				
				entity.setCreation_date(new Date());				
				entity.setModification_date(new Date());				
				antibodyMarkerDAO.persist(entity);
				
				modified = true;
				log.info("process Marker/create processed: " + entity.get_antibodymarker_key());					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				
				log.info("processAntibodyMarker delete");
				AntibodyMarker entity = antibodyMarkerDAO.get(Integer.valueOf(domain.get(i).getAntibodyMarkerKey()));
				antibodyMarkerDAO.remove(entity);
				modified = true;
				log.info("processAntibodyMarker delete successful");
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAntibodyMarker update");
				// IN PROGRESS
			}
		
		    log.info("processAntibodyMarker/processing successful");
		}	
		return modified;
    }
}
