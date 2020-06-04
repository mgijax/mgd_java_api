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
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeSourceDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeTissueDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeSourceTranslator;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
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
		log.info("probe source incoming domain description: " + domain.getDescription());
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
		
		log.info("probe source incoming domain organismKey: " + domain.getOrganismKey());
		if(domain.getOrganismKey() == null ||  domain.getOrganismKey().isEmpty()) {
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
		
		// special handling of source
		
		// if organism other than mouse (or Not Specified) then strain default is Not Applicable
		if (domain.getOrganismKey() != "1" && domain.getOrganismKey() != "-1") {
			domain.setStrainKey("-2"); 
		}
		entity.setStrain(probeStrainDAO.get(Integer.valueOf(domain.getStrainKey())));
		
		// if tissue is specified, then cell line is Not applicable
		if (domain.getTissueKey() != "-1") {
			domain.setCellLineKey("316336");
		}
		entity.setCellLine(termDAO.get(Integer.valueOf(domain.getCellLineKey())));

		// when cell line is specified and age is 'Not Specified', change default to 'Not Applicable'
		if(domain.getCellLineKey() != "316335" && domain.getCellLineKey() != "316336" && domain.getAge() == "Not Specified") {
			domain.setAge("Not Applicable");
		}
		entity.setAge(domain.getAge());
		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		probeSourceDAO.persist(entity);
		
		int newKey = entity.get_source_key();
		String cmd = "\nselect count(*) from MGI_resetAgeMinMax ('PRB_Source', " +  newKey + ")";
		log.info("cmd: " + cmd);
		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("getting resultlist");
		
		query.getResultList();
		
		// return entity translated to domain
		log.info("Source/create/returning results");
		log.info("source service create outgoing domain description: " + translator.translate(entity).getDescription());
		log.info("source service create outgoing domain organism: " + translator.translate(entity).getOrganism());
		results.setItem(translator.translate(entity));
		// break up the pieces so we can log stuff
		//ProbeSourceDomain d = translator.translate(entity);
		//log.info("New key: " + d.getSourceKey());
		//log.info("New ageMin: " + probeSourceDAO.get(Integer.valueOf(d.getSourceKey())).getAgeMin() + " ageMax: " + probeSourceDAO.get(Integer.valueOf(d.getSourceKey())).getAgeMax());
		//results.setItem(d);
		log.info("source service results description: " + results.items.get(0).getDescription());
		return results;
	}
	@Transactional
	public String createAntigenSource(ProbeSourceDomain domain, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		results = create(domain, user);
		return results.items.get(0).getSourceKey();
	}
	
	@Transactional
	public SearchResults<ProbeSourceDomain> runAgeMinMax(Integer key, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
				
		String cmd = "\nselect count(*) from MGI_resetAgeMinMax ('PRB_Source', " +  key + ")";
		log.info("cmd: " + cmd);
		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("getting resultlist");	
		query.getResultList();
		
		return results;
	}

	@Transactional
	public SearchResults<ProbeSourceDomain> update(ProbeSourceDomain domain, User user) {
	
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		//results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		ProbeSource entity = probeSourceDAO.get(Integer.valueOf(domain.getSourceKey()));
		
		Boolean modified = false;
		
		// description is null unless specified
		log.info("probe source incoming domain description: " + domain.getDescription());
		if(domain.getDescription() == null || domain.getDescription().isEmpty()) {
			entity.setDescription(null);
		}
		else {
			if (entity.getDescription() != domain.getDescription()) {
				modified = true;
			}
			entity.setDescription(domain.getDescription());
		}
		if(domain.getAge() == null || domain.getAge().isEmpty()) {
			domain.setAge("Not Specified");
		}
		else {
			if (entity.getAge() != domain.getAge()) {
				modified = true;
			}
			entity.setAge(domain.getAge());
		}
		log.info("probe source incoming domain organismKey: " + domain.getOrganismKey());
		if(domain.getOrganismKey() == null ||  domain.getOrganismKey().isEmpty()) {
			// 'Not Specified'
			domain.setOrganismKey("76");
		}
		else {
			if (String.valueOf(entity.getOrganism().get_organism_key()) != domain.getOrganism()) {
				modified = true;
			}
			entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		}
		if(domain.getStrainKey() == null || domain.getStrainKey().isEmpty()) {
			// 'Not Specified'
			domain.setStrainKey("-1");
		}
		else {
			if(String.valueOf(entity.getStrain().get_strain_key()) != domain.getStrainKey()) {
				modified = true;
			}
			entity.setStrain(probeStrainDAO.get(Integer.valueOf(domain.getStrainKey())));
		}
		if(domain.getTissueKey() == null || domain.getTissueKey().isEmpty()) {
			// 'Not Specified'
			domain.setTissueKey("-1");
		}
		else {
			if (String.valueOf(entity.getTissue().get_tissue_key()) != domain.getTissueKey()) {
				modified = true;
			}
			entity.setTissue(probeTissueDAO.get(Integer.valueOf(domain.getTissueKey())));
		}
		if(domain.getGenderKey() == null || domain.getGenderKey().isEmpty()) {
			domain.setGenderKey("315167");
		}
		else {
			if (String.valueOf(entity.getGender().get_term_key()) != domain.getGenderKey()) {
				modified = true;
			}
			entity.setGender(termDAO.get(Integer.valueOf(domain.getGenderKey())));
		}
		
		if(domain.getCellLineKey() == null ||  domain.getStrainKey().isEmpty()) {
			// 'Not Specified'
			domain.setCellLineKey("316335");
		}
		else {
			if (String.valueOf(entity.getCellLine().get_term_key()) != domain.getCellLineKey()) {
				modified = true;
			}
			entity.setCellLine(termDAO.get(Integer.valueOf(domain.getCellLineKey())));
		}
		// special handling of source
		
		// if organism other than mouse (or Not Specified) then strain default is Not Applicable
		if (domain.getOrganismKey() != "1" && domain.getOrganismKey() != "-1") {
			domain.setStrainKey("-2"); 
		}
		entity.setStrain(probeStrainDAO.get(Integer.valueOf(domain.getStrainKey())));
		
		// if tissue is specified, then cell line is Not applicable
		if (domain.getTissueKey() != "-1") {
			domain.setCellLineKey("316336");
		}
		entity.setCellLine(termDAO.get(Integer.valueOf(domain.getCellLineKey())));

		// when cell line is specified and age is 'Not Specified', change default to 'Not Applicable'
		if(domain.getCellLineKey() != "316335" && domain.getCellLineKey() != "316336" && domain.getAge() == "Not Specified") {
			domain.setAge("Not Applicable");
		}
		entity.setAge(domain.getAge());
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			probeSourceDAO.update(entity);
			log.info("processMarker/changes processed: " + domain.getSourceKey());
		}
		else {
			log.info("processMarker/no changes processed: " + domain.getSourceKey());
		}
		
		// update ageMin/ageMax
		String cmd = "\nselect count(*) from MGI_resetAgeMinMax ('PRB_Source', " +  entity.get_source_key() + ")";
		log.info("cmd: " + cmd);
		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("getting resultlist");
		
		query.getResultList();
		
		// return entity translated to domain
		log.info("processSource/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processSource/update/returned results succsssful");
		
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeSourceDomain> delete(Integer key, User user) {
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		//results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		ProbeSource entity = probeSourceDAO.get(key);
		results.setItem(translator.translate(probeSourceDAO.get(key)));
		probeSourceDAO.remove(entity);
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
