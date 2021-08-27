package org.jax.mgi.mgd.api.model.gxd.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyPrepDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayTypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.ProbePrepDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeReplaceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimEmapaDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAssayTranslator;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberEmapaDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberGenotypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.RunCommand;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AssayService extends BaseService<AssayDomain> {

	//
	// gxd_assay 
	// 
	// 	-> gxd_specimen -> gxd_insituresult
	// 				-> gxd_insituresultimage
	// 				-> gxd_isresultstructure
	//
	// 	-> gxd_gelrow
	// 	-> gxd_gellane -> gxd_gellanestructure
	// 	-> gxd_gellane -> gxd_gelband : has gellane, gelrow
	//
	
	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AssayDAO assayDAO;
	@Inject
	private AssayTypeDAO assayTypeDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private AntibodyPrepDAO antibodyPrepDAO;
	@Inject
	private ProbePrepDAO probePrepDAO;
	@Inject
	private ImagePaneDAO imagePaneDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private AssayNoteService assayNoteService;
	@Inject
	private SpecimenService specimenService;
	@Inject
	private GelLaneService gelLaneService;
//	@Inject
//	private GelRowService gelRowService;
	@Inject
	private AntibodyPrepService antibodyPrepService;
	@Inject
	private ProbePrepService probePrepService;
	
	private AssayTranslator translator = new AssayTranslator();
	private SlimAssayTranslator slimtranslator = new SlimAssayTranslator();

	private SQLExecutor sqlExecutor = new SQLExecutor();

	String mgiTypeKey = "8";
	String mgiTypeName = "Assay";
	
	@Transactional
	public SearchResults<AssayDomain> create(AssayDomain domain, User user) {
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		Assay entity = new Assay();
		Boolean modified = false;

		log.info("processAssay/create");

		entity.setAssayType(assayTypeDAO.get(Integer.valueOf(domain.getAssayTypeKey())));	
		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));
				
		if (domain.getDetectionKey().equals("2")) {
			Integer antibodyPrepKey = antibodyPrepService.process(entity.get_assay_key(), domain.getAntibodyPrep(), user);
			if (antibodyPrepKey > 1) {
				entity.setAntibodyPrep(antibodyPrepDAO.get(antibodyPrepKey));
				modified = true;
			}
			else if (antibodyPrepKey == 1) {
				modified = true;
			}
			entity.setProbePrep(null);			
		}
		else if (domain.getDetectionKey().equals("1")) {
			Integer probePrepKey = probePrepService.process(entity.get_assay_key(), domain.getProbePrep(), user);
			if (probePrepKey > 1) {
				entity.setProbePrep(probePrepDAO.get(probePrepKey));
				modified = true;
			}
			else if (probePrepKey == 1) {
				modified = true;
			}
			entity.setAntibodyPrep(null);
		}
		else {
			entity.setAntibodyPrep(null);
			entity.setProbePrep(null);
			modified = true;
		}
				
		if (domain.getImagePaneKey() != null && !domain.getImagePaneKey().isEmpty()) {
			entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.getImagePaneKey())));		
		}
		else {
			entity.setImagePane(null);
		}
			
		if (domain.getReporterGeneKey() != null && !domain.getReporterGeneKey().isEmpty()) {
			entity.setReporterGene(termDAO.get(Integer.valueOf(domain.getReporterGeneKey())));		
		}
		else {
			entity.setReporterGene(null);
		}
		
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		assayDAO.persist(entity);

		// process gxd_assaynote
		if (domain.getAssayNote() != null) {
			if (assayNoteService.process(entity.get_assay_key(), domain.getAssayNote(), user)) {
				modified = true;
			}
		}
				
		// process gxd_specimen		
		// if assaytype in Specimen Assay Type
		if (domain.getSpecimens() != null && !domain.getSpecimens().isEmpty()) {
			if (specimenService.process(entity.get_assay_key(), domain.getSpecimens(), user)) {
				modified = true;
			}
		}
		
		// process gxd_gellane
		// if assaytype in Gel Assay Type
		if (domain.getGelLanes() != null && !domain.getGelLanes().isEmpty()) {
			if (gelLaneService.process(entity.get_assay_key(), Integer.valueOf(domain.getAssayTypeKey()), domain.getGelLanes(), user)) {
				modified = true;
			}
		}

//		// process gxd_gelrow
//		// if assaytype in Gel Assay Type
//		if (domain.getGelRows() != null && !domain.getGelRows().isEmpty()) {
//			if (gelRowService.process(entity.get_assay_key(), domain.getGelRows(), user)) {
//				modified = true;
//			}
//		}
		
		// return entity translated to domain
		log.info("processAssay/create/returning results : " + modified);
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<AssayDomain> update(AssayDomain domain, User user) {
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"
				
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		Assay entity = assayDAO.get(Integer.valueOf(domain.getAssayKey()));
		Boolean modified = false;
		String cmd;
		Query query;
		
		log.info("processAssay/update");
		
		entity.setAssayType(assayTypeDAO.get(Integer.valueOf(domain.getAssayTypeKey())));	
		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));
	
		if (domain.getDetectionKey().equals("2")) {
			Integer antibodyPrepKey = antibodyPrepService.process(entity.get_assay_key(), domain.getAntibodyPrep(), user);
			if (antibodyPrepKey > 1) {
				entity.setAntibodyPrep(antibodyPrepDAO.get(antibodyPrepKey));
				modified = true;
			}
			else if (antibodyPrepKey == 1) {
				modified = true;
			}
			entity.setProbePrep(null);			
		}
		else if (domain.getDetectionKey().equals("1")) {
			Integer probePrepKey = probePrepService.process(entity.get_assay_key(), domain.getProbePrep(), user);
			if (probePrepKey > 1) {
				entity.setProbePrep(probePrepDAO.get(probePrepKey));
				modified = true;
			}
			else if (probePrepKey == 1) {
				modified = true;
			}
			entity.setAntibodyPrep(null);
		}
		else {
			entity.setAntibodyPrep(null);
			entity.setProbePrep(null);
			modified = true;
		}
				
		if (domain.getImagePaneKey() != null && !domain.getImagePaneKey().isEmpty()) {
			entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.getImagePaneKey())));		
		}
		else {
			entity.setImagePane(null);
		}
			
		if (domain.getReporterGeneKey() != null && !domain.getReporterGeneKey().isEmpty()) {
			entity.setReporterGene(termDAO.get(Integer.valueOf(domain.getReporterGeneKey())));		
		}
		else {
			entity.setReporterGene(null);
		}
				
		// process gxd_assaynote
		if (domain.getAssayNote() != null) {
			if (assayNoteService.process(Integer.valueOf(domain.getAssayKey()), domain.getAssayNote(), user)) {
				modified = true;
			}
		}
				
		// process gxd_specimen		
		// if assaytype in Specimen Assay Type
		if (domain.getSpecimens() != null && !domain.getSpecimens().isEmpty()) {
			if (specimenService.process(Integer.valueOf(domain.getAssayKey()), domain.getSpecimens(), user)) {
				modified = true;
			}
		}
		
		// process gxd_gellane
		// if assaytype in Gel Assay Type
		if (domain.getGelLanes() != null && !domain.getGelLanes().isEmpty()) {
			if (gelLaneService.process(Integer.valueOf(domain.getAssayKey()), Integer.valueOf(domain.getAssayTypeKey()), domain.getGelLanes(), user)) {
				modified = true;
			}
		}
		
//		// process gxd_gelrow
//		// if assaytype in Gel Assay Type
//		if (domain.getGelRows() != null && !domain.getGelRows().isEmpty()) {
//			if (gelRowService.process(Integer.valueOf(domain.getAssayKey()), domain.getGelRows(), domain.getLanes(), user)) {
//				modified = true;
//			}
//		}
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			assayDAO.update(entity);
			log.info("processAssay/changes processed: " + domain.getAssayKey());
		}
		else {
			log.info("processAssay/no changes processed: " + domain.getAssayKey());
		}

		// process order reset
		cmd = "select count(*) from MGI_resetSequenceNum ('GXD_Specimen'," + entity.get_assay_key() + "," + user.get_user_key() + ")";
		log.info("processAssay/process order reset: " + cmd);
		query = assayDAO.createNativeQuery(cmd);
		query.getResultList();
		
		// return entity translated to domain
		log.info("processAssay/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAssay/update/returned results succsssful");
		return results;		
	}
	 
	@Transactional
	public SearchResults<AssayDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		Assay entity = assayDAO.get(key);
		results.setItem(translator.translate(assayDAO.get(key)));
		assayDAO.remove(entity);
		return results;
	}  
		
	@Transactional
	public AssayDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AssayDomain domain = new AssayDomain();
		if (assayDAO.get(key) != null) {
			domain = translator.translate(assayDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AssayDomain> getResults(Integer key) {
        SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
        results.setItem(translator.translate(assayDAO.get(key)));
        return results;
    } 

	@Transactional	
	public SearchResults<AssayDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		String cmd = "select count(*) as objectCount from gxd_assay";
		
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
	public List<SlimAssayDomain> search(AssayDomain searchDomain) {

		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._assay_key, r.jnumid, t.assayType, m.symbol";
		String from = "from gxd_assay a, gxd_assaytype t, bib_citation_cache r, mrk_marker m";
		String where = "where a._assaytype_key = t._assaytype_key"
				+ "\nand a._refs_key = r._refs_key"
				+ "\nand a._marker_key = m._marker_key";
		String orderBy = "order by r.jnumid, t.assayType, m.symbol";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		String agePrefix;	
		String ageRange;
		Boolean from_accession = false;
		Boolean from_assaynote = false;
		Boolean from_probeprep = false;
		Boolean from_probeacc = false;
		Boolean from_probe = false;
		Boolean from_antibodyprep = false;
		Boolean from_antibodyacc = false;
		Boolean from_antibody = false;
		Boolean from_specimen = false;
		Boolean from_gellane = false;
		Boolean from_genotype = false;
		Boolean from_isresults = false;
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		value = searchDomain.getAssayTypeKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a._assaytype_key = " + value;		
		}
		
		value = searchDomain.getImagePaneKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a._imagepane_key = " + value;		
		}
		
		value = searchDomain.getReporterGeneKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a._reportergene_key = " + value;		
		}
		
		value = searchDomain.getDetectionKey();
		if (value != null && !value.isEmpty()) {
			if (value.equals("1")) {
				where = where + "\nand a._ProbePrep_key is not null";
			}
			else if (value.equals("2")) {
				where = where + "\nand a._AntibodyPrep_key is not null";
			}
			else {
				where = where + "\nand a._ProbePrep_key is null and a._AntibodyPrep_key is null";				
			}
		}
		
		// reference
		value = searchDomain.getRefsKey();
		String jnumid = searchDomain.getJnumid();		
		if (value != null && !value.isEmpty()) {
			where = where + "\nand r._Refs_key = " + value;
		}
		else if (jnumid != null && !jnumid.isEmpty()) {
			jnumid = jnumid.toUpperCase();
			if (!jnumid.contains("J:")) {
					jnumid = "J:" + jnumid;
			}
			where = where + "\nand r.jnumid = '" + jnumid + "'";
		}
		
		// marker
		value = searchDomain.getMarkerKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a._marker_key = " + value;					
		}
		else {
			value = searchDomain.getMarkerSymbol();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand m.symbol ilike '" + value + "'";
			}
		}

		if (searchDomain.getProbePrep() != null) {
			value = searchDomain.getProbePrep().getProbePrepKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand a._probeprep_key = " + value;		
			}
			value = searchDomain.getProbePrep().getProbeSenseKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand pprep._sense_key = " + value;
				from_probeprep = true;
			}
			value = searchDomain.getProbePrep().getLabelKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand pprep._label_key = " + value;
				from_probeprep = true;				
			}
			value = searchDomain.getProbePrep().getVisualizationMethodKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand pprep._visualization_key = " + value;	
				from_probeprep = true;				
			}
			value = searchDomain.getProbePrep().getPrepType();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand pprep.type ilike '" + value + "'";
				from_probeprep = true;				
			}
			value = searchDomain.getProbePrep().getProbeAccID();
			if (value != null && !value.isEmpty()) {
				if (!value.contains("MGI:")) {
					value = "MGI:" + value;
				}
				where = where + "\nand pacc.accID = '" + value + "'";
				from_probeprep = true;
				from_probeacc = true;
			}
			value = searchDomain.getProbePrep().getProbeName();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand probe.name ilike '" + value + "'";
				from_probeprep = true;
				from_probe = true;
			}			
		}

		if (searchDomain.getAntibodyPrep() != null) {		
			value = searchDomain.getAntibodyPrep().getAntibodyPrepKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand a._antibodyprep_key = " + value;		
			}
			value = searchDomain.getAntibodyPrep().getSecondaryKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand aprep._secondary_key = " + value;
				from_antibodyprep = true;
			}
			value = searchDomain.getAntibodyPrep().getLabelKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand aprep._label_key = " + value;
				from_antibodyprep = true;				
			}
			value = searchDomain.getAntibodyPrep().getAntibodyAccID();
			if (value != null && !value.isEmpty()) {
				if (!value.contains("MGI:")) {
					value = "MGI:" + value;
				}
				where = where + "\nand aacc.accID = '" + value + "'";
				from_antibodyprep = true;
				from_antibodyacc = true;
			}
			value = searchDomain.getAntibodyPrep().getAntibodyName();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand antibody.antibodyname ilike '" + value + "'";
				from_antibodyprep = true;
				from_antibody = true;
			}			
		}
		
		// specimens
		
		if (searchDomain.getSpecimens() != null) {
			
			value = searchDomain.getSpecimens().get(0).getSpecimenLabel();
			value = value.replace("'",  "''");
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.specimenLabel ilike '" + value + "'";				
				from_specimen = true;
			}
			
			value = searchDomain.getSpecimens().get(0).getGenotypeAccID();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand g.accID = '" + value + "'";	
				from_genotype = true;
				from_specimen = true;
			}	
			
			value = "";
			agePrefix = searchDomain.getSpecimens().get(0).getAgePrefix();	
			ageRange = searchDomain.getSpecimens().get(0).getAgeStage();
			if (agePrefix != null && !agePrefix.isEmpty() && ageRange != null && !ageRange.isEmpty()) {
				value = agePrefix + " " + ageRange;
			}
			else if (agePrefix != null && !agePrefix.isEmpty()) {
				value = agePrefix + "%";
			}
			else if (ageRange != null && !ageRange.isEmpty()) {
				value = value + "%" + ageRange; 
			}
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.age ilike '" + value + "'";				
				from_specimen = true;
			}
			
			value = searchDomain.getSpecimens().get(0).getAgeNote();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.ageNote ilike '" + value + "'";				
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getSex();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.sex ilike '" + value + "'";				
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getFixationKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s._fixation_key = " + value;							
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getEmbeddingKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s._embedding_key = " + value;				
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getHybridization();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.hybridization ilike '" + value + "'";				
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getSpecimenNote();
			if (value != null && !value.isEmpty()) {
				value = value.replace("\\", "\\\\");
				where = where + "\nand s.specimenNote ilike '" + value + "'";				
				from_specimen = true;
			}
			
			// results
			if (searchDomain.getSpecimens().get(0).getSresults() != null) {
			
				value = searchDomain.getSpecimens().get(0).getSresults().get(0).getPatternKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand isresults._pattern_key = " + value;							
					from_specimen = true;
					from_isresults = true;
				}
				
				value = searchDomain.getSpecimens().get(0).getSresults().get(0).getStrengthKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand isresults._strength_key = " + value;							
					from_specimen = true;
					from_isresults = true;
				}
				
				value = searchDomain.getSpecimens().get(0).getSresults().get(0).getResultNote();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand isresults.resultnote ilike '" + value + "'";							
					from_specimen = true;
					from_isresults = true;
				}				
			}
		}
		
		// gels
		
		if (searchDomain.getGelLanes() != null) {
			value = searchDomain.getGelLanes().get(0).getLaneLabel();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.laneLabel ilike '" + value + "'";				
				from_gellane = true;
			}
			value = searchDomain.getGelLanes().get(0).getGenotypeAccID();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand g.accID = '" + value + "'";	
				from_genotype = true;
				from_gellane = true;
			}			

			value = "";
			agePrefix = searchDomain.getGelLanes().get(0).getAgePrefix();	
			ageRange = searchDomain.getGelLanes().get(0).getAgeStage();
			if (agePrefix != null && !agePrefix.isEmpty() && ageRange != null && !ageRange.isEmpty()) {
				value = agePrefix + " " + ageRange;
			}
			else if (agePrefix != null && !agePrefix.isEmpty()) {
				value = agePrefix + "%";
			}
			else if (ageRange != null && !ageRange.isEmpty()) {
				value = value + "%" + ageRange; 
			}
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.age ilike '" + value + "'";				
				from_gellane = true;
			}
			
			value = searchDomain.getGelLanes().get(0).getAgeNote();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.ageNote ilike '" + value + "'";				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getSex();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.sex ilike '" + value + "'";				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getGelRNATypeKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s._gelrnatype_key = " + value;				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getGelControlKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s._gelcontrol_key = " + value;
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getSampleAmount();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.sampleAmount ilike '" + value + "'";			
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getLaneNote();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.laneNote ilike '" + value + "'";				
				from_gellane = true;
			}			
		}
		
		// assay accession id 
		value = searchDomain.getAccID();			
		if (value != null && !value.isEmpty()) {	
			if (!value.contains("MGI:")) {
				value = "MGI:" + value;
			}
			where = where + "\nand acc.accID = '" + value + "'";
			from_accession = true;
		}
		
		if (searchDomain.getAssayNote().getAssayNote() != null && !searchDomain.getAssayNote().getAssayNote().isEmpty()) {
			value = searchDomain.getAssayNote().getAssayNote();
			where = where + "\nand n.assaynote ilike '" + value + "'";
			from_assaynote = true;
		}
		
		if (from_accession == true) {
			from = from + ", gxd_assay_acc_view acc";
			where = where + "\nand a._assay_key = acc._object_key"; 
		}
		if (from_assaynote == true) {
			from = from + ", gxd_assaynote n";
			where = where + "\nand a._assay_key = n._assay_key";
		}
		if (from_probeprep == true) {
			from = from + ", gxd_probeprep pprep";
			where = where + "\nand a._probeprep_key = pprep._probeprep_key";
		}
		if (from_probeacc == true) {
			from = from + ", prb_acc_view pacc";
			where = where + "\nand pprep._probe_key = pacc._object_key";
		}
		if (from_probe == true) {
			from = from + ", prb_probe probe";
			where = where + "\nand pprep._probe_key = probe._probe_key";
		}
		if (from_antibodyprep == true) {
			from = from + ", gxd_antibodyprep aprep";
			where = where + "\nand a._antibodyprep_key = aprep._antibodyprep_key";
		}
		if (from_antibodyacc == true) {
			from = from + ", gxd_antibody_acc_view aacc";
			where = where + "\nand aprep._antibody_key = aacc._object_key";
		}
		if (from_antibody == true) {
			from = from + ", gxd_antibody antibody";
			where = where + "\nand aprep._antibody_key = antibody._antibody_key";
		}
		if (from_specimen == true) {
			from = from + ", gxd_specimen s";
			where = where + "\nand a._assay_key = s._assay_key";
		}
		if (from_gellane == true) {
			from = from + ", gxd_gellane s";
			where = where + "\nand a._assay_key = s._assay_key";
		}		
		if (from_genotype == true) {
			from = from + ", gxd_genotype_acc_view g";
			where = where + "\nand s._genotype_key = g._object_key";
		}
		if (from_isresults == true) {
			from = from + ", gxd_insituresult isresults";
			where = where + "\nand s._specimen_key = isresults._specimen_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		//cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAssayDomain domain = new SlimAssayDomain();
				domain = slimtranslator.translate(assayDAO.get(rs.getInt("_assay_key")));				
				assayDAO.clear();
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
	public List<MGISetMemberGenotypeDomain> getGenotypeBySetUser(SlimAssayDomain searchDomain) {
		// return 
		// all set members of genotype (_set_key = 1055) + user (searchDomain.getCreatedByKey())
		// union
		// all genotypes for given assay (searchDomain.getAssayKey())

		List<MGISetMemberGenotypeDomain> results = new ArrayList<MGISetMemberGenotypeDomain>();		
		List<String> labelList = new ArrayList<String>();
		
		// search mgi_setmembers where _set_key = 1055 (genotype)
		String cmd = 
				"\n(select distinct s._object_key as objectKey," +
				"\na.accID as label," +
				"\n'*['||a.accID||'] '||s.label as displayIt," + 
				"\ns._set_key as setKey, s._setmember_key as setMemberKey, s._createdby_key as createdByKey, u.login as createdBy, " +
				"\n1 as orderBy" +
				"\nfrom mgi_setmember s, acc_accession a, mgi_user u" + 
				"\nwhere s._set_key = 1055" + 
				"\nand s._createdby_key = u._user_key" +
				"\nand u.login = '" + searchDomain.getCreatedBy() + "'" +
				"\nand s._object_key = a._object_key" + 
				"\nand a._mgitype_key = 12" + 
				"\nand a._logicaldb_key = 1" + 
				"\nand a.prefixPart = 'MGI:'" + 
				"\nand a.preferred = 1";
		
		if (searchDomain.getAssayKey() != null && !searchDomain.getAssayKey().isEmpty()) {
			
			// search gxd_specimen
			cmd = cmd + "\nunion all" + 				
				"\nselect distinct g._Genotype_key," +
				"\ng.mgiid as label," +
				"\nconcat(g.displayIt,',',a1.symbol,',',a2.symbol) as displayIt," +				
				"\n0 as setKey, 0 as setMemberKey, g._createdby_key, g.createdBy," +
				"\n0 as orderBy" +
				"\nfrom GXD_Genotype_View g" + 
				"\nINNER JOIN GXD_Specimen s on (g._Genotype_key = s._Genotype_key)" + 
				"\nLEFT OUTER JOIN GXD_AllelePair ap on (g._Genotype_key = ap._Genotype_key)" + 
				"\nLEFT OUTER JOIN ALL_Allele a1 on (ap._Allele_key_1 = a1._Allele_key)" + 
				"\nLEFT OUTER JOIN ALL_Allele a2 on (ap._Allele_key_2 = a2._Allele_key)" + 
				"\nwhere s._Assay_key = " + searchDomain.getAssayKey();
			
			// search gxd_gellane
			cmd = cmd + "\nunion all" + 				
					"\nselect distinct g._Genotype_key, " +
					"\ng.mgiid as label," +					
					"\nconcat(g.displayIt,',',a1.symbol,',',a2.symbol) as displayIt," +				
					"\n0 as setKey, 0 as setMemberKey, g._createdby_key, g.createdBy," +
					"\n0 as orderBy" +
					"\nfrom GXD_Genotype_View g" + 
					"\nINNER JOIN GXD_GelLane s on (g._Genotype_key = s._Genotype_key)" + 
					"\nLEFT OUTER JOIN GXD_AllelePair ap on (g._Genotype_key = ap._Genotype_key)" + 
					"\nLEFT OUTER JOIN ALL_Allele a1 on (ap._Allele_key_1 = a1._Allele_key)" + 
					"\nLEFT OUTER JOIN ALL_Allele a2 on (ap._Allele_key_2 = a2._Allele_key)" + 
					"\nwhere s._Assay_key = " + searchDomain.getAssayKey();			
			}
		
		cmd = cmd + "\norder by orderBy, label\n)";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				
				// only add at most one MGI:xxx (label) to results
				String label = rs.getString("label");
				if (labelList.contains(label)) {
					continue;
				}
				else {
					labelList.add(label);
				}
				
				MGISetMemberGenotypeDomain domain = new MGISetMemberGenotypeDomain();
				domain.setSetKey(rs.getString("setKey"));
				domain.setSetMemberKey(rs.getString("setMemberKey"));
				domain.setObjectKey(rs.getString("objectKey"));
				domain.setLabel(rs.getString("label"));
				domain.setDisplayIt(rs.getString("displayIt"));
				domain.setCreatedByKey(rs.getString("createdByKey"));
				domain.setCreatedBy(rs.getString("createdBy"));
				results.add(domain);		
				assayDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional	
	public List<MGISetMemberEmapaDomain> getEmapaInSituBySetUser(SlimEmapaDomain searchDomain) {
		// return 
		// all set members of emapa/stage (_set_key = 1046) + user (searchDomain.getCreatedByKey())
		// union
		// all emapa for given specimen (searchDomain.getSpecimienKey())

		List<MGISetMemberEmapaDomain> results = new ArrayList<MGISetMemberEmapaDomain>();		
		String displayIt = "";
		
		// search mgi_setmembers where _set_key = 1046 (emapa/stage)
		String cmd = 
				"\n(select distinct '*TS'||cast(t2.stage as varchar(5))||';'||t1.term as displayIt, t1.term, t2.stage," +
				"\ns._setmember_key as setMemberKey, s._set_key as setKey, s._object_key as objectKey, s._createdby_key as createdByKey, u.login," +
				"\ns.sequenceNum as sequenceNum, 1 as orderBy" +
				"\nfrom mgi_setmember s, mgi_setmember_emapa s2, voc_term t1, gxd_theilerstage t2, mgi_user u" +
				"\nwhere not exists (select 1 from GXD_ISResultStructure_View v where s._Object_key = v._EMAPA_Term_key" +
				"\nand s2._Stage_key = v._Stage_key" +
				"\nand v._Specimen_key = " + searchDomain.getSpecimenKey() + ")" +
				"\nand s._set_key = 1046" +
				"\nand s._setmember_key = s2._setmember_key" +
				"\nand s._object_key = t1._term_key" +
				"\nand s2._Stage_key = t2._stage_key" +
				"\nand s._CreatedBy_key = u._user_key" +
				"\nand u.login = '" + searchDomain.getCreatedBy() + "'" +		
				"\nunion all" +
				"\nselect i.displayIt||' ('||count(*)||')' as displayIt, term, stage," +
				"\n0 as setMemberKey, 0 as setKey, i._emapa_term_key as objectKey, 0 as createdByKey, null as createdBy," +
				"\nmin(i.sequenceNum), 0 as orderBy" +				
				"\nfrom GXD_ISResultStructure_View i, GXD_Specimen s" +
				"\nwhere s._Specimen_key = i._Specimen_key" +
				"\nand s._Specimen_key = " + searchDomain.getSpecimenKey() +
				"\ngroup by _EMAPA_Term_key, _Stage_key, displayIt, term, stage" +
				"\n) order by orderBy, sequenceNum, stage, term";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberEmapaDomain domain = new MGISetMemberEmapaDomain();
				domain.setProcessStatus("x");
				domain.setSetKey(rs.getString("setKey"));
				domain.setSetMemberKey(rs.getString("setMemberKey"));
				domain.setObjectKey(rs.getString("objectKey"));
				domain.setTerm(rs.getString("term"));
				domain.setStage(rs.getString("stage"));
				domain.setCreatedByKey(rs.getString("createdByKey"));
				domain.setCreatedBy(rs.getString("login"));
				
				displayIt = rs.getString("displayIt").replaceAll("\\(1\\)", "");
				domain.setDisplayIt(displayIt);

				domain.setIsUsed(false);
				results.add(domain);				
				assayDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@Transactional	
	public List<MGISetMemberEmapaDomain> getEmapaGelBySetUser(SlimEmapaDomain searchDomain) {
		// return 
		// all set members of emapa/stage (_set_key = 1046) + user (searchDomain.getCreatedByKey())
		// union
		// all emapa for given assay (searchDomain.getAssayKey())

		List<MGISetMemberEmapaDomain> results = new ArrayList<MGISetMemberEmapaDomain>();		
		String displayIt = "";
		
		// search mgi_setmembers where _set_key = 1046 (emapa/stage)
		String cmd = 
				"\n(select distinct '*TS'||cast(t2.stage as varchar(5))||';'||t1.term as displayIt, t1.term, t2.stage," +
				"\ns._setmember_key as setMemberKey, s._set_key as setKey, s._object_key as objectKey, s._createdby_key as createdByKey, u.login," +
				"\ns.sequenceNum as sequenceNum, 1 as orderBy" +
				"\nfrom mgi_setmember s, mgi_setmember_emapa s2, voc_term t1, gxd_theilerstage t2, mgi_user u" +
				"\nwhere not exists (select 1 from GXD_GelLaneStructure_View v where s._Object_key = v._EMAPA_Term_key" +
				"\nand s2._Stage_key = v._Stage_key" +
				"\nand v._Assay_key = " + searchDomain.getAssayKey() + ")" +
				"\nand s._set_key = 1046" +
				"\nand s._setmember_key = s2._setmember_key" +
				"\nand s._object_key = t1._term_key" +
				"\nand s2._Stage_key = t2._stage_key" +
				"\nand s._CreatedBy_key = u._user_key" +
				"\nand u.login = '" + searchDomain.getCreatedBy() + "'" +		
				"\nunion all" +
				"\nselect i.displayIt||' ('||count(*)||')' as displayIt, term, stage," +
				"\n0 as setMemberKey, 0 as setKey, i._emapa_term_key as objectKey, 0 as createdByKey, null as createdBy," +
				"\nmin(i.sequenceNum), 0 as orderBy" +				
				"\nfrom GXD_GelLaneStructure_View i, GXD_GelLane s" +
				"\nwhere s._GelLane_key = i._GelLane_key" +
				"\nand s._Assay_key = " + searchDomain.getAssayKey() +
				"\ngroup by _EMAPA_Term_key, _Stage_key, displayIt, term, stage" +
				"\n) order by orderBy, sequenceNum, stage, term";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberEmapaDomain domain = new MGISetMemberEmapaDomain();
				domain.setProcessStatus("x");
				domain.setSetKey(rs.getString("setKey"));
				domain.setSetMemberKey(rs.getString("setMemberKey"));
				domain.setObjectKey(rs.getString("objectKey"));
				domain.setTerm(rs.getString("term"));
				domain.setStage(rs.getString("stage"));
				domain.setCreatedByKey(rs.getString("createdByKey"));
				domain.setCreatedBy(rs.getString("login"));
				
				displayIt = rs.getString("displayIt").replaceAll("\\(1\\)", "");
				domain.setDisplayIt(displayIt);

				domain.setIsUsed(false);
				results.add(domain);				
				assayDAO.clear();
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@Transactional		
	public Boolean gxdexpressionUtilities(String assayKey) throws IOException, InterruptedException {
		// see mgicacheload/gxdexpression.py
		
		// these swarm variables are in 'app.properties'
    	String utilitiesScript = System.getProperty("swarm.ds.gxdexpressionUtilities");
    	String server = System.getProperty("swarm.ds.dbserver");
        String db = System.getProperty("swarm.ds.dbname");
        String username = System.getProperty("swarm.ds.username");
        String pwd = System.getProperty("swarm.ds.dbpasswordfile");
        
        // input:  assayKey

        // output: true/false
        Boolean returnCode = false;
        
		String runCmd = utilitiesScript;
        runCmd = runCmd + " -S" + server;
        runCmd = runCmd + " -D" + db;
        runCmd = runCmd + " -U" + username;
        runCmd = runCmd + " -P" + pwd;             
		runCmd = runCmd + " -K" + assayKey;
		
		// run the runCmd
		log.info(Constants.LOG_INPROGRESS_EIUTILITIES + runCmd);
		RunCommand runner = RunCommand.runCommand(runCmd);
		
		// check exit code from RunCommand
		if (runner.getExitCode() == 0) {
			log.info(Constants.LOG_SUCCESS_EIUTILITIES);
			returnCode = true;
		}
		else {
			log.info(Constants.LOG_FAIL_EIUTILITIES);
			returnCode = false;
		}			
		
		return returnCode;
	}

	@Transactional
	public List<GenotypeReplaceDomain> processReplaceGenotype(GenotypeReplaceDomain domain) {
		// select * from GXD_replaceGenotype (user, refsKey, currentGenotypeKey, newGenotypeKey)
		
		List<GenotypeReplaceDomain> results = new ArrayList<GenotypeReplaceDomain>();		

	    String cmd = "select count(*) from GXD_replaceGenotype('" + domain.getCreatedBy() + "'," 
	    			+ domain.getRefsKey() + "," 
	    			+ domain.getCurrentKey() + "," 
	    			+ domain.getNewKey() + ")";
	    Query query;
		
	    log.info(cmd);
	    query = assayDAO.createNativeQuery(cmd);
	    query.getResultList();
		
		return results;
	}

	@Transactional
	public List<SlimAssayDomain> addToEmapaClipboard(SlimAssayDomain domain) {
		// select * from GXD_addEMAPSet (user, assayKey)
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

	    String cmd = "select count(*) from GXD_addEMAPASet('" + domain.getCreatedBy() + "'," 
	    			+ domain.getAssayKey() + ")";
	    Query query;
		
	    log.info(cmd);
	    query = assayDAO.createNativeQuery(cmd);
	    query.getResultList();
		
		return results;
	}
	
}
