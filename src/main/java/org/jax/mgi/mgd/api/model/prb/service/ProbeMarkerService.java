package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeMarkerDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeMarkerTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeMarkerService extends BaseService<ProbeMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeMarkerDAO probeDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private ReferenceDAO referenceDAO;

	private ProbeMarkerTranslator translator = new ProbeMarkerTranslator();						
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeMarkerDomain> create(ProbeMarkerDomain domain, User user) {
		SearchResults<ProbeMarkerDomain> results = new SearchResults<ProbeMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeMarkerDomain> update(ProbeMarkerDomain domain, User user) {
		SearchResults<ProbeMarkerDomain> results = new SearchResults<ProbeMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public ProbeMarkerDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeMarkerDomain domain = new ProbeMarkerDomain();
		if (probeDAO.get(key) != null) {
			domain = translator.translate(probeDAO.get(key));
		}
		probeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<ProbeMarkerDomain> getResults(Integer key) {
        SearchResults<ProbeMarkerDomain> results = new SearchResults<ProbeMarkerDomain>();
        results.setItem(translator.translate(probeDAO.get(key)));
        probeDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<ProbeMarkerDomain> delete(Integer key, User user) {
		SearchResults<ProbeMarkerDomain> results = new SearchResults<ProbeMarkerDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional	
	public List<ProbeMarkerDomain> search(Integer key) {

		List<ProbeMarkerDomain> results = new ArrayList<ProbeMarkerDomain>();

		String cmd = "\nselect _assoc_key from prb_marker where _probe_key = " + key;
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ProbeMarkerDomain domain = new ProbeMarkerDomain();	
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
	public Boolean process(String parentKey, ProbeMarkerDomain domain, User user) {
		// process probe marker (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processProbeMarker");
		
		if (!domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			if (domain == null || domain.getMarkerKey().isEmpty()) {
				log.info("processProbeMarker/nothing to process");
				return modified;
			}
		}
										
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {				
			log.info("processProbeMarker create");
			ProbeMarker entity = new ProbeMarker();
			entity.set_probe_key(Integer.valueOf(domain.getProbeKey()));
			entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
			entity.setRelationship(domain.getRelationship());
			entity.setCreatedBy(user);
			entity.setModifiedBy(user);
			entity.setCreation_date(new Date());				
			entity.setModification_date(new Date());
			probeDAO.persist(entity);				
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processProbeMarker delete");				
			ProbeMarker entity = probeDAO.get(Integer.valueOf(domain.getAssocKey()));
			probeDAO.remove(entity);
			modified = true;
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processProbeMarker update");								
			ProbeMarker entity = probeDAO.get(Integer.valueOf(domain.getAssocKey()));
			entity.set_probe_key(Integer.valueOf(domain.getProbeKey()));
			entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));			
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
			entity.setRelationship(domain.getRelationship());
			entity.setModifiedBy(user);
			entity.setModification_date(new Date());
			probeDAO.update(entity);
			modified = true;
			log.info("processProbeMarker/changes processed: " + domain.getAssocKey());
		}
		else {
			log.info("processProbeMarker/no changes processed: " + domain.getAssocKey());
		}
		
		log.info("processProbeMarker/processing successful");
		return modified;
	}
	
}
