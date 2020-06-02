package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.CellLineDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
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
		domain.setSegmentTypeKey("63474");
		entity.setSegmentType(termDAO.get(Integer.valueOf(domain.getSegmentTypeKey())));
		
		// vector is always 'NS"
		domain.setVectorKey("316370");
		entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
	
		if(domain.getAge() == null || domain.getAge().isEmpty()) {
			domain.setAge("Not Specified");
		}
		entity.setAge(domain.getAge());
		// default min/max to 'Not Specified' call SP later to fix
		
		entity.setAgeMin(-1);
		entity.setAgeMax(-1);
		/* 
		//############ set ageMin/ageMax 
		// attempt to use PRB_ageMinMax sp to get a row with ageMin/ageMax and set those
		// values in the domain
		String cmd = "\nselect * from PRB_ageMinMax ('" + domain.getAge() + "')";
		log.info("cmd: " + cmd);
		String ageMin = "";
		String ageMax = "";
		try {
		    log.info("running query");
			Query query = probeSourceDAO.createNativeQuery(cmd);
			// Get single result, returns type Object, won't cast to string
			//String r = (String) query.getSingleResult();
			//log.info("result: " + r);
			//log.info("getting results: " + query.getSingleResult());
			
			// try getting result list, doesn't work either
			List <?> resultList = query.getResultList();
			log.info("get results 1 as string");
			ageMin = resultList.get(0).toString();
			log.info("get results 2 as string");
			ageMax = resultList.get(1).toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// domain does not have agemin/agemax - set in entity
		entity.setAgeMin(new Integer(ageMin).intValue());
		entity.setAgeMax(new Integer(ageMax).intValue());
		//#########
		*/
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
		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		probeSourceDAO.persist(entity);
		
		// update not happening here
		int newKey = entity.get_source_key();
		String cmd = "\nselect * from MGI_resetAgeMinMax ('PRB_Source', " +  newKey + ")";
		log.info("cmd: " + cmd);
		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("getting resultlist");
		// below produces this error:
		// "message": "org.hibernate.MappingException: No Dialect mapping for JDBC type: 1111 [TypeNames.java:70] (No Dialect mapping for JDBC type: 1111)", "status_code": 500
		query.getResultList();
		
		// return entity translated to domain
		log.info("Source/create/returning results");
		results.setItem(translator.translate(entity));
		// break up the pieces so we can log stuff
		//ProbeSourceDomain d = translator.translate(entity);
		//log.info("New key: " + d.getSourceKey());
		//log.info("New ageMin: " + probeSourceDAO.get(Integer.valueOf(d.getSourceKey())).getAgeMin() + " ageMax: " + probeSourceDAO.get(Integer.valueOf(d.getSourceKey())).getAgeMax());
		//results.setItem(d);
		
		return results;
	}
	@Transactional
	public SearchResults<ProbeSourceDomain> runAgeMinMax(Integer key, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		ProbeSource entity = new ProbeSource();
		
		String cmd = "\nselect * from MGI_resetAgeMinMax ('PRB_Source', " +  key + ")";
		log.info("cmd: " + cmd);
		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("getting resultlist");
		// below produces this error:
		// "message": "org.hibernate.MappingException: No Dialect mapping for JDBC type: 1111 [TypeNames.java:70] (No Dialect mapping for JDBC type: 1111)", "status_code": 500
		query.getResultList();
		//entity = probeSourceDAO.get(key);
		//results.setItem(translator.translate(entity));
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
