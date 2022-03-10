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
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.mgi.dao.OrganismDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeSourceDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeTissueDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeSourceTranslator;
import org.jax.mgi.mgd.api.model.prb.translator.SlimProbeSourceTranslator;
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
	@Inject
	private AccessionService accessionService;
	
	private ProbeSourceTranslator translator = new ProbeSourceTranslator();
	private SlimProbeSourceTranslator slimtranslator = new SlimProbeSourceTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	String mgiTypeName = "Source";
	
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
				
		// Not Applicable/Not Specified
		if(domain.getCellLineKey() == null || domain.getCellLineKey().isEmpty()) {
			if (domain.getTissueKey().equals("-2")) {
				domain.setCellLineKey("316336");
			}
			else {
				domain.setCellLineKey("316335");
			}
		}
		else {
			// if cell line entered, and age is not, then default age = Not Applicable
			if(domain.getAgePrefix() == null || domain.getAgePrefix().isEmpty()) {
				domain.setAgePrefix("Not Applicable");
			}
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

		// if cell line is !Not Specified and !Not Applicable and agePrefix is null/empty, then set agePrefix is Not Applicable
		if (!domain.getCellLineKey().equals("316335") && !domain.getCellLineKey().equals("316336") && (domain.getAgePrefix() == null || domain.getAgePrefix().isEmpty()) ) {
			log.info("setting age to not applicable");
			domain.setAgePrefix("Not Applicable");
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

		// process accession ids				
		accessionService.process(String.valueOf(entity.get_source_key()), domain.getAccessionIds(), mgiTypeName, user);		

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

		// process accession ids
		if (accessionService.process(domain.getSourceKey(), domain.getAccessionIds(), mgiTypeName, user)) {
			modified = true;
		}
		
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
	public SearchResults<ProbeSourceDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbeSourceDomain> results = new SearchResults<ProbeSourceDomain>();
		String cmd = "select count(*) as objectCount from prb_source";
		
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
	public List<ProbeSourceDomain> search(ProbeSourceDomain searchDomain) {

		List<ProbeSourceDomain> results = new ArrayList<ProbeSourceDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select s._source_key";
		String from = "from prb_source s";
		String where = "where s._source_key is not null";
		String orderBy = "order by s.name";
		Boolean from_strain = false;
		Boolean from_tissue = false;
		Boolean from_cellline = false;
		Boolean from_accession = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		if (searchDomain.getSourceKey() != null && !searchDomain.getSourceKey().isEmpty()) {
			where = where + "\nand s._source_key = " + searchDomain.getSourceKey();
		}
		
		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand s.name ilike '" + searchDomain.getName() + "'";
		}			

		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand s._Refs_key = " + searchDomain.getRefsKey();
		}
		
		if (searchDomain.getSegmentTypeKey() != null && !searchDomain.getSegmentTypeKey().isEmpty()) {
			where = where + "\nand s._segmenttype_key = " + searchDomain.getSegmentTypeKey();
		}

		if (searchDomain.getVectorKey() != null && !searchDomain.getVectorKey().isEmpty()) {
			where = where + "\nand s._vector_key = " + searchDomain.getVectorKey();
		}
		
		if (searchDomain.getStrainKey() != null && !searchDomain.getStrainKey().isEmpty()) {
			where = where + "\nand s._strain_key = " + searchDomain.getStrainKey();
		}
		else if (searchDomain.getStrain() != null && !searchDomain.getStrain().isEmpty()) {
			where = where + "\nand ss.strain ilike '" + searchDomain.getStrain() + "'";
			from_strain = true;					
		}
	
		if (searchDomain.getTissueKey() != null && !searchDomain.getTissueKey().isEmpty()) {
			where = where + "\nand s._tissue_key = " + searchDomain.getTissueKey();
		}
		else if (searchDomain.getTissue() != null && !searchDomain.getTissue().isEmpty()) {
			where = where + "\nand st.tissue ilike '" + searchDomain.getTissue() + "'";
			from_tissue = true;					
		}
					
		if (searchDomain.getCellLineKey() != null && !searchDomain.getCellLineKey().isEmpty()) {
			where = where + "\nand s._cellline_key = " + searchDomain.getCellLineKey();
		}
		else if (searchDomain.getCellLine() != null && !searchDomain.getCellLine().isEmpty()) {
			where = where + "\nand sc.term ilike '" + searchDomain.getCellLine() + "'";
			from_cellline = true;					
		}		
	
		if (searchDomain.getOrganismKey() != null && !searchDomain.getOrganismKey().isEmpty()) {
			where = where + "\nand s._organism_key = " + searchDomain.getOrganismKey();
		}
					
		if (searchDomain.getDescription() != null && !searchDomain.getDescription().isEmpty()) {
			where = where + "\nand s.description ilike '" + searchDomain.getDescription() + "'" ;
		}	
	
		if (searchDomain.getGenderKey() != null && !searchDomain.getGenderKey().isEmpty()) {
			where = where + "\nand s._gender_key = " + searchDomain.getGenderKey();
		}
				
		String agePrefix = "";
		String ageStage = "";
		if (searchDomain.getAgePrefix() != null && !searchDomain.getAgePrefix().isEmpty()) {
			agePrefix = searchDomain.getAgePrefix() + "%";
		}
		if (searchDomain.getAgeStage() != null && !searchDomain.getAgeStage().isEmpty()) {
			ageStage = "% " + searchDomain.getAgeStage();
		}
		if (agePrefix.length() > 0 || ageStage.length() > 0) {
			where = where + "\nand s.age ilike '" + agePrefix + ageStage + "'";
		}

		if (searchDomain.getAccessionIds() != null) {
			if (searchDomain.getAccessionIds().get(0).getAccID() != null && !searchDomain.getAccessionIds().get(0).getAccID().isEmpty()) {
				if (searchDomain.getAccessionIds().get(0).getLogicaldbKey() != null && !searchDomain.getAccessionIds().get(0).getLogicaldbKey().isEmpty()) {
					where = where + "\nand acc._logicaldb_key = " + searchDomain.getAccessionIds().get(0).getLogicaldbKey();
				}	
				where = where + "\nand acc.accID ilike '" + searchDomain.getAccessionIds().get(0).getAccID() + "'";
				from_accession = true;			
				}				
		}	
		
		if (from_strain == true) {
			from = from + ", prb_strain ss";
			where = where + "\nand s._strain_key = ss._strain_key";
		}
	
		if (from_tissue == true) {
			from = from + ", prb_tissue st";
			where = where + "\nand s._tissue_key = st._tissue_key";
		}

		if (from_cellline == true) {
			from = from + ", voc_term sc";
			where = where + "\nand s._cellline_key = sc._term_key and sc._vocab_key = 18";
		}
		if (from_accession == true) {
			from = from + ", acc_accession acc";
			where = where + "\nand acc._mgitype_key = 5 and s._source_key = acc._object_key";
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
	public List<SlimProbeSourceDomain> searchLibrarySet() {
		// search a probe source library set
		
		List<SlimProbeSourceDomain> results = new ArrayList<SlimProbeSourceDomain>();	

		String cmd = "select _source_key from PRB_Source where name is not null and _Source_key > 0"
				+ "\norder by name";
		log.info(cmd);
		
		try {			
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimProbeSourceDomain domain = new SlimProbeSourceDomain();
				domain = slimtranslator.translate(probeSourceDAO.get(rs.getInt("_source_key")));				
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

