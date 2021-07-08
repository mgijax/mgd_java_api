package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.GXDLabelDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.ProbePrepDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.ProbeSenseDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.VisualizationMethodDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.ProbePrepDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.ProbePrep;
import org.jax.mgi.mgd.api.model.gxd.translator.ProbePrepTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbePrepService extends BaseService<ProbePrepDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbePrepDAO probePrepDAO;
	@Inject
	private ProbeDAO probeDAO;
	@Inject
	private GXDLabelDAO labelDAO;
	@Inject
	private ProbeSenseDAO senseDAO;
	@Inject
	private VisualizationMethodDAO visualizationDAO;
	
	private ProbePrepTranslator translator = new ProbePrepTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<ProbePrepDomain> create(ProbePrepDomain domain, User user) {
		SearchResults<ProbePrepDomain> results = new SearchResults<ProbePrepDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;	
	}

	@Transactional
	public SearchResults<ProbePrepDomain> update(ProbePrepDomain domain, User user) {
		SearchResults<ProbePrepDomain> results = new SearchResults<ProbePrepDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;	
	}

	@Transactional
	public SearchResults<ProbePrepDomain> delete(Integer key, User user) {
		// get the entity object and delete		
		SearchResults<ProbePrepDomain> results = new SearchResults<ProbePrepDomain>();
		ProbePrep entity = probePrepDAO.get(key);
		results.setItem(translator.translate(probePrepDAO.get(key)));
		probePrepDAO.remove(entity);
		return results;	
	}
	
	@Transactional
	public ProbePrepDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbePrepDomain domain = new ProbePrepDomain();
		if (probePrepDAO.get(key) != null) {
			domain = translator.translate(probePrepDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbePrepDomain> getResults(Integer key) {
        SearchResults<ProbePrepDomain> results = new SearchResults<ProbePrepDomain>();
        results.setItem(translator.translate(probePrepDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<ProbePrepDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbePrepDomain> results = new SearchResults<ProbePrepDomain>();
		String cmd = "select count(*) as objectCount from gxd_ProbePrep";
		
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
	public List<ProbePrepDomain> search(ProbePrepDomain searchDomain) {

		List<ProbePrepDomain> results = new ArrayList<ProbePrepDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_probePrep a";
		String where = "where a._ProbePrep_key is not null";
		String orderBy = "order by a._ProbePrep_key";
		
		// if parameter exists, then add to where-clause
		if(searchDomain.getProbeKey() != null && ! searchDomain.getProbeKey().isEmpty()) {
			where = where + "\n and a._probe_key = " + searchDomain.getProbeKey();
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
				ProbePrepDomain domain = new ProbePrepDomain();
				domain = translator.translate(probePrepDAO.get(rs.getInt("_probeprep_key")));				
				probePrepDAO.clear();
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
	public Boolean process(Integer parentKey, ProbePrepDomain domain, User user) {
		// process probeprep (create, delete, update)
		
		Boolean modified = false;
		
		if (domain == null) {
			log.info("processProbePrep/nothing to process");
			return modified;
		}
				
		// iterate thru the list of rows in the domain
		// for each row, determine whether to perform an insert, delete or update
		
		if (domain.getProcessStatus().equals(Constants.PROCESS_CREATE)) {					
			log.info("processProbePrep/create");							
				ProbePrep entity = new ProbePrep();
				entity.setType(domain.getPrepType());
				entity.setProbe(probeDAO.get(Integer.valueOf(domain.getProbeKey())));
				entity.setLabel(labelDAO.get(Integer.valueOf(domain.getLabelKey())));
				entity.setProbeSense(senseDAO.get(Integer.valueOf(domain.getProbeSenseKey())));
				entity.setVisualizationMethod(visualizationDAO.get(Integer.valueOf(domain.getVisualizationMethodKey())));
				entity.setCreation_date(new Date());
				entity.setModification_date(new Date());
				// execute persist/insert/send to database
				probePrepDAO.persist(entity);
				modified = true;
				log.info("processProbePrep create processed: " + entity.get_probeprep_key());									
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_DELETE)) {
			log.info("processProbePrep delete");
			ProbePrep entity = probePrepDAO.get(Integer.valueOf(domain.getProbePrepKey()));
			probePrepDAO.remove(entity);
			modified = true;
			log.info("processProbePrep delete successful");
		}
		else if (domain.getProcessStatus().equals(Constants.PROCESS_UPDATE)) {
			log.info("processProbePrep update");
			ProbePrep entity = probePrepDAO.get(Integer.valueOf(domain.getProbePrepKey()));		
			entity.setType(domain.getPrepType());
			entity.setProbe(probeDAO.get(Integer.valueOf(domain.getProbeKey())));
			entity.setLabel(labelDAO.get(Integer.valueOf(domain.getLabelKey())));
			entity.setProbeSense(senseDAO.get(Integer.valueOf(domain.getProbeSenseKey())));
			entity.setVisualizationMethod(visualizationDAO.get(Integer.valueOf(domain.getVisualizationMethodKey())));
			entity.setModification_date(new Date());			
			probePrepDAO.update(entity);
			modified = true;
			log.info("processProbePrep/changes processed: " + domain.getProbePrepKey());	
		}
		else {
			log.info("processProbePrep/no changes processed: " + domain.getProbePrepKey());
		}
		
		log.info("processProbePrep/processing successful");
		return modified;
	}
	
}
