package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeSourceDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeTissueDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeSourceTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeSourceService extends BaseService<ProbeSourceDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeSourceDAO probeSourceDAO;
	@Inject 
	ProbeStrainDAO probeStrainDAO;
	@Inject
	ProbeTissueDAO probeTissueDAO;
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private TermDAO termDAO;
	//@Inject CellLineDAO cellLineDAO;
	
	private ProbeSourceTranslator translator = new ProbeSourceTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeSourceDomain> create(ProbeSourceDomain domain, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		ProbeSource entity = new ProbeSource();
		
		// name is always null
		entity.setName(null);
		
		// reference is always null
		entity.setReference(null);
		
		// description is null unless specified
		if(domain.getDescription() == null || domain.getDescription().isEmpty()) {
			entity.setDescription(null);
		}
		else {
			entity.setDescription(domain.getDescription());
		}
		
		// segment type is always 'NS'
		domain.setSegmentTypeKey("63437");
		entity.setSegmentType(termDAO.get(Integer.valueOf(domain.getSegmentTypeKey())));
		
		// vector is always 'NS"
		domain.setVectorKey("316370");
		entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
	
		if(domain.getAge() == null || domain.getAge().isEmpty()) {
			domain.setAge("Not Specified");
		}
		entity.setAge(domain.getAge());
		
		//############ set ageMin/ageMax why not in domain/entity
		String cmd = "\nselect * from PRB_ageMinMax (" + domain.getAge() + ")";
		log.info("cmd: " + cmd);
		String ageMin = "";
		String ageMax = "";
		try {
			Query query = probeSourceDAO.createNativeQuery(cmd);
			//String r = (String) query.getSingleResult();
			List<String> resultList = query.getResultList();
			ageMin = (String)resultList.get(0);
			ageMax = (String)resultList.get(1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}			
		//#########
		
		entity.setAgeMin(new Integer(ageMin).intValue());
		entity.setAgeMax(new Integer(ageMax).intValue());
		
		if(domain.getOrganismKey() == null ||  domain.getOrganism().isEmpty()) {
			// 'Not Specified'
			domain.setOrganismKey("76");
		}
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		
		if(domain.getStrainKey() == null || domain.getStrainKey().isEmpty()) {
			// 'Not Specified'
			domain.setStrainKey("-1");
		}
		entity.setStrain(probeStrainDAO.get(Integer.valueOf(domain.getStrainKey())));
		
		if(domain.getTissueKey() == null || domain.getTissueKey().isEmpty()) {
			// 'Not Specified'
			domain.setTissueKey("-1");
		}
		entity.setTissue(probeTissueDAO.get(Integer.valueOf(domain.getTissueKey())));
		
		if(domain.getGenderKey() == null || domain.getGenderKey().isEmpty()) {
			domain.setGenderKey("315167");
		}
		entity.setGender(termDAO.get(Integer.valueOf(domain.getGenderKey())));
		
		
		if(domain.getCellLineKey() == null ||  domain.getStrainKey().isEmpty()) {
			// 'Not Specified'
			domain.setCellLineKey("316335");
		}
		entity.setCellLine(termDAO.get(Integer.valueOf(domain.getCellLineKey())));
		
		// execute persist/insert/send to database
		probeSourceDAO.persist(entity);
		
		// return entity translated to domain
		log.info("Source/create/returning results");
		results.setItem(translator.translate(entity));
		
		return results;
	}

	@Transactional
	public SearchResults<ProbeSourceDomain> update(ProbeSourceDomain domain, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeSourceDomain> delete(Integer key, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeSourceDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeSourceDomain domain = new ProbeSourceDomain();
		if (probeSourceDAO.get(key) != null) {
			domain = translator.translate(probeSourceDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeSourceDomain> getResults(Integer key) {
        SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
        results.setItem(translator.translate(probeSourceDAO.get(key)));
        return results;
    }

	/*@Transactional	
    public SearchResults<ProbeSourceDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		String cmd = "select count(*) as objectCount from gxd_antigen";
		
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
	}*/
	
	@Transactional
	public List<ProbeSourceDomain> search(ProbeSourceDomain searchDomain) {

		List<ProbeSourceDomain> results = new ArrayList<ProbeSourceDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from prb_source a";
		String where = "where s._source_key is not null";
		String orderBy = "order by a.name";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
		Boolean from_accession = false;
		
		//
		// IN PROGRESSS
		//
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
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
				ProbeSourceDomain domain = new ProbeSourceDomain();
				domain = translator.translate(probeSourceDAO.get(rs.getInt("_source_key")));				
				probeSourceDAO.clear();
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
