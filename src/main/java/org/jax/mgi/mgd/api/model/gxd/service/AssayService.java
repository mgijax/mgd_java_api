package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyPrepDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayTypeDAO;
import org.jax.mgi.mgd.api.model.gxd.dao.ProbePrepDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimAssayTranslator;
import org.jax.mgi.mgd.api.model.img.dao.ImagePaneDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
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
	@Inject
	private GelRowService gelRowService;
	
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
			if (gelLaneService.process(entity.get_assay_key(), domain.getGelLanes(), user)) {
				modified = true;
			}
		}

		// process gxd_gelrow
		// if assaytype in Gel Assay Type
		if (domain.getGelRows() != null && !domain.getGelRows().isEmpty()) {
			if (gelRowService.process(entity.get_assay_key(), domain.getGelRows(), user)) {
				modified = true;
			}
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
		
		log.info("processAssay/update");
		
		entity.setAssayType(assayTypeDAO.get(Integer.valueOf(domain.getAssayTypeKey())));	
		entity.setReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));
		
		if (domain.getAntibodyPrep() != null) {
			entity.setAntibodyPrep(antibodyPrepDAO.get(Integer.valueOf(domain.getAntibodyPrep().getAntibodyPrepKey())));				
		}

		if (domain.getProbePrep() != null) {
			entity.setProbePrep(probePrepDAO.get(Integer.valueOf(domain.getProbePrep().getProbePrepKey())));				
		}

		if (domain.getImagePaneKey() != null) {
			entity.setImagePane(imagePaneDAO.get(Integer.valueOf(domain.getImagePaneKey())));		
		}

		if (domain.getReporterGeneKey() != null) {
			entity.setReporterGene(termDAO.get(Integer.valueOf(domain.getReporterGeneKey())));		
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
			if (gelLaneService.process(Integer.valueOf(domain.getAssayKey()), domain.getGelLanes(), user)) {
				modified = true;
			}
		}

		// process gxd_gelrow
		// if assaytype in Gel Assay Type
		if (domain.getGelRows() != null && !domain.getGelRows().isEmpty()) {
			if (gelRowService.process(Integer.valueOf(domain.getAssayKey()), domain.getGelRows(), user)) {
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
		String select = "select a._assay_key, r.jnumid, t.assayType, m.symbol";
		String from = "from gxd_assay a, gxd_assaytype t, bib_citation_cache r, mrk_marker m";
		String where = "where a._assaytype_key = t._assaytype_key"
				+ "\nand a._refs_key = r._refs_key"
				+ "\nand a._marker_key = m._marker_key";
		String orderBy = "order by r.jnumid, t.assayType, m.symbol";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
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
		
		if (searchDomain.getSpecimens() != null) {
			value = searchDomain.getSpecimens().get(0).getSpecimenLabel();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.specimenLabel ilike '" + value + "'";				
				from_specimen = true;
			}
			value = searchDomain.getSpecimens().get(0).getGenotypeID();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand g.accID ilike '" + value + "'";	
				from_genotype = true;
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getAgePrefix();	
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.age ilike '" + value + "%'";				
				from_specimen = true;
			}			
			value = searchDomain.getSpecimens().get(0).getAgePostfix();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand s.age ilike '%" + value + "'";				
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
				where = where + "\nand s.fixation_key = " + value;							
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
				where = where + "\nand s.specimenNote ilike '" + value + "'";				
				from_specimen = true;
			}			
		}
		
		if (searchDomain.getGelLanes() != null) {
			value = searchDomain.getGelLanes().get(0).getLaneLabel();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.laneLabel ilike '" + value + "'";				
				from_gellane = true;
			}
			value = searchDomain.getGelLanes().get(0).getGenotypeID();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand g.accID ilike '" + value + "'";	
				from_genotype = true;
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getAgePrefix();	
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.age ilike '" + value + "%'";				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getAgePostfix();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.age ilike '%" + value + "'";				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getAgeNote();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.ageNote ilike '" + value + "'";				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getSex();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.sex ilike '" + value + "'";				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getGelRNATypeKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l._gelrnatype_key = " + value;				
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getGelControlKey();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l._gelcontrol_key = " + value;
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getSampleAmount();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.sampleAmount ilike '" + value + "'";			
				from_gellane = true;
			}			
			value = searchDomain.getGelLanes().get(0).getLaneNote();
			if (value != null && !value.isEmpty()) {
				where = where + "\nand l.laneNote ilike '" + value + "'";				
				from_gellane = true;
			}			
		}
		
		// assay accession id 
		value = searchDomain.getAccID();			
		if (value != null && !value.isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + value + "'";
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
			from = from + ", gxd_gellane l";
			where = where + "\nand a._assay_key = l._assay_key";
		}		
		if (from_genotype == true) {
			from = from + ", gxd_genotype_acc_view g";
			where = where + "\nand a._assay_key = g._object_key";
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
	public List<MGISetDomain> getGenotypesBySetUser(SlimAssayDomain searchDomain) {
		// return all genotypes for given assay (searchDomain.getAssayKey())
		// union set members of genotype (_set_key = 1055) + user (searchDomain.getCreatedByKey())

		List<MGISetDomain> results = new ArrayList<MGISetDomain>();		
		List<MGISetMemberDomain> listOfMembers = new ArrayList<MGISetMemberDomain>();
		MGISetDomain domain = new MGISetDomain();
		
		String cmd = "\n(select distinct g._Genotype_key, " +
				"\nCONCAT(g.displayIt,',',a1.symbol,',',a2.symbol) as displayIt, g.mgiID, 0 as setMemberKey" + 
				"\nfrom GXD_Genotype_View g" + 
				"\nINNER JOIN GXD_Specimen s on (g._Genotype_key = s._Genotype_key)" + 
				"\nLEFT OUTER JOIN GXD_AllelePair ap on (g._Genotype_key = ap._Genotype_key)" + 
				"\nLEFT OUTER JOIN ALL_Allele a1 on (ap._Allele_key_1 = a1._Allele_key)" + 
				"\nLEFT OUTER JOIN ALL_Allele a2 on (ap._Allele_key_2 = a2._Allele_key)" + 
				"\nwhere s._Assay_key = " + searchDomain.getAssayKey() +
				"\nunion all" + 
				"\nselect distinct s._Object_key," + 
				"\n'*['||a.accID||'] '||s.label," + 
				"\na.accID," + 
				"\ns._setmember_key as setMemberKey" + 
				"\nfrom mgi_setmember s, acc_accession a" + 
				"\nwhere s._set_key = 1055" + 
				"\nand s._object_key = a._object_key" + 
				"\nand s._createdby_key = " + searchDomain.getCreatedByKey() +
				"\nand a._mgitype_key = 12" + 
				"\nand a._logicaldb_key = 1" + 
				"\nand a.prefixPart = 'MGI:'" + 
				"\nand a.preferred = 1" +
				")\n)";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				MGISetMemberDomain memberDomain = new MGISetMemberDomain();
				memberDomain.setSetMemberKey(rs.getString("setMemberKey"));
				memberDomain.setSetKey("1055");
				memberDomain.setObjectKey(rs.getString("_Object_key"));
				memberDomain.setLabel(rs.getString("displayIt"));
				assayDAO.clear();
				listOfMembers.add(memberDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		domain.setGenotypeClipboardMembers(listOfMembers);
		results.add(domain);
		return results;
	}
	
}
