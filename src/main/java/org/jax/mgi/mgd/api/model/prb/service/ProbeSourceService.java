package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
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
	private ProbeStrainDAO strainDAO;
	@Inject
	private ProbeTissueDAO tissueDAO;
	@Inject
	private OrganismDAO organismDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private TermDAO termDAO;
	
	private ProbeSourceTranslator translator = new ProbeSourceTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeSourceDomain> create(ProbeSourceDomain domain, User user) {

		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		ProbeSource entity = new ProbeSource();
		
		log.info("processProbeSource/create");
		
		// Not Specified
		if(domain.getSegmentTypeKey() == null || domain.getSegmentTypeKey().isEmpty()) {
			domain.setSegmentTypeKey("63474");
		}

		// Not Specified
		if(domain.getVectorKey() == null || domain.getVectorKey().isEmpty()) {
			domain.setVectorKey("316370");
		}
		
		// Not Specified 
		if(domain.getOrganismKey() == null || domain.getOrganismKey().isEmpty()) {
			domain.setOrganismKey("76");
		}

		// Not Specified
		if(domain.getStrainKey() == null || domain.getStrainKey().isEmpty()) {
			domain.setStrainKey("-1");
		}

		// If key is empty we either set to default or check tissue
		if(domain.getTissueKey() == null || domain.getTissueKey().isEmpty()) {
			// Not Specified
			domain.setTissueKey("-1");

		}
		
		// Not Specified
		if(domain.getCellLineKey() == null || domain.getCellLineKey().isEmpty()) {
			domain.setCellLineKey("316335");
		}
		
		// Not Specified
		if(domain.getGenderKey() == null || domain.getGenderKey().isEmpty()) {
			domain.setGenderKey("315167");
		}

		// Not Specified
		if(domain.getAgePrefix() == null || domain.getAgePrefix().isEmpty()) {
			domain.setAgePrefix("Not Specified");
		}

		// true/1
		if (domain.getIsCuratorEdited() == null || domain.getIsCuratorEdited().isEmpty()) {
			domain.setIsCuratorEdited("1");
		}
		
		// Special defaults
		// if organism not Mouse or not Not Specified, default strain is Not Applicable
		if (!domain.getOrganismKey().equals("1") && ! domain.getOrganismKey().equals("76") ) {
			log.info("setting strain key to not applicable not mouse not not resolved");
			domain.setStrainKey("-2");
		}
		
		//if tissue is specified (NOT Not Specified), cell line default is Not Applicable
		
		if (!domain.getTissueKey().equals("-1")) {
			log.info("setting cell line to not applicable");
			domain.setCellLineKey("316336");
		}

		// if cell line is specified (NOT Not Specified), age is Not Applicable
		if (!domain.getCellLineKey().equals("316335") ) {
			log.info("setting age to Not applicable");
			domain.setAge("Not Applicable");
		}
		
		entity.setSegmentType(termDAO.get(Integer.valueOf(domain.getSegmentTypeKey())));
		entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainKey())));
		entity.setTissue(tissueDAO.get(Integer.valueOf(domain.getTissueKey())));
		entity.setCellLine(termDAO.get(Integer.valueOf(domain.getCellLineKey())));
		entity.setGender(termDAO.get(Integer.valueOf(domain.getGenderKey())));
		
		// age stuff
		log.info("domain agePrefix + ageStage: " + domain.getAgePrefix() + " " + domain.getAgeStage());
		//entity.setAge(domain.getAge());
		String age = domain.getAgePrefix();
		
		if (domain.getAgeStage() != null && ! domain.getAgeStage().isEmpty()) {
			age = domain.getAgePrefix() + " " + domain.getAgeStage();
		}
		log.info ("setting entity age to: " + age);
		entity.setAge(age);
		// will be properly set using MGI_resetAgeMinMax() below
		
		entity.setAgeMin(-1);
		entity.setAgeMax(-1);								

		if (domain.getRefsKey() != null && !domain.getRefsKey().isEmpty()) {
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
		}
			
		if (domain.getName() != null && !domain.getName().isEmpty()) {
			entity.setName(domain.getName());
		}

		if (domain.getDescription() != null && !domain.getDescription().isEmpty()) {
			entity.setDescription(domain.getDescription());
		}				
	
		entity.setIsCuratorEdited(Integer.valueOf(domain.getIsCuratorEdited()));		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());				
		
		
		probeSourceDAO.persist(entity);				

		// validate age and set age min/age max
		String cmd = "\nselect count(*) from MGI_resetAgeMinMax ('PRB_Source', " +  entity.get_source_key() + ")";
		log.info("cmd: " + cmd);		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("processProbeSource/MGI_resetAgeMinMax results");
		query.getResultList();
		
		// return entity translated to domain
		log.info("processProbeSource/create/returning results");
		results.setItem(translator.translate(entity));
		return results;		
	}

	@Transactional
	public SearchResults<ProbeSourceDomain> update(ProbeSourceDomain domain, User user) {
	
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		ProbeSource entity = probeSourceDAO.get(Integer.valueOf(domain.getSourceKey()));	
		Boolean modified = true;

		log.info("processProbeSource/update");
		
		entity.setSegmentType(termDAO.get(Integer.valueOf(domain.getSegmentTypeKey())));
		entity.setVector(termDAO.get(Integer.valueOf(domain.getVectorKey())));
		entity.setOrganism(organismDAO.get(Integer.valueOf(domain.getOrganismKey())));
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainKey())));
		entity.setTissue(tissueDAO.get(Integer.valueOf(domain.getTissueKey())));
		entity.setGender(termDAO.get(Integer.valueOf(domain.getGenderKey())));
		entity.setCellLine(termDAO.get(Integer.valueOf(domain.getCellLineKey())));
		
		// agePrefix and ageStage are catenated into age
		String age = domain.getAgePrefix();
		if (domain.getAgeStage() != null && ! domain.getAgeStage().isEmpty()) {
			age = domain.getAgePrefix() + " " + domain.getAgeStage();
		}
		entity.setAge(age);
		entity.setAgeMin(-1);
		entity.setAgeMax(-1);								

		if (domain.getRefsKey() != null && !domain.getRefsKey().isEmpty()) {
			entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));
		}
			
		if (domain.getName() != null && !domain.getName().isEmpty()) {
			entity.setName(domain.getName());
		}
		else {
			entity.setName(null);
		}

		if (domain.getDescription() != null && !domain.getDescription().isEmpty()) {
			entity.setDescription(domain.getDescription());
		}
		else {
			entity.setDescription(null);
		}
			
		entity.setIsCuratorEdited(Integer.valueOf(domain.getIsCuratorEdited()));
				
		// finish update
		if (modified) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			probeSourceDAO.update(entity);
			log.info("processAllele/changes processed: " + domain.getSourceKey());
		}

		// validate age and set age min/age max
		String cmd = "\nselect count(*) from MGI_resetAgeMinMax ('PRB_Source', " +  domain.getSourceKey() + ")";
		log.info("cmd: " + cmd);		
		Query query = probeSourceDAO.createNativeQuery(cmd); 
		log.info("processProbeSource/MGI_resetAgeMinMax results");
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
	
	@Transactional
	public List<ProbeSourceDomain> search(ProbeSourceDomain searchDomain) {

		List<ProbeSourceDomain> results = new ArrayList<ProbeSourceDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a._source_key";
		String from = "from prb_source a";
		String where = "where a._source_key is not null";
		String orderBy = "order by a.name";
		
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

	@Transactional
	public List<ProbeSourceDomain> searchLibrarySet() {
		// search a probe source library set
		
		List<ProbeSourceDomain> results = new ArrayList<ProbeSourceDomain>();	

		String cmd = "select _source_key from PRB_Source where name is not null and _Source_key > 0 order by name";
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

