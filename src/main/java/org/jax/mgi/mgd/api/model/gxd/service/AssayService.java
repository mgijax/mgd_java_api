package org.jax.mgi.mgd.api.model.gxd.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceCitationCacheDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyPrepDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayTypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.ExpressionCacheDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.GenotypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.ProbePrepDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeReplaceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDLDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimCellTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimEmapaDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryResultDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.GenotypeTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SummaryResultTranslator;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberCellTypeDomain;
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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

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
	private ReferenceCitationCacheDAO referenceDAO;
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
	private GenotypeDAO genotypeDAO;
	@Inject
	private ExpressionCacheDAO expressionCacheDAO;
	@Inject
	private AssayNoteService assayNoteService;
	@Inject
	private SpecimenService specimenService;
	@Inject
	private GelLaneService gelLaneService;
	@Inject
	private GelRowService gelRowService;
	@Inject
	private AntibodyPrepService antibodyPrepService;
	@Inject
	private ProbePrepService probePrepService;
	
	private AssayTranslator translator = new AssayTranslator();
	private SlimAssayTranslator slimtranslator = new SlimAssayTranslator();
	private GenotypeTranslator genotypetranslator = new GenotypeTranslator();
	private SummaryResultTranslator summaryresulttranslator = new SummaryResultTranslator();
	
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
		String cmd;
		Query query;
		
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
			
		if (domain.getIsGel()) {
			if (domain.getImagePane().getImagePaneKey() == null || domain.getImagePane().getImagePaneKey().isEmpty() ) {
				entity.setImagePane(null);		
			}
			else {
				entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.getImagePane().getImagePaneKey())));	
			}
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
			List<GelLaneDomain> laneResults = new ArrayList<GelLaneDomain>();
			laneResults = gelLaneService.process(entity.get_assay_key(), Integer.valueOf(domain.getAssayTypeKey()), domain.getGelLanes(), user);
			domain.setGelLanes(laneResults);			
		}

		// process gxd_gelrow
		// if assaytype in Gel Assay Type
		if (domain.getGelRows() != null && !domain.getGelRows().isEmpty()) {
			if (gelRowService.process(entity.get_assay_key(), domain.getGelRows(), domain.getGelLanes(), user)) {
				modified = true;
			}
		}

		if (domain.getDetectionKey().equals("2")) {
			// add antibody/reference
			cmd = "select count(*) from MGI_insertReferenceAssoc (" + user.get_user_key() + ",6," + entity.getAntibodyPrep().getAntibody().get_antibody_key() + "," + domain.getRefsKey() + ",1027)";
			log.info("processAssay/process MGI_insertReferenceAssoc(): " + cmd);
			query = assayDAO.createNativeQuery(cmd);
			query.getResultList();			
		}
		else if (domain.getDetectionKey().equals("1")) {
			cmd = "select count(*) from PRB_insertReference (" + user.get_user_key() + "," + domain.getRefsKey() + "," + entity.getProbePrep().getProbe().get_probe_key() + ")";
			log.info("processAssay/process PRB_insertReference(): " + cmd);
			query = assayDAO.createNativeQuery(cmd);
			query.getResultList();
		}
		
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

		if (domain.getIsGel()) {
			if (domain.getImagePane().getImagePaneKey() == null || domain.getImagePane().getImagePaneKey().isEmpty() ) {
				entity.setImagePane(null);		
			}
			else {
				entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.getImagePane().getImagePaneKey())));	
			}
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
			List<GelLaneDomain> laneResults = new ArrayList<GelLaneDomain>();
			laneResults = gelLaneService.process(Integer.valueOf(domain.getAssayKey()), Integer.valueOf(domain.getAssayTypeKey()), domain.getGelLanes(), user);
			domain.setGelLanes(laneResults);
			modified = true;
		}
		
		// process gxd_gelrow
		// if assaytype in Gel Assay Type
		if (domain.getGelRows() != null && !domain.getGelRows().isEmpty()) {
			if (gelRowService.process(Integer.valueOf(domain.getAssayKey()), domain.getGelRows(), domain.getGelLanes(), user)) {
				modified = true;
			}
		}
		
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
		log.info("processAssay/process MGI_resetSequenceNum: " + cmd);
		query = assayDAO.createNativeQuery(cmd);
		query.getResultList();
		
		// process order reset
		cmd = "select count(*) from MGI_resetSequenceNum ('GXD_GelLane'," + entity.get_assay_key() + "," + user.get_user_key() + ")";
		log.info("processAssay/process MGI_resetSequenceNum: " + cmd);
		query = assayDAO.createNativeQuery(cmd);
		query.getResultList();
		
		if (domain.getDetectionKey().equals("2")) {
			// add antibody/reference
			cmd = "select count(*) from MGI_insertReferenceAssoc (" + user.get_user_key() + ",6," + entity.getAntibodyPrep().getAntibody().get_antibody_key() + "," + domain.getRefsKey() + ",1027)";
			log.info("processAssay/process MGI_insertReferenceAssoc(): " + cmd);
			query = assayDAO.createNativeQuery(cmd);
			query.getResultList();			
		}
		else if (domain.getDetectionKey().equals("1")) {
			cmd = "select count(*) from PRB_insertReference (" + user.get_user_key() + "," + domain.getRefsKey() + "," + entity.getProbePrep().getProbe().get_probe_key() + ")";
			log.info("processAssay/process PRB_insertReference(): " + cmd);
			query = assayDAO.createNativeQuery(cmd);
			query.getResultList();
		}
		
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
		String select = "select distinct a._assay_key, r._refs_key, r.jnumid, r.numericpart, t.assayType, m.symbol";
		String from = "from gxd_assay a, gxd_assaytype t, bib_citation_cache r, mrk_marker m";
		String where = "where a._assaytype_key = t._assaytype_key"
				+ "\nand a._refs_key = r._refs_key"
				+ "\nand a._marker_key = m._marker_key";
		String orderBy = "order by r.numericpart, t.assayType, m.symbol";
		String value;
		BigDecimal bigDec;
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
		Boolean from_gelband = false;
		Boolean from_gelrow = false;
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
		
		if (searchDomain.getImagePane() != null) {
			value = searchDomain.getImagePane().getImagePaneKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand a._imagepane_key = " + value;		
			}
		}
		
		value = searchDomain.getReporterGeneKey();
		if (value != null && !value.isEmpty()) {
			where = where + "\nand a._reportergene_key = " + value;		
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
			
			value = searchDomain.getSpecimens().get(0).getSpecimenLabel().replaceAll("'","''");
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
			value = searchDomain.getGelLanes().get(0).getLaneLabel().replaceAll("'","''");
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
			
			if (searchDomain.getGelLanes().get(0).getGelBands() != null) {
				value = searchDomain.getGelLanes().get(0).getGelBands().get(0).getStrengthKey();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand gb._strength_key = " + value;				
					from_gelband = true;
					from_gelrow = true;
				}				
				value = searchDomain.getGelLanes().get(0).getGelBands().get(0).getBandNote();
				if (value != null && !value.isEmpty()) {
					where = where + "\nand gb.bandNote ilike '" + value + "'";				
					from_gelband = true;
					from_gelrow = true;
				}					
			}			
		}

		if (searchDomain.getGelRows() != null) {
			value = searchDomain.getGelRows().get(0).getGelUnitsKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand gr._gelunits_key = " + value;				
				from_gelrow = true;
			}
			bigDec = searchDomain.getGelRows().get(0).getSize();
			if (bigDec != null) {
				where = where + "\nand gr.size = " + bigDec;				
				from_gelrow = true;
			}
			value = searchDomain.getGelRows().get(0).getRowNote();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand gr.rowNote ilike '" + value + "'";				
				from_gelrow = true;
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
		
		if (searchDomain.getAssayNote() != null) {
			if (searchDomain.getAssayNote().getAssayNote() != null && !searchDomain.getAssayNote().getAssayNote().isEmpty()) {
				value = searchDomain.getAssayNote().getAssayNote();
				where = where + "\nand n.assaynote ilike '" + value + "'";
				from_assaynote = true;
			}
		}
		
		// check detectionKey
		if (searchDomain.getDetectionKey().equals("1")) {
			from_probeprep = true;
		}
		else if (searchDomain.getDetectionKey().equals("2")) {
			from_antibodyprep = true;
		}
		else if (searchDomain.getDetectionKey().equals("3")) {
			where = where + "\nand a._probeprep_key is null";
			where = where + "\nand a._antibodyprep_key is null";			
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
		if (from_gelband == true) {
			from = from + ", gxd_gelband gb";
			where = where + "\nand gr._gelrow_key = gb._gelrow_key";
		}		
		if (from_gelrow == true) {
			from = from + ", gxd_gelrow gr";
			where = where + "\nand a._assay_key = gr._assay_key";
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
				domain.setAssayDisplay(rs.getString("jnumid") + "; " + domain.getAssayTypeAbbrev() + "; " + rs.getString("symbol"));	
				domain.setRefsKey(rs.getString("_refs_key"));
				domain.setJnumid(rs.getString("jnumid"));
				domain.setJnum(rs.getString("numericpart"));				
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
				
				// translate the genotype to get some more information
				GenotypeDomain gdomain = new GenotypeDomain();
				gdomain = genotypetranslator.translate(genotypeDAO.get(rs.getInt("objectKey")));
				
				domain.setStrain(gdomain.getStrain());
				
				if (gdomain.getAlleleDetailNote() != null) {
					String alleleDetailNote = gdomain.getAlleleDetailNote().getNoteChunk().replaceAll("\\n", ",");
					domain.setAlleleDetailNote(alleleDetailNote);
				}

				if (gdomain.getIsConditional().equals("1")) {
					domain.setIsConditional("Conditional mutant");
				}
				else {
					domain.setIsConditional("");
				}
				
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
	public List<MGISetMemberCellTypeDomain> getCellTypeInSituBySetUser(SlimCellTypeDomain searchDomain) {
		// return 
		// all set members of cell type (_set_key = 1059) + user (searchDomain.getCreatedByKey())
		// union
		// all cell types for given specimen (searchDomain.getSpecimienKey())

		List<MGISetMemberCellTypeDomain> results = new ArrayList<MGISetMemberCellTypeDomain>();		
		String displayIt = "";
		
		// search mgi_setmembers where _set_key = 1059 (cell type)
		String cmd = 
				"\n(select distinct '*'||t.term as displayIt, t.term," +
				"\ns._setmember_key as setMemberKey, s._set_key as setKey, s._object_key as objectKey, s._createdby_key as createdByKey, u.login," +
				"\ns.sequenceNum as sequenceNum, 1 as orderBy" +
				"\nfrom mgi_setmember s, voc_term t, mgi_user u" +
				"\nwhere not exists (select 1 from GXD_ISResultCellType_View v where s._Object_key = v._CellType_Term_key" +
				"\nand v._Specimen_key = " + searchDomain.getSpecimenKey() + ")" +
				"\nand s._set_key = 1059" +
				"\nand s._object_key = t._term_key" +
				"\nand s._CreatedBy_key = u._user_key" +
				"\nand u.login = '" + searchDomain.getCreatedBy() + "'" +		
				"\nunion all" +
				"\nselect i.displayIt||' ('||count(*)||')' as displayIt, term," +
				"\n0 as setMemberKey, 0 as setKey, i._CellType_term_key as objectKey, 0 as createdByKey, null as createdBy," +
				"\nmin(i.sequenceNum), 0 as orderBy" +				
				"\nfrom GXD_ISResultCellType_View i, GXD_Specimen s" +
				"\nwhere s._Specimen_key = i._Specimen_key" +
				"\nand s._Specimen_key = " + searchDomain.getSpecimenKey() +
				"\ngroup by _CellType_Term_key, displayIt, term" +
				"\n) order by orderBy, sequenceNum, term";

		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberCellTypeDomain domain = new MGISetMemberCellTypeDomain();
				domain.setProcessStatus("x");
				domain.setSetKey(rs.getString("setKey"));
				domain.setSetMemberKey(rs.getString("setMemberKey"));
				domain.setObjectKey(rs.getString("objectKey"));
				domain.setTerm(rs.getString("term"));
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
	public List<SlimAssayDomain> addToEmapaClipboard(SlimAssayDomain domain) {
		// select * from GXD_addEMAPSet (user, assayKey)
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

	    String cmd = "select count(*) from GXD_addEMAPASet('" + domain.getCreatedBy() + "'," + domain.getAssayKey() + ")";
	    Query query;
		
	    log.info(cmd);
	    query = assayDAO.createNativeQuery(cmd);
	    query.getResultList();
		
		return results;
	}

	@Transactional
	public List<SlimAssayDomain> addToCellTypeClipboard(SlimAssayDomain domain) {
		// select * from GXD_addCellTypeSet (user, assayKey)
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

	    String cmd = "select count(*) from GXD_addCellTypeSet('" + domain.getCreatedBy() + "'," + domain.getAssayKey() + ")";
	    Query query;
		
	    log.info(cmd);
	    query = assayDAO.createNativeQuery(cmd);
	    query.getResultList();
		
		return results;
	}
	
	@Transactional
	public List<SlimAssayDomain> addToGenotypeClipboard(SlimAssayDomain domain) {
		// select * from GXD_addGenotypeSet (user, assayKey)
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

	    String cmd = "select count(*) from GXD_addGenotypeSet('" + domain.getCreatedBy() + "'," + domain.getAssayKey() + ")";
	    Query query;
		
	    log.info(cmd);
	    query = assayDAO.createNativeQuery(cmd);
	    query.getResultList();
		
		return results;
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

	// ---------------------------------------------------------------------------------------
	// ----------- GET/DOWNLOAD ASSAYS -------------------------------------------------------
	// ---------------------------------------------------------------------------------------

	// get assay by allele

	@Transactional	
	public List<SlimAssayDomain> getAssayByAllele(String accid) {
		// return list of assay domains by allele acc id
		String cmd = getAssayBySQL("allele", accid);
		return processSlimAssayDomain(cmd);
	}

	public Response downloadAssayByAllele(String accid) {
		String cmd = getAssayBySQL("allele", accid);
		return download(cmd, getTsvFileName("getAssayByAllele", accid), new AssayFormatter());
	}

	// get assay by antibody

	@Transactional	
	public List<SlimAssayDomain> getAssayByAntibody(String accid) {
		// return list of assay domains by antibody acc id
		String cmd = getAssayBySQL("antibody", accid);
		return processSlimAssayDomain(cmd);
	}

	public Response downloadAssayByAntibody(String accid) {
	    String cmd = getAssayBySQL("antibody", accid);
	    return download(cmd, getTsvFileName("getAssayByAntibody", accid), new AssayFormatter());
	}

	// get assay by marker
		
	@Transactional	
	public List<SlimAssayDomain> getAssayByMarker(String accid) {
		// return list of assay domains by marker acc id
		String cmd = getAssayBySQL("marker", accid);
		return processSlimAssayDomain(cmd);
	}

	public Response downloadAssayByMarker(String accid) {
	    String cmd = getAssayBySQL("marker", accid);
	    return download(cmd, getTsvFileName("getAssayByMarker", accid), new AssayFormatter());
	}

	// get assay by probe
		
	@Transactional	
	public List<SlimAssayDomain> getAssayByProbe(String accid) {
		// return list of assay domains by probe acc id
		String cmd = getAssayBySQL("probe", accid);
		return processSlimAssayDomain(cmd);
	}
		
	public Response downloadAssayByProbe(String accid) {
	    String cmd = getAssayBySQL("probe", accid);
	    return download(cmd, getTsvFileName("getAssayByProbe", accid), new AssayFormatter());
	}

	// get assay by reference
		
	@Transactional	
	public List<SlimAssayDomain> getAssayByRef(String jnumid) {
		// return list of assay domains by reference jnum id
		String cmd = getAssayBySQL("reference", jnumid);
		return processSlimAssayDomain(cmd);
	}

	public Response downloadAssayByRef(String accid) {
	    String cmd = getAssayBySQL("reference", accid);
	    return download(cmd, getTsvFileName("getAssayByReference", accid), new AssayFormatter());
	}

	// Formatter for assay downloads. 
	public static class AssayFormatter implements TsvFormatter {
		public String format (ResultSet obj) {
			String[][] cols = {
				{"Assay ID",       "assayid"},
				{"Gene",           "markersymbol"},
				{"Gene MGI ID",    "markerid"},
				{"Assay Type",     "assaytype"},
				{"Reference J#",   "jnumid"},
				{"Short Citation", "short_citation"},
			};
			return formatTsvHelper(obj, cols);
		}
	}

	public String getAssayBySQL (String queryType, String accid) {
		
		String cmd ;
		String with = "";
		String select = "\nselect distinct a._assay_key, ea.accid as assayid, ma.accid as markerid, m.symbol as markersymbol, at.assaytype, at.sequenceNum, r.jnumid, r.short_citation " ;
		String from = "\nfrom gxd_assay a, acc_accession ea, mrk_marker m,  acc_accession ma, gxd_assaytype at, bib_citation_cache r " ;
		String where = "\nwhere a._assay_key = ea._object_key " +
			"\nand ea._mgitype_key = 8 " +
			"\nand ea._logicaldb_key = 1 " +
			"\nand ea.preferred = 1 " +
			"\nand a._marker_key = m._marker_key " +
			"\nand m._marker_key = ma._object_key " +
			"\nand ma._mgitype_key = 2 " +
			"\nand ma._logicaldb_key = 1 " +
			"\nand ma.preferred = 1 " +
			"\nand a._assaytype_key = at._assaytype_key " +
			"\nand a._refs_key = r._refs_key " +
			"";
		
		switch (queryType) {
		case "allele":
			with = "with genoTemp as ( " +
				"\nselect distinct s._genotype_key, s._assay_key from gxd_specimen s " +
				"\nunion " +
				"\nselect distinct l._genotype_key, l._assay_key from gxd_gellane l " +
				"\n)"  +
				"" ;
			from += ", genoTemp gt, gxd_genotype g, gxd_allelegenotype ag, acc_accession aa ";

			where += "\n and a._assay_key = gt._assay_key " + 
				"\n and gt._genotype_key = g._genotype_key " +
				"\n and g._genotype_key = ag._genotype_key " +
				"\n and ag._allele_key = aa._object_key " +
				"\n and aa._mgitype_key = 11 " +
				"\n and aa._logicaldb_key = 1 " +
				"\n and aa.accid = '" + accid + "'" +
				"\n";
			break;
		case "antibody":
			from += ", gxd_antibodyprep ap, acc_accession aa ";

			where += "\n    and a._antibodyprep_key = ap._antibodyprep_key " +
			       "\n    and ap._antibody_key = aa._object_key " +
			       "\n    and aa._mgitype_key = 6 " +
			       "\n    and aa._logicaldb_key = 1 " +
			       "\n    and aa.accid = '" + accid + "'" +
			       "\n";
			break;
		case "probe":
			from += ", gxd_probeprep pp, acc_accession pa ";

			where += "\n    and a._probeprep_key = pp._probeprep_key " +
			       "\n    and pp._probe_key = pa._object_key " +
			       "\n    and pa._mgitype_key = 3 " +
			       "\n    and pa._logicaldb_key = 1 " +
			       "\n    and pa.accid = '" + accid + "'" +
			       "\n";
			break;
		case "marker":
			where += "\nand ma.accid = '" + accid + "'";
			break;
		case "reference":
			where += "\nand r.jnumid = '" + accid + "'";
			break;
		default:
			throw new RuntimeException("Unknown queryType: " + queryType);
		}

		String orderby = "\norder by m.symbol, at.sequenceNum, r.short_citation, ea.accid ";

		cmd = with + select + from + where + orderby;
		return cmd;
	}

	public List<SlimAssayDomain> processSlimAssayDomain(String cmd) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();
		log.info("processSlimAssayDomain: " + cmd);	
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAssayDomain domain = new SlimAssayDomain();
				domain = slimtranslator.translate(assayDAO.get(rs.getInt("_assay_key")));
				results.add(domain);
				assayDAO.clear();			
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("error: " + e.toString());	
		}		
		return results;
	}

	// ---------------------------------------------------------------------------------------
	// ----------- GET/DOWNLOAD RESULTS ------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	
	// results by cell type

	@Transactional	
	public SearchResults<SummaryResultDomain> getResultByCellType(String accid, int offset, int limit) {
		// return list of summary results domains by cell type id

		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();
		
		String cmd = getResultBySQL("celltype", accid, offset, limit, true);
		results.total_count = processSummaryResultCount(cmd);
		
		cmd = getResultBySQL("celltype", accid, offset, limit, false);
		results.items = processSummaryResultDomain(accid, offset, limit, cmd);
		
		return results;
	}

	@Transactional	
	public Response downloadResultByCellType(String accid) {
		String cmd = getResultBySQL("celltype", accid, -1, -1, false);
		return download(cmd, getTsvFileName("getResultByCellType", accid), new ResultFormatter());
	}
	
	// results by marker

	@Transactional	
	public SearchResults<SummaryResultDomain> getResultByMarker(String accid, int offset, int limit) {
		// return list of summary results domains by marker acc id

		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();
		
		String cmd = getResultBySQL("marker", accid, offset, limit, true);
		results.total_count = processSummaryResultCount(cmd);
		
		cmd = getResultBySQL("marker", accid, offset, limit, false);
		results.items = processSummaryResultDomain(accid, offset, limit, cmd);

		return results;
	}
	
	@Transactional	
	public Response downloadResultByMarker(String accid) {
		String cmd = getResultBySQL("marker", accid, -1, -1, false);
		return download(cmd, getTsvFileName("getResultByMarker", accid), new ResultFormatter());
	}

	// result by reference

	@Transactional	
	public SearchResults<SummaryResultDomain> getResultByRef(String accid, int offset, int limit) {
		// return list of summary results domains by reference jnum id

		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();
		
		String cmd = getResultBySQL("reference", accid, offset, limit, true);
		results.total_count = processSummaryResultCount(cmd);
		
		cmd = getResultBySQL("reference", accid, offset, limit, false);
		results.items = processSummaryResultDomain(accid, offset, limit, cmd);

		return results;
	}
	
	@Transactional	
	public Response downloadResultByRef(String accid) {
		String cmd = getResultBySQL("reference", accid, -1, -1, false);
		return download(cmd, getTsvFileName("getResultByRef", accid), new ResultFormatter());
	}
	
	// result by structure

	@Transactional	
	public SearchResults<SummaryResultDomain> getResultByStructure(String accid, int offset, int limit) {
		// return list of summary results domains by structure/emapa id

		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();

		String cmd = getResultBySQL ("structure", accid, offset, limit, true);
		results.total_count = processSummaryResultCount(cmd);
		
		cmd = getResultBySQL ("structure", accid, offset, limit, false);
		results.items = processSummaryResultDomain(accid, offset, limit, cmd);
		
		return results;
	}
	
	@Transactional	
	public Response downloadResultByStructure(String accid) {
		String cmd = getResultBySQL ("structure", accid, -1, -1, false);
		return download(cmd, getTsvFileName("getResultByStructure", accid), new ResultFormatter());
	}

	public static class ResultFormatter implements TsvFormatter {
		public String format (ResultSet obj) {
			String[][] cols = {
				{"Assay ID",       "assayid"},
				{"Marker Symbol",  "markersymbol"},
				{"Assay Type",     "assaytype"},
				{"Age",            "age"},
				{"Stage",          "stage"},
				{"Structure",      "structure"},
				{"Cell Type",      "celltype"},
				{"Strength",       "strength"},
				{"Specimen Label", "specimenlabel"},
				{"Mutant Allele",  "mutantalleles"},
				{"IsConditional",  "isConditional"},
				{"Result Note",    "resultNote"}
			};
			return formatTsvHelper(obj, cols);
		}
	}
	
	public String getResultBySQL(String queryType, String accid, int offset, int limit, boolean returnCount) {
		
		String cmd;
		String joinPoint;
		int mgiTypeKey;
		int logicalDbKey;
		String stageClause = "";
		
		switch (queryType) {
		case "reference":
			joinPoint = "_refs_key";
			mgiTypeKey = 1;
			logicalDbKey = 1;
			break;
		case "marker":
			joinPoint = "_marker_key";
			mgiTypeKey = 2;
			logicalDbKey = 1;
			break;
		case "celltype":
			joinPoint = "_celltype_term_key";
			mgiTypeKey = 13;
			logicalDbKey = 173;
			break;
		case "structure":
			joinPoint = "_emapa_term_key";
			mgiTypeKey = 13;
			logicalDbKey = 169;
        		if (accid.startsWith("EMAPS:")) {
				int alen = accid.length();
				String stage = accid.substring(alen-2, alen);
				accid = "EMAPA:" + accid.substring(6, alen-2);
				stageClause = "\nand e._stage_key = " + stage;
        		}
			break;
		default:
			throw new RuntimeException("Unrecognized parameter value: " + queryType);
		}

		if (returnCount) {
			cmd = "\nselect count(*) as total_count" +
				"\nfrom acc_accession a, gxd_expression e" +
				"\nwhere a._object_key = e." + joinPoint +
				"\nand a._mgitype_key = " + mgiTypeKey +
				"\nand a._logicaldb_key = " + logicalDbKey + 
				"\nand a.accid = '" + accid + "'"
				+ stageClause;
		} else {
			cmd = "\nselect e._expression_key, aa.accid as assayid, m.symbol as markersymbol, gt.assaytype, e.age, e._stage_key as stage, st.term as structure, ct.term as celltype, e.strength, e.resultnote, sp.specimenlabel, mn.note as mutantalleles, gg.isconditional" +
				"\nfrom acc_accession a, acc_accession aa," +
				"\ngxd_expression e " +
				"\n     left outer join voc_term ct on (e._celltype_term_key = ct._term_key)" +
				"\n     left outer join gxd_specimen sp on (e._specimen_key = sp._specimen_key)" +
				"\n     left outer join mgi_note mn on (e._genotype_key = mn._object_key and mn._notetype_key = 1016)," +
				"\nvoc_term st, mrk_marker m, gxd_assaytype gt, gxd_genotype gg" +
				"\nwhere e._emapa_term_key = st._term_key" +
				"\nand e._genotype_key = gg._genotype_key" +
				"\nand e._marker_key = m._marker_key" +
				"\nand e._assaytype_key = gt._assaytype_key" +
				"\nand aa._object_key = e._assay_key" +
				"\nand aa._mgitype_key = 8" +
				"\nand aa._logicaldb_key = 1" +
				"\nand aa.preferred = 1" +
				"\nand a._object_key = e." + joinPoint + 
				"\nand a._mgitype_key = " + mgiTypeKey +
				"\nand a._logicaldb_key = " + logicalDbKey + 
				"\nand a.accid = '" + accid + "'"
				+ stageClause;
			cmd = addPaginationSQL(cmd,
			          "e.isforgxd desc, e._stage_key, st.term, ct.term, m.symbol, gt.sequenceNum, aa.accid, sp.specimenlabel",
				  offset, limit);
		}
		return cmd;
	}
	
	@Transactional	
	public Long processSummaryResultCount(String cmd) {
		// return count of summary results domains using search cmd

		Long total_count = null;
		
		log.info(cmd);
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				total_count = rs.getLong("total_count");
				expressionCacheDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	

		return total_count;
	}
	
	@Transactional	
	public List<SummaryResultDomain> processSummaryResultDomain(String id, int offset, int limit, String cmd) {
		// return list of summary results domains by search cmd

		List<SummaryResultDomain> summaryResults = new ArrayList<SummaryResultDomain>();

		// attach most of the sorting rules
		log.info(cmd);	

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SummaryResultDomain domain = new SummaryResultDomain();
				domain = summaryresulttranslator.translate(expressionCacheDAO.get(rs.getInt("_expression_key")));
				summaryResults.add(domain);
				expressionCacheDAO.clear();				
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		

		return summaryResults;
	}
	
	@Transactional
	public List<SlimAssayDLDomain> getAssayDLByKey(String assayKey) {
		// return assay info for an Assay for which another Assay exists with this criteria:
		//
		// 1. Assay Type in (1,6,9)
		// 2. same Reference (J:)
		// 3. same Image Pane
		//

		List<SlimAssayDLDomain> results = new ArrayList<SlimAssayDLDomain>();
		
		String value = "";
		String cmd = "\nselect * from GXD_Assay_DLTemplate_View where _assay_key = " + assayKey;
		log.info(cmd);	

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			Integer prevKey = 0;
			List<String> assayTypes = new ArrayList<String>();
			List<String> assayExtraWords = new ArrayList<String>();
			List<String> assayIDs = new ArrayList<String>();
			List<String> markers = new ArrayList<String>();
			SlimAssayDLDomain domain = new SlimAssayDLDomain();
			
			while (rs.next()) {
				
				if (prevKey != rs.getInt("sequenceNum")) {
										
					if (prevKey > 0) {
						domain.setAssayTypes(String.join("|", assayTypes));
						domain.setAssayExtraWords(String.join("|",  assayExtraWords));
						domain.setAssayIDs(String.join("|", assayIDs));
						domain.setMarkers(String.join("|", markers));
						results.add(domain);
						assayTypes.clear();
						assayExtraWords.clear();
						assayIDs.clear();
						markers.clear();
					}
					
					domain = new SlimAssayDLDomain();
					domain.setSpecimenKey(rs.getString("_specimen_key"));
					domain.setSpecimenLabel(rs.getString("specimenlabel"));
					domain.setAssayKey(rs.getString("_assay_key"));
					value = rs.getString("at1");
					domain.setAssayTypeKey1(value);
					value = value.replaceAll("1", " mRNA");
					value = value.replaceAll("6", " protein");
					value = value.replaceAll("9", " reporter");
					value = value.replaceAll("10", " reporter");
					value = value.replaceAll("11", " reporter");
					domain.setAssayExtraWords1(value);
					prevKey = rs.getInt("sequenceNum");
				}
				
				// add at2, accid, symbol
				value = rs.getString("at2");
				assayTypes.add(value);
				value = value.replaceAll("1", " mRNA");
				value = value.replaceAll("6", " protein");
				value = value.replaceAll("9", " reporter");
				value = value.replaceAll("10", " reporter");
				value = value.replaceAll("11", " reporter");
				assayExtraWords.add(value);
				assayIDs.add(rs.getString("accid"));
				markers.add(rs.getString("symbol"));
				
				if (rs.isLast()) {
					domain.setAssayTypes(String.join("|", assayTypes));
					domain.setAssayExtraWords(String.join("|",  assayExtraWords));
					domain.setAssayIDs(String.join("|", assayIDs));
					domain.setMarkers(String.join("|", markers));
					results.add(domain);
				}
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}

