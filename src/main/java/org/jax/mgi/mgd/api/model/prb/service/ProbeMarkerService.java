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
	public Boolean process(String parentKey, List<ProbeMarkerDomain> domain, User user) {
		// process probe marker (create, delete, update)
		
		Boolean modified = false;
		
		log.info("processProbeMarker");

		if (domain == null || domain.isEmpty()) {
			log.info("processProbeMarker/nothing to process");
			return modified;
		}
		
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		for (int i = 0; i < domain.size(); i++) {
		
        	if (domain.get(i).getMarkerKey() == null || domain.get(i).getMarkerKey().isEmpty()) {
        		return modified;
        	}
        			
			if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_CREATE)) {								
				log.info("processProbeMarker/create");
				ProbeMarker entity = new ProbeMarker();									
				entity.set_probe_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
				entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				entity.setRelationship(domain.get(i).getRelationship());
				entity.setCreatedBy(user);
				entity.setModifiedBy(user);
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());				
				probeDAO.persist(entity);				
				log.info("processProbeMarker/create/returning results");	
				modified = true;
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_DELETE)) {
				log.info("processProbeMarker/delete");
				if (domain.get(i).getAssocKey() != null && !domain.get(i).getAssocKey().isEmpty()) {
					ProbeMarker entity = probeDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));
					probeDAO.remove(entity);
					modified = true;
				}
			}
			else if (domain.get(i).getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
				log.info("processProbeMarker/update");
				ProbeMarker entity = probeDAO.get(Integer.valueOf(domain.get(i).getAssocKey()));	
				entity.set_probe_key(Integer.valueOf(parentKey));
				entity.setMarker(markerDAO.get(Integer.valueOf(domain.get(i).getMarkerKey())));
				entity.setReference(referenceDAO.get(Integer.valueOf(domain.get(i).getRefsKey())));
				entity.setRelationship(domain.get(i).getRelationship());
				entity.setModifiedBy(user);
				entity.setModification_date(new Date());
				probeDAO.update(entity);
				log.info("processProbeMarker/changes processed: " + domain.get(i).getAssocKey());				
				modified = true;
			}
			else {
				log.info("processProbeMarker/no changes processed: " + domain.get(i).getAssocKey());
			}           
		}
		
		log.info("processProbeMarker/processing successful");
		return modified;
	}		

	@Transactional
	public List<ProbeMarkerDomain> validateProbeMarker(ProbeMarkerDomain searchDomain) {
		
		List<ProbeMarkerDomain> results = new ArrayList<ProbeMarkerDomain>();
		
		String cmd = "select pm._assoc_key, pm._probe_key, pm._marker_key from PRB_Marker pm" +				"\nwhere exists (select 1 from PRB_Probe p, VOC_Term t" +
				"\nwhere pm._Probe_key = p._Probe_key" +
				"\nand p._SegmentType_key = t._Term_key" +
				"\nand t.term != 'primer'" +
				"\nand pm.relationship in ('E', 'H')" +
				"\nand pm._Probe_key = " + searchDomain.getProbeKey() +
				"\nand pm._Marker_key = " + searchDomain.getMarkerKey() +	
				"\nunion all" +
				"\nselect pm._assoc_key, pm._probe_key, pm._marker_key from PRB_Marker pm" +
				"\nwhere exists (select 1 from PRB_Probe p, VOC_Term t" +
				"\nwhere pm._Probe_key = p._Probe_key" +
				"\nand p._SegmentType_key = t._Term_key" +
				"\nand t.term = 'primer'" +
				"\nand pm.relationship = 'A'" +
				"\nand pm._Probe_key = " + searchDomain.getProbeKey() +
				"\nand pm._Marker_key = " + searchDomain.getMarkerKey();			
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				ProbeMarkerDomain domain = new ProbeMarkerDomain();
				domain.setAssocKey(rs.getString("_assoc_key"));
				domain.setProbeKey(rs.getString("_probe_key"));
				domain.setMarkerKey(rs.getString("_marker_key"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
