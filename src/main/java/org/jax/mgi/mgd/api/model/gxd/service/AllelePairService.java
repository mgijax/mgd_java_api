package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AllelePairDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;
import org.jax.mgi.mgd.api.model.gxd.translator.AllelePairTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AllelePairService extends BaseService<AllelePairDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AllelePairDAO allelePairDAO;
	@Inject
	private MarkerDAO markerDAO;
	
	private AllelePairTranslator translator = new AllelePairTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AllelePairDomain> create(AllelePairDomain domain, User user) {
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<AllelePairDomain> update(AllelePairDomain domain, User user) {
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public SearchResults<AllelePairDomain> delete(Integer key, User user) {
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public AllelePairDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AllelePairDomain domain = new AllelePairDomain();
		if (allelePairDAO.get(key) != null) {
			domain = translator.translate(allelePairDAO.get(key));
		}
		return domain;
	}

	@Transactional
	public SearchResults<AllelePairDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
		SearchResults<AllelePairDomain> results = new SearchResults<AllelePairDomain>();
		results.setItem(translator.translate(allelePairDAO.get(key)));
		return results;
	}

	@Transactional	
	public List<AllelePairDomain> search(Integer key) {
		// using searchDomain fields, generate SQL command
		
		List<AllelePairDomain> results = new ArrayList<AllelePairDomain>();

		String cmd = "\nselect * from img_imagepane"
				+ "\nwhere _image_key = " + key
				+ "\norder by _imagepane_key";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AllelePairDomain domain = new AllelePairDomain();	
				domain = translator.translate(allelePairDAO.get(rs.getInt("_imagepane_key")));
				allelePairDAO.clear();
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
	public Boolean process(String parentKey, List<AllelePairDomain> domain, User user) {
		// process image pane (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processAllelePair");
		
		if (domain == null || domain.isEmpty()) {
			log.info("processImagePane/nothing to process");
			return modified;
		}
						
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {
				log.info("processAllelePair create");
				AllelePair entity = new AllelePair();	
				entity.set_allelepair_key(Integer.valueOf(parentKey));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				allelePairDAO.persist(entity);
				modified = true;
				log.info("processAllelePair/create/returning results");					
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processAllelePair delete");
				AllelePair entity = allelePairDAO.get(Integer.valueOf(domain.get(i).getAllelePairKey()));
				allelePairDAO.remove(entity);
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processAllelePair update");
				Boolean isUpdated = false;
				AllelePair entity = allelePairDAO.get(Integer.valueOf(domain.get(i).getAllelePairKey()));

				if (!String.valueOf(entity.getMarker().get_marker_key()).equals(domain.get(i).getMarkerKey())) {
					entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
					isUpdated = true;
				}
				
				if (isUpdated) {
					log.info("processAllelePair modified == true");
					entity.setModification_date(new Date());
					allelePairDAO.update(entity);
					modified = true;
					log.info("processAllelePair/changes processed: " + domain.get(i).getAllelePairKey());
				}
				else {
					log.info("processAllelePair/no changes processed: " + domain.get(i).getAllelePairKey());
				}
			}
			else {
				log.info("processAllelePair/no changes processed: " + domain.get(i).getAllelePairKey());
			}
		}
		
		log.info("processAllelePair/processing successful");
		return modified;
	}
	
}