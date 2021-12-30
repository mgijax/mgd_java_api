package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleRefAssocTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.bib.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneAssocService;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerNoteService;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleService extends BaseService<AlleleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleDAO alleleDAO;
	@Inject
	private MarkerDAO markerDAO;
	@Inject
	private ProbeStrainDAO strainDAO;
	@Inject
	private TermDAO termDAO;
	@Inject
	private ReferenceDAO referenceDAO;
	@Inject
	private MarkerNoteService markerNoteService;
	@Inject
	private NoteService noteService;
	@Inject
	private AlleleCellLineService alleleCellLineService;
	@Inject
	private MGIReferenceAssocService referenceAssocService;
	@Inject
	private MGISynonymService synonymService;
	@Inject
	private AlleleMutationService molmutationService;
	@Inject
	private AnnotationService annotationService;
	@Inject
	private RelationshipService relationshipService;
	@Inject
	private ImagePaneAssocService imagePaneAssocService;
	
	private AlleleTranslator translator = new AlleleTranslator();
	private SlimAlleleTranslator slimtranslator = new SlimAlleleTranslator();	
	private SlimAlleleRefAssocTranslator slimreftranslator = new SlimAlleleRefAssocTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	String mgiTypeKey = "11";
	
	@Transactional
	public SearchResults<AlleleDomain> create(AlleleDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		Allele entity = new Allele();
		
		log.info("processAllele/create");
		
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());			
		entity.setAlleleType(termDAO.get(Integer.valueOf(domain.getAlleleTypeKey())));	
		
		// default allele status = Reserved (847113)
		if (domain.getAlleleStatusKey() != null && !domain.getAlleleStatusKey().isEmpty()) {
			entity.setAlleleStatus(termDAO.get(Integer.valueOf(domain.getAlleleStatusKey())));			
		}
		else {
			entity.setAlleleStatus(termDAO.get(847113));						
		}
				
		if (domain.getName().equals("wild type") || domain.getName().equals("wild-type")) {
			entity.setIsWildType(1);
		}
		else {
			entity.setIsWildType(0);			
		}
		
		// default = No
		if (domain.getIsExtinct() == null || domain.getIsExtinct().isEmpty()) {
			entity.setIsExtinct(0);			
		}
		else {
			entity.setIsExtinct(Integer.valueOf(domain.getIsExtinct()));						
		}
		
		// if Mixed Reference exists, then Mixed = Yes
		if (domain.getMixedRefKey() != null && !domain.getMixedRefKey().isEmpty()) {
			entity.setIsMixed(1);
		}
		else if (domain.getIsMixed() == null || domain.getIsMixed().isEmpty()) {
			entity.setIsMixed(0);
		}
		else {
			entity.setIsMixed(Integer.valueOf(domain.getIsMixed()));			
		}		
		
		if (domain.getStrainOfOriginKey() != null && !domain.getStrainOfOriginKey().isEmpty()) {
			entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainOfOriginKey())));
		}
		else {
			// if allele type = 'Gene trapped' or 'Targeted', then SOO = 'Not Specified'
			if (domain.getAlleleTypeKey().equals("847121") || domain.getAlleleTypeKey().equals("847116")) {
				// not specified
				entity.setStrain(strainDAO.get(-1));
			}
			else {
				// not applicable
				entity.setStrain(strainDAO.get(-2));				
			}
		}

		// default inheritance mode = Not Applicable (847095)
		if (domain.getInheritanceModeKey() != null && !domain.getInheritanceModeKey().isEmpty()) {
			entity.setInheritanceMode(termDAO.get(Integer.valueOf(domain.getInheritanceModeKey())));
		}
		else {
			entity.setInheritanceMode(termDAO.get(847095));			
		}
		
        // if Germ Line Transmission has not been selected
        //      if the Mutant Cell Line = null/Not Specified then
        //              default = Not Applicable
        //      else
        //              default = Not Specified
		if (domain.getTransmissionKey() != null && !domain.getTransmissionKey().isEmpty()) {
			entity.setTransmission(termDAO.get(Integer.valueOf(domain.getTransmissionKey())));
		}
		else {
			if (domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLine() == null
					|| domain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLine().equals("Not Specified")) {
				entity.setTransmission(termDAO.get(3982955));
			}
			else {
				entity.setTransmission(termDAO.get(3982954));				
			}
		}
		
		// default collection = Not Specified (11025586)
		if (domain.getCollectionKey() != null && !domain.getCollectionKey().isEmpty()) {
			entity.setCollection(termDAO.get(Integer.valueOf(domain.getCollectionKey())));
		}
		else {
			entity.setCollection(termDAO.get(11025586));			
		}
		
		// default allele status = Curated (4268545)
		if (domain.getMarkerAlleleStatusKey() != null && !domain.getMarkerAlleleStatusKey().isEmpty()) {
			entity.setMarkerAlleleStatus(termDAO.get(Integer.valueOf(domain.getMarkerAlleleStatusKey())));			
		}
		else {
			entity.setMarkerAlleleStatus(termDAO.get(4268545));			
		}
		
		// marker/reference/can be null
		if (domain.getMarkerKey() != null && !domain.getMarkerKey().isEmpty()) {
			entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));	
		}
		else {
			entity.setMarker(null);
		}

		if (domain.getRefsKey() != null && !domain.getRefsKey().isEmpty()) {
			entity.setMarkerReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		}
		else {
			entity.setMarkerReference(null);
		}
		
		// if allele status is being set to Approved
		if (domain.getAlleleStatusKey().equals("847114")
				&& (domain.getApprovedByKey() == null || domain.getApprovedByKey().isEmpty())) {
			entity.setApproval_date(new Date());
			entity.setApprovedBy(user);			
		}
		else {
			entity.setApproval_date(null);
			entity.setApprovedBy(null);			
		}
		
		// add creation/modification 
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// execute persist/insert/send to database
		alleleDAO.persist(entity);

		// process detail clip
		markerNoteService.process(domain.getMarkerKey(), domain.getDetailClip(), user);
		
		// process all notes
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getGeneralNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getMolecularNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getNomenNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getInducibleNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getProidNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getCreNote(), mgiTypeKey, user);
		noteService.process(String.valueOf(entity.get_allele_key()), domain.getIkmcNote(), mgiTypeKey, user);
		
		// process allele reference
		log.info("processAllele/referenes");
		referenceAssocService.process(String.valueOf(entity.get_allele_key()), domain.getRefAssocs(), mgiTypeKey, user);
				
		// process mutant cell lines
		log.info("processAllele/mutant cell lines");
		alleleCellLineService.process(String.valueOf(entity.get_allele_key()), domain.getAlleleTypeKey(), domain.getAlleleType(), domain.getMutantCellLineAssocs(), user);

		// process synonyms
		log.info("processAllele/synonyms");		
		synonymService.process(String.valueOf(entity.get_allele_key()), domain.getSynonyms(), mgiTypeKey, user);
		
		// process allele attributes/subtypes
		log.info("processAllele/attribute/subtype");
		processSubtype(String.valueOf(entity.get_allele_key()), domain, user);
		
		// process molecular mutations
		log.info("processAllele/molecular mutation");
		molmutationService.process(String.valueOf(entity.get_allele_key()), domain.getMutations(), user);
		
		// process driver genes
		log.info("processAllele/driver gene");
		processDriverGene(String.valueOf(entity.get_allele_key()), domain, user);

		// process image pane assoc
		log.info("processAllele/image pane assoc");
		processImagePaneAssoc(domain.getAlleleKey(), domain, user);
		
		// return entity translated to domain
		log.info("processAllele/create/returning results");
		results.setItem(translator.translate(entity));
		return results;
	}

	@Transactional
	public SearchResults<AlleleDomain> update(AlleleDomain domain, User user) {
		
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		Allele entity = alleleDAO.get(Integer.valueOf(domain.getAlleleKey()));
		Boolean modified = true;
		Boolean setStrainNeedsReview = false;
		
		log.info("processAllele/update");
		
		// only if allele symbol has been changed
		if (!domain.getSymbol().equals(entity.getSymbol())) {
			setStrainNeedsReview = true;
		}

		entity.setSymbol(domain.getSymbol());		
		entity.setName(domain.getName());
		entity.setAlleleType(termDAO.get(Integer.valueOf(domain.getAlleleTypeKey())));
		entity.setAlleleStatus(termDAO.get(Integer.valueOf(domain.getAlleleStatusKey())));
		entity.setIsWildType(Integer.valueOf(domain.getIsWildType()));
		entity.setIsExtinct(Integer.valueOf(domain.getIsExtinct()));
		entity.setIsMixed(Integer.valueOf(domain.getIsMixed()));		
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainOfOriginKey())));
		entity.setInheritanceMode(termDAO.get(Integer.valueOf(domain.getInheritanceModeKey())));
		entity.setTransmission(termDAO.get(Integer.valueOf(domain.getTransmissionKey())));
		entity.setCollection(termDAO.get(Integer.valueOf(domain.getCollectionKey())));
		entity.setMarkerAlleleStatus(termDAO.get(Integer.valueOf(domain.getMarkerAlleleStatusKey())));
		
		if (domain.getMarkerKey() != null && !domain.getMarkerKey().isEmpty()) {
			log.info("processAllele/marker");
			entity.setMarker(markerDAO.get(Integer.valueOf(domain.getMarkerKey())));	
		}
		else {
			log.info("processAllele/no marker");			
			entity.setMarker(null);
		}

		if (domain.getRefsKey() != null && !domain.getRefsKey().isEmpty()) {
			log.info("processAllele/marker/reference");
			entity.setMarkerReference(referenceDAO.get(Integer.valueOf(domain.getRefsKey())));	
		}
		else {
			log.info("processAllele/marker/no reference");
			entity.setMarkerReference(null);
		}
		
		// if allele status is being set to Approved
		if (domain.getAlleleStatusKey().equals("847114")) {
			if (domain.getApprovedByKey() == null || domain.getApprovedByKey().isEmpty()) {
				log.info("processAllele/approval");
				entity.setApproval_date(new Date());
				entity.setApprovedBy(user);
			}
			// else do nothing
		}
		else {
			log.info("processAllele/no approval");			
			entity.setApproval_date(null);
			entity.setApprovedBy(null);			
		}

		// process detail clip
		if (markerNoteService.process(domain.getMarkerKey(), domain.getDetailClip(), user)) {
			modified = true;
		}
		
		// process all notes
		if (noteService.process(domain.getAlleleKey(), domain.getGeneralNote(), mgiTypeKey, user)) {
			modified = true;
		}
		if (noteService.process(domain.getAlleleKey(), domain.getMolecularNote(), mgiTypeKey, user)) {
			modified = true;
		}
		if (noteService.process(domain.getAlleleKey(), domain.getNomenNote(), mgiTypeKey, user)) {
			modified = true;
		}
		if (noteService.process(domain.getAlleleKey(), domain.getInducibleNote(), mgiTypeKey, user)) {
			modified = true;
		}
		if (noteService.process(domain.getAlleleKey(), domain.getProidNote(), mgiTypeKey, user)) {
			modified = true;
		}
		if (noteService.process(domain.getAlleleKey(), domain.getCreNote(), mgiTypeKey, user)) {
			modified = true;
		}
		if (noteService.process(domain.getAlleleKey(), domain.getIkmcNote(), mgiTypeKey, user)) {
			modified = true;
		}
		
		// process allele reference
		log.info("processAllele/referenes");
		if (referenceAssocService.process(domain.getAlleleKey(), domain.getRefAssocs(), mgiTypeKey, user)) {
			modified = true;
		}
		
		// process mutant cell lines
		log.info("processAllele/mutant cell lines");
		if (alleleCellLineService.process(domain.getAlleleKey(), domain.getAlleleTypeKey(), domain.getAlleleType(), domain.getMutantCellLineAssocs(), user)) {
			modified = true;
		}
		
		// process synonyms
		log.info("processAllele/synonyms");		
		if (synonymService.process(domain.getAlleleKey(), domain.getSynonyms(), mgiTypeKey, user)) {
			modified = true;
		}
		
		// process allele attributes/subtypes
		log.info("processAllele/attribute/subtype");
		if (processSubtype(domain.getAlleleKey(), domain, user)) {
			modified = true;
		}
		
		// process molecular mutations
		log.info("processAllele/molecular mutation");
		if (molmutationService.process(domain.getAlleleKey(), domain.getMutations(), user)) {
			modified = true;
		}

		// process driver genes
		log.info("processAllele/driver gene");
		if (processDriverGene(domain.getAlleleKey(), domain, user)) {
			modified = true;
		}
		
		// process image pane assoc
		log.info("processAllele/image pane assoc");
		if (processImagePaneAssoc(domain.getAlleleKey(), domain, user)) {
			modified = true;
		}
		
		// finish update
		if (modified) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			alleleDAO.update(entity);
			log.info("processAllele/changes processed: " + domain.getAlleleKey());
		}

		if (setStrainNeedsReview == true) {
			String cmd;
			Query query;
			
		    cmd = "select count(*) from PRB_setStrainReview (NULL, " + domain.getAlleleKey() + ")";
		    log.info("cmd: " + cmd);
		    query = alleleDAO.createNativeQuery(cmd);
		    query.getResultList();	
		}
		
		// return entity translated to domain
		log.info("processAllele/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAllele/update/returned results succsssful");
		return results;
	}

	@Transactional
	public Boolean processSubtype(String alleleKey, AlleleDomain domain, User user) {
		// process the subtype/annotation 
		
		List<AnnotationDomain> annotDomain = new ArrayList<AnnotationDomain>();

		for (int i = 0; i < domain.getSubtypeAnnots().size(); i++) {

			if (domain.getSubtypeAnnots().get(i).getTermKey().isEmpty()) {
				continue;
			}
			
			AnnotationDomain adomain = new AnnotationDomain();

			adomain.setProcessStatus(domain.getSubtypeAnnots().get(i).getProcessStatus());
			adomain.setAnnotKey(domain.getSubtypeAnnots().get(i).getAnnotKey());						
			adomain.setAnnotTypeKey(domain.getSubtypeAnnots().get(i).getAnnotTypeKey());
			adomain.setObjectKey(alleleKey);
			adomain.setTermKey(domain.getSubtypeAnnots().get(i).getTermKey());
			adomain.setQualifierKey(domain.getSubtypeAnnots().get(i).getQualifierKey());
			
			annotDomain.add(adomain);
		}
		
		log.info("processSubtype: " + annotDomain.size());
		if (annotDomain.size() > 0) {
			return(annotationService.process(annotDomain, user));
		}
		else {
			return(true);
		}
	}
	
	@Transactional
	public Boolean processDriverGene(String alleleKey, AlleleDomain domain, User user) {
		// process the driver gene/relationship
		
		List<RelationshipDomain> relationshipDomain = new ArrayList<RelationshipDomain>();

		for (int i = 0; i < domain.getDriverGenes().size(); i++) {

			if (domain.getDriverGenes().get(i).getMarkerKey().isEmpty()) {
				continue;
			}
			
			RelationshipDomain rdomain = new RelationshipDomain();

			rdomain.setProcessStatus(domain.getDriverGenes().get(i).getProcessStatus());			
			rdomain.setRelationshipKey(domain.getDriverGenes().get(i).getRelationshipKey());
			rdomain.setCategoryKey("1006");
			rdomain.setObjectKey1(alleleKey);
			rdomain.setObjectKey2(domain.getDriverGenes().get(i).getMarkerKey());

			// has_driver
			rdomain.setRelationshipTermKey("36770349");

			// not specified
			rdomain.setQualifierKey("11391898");
			
			// not specified
			rdomain.setEvidenceKey("17396909");
			
			// molecular reference
			// MUST EXIST
			rdomain.setRefsKey(domain.getMolRefKey());
			
			relationshipDomain.add(rdomain);
		}
		
		log.info("processDriverGene/relationship: " + relationshipDomain.size());
		if (relationshipDomain.size() > 0) {
			return(relationshipService.process(relationshipDomain, mgiTypeKey, user));		
		}
		else {
			return(true);
		}		
	}

	@Transactional
	public Boolean processImagePaneAssoc(String alleleKey, AlleleDomain domain, User user) {
		// process the image pane assoc
		
		List<ImagePaneAssocDomain> imagepaneDomain = new ArrayList<ImagePaneAssocDomain>();

		for (int i = 0; i < domain.getImagePaneAssocs().size(); i++) {

			if (domain.getImagePaneAssocs().get(i).getImagePaneKey().isEmpty()) {
				continue;
			}
			
			ImagePaneAssocDomain rdomain = new ImagePaneAssocDomain();

			rdomain.setProcessStatus(domain.getImagePaneAssocs().get(i).getProcessStatus());			
			rdomain.setAssocKey(domain.getImagePaneAssocs().get(i).getAssocKey());
			rdomain.setImagePaneKey(domain.getImagePaneAssocs().get(i).getImagePaneKey());
			rdomain.setMgiTypeKey(domain.getImagePaneAssocs().get(i).getMgiTypeKey());
			rdomain.setObjectKey(domain.getImagePaneAssocs().get(i).getObjectKey());
			rdomain.setIsPrimary(domain.getImagePaneAssocs().get(i).getIsPrimary());
			imagepaneDomain.add(rdomain);
		}
		
		log.info("processImagePaneAssoc: " + imagepaneDomain.size());
		if (imagepaneDomain.size() > 0) {
			return(imagePaneAssocService.process(null, imagepaneDomain, user));		
		}
		else {
			return(true);
		}		
	}
	
	@Transactional
	public AlleleDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		AlleleDomain domain = new AlleleDomain();
		if (alleleDAO.get(key) != null) {
			domain = translator.translate(alleleDAO.get(key));
		}
		alleleDAO.clear();
		return domain;	
	}
	
    @Transactional
    public SearchResults<AlleleDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results 
    	SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
        results.setItem(translator.translate(alleleDAO.get(key)));
        alleleDAO.clear();
        return results;
    }

	@Transactional
	public SearchResults<AlleleDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		Allele entity = alleleDAO.get(key);
		results.setItem(translator.translate(alleleDAO.get(key)));
		alleleDAO.remove(entity);
		return results;
	}

	@Transactional	
	public SearchResults<AlleleDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		String cmd = "select count(*) as objectCount from all_allele";
		
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
	public List<SlimAlleleDomain> search(AlleleDomain searchDomain) {

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._allele_key, a.symbol, v1.sequenceNum";
		String from = "from all_allele a, voc_term v1";
		String where = "where a._allele_status_key = v1._term_key";
		String orderBy = "order by v1.sequenceNum, a.symbol";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		Boolean from_marker = false;
		Boolean from_accession = false;
		Boolean from_reference = false;
		Boolean from_displayclip = false;
		Boolean from_generalNote = false;
		Boolean from_molecularNote = false;
		Boolean from_nomenNote = false;
		Boolean from_inducibleNote = false;
		Boolean from_proidNote = false;
		Boolean from_ikmcNote = false;
		Boolean from_creNote = false;
		Boolean from_cellLine = false;
		Boolean from_synonym = false;
		Boolean from_subtype = false;
		Boolean from_mutation = false;
		Boolean from_drivergene = false;
		Boolean from_imagepane = false;
				
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			where = where + "\nand a.symbol ilike '" + searchDomain.getSymbol() + "'" ;
		}

		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand a.name ilike '" + searchDomain.getName() + "'" ;
		}
		
		if (searchDomain.getIsExtinct() != null && !searchDomain.getIsExtinct().isEmpty()) {
			where = where + "\nand a.isExtinct = " + searchDomain.getIsExtinct();
		}
		
		if (searchDomain.getIsMixed() != null && !searchDomain.getIsMixed().isEmpty()) {
			where = where + "\nand a.isMixed = " + searchDomain.getIsMixed();
		}

		if (searchDomain.getInheritanceModeKey() != null && !searchDomain.getInheritanceModeKey().isEmpty()) {
			where = where + "\nand a._mode_key = " + searchDomain.getInheritanceModeKey();
		}

		if (searchDomain.getAlleleTypeKey() != null && !searchDomain.getAlleleTypeKey().isEmpty()) {
			where = where + "\nand a._allele_type_key = " + searchDomain.getAlleleTypeKey();
		}

		if (searchDomain.getAlleleStatusKey() != null && !searchDomain.getAlleleStatusKey().isEmpty()) {
			where = where + "\nand a._allele_status_key = " + searchDomain.getAlleleStatusKey();
		}
		
		if (searchDomain.getTransmissionKey() != null && !searchDomain.getTransmissionKey().isEmpty()) {
			where = where + "\nand a._transmission_key = " + searchDomain.getTransmissionKey();
		}

		if (searchDomain.getCollectionKey() != null && !searchDomain.getCollectionKey().isEmpty()) {
			where = where + "\nand a._collection_key = " + searchDomain.getCollectionKey();
		}

		if (searchDomain.getStrainOfOriginKey() != null && !searchDomain.getStrainOfOriginKey().isEmpty()) {
			where = where + "\nand a._strain_key = " + searchDomain.getStrainOfOriginKey();
		}
		
		// marker
		if (searchDomain.getMarkerKey() != null && !searchDomain.getMarkerKey().isEmpty()) {
			where = where + "\nand a._marker_key = " + searchDomain.getMarkerKey();
		}
		if (searchDomain.getMarkerSymbol() != null && !searchDomain.getMarkerSymbol().isEmpty()) {
			where = where + "\nand m.symbol ilike '" + searchDomain.getMarkerSymbol() + "'";
			from_marker = true;
		}
		if (searchDomain.getRefsKey() != null && !searchDomain.getRefsKey().isEmpty()) {
			where = where + "\nand a._Refs_key = " + searchDomain.getRefsKey();
		}
		if (searchDomain.getMarkerAlleleStatusKey() != null && !searchDomain.getMarkerAlleleStatusKey().isEmpty()) {
			where = where + "\nand a._markerallele_status_key = " + searchDomain.getMarkerAlleleStatusKey();
		}
		if (searchDomain.getDetailClip() != null) {
			if (searchDomain.getDetailClip().getNote() != null && !searchDomain.getDetailClip().getNote().isEmpty()) {
				where = where + "\nand notec.note ilike '" + searchDomain.getDetailClip().getNote() + "'" ;
				from_displayclip = true;
			}
		}
				
		// mgi accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			if (!searchDomain.getAccID().startsWith("MGI:")) {
				where = where + "\nand acc.numericPart = '" + searchDomain.getAccID() + "'";
			}
			else {
				where = where + "\nand acc.accID = '" + searchDomain.getAccID().toUpperCase() + "'";
			}
			from_accession = true;
		}
		// other non-mgi accession ids
		else if	(searchDomain.getOtherAccIDs() != null && !searchDomain.getOtherAccIDs().isEmpty()) {	
			if (searchDomain.getOtherAccIDs().get(0).getAccID() != null && !searchDomain.getOtherAccIDs().get(0).getAccID().isEmpty()) {				
				where = where + "\nand acc.accID ilike '" + searchDomain.getOtherAccIDs().get(0).getAccID() + "'";
				from_accession = true;
			}
			if (searchDomain.getOtherAccIDs().get(0).getLogicaldbKey() != null && !searchDomain.getOtherAccIDs().get(0).getLogicaldbKey().isEmpty()) {				
				where = where + "\nand acc._logicaldb_key = " + searchDomain.getOtherAccIDs().get(0).getLogicaldbKey();
				from_accession = true;
			}			
		}
		
		// reference
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand ref._Refs_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replace("'",  "''");
				where = where + "\nand ref.short_citation ilike '" + value + "'";
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getJnumid() != null && !searchDomain.getRefAssocs().get(0).getJnumid().isEmpty()) {
				//search for J: in *any* refassoctype
				//where = where + "\nand ref._refassoctype_key =" + searchDomain.getRefAssocs().get(0).getRefAssocTypeKey();
				where = where + "\nand ref.jnumid ilike '" + searchDomain.getRefAssocs().get(0).getJnumid() + "'";
				from_reference = true;				
			}
		}

		if (searchDomain.getGeneralNote() != null && !searchDomain.getGeneralNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getGeneralNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note1.note ilike '" + value + "'" ;
			from_generalNote = true;
		}
		if (searchDomain.getMolecularNote() != null && !searchDomain.getMolecularNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getMolecularNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note2.note ilike '" + value + "'" ;
			from_molecularNote = true;
		}
		if (searchDomain.getNomenNote() != null && !searchDomain.getNomenNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getNomenNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note3.note ilike '" + value + "'" ;
			from_nomenNote = true;
		}
		if (searchDomain.getInducibleNote() != null && !searchDomain.getInducibleNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getInducibleNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note4.note ilike '" + value + "'" ;
			from_inducibleNote = true;
		}		
		if (searchDomain.getProidNote() != null && !searchDomain.getProidNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getProidNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note5.note ilike '" + value + "'" ;
			from_proidNote = true;
		}				
		if (searchDomain.getIkmcNote() != null && !searchDomain.getIkmcNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getIkmcNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note6.note ilike '" + value + "'" ;
			from_ikmcNote = true;
		}
		if (searchDomain.getCreNote() != null && !searchDomain.getCreNote().getNoteChunk().isEmpty()) {
			value = searchDomain.getCreNote().getNoteChunk().replace("'",  "''");
			where = where + "\nand note7.note ilike '" + value + "'" ;
			from_creNote = true;
		}
		
		// mutant cell lines
		if (searchDomain.getMutantCellLineAssocs() != null) {			
			// mutant cell line : cell line, creator, modification date, strain of origin
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine() != null) {
	
				if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineKey().isEmpty()) {
					where = where + "\nand c._mutantcellline_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineKey();
					from_cellLine = true;
				}
				else if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLine() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLine().isEmpty()) {
					where = where + "\nand c.cellLine ilike '" + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLine() + "'";
					from_cellLine = true;
				}		
			
				// strain of origin (aka all_allele._strain_key) is not currently in all_allele_cellline_view
//				if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getStrain() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getStrain().isEmpty()) {
//					where = where + "\nand c.strainoforigin ilile '" + searchDomain.getStrain() + "'";
//					from_cellLine = true;
//				}
				
				if ((searchDomain.getMutantCellLineAssocs().get(0).getModifiedBy() != null && !searchDomain.getMutantCellLineAssocs().get(0).getModifiedBy().isEmpty())
						|| (searchDomain.getMutantCellLineAssocs().get(0).getModification_date() != null && !searchDomain.getMutantCellLineAssocs().get(0).getModification_date().isEmpty())) {
					String cmResults2[] = DateSQLQuery.queryByCreationModification("c", searchDomain.getMutantCellLineAssocs().get(0).getCreatedBy(), searchDomain.getMutantCellLineAssocs().get(0).getModifiedBy(), searchDomain.getMutantCellLineAssocs().get(0).getCreation_date(), searchDomain.getMutantCellLineAssocs().get(0).getModification_date());
					if (cmResults2.length > 0) {
						from = from + cmResults2[0];
						where = where + cmResults2[1];
						from_cellLine = true;
					}
				}				
			}

			// parent cell line : cell line, strain, cell line type		
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine() != null) {
				if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineKey().isEmpty()) {
					where = where + "\nand c.parentcellline_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineKey();
					from_cellLine = true;
				}
				else if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLine() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLine().isEmpty()) {
					where = where + "\nand c.parentcellline ilike '" + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLine() + "'";
					from_cellLine = true;
				}			
				if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineTypeKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineTypeKey().isEmpty()) {
					where = where + "\nand c.parentcelllinetype_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getCellLineTypeKey();
					from_cellLine = true;
				}
				if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getStrainKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getStrainKey().isEmpty()) {
					where = where + "\nand c.celllinestrain_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getStrainKey();
				}
				else if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getStrain() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getStrain().isEmpty()) {
					where = where + "\nand c.celllinestrain ilike '" + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getParentCellLine().getStrain() + "'";
					from_cellLine = true;
				}				
			}			
		}
		
		// synonym, j:
		if (searchDomain.getSynonyms() != null) {
			if (searchDomain.getSynonyms().get(0).getSynonym() != null && !searchDomain.getSynonyms().get(0).getSynonym().isEmpty()) {
				where = where + "\nand ms.synonym ilike '" + searchDomain.getSynonyms().get(0).getSynonym() + "'";
				from_synonym = true;
			}
			if (searchDomain.getSynonyms().get(0).getRefsKey() != null && !searchDomain.getSynonyms().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand ms._Refs_key = " + searchDomain.getSynonyms().get(0).getRefsKey();
				from_synonym = true;
			}
			else if (searchDomain.getSynonyms().get(0).getJnumid() != null && !searchDomain.getSynonyms().get(0).getJnumid().isEmpty()) {
				String jnumid = searchDomain.getSynonyms().get(0).getJnumid().toUpperCase();
				if (!jnumid.contains("J:")) {
					jnumid = "J:" + jnumid;
				}
				where = where + "\nand ms.jnumid ilike '" + jnumid + "'";
				from_synonym = true;
			}	
		}
		
		// allele attribute/subtype
		if (searchDomain.getSubtypeAnnots() != null) {
			if (searchDomain.getSubtypeAnnots().get(0).getTermKey() != null && !searchDomain.getSubtypeAnnots().get(0).getTermKey().isEmpty()) {
				where = where + "\nand st._term_key = " + searchDomain.getSubtypeAnnots().get(0).getTermKey();
				from_subtype = true;
			}	
		}

		// molecular mutations
		if (searchDomain.getMutations() != null) {
			if (searchDomain.getMutations().get(0).getMutationKey() != null && !searchDomain.getMutations().get(0).getMutationKey().isEmpty()) {
				where = where + "\nand mt._mutation_key = " + searchDomain.getMutations().get(0).getMutationKey();
				from_mutation = true;
			}	
		}
				
		// driver gene
		if (searchDomain.getDriverGenes() != null) {
			if (searchDomain.getDriverGenes().get(0).getMarkerSymbol() != null && !searchDomain.getDriverGenes().get(0).getMarkerSymbol().isEmpty()) {
				where = where + "\nand dg.symbol ilike '" + searchDomain.getDriverGenes().get(0).getMarkerSymbol() + "'";
				from_drivergene = true;
			}
			if (searchDomain.getDriverGenes().get(0).getOrganismKey() != null && !searchDomain.getDriverGenes().get(0).getOrganismKey().isEmpty()) {
				where = where + "\nand dg._organism_key = " + searchDomain.getDriverGenes().get(0).getOrganismKey();
				from_drivergene = true;
			}			
		}
		
		// image pane
		if (searchDomain.getImagePaneAssocs() != null) {
			if (searchDomain.getImagePaneAssocs().get(0).getMgiID() != null && !searchDomain.getImagePaneAssocs().get(0).getMgiID().isEmpty()) {
				where = where + "\nand i.mgiID ilike '" + searchDomain.getImagePaneAssocs().get(0).getMgiID() + "'";
				from_imagepane = true;
			}
			if (searchDomain.getImagePaneAssocs().get(0).getPixID() != null && !searchDomain.getImagePaneAssocs().get(0).getPixID().isEmpty()) {
				where = where + "\nand i.pixID ilike '" + searchDomain.getImagePaneAssocs().get(0).getPixID() + "'";
				from_imagepane = true;
			}			
		}		
		
		if (from_marker == true) {
			from = from + ", mrk_marker m";
			where = where + "\nand a._marker_key = m._marker_key";
		}
		if (from_displayclip == true) {
			from = from + ", mrk_notes notec";
			where = where + "\nand a._marker_key = notec._marker_key";
		}
		if (from_accession == true) {
			from = from + ", all_acc_view acc";
			where = where + "\nand a._allele_key = acc._object_key"; 
		}
		if (from_reference == true) {
			from = from + ", mgi_reference_allele_view ref";
			where = where + "\nand a._allele_key = ref._object_key";
		}
		if (from_generalNote == true) {
			from = from + ", mgi_note_allele_view note1";
			where = where + "\nand a._allele_key = note1._object_key";
			where = where + "\nand note1._notetype_key = 1020";			
		}
		if (from_molecularNote == true) {
			from = from + ", mgi_note_allele_view note2";
			where = where + "\nand a._allele_key = note2._object_key";
			where = where + "\nand note2._notetype_key = 1021";
		}
		if (from_nomenNote == true) {
			from = from + ", mgi_note_allele_view note3";
			where = where + "\nand a._allele_key = note3._object_key";
			where = where + "\nand note3._notetype_key = 1022";
		}
		if (from_inducibleNote == true) {
			from = from + ", mgi_note_allele_view note4";
			where = where + "\nand a._allele_key = note4._object_key";
			where = where + "\nand note4._notetype_key = 1032";
		}			
		if (from_proidNote == true) {
			from = from + ", mgi_note_allele_view note5";
			where = where + "\nand a._allele_key = note5._object_key";
			where = where + "\nand note5._notetype_key = 1036";
		}
		if (from_ikmcNote == true) {
			from = from + ", mgi_note_allele_view note6";
			where = where + "\nand a._allele_key = note6._object_key";
			where = where + "\nand note6._notetype_key = 1041";
		}	
		if (from_creNote == true) {
			from = from + ", mgi_note_allele_view note7";
			where = where + "\nand a._allele_key = note7._object_key";
			where = where + "\nand note7._notetype_key = 1040";
		}	
		if (from_cellLine == true) {
			from = from + ", all_allele_cellline_view c";
			where = where +"\nand a._allele_key = c._allele_key";
		}
		if (from_synonym == true) {
			from = from + ", mgi_synonym_allele_view ms";
			where = where + "\nand a._allele_key = ms._object_key";
		}
		if (from_subtype == true) {
			from = from + ", all_allele_subtype_view st";
			where = where + "\nand a._allele_key = st._allele_key";
		}
		if (from_mutation == true) {
			from = from + ", all_allele_mutation_view mt";
			where = where + "\nand a._allele_key = mt._allele_key";
		}
		if (from_drivergene == true) {
			from = from + ", all_allele_driver_view dg";
			where = where + "\nand a._allele_key = dg._allele_key";
		}
		if (from_imagepane == true) {
			from = from + ", img_imagepane_assoc_view i";
			where = where + "\nand a._allele_key = i._object_key"
					+ "\nand i._mgitype_key = 11";
		}		
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleDomain domain = new SlimAlleleDomain();
				domain = slimtranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));				
				alleleDAO.clear();
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
	public List<SlimAlleleRefAssocDomain> searchVariant(AlleleDomain searchDomain) {

		List<SlimAlleleRefAssocDomain> results = new ArrayList<SlimAlleleRefAssocDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._allele_key, a.symbol";
		String from = "from all_allele a, voc_term v1, all_variant av";
		String where = "where a._allele_type_key = v1._term_key"
				+ "\nand a._allele_key = av._allele_key \nand av._sourcevariant_key is not null";
		String orderBy = "order by a.symbol";
		String limit = Constants.SEARCH_RETURN_LIMIT;
		String value;
		Boolean from_marker = false;
		Boolean from_accession = false;
		Boolean from_reference = false;

		String cmResults[] = DateSQLQuery.queryByCreationModification("av", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			where = where + "\nand a.symbol ilike '" + searchDomain.getSymbol() + "'" ;
		}

		if (searchDomain.getName() != null && !searchDomain.getName().isEmpty()) {
			where = where + "\nand a.name ilike '" + searchDomain.getName() + "'" ;
		}

		if (searchDomain.getMarkerKey() != null && !searchDomain.getMarkerKey().isEmpty()) {
			where = where + "\nand a._marker_key = " + searchDomain.getMarkerKey();
		}
		
		// marker location cache		
		if (searchDomain.getChromosome() != null && !searchDomain.getChromosome().isEmpty()) {
			where = where + "\nand m.chromosome ilike '" + searchDomain.getChromosome() + "'";
			from_marker = true;
		}
		if (searchDomain.getStrand() != null && !searchDomain.getStrand().isEmpty()) {
			where = where + "\nand m.strand ilike '" + searchDomain.getStrand() + "'";
			from_marker = true;
		}
		
		// allele accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			from_accession = true;
		}
						
		// reference; allow > 1 jnumid
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand ref._Refs_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replace("'",  "''");
				where = where + "\nand ref.short_citation ilike '" + value + "'";
				from_reference = true;
			}
			
			// accepts > 1 jnumid
			StringBuffer jnumClauses = new StringBuffer("");
			for (String jnumID : searchDomain.getRefAssocs().get(0).getJnumid().split(" ")) {
				from_reference = true;
				if (jnumClauses.length() > 0) {
					jnumClauses.append(" or ");
				}
				jnumClauses.append("ref.jnumid ilike '" + jnumID + "'");
			}
			where = where + "\nand (" + jnumClauses.toString() + ")";
		}
	
		if (from_marker == true) {
			from = from + ", mrk_location_cache m";
			where = where + "\nand a._marker_key = m._marker_key";
		}
		if (from_accession == true) {
			from = from + ", all_acc_view acc";
			where = where + "\nand a._allele_key = acc._object_key"; 
		}
		if (from_reference == true) {
			from = from + ", mgi_reference_allele_view ref";
			where = where + "\nand a._allele_key = ref._object_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleRefAssocDomain domain = new SlimAlleleRefAssocDomain();
				domain = slimreftranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));				
				alleleDAO.clear();
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
	public List<SlimAlleleRefAssocDomain> validateAlleleAnyStatus(SlimAlleleRefAssocDomain searchDomain) {
		log.info("In Allele Service validateAlleleAnyStatus" );
		List<SlimAlleleRefAssocDomain> results = new ArrayList<SlimAlleleRefAssocDomain>();
		
		String cmd = "\nselect aa._allele_key"
				+ "\nfrom all_allele aa, acc_accession a"
				+ "\nwhere aa._allele_key = a._object_key"
				+ "\nand a._mgitype_key = 11"
				+ "\nand a._logicaldb_key = 1"
				+ "\nand a.preferred = 1"
				+ "\nand a.prefixPart = 'MGI:'";
		
		if (searchDomain.getSymbol() != null) {
			if(searchDomain.getSymbol().contains("%")) {
				return results;
			}
			else {
				cmd = cmd + "\nand lower(aa.symbol) = '" + searchDomain.getSymbol().toLowerCase() + "'";
			}				
		}

		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) { 
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			cmd = cmd + "\nand lower(a.accID) = '" + mgiid.toLowerCase() + "'";			
		}
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimAlleleRefAssocDomain slimdomain = new SlimAlleleRefAssocDomain();
				slimdomain = slimreftranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));	
				log.info("slim domain allele status: " + slimdomain.getAlleleStatus());
				alleleDAO.clear();
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// if more than 1 result, then use *exact* case from value
		// if no match on exact case, then empty results should be returned
		if (results.size() > 1) {
			List<SlimAlleleRefAssocDomain> newResults = new ArrayList<SlimAlleleRefAssocDomain>();
			for (int i = 0; i < results.size(); i++) {
				if (results.get(i).getSymbol().equals(searchDomain.getSymbol())) {
					newResults.add(results.get(i));
					break;
				}
			}
			results = newResults;
		}
		
		return results;
	}

	@Transactional
	public List<SlimAlleleDomain> validateAllele(SlimAlleleDomain searchDomain) {
		
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		String cmd = "\nselect aa._allele_key"
				+ "\nfrom all_allele aa, acc_accession a"
				+ "\nwhere aa._allele_status_key in (847114, 3983021)" //Approved,Autoload
				+ "\nand aa._allele_key = a._object_key"
				+ "\nand a._mgitype_key = 11"
				+ "\nand a._logicaldb_key = 1"
				+ "\nand a.preferred = 1"
				+ "\nand a.prefixPart = 'MGI:'";
		
		if (searchDomain.getSymbol() != null) {
			if(searchDomain.getSymbol().contains("%")) {
				return results;
			}
			else {
				cmd = cmd + "\nand lower(aa.symbol) = '" + searchDomain.getSymbol().toLowerCase() + "'";
			}
				
		}
		
		if (searchDomain.getMarkerKey() != null && !searchDomain.getMarkerKey().isEmpty()) {
			cmd = cmd + "\nand aa._marker_key = " + searchDomain.getMarkerKey();
		}

		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) { 
			String mgiid = searchDomain.getAccID().toUpperCase();
			if (!mgiid.contains("MGI:")) {
				mgiid = "MGI:" + mgiid;
			}
			cmd = cmd + "\nand lower(a.accID) = '" + mgiid.toLowerCase() + "'";	
		}
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimAlleleDomain slimdomain = new SlimAlleleDomain();
				slimdomain = slimtranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));				
				alleleDAO.clear();
				results.add(slimdomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// if more than 1 result, then use *exact* case from value
		// if no match on exact case, then empty results should be returned
		if (results.size() > 1) {
			List<SlimAlleleDomain> newResults = new ArrayList<SlimAlleleDomain>();
			for (int i = 0; i < results.size(); i++) {
				if (results.get(i).getSymbol().equals(searchDomain.getSymbol())) {
					newResults.add(results.get(i));
					break;
				}
			}
			results = newResults;
		}
		
		return results;
	}

	@Transactional
	public List<SlimAlleleDomain> validateAlleleByMGIIds(List<String> mgiIds) {
		// validate list of mgi ids are valid, non-wild type
		
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		if (mgiIds.isEmpty()) {
			return results;
		}
		
		String value;
		for (int i = 0; i < mgiIds.size(); i++) {
			value = "'" + mgiIds.get(i) + "'";
			mgiIds.set(i, value);
		}
		
		String cmd = "\nselect distinct aa._allele_key, a.accID"
				+ "\nfrom all_allele aa, acc_accession a"
				+ "\nwhere aa._allele_status_key in (847114, 3983021)" //Approved,Autoload
				+ "\nand aa.iswildtype= 0"
				+ "\nand aa._allele_key = a._object_key"
				+ "\nand a._mgitype_key = 11"
				+ "\nand a._logicaldb_key = 1"
				+ "\nand a.preferred = 1"
				+ "\nand a.prefixPart = 'MGI:'"
				+ "\nand a.accID in (" + String.join("," , mgiIds) + ")";
		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			
			while (rs.next()) {
				SlimAlleleDomain alleleDomain = new SlimAlleleDomain();
				alleleDomain = slimtranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));				
				alleleDAO.clear();
				results.add(alleleDomain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
		return results;
	}

	@Transactional
	public Boolean validateAlleleConditional(List<SlimAlleleDomain> searchDomain) {
		// validate list of alleles meet conditionally targeted rules
		// 
		// finds alleles where attribute = 'recombinase' (11025588)
		// finds alleles where attribute = 'conditional ready' (11025588)
		// 
		// returns true/false
		//
		
		Boolean results = true;
		List<String> alleleList = new ArrayList<String>();
		
		for (int i = 0; i < searchDomain.size(); i++) {
			alleleList.add(searchDomain.get(i).getAlleleKey());
		}
		
		// finds alleles where attribute = 'recombinase' (11025588)		
		String cmd = "\nselect a._Allele_key" + 
				"\nfrom ALL_Allele a, VOC_Annot va" + 
				"\nwhere a._Allele_key in (" + String.join(",",  alleleList) + ")" +
				"\nand a._Allele_key = va._Object_key" + 
				"\nand va._AnnotType_key = 1014" + 
				"\nand va._Term_key = 11025587";		
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);			
			if (rs.next() == false) {
				results = false;
			}
			else {
				results = true;
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// finds alleles where attribute = 'conditional ready' (11025588)		
		cmd = "\nselect a._Allele_key, va._Term_key" + 
				"\nfrom ALL_Allele a, VOC_Annot va" + 
				"\nwhere a._Allele_key in (" + String.join(",",  alleleList) + ")" +
				"\nand a._Allele_key = va._Object_key" + 
				"\nand va._AnnotType_key = 1014" + 
				"\nand va._Term_key = 11025588";	
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);		
			if (rs.next() == false) {
				results = false;
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}

	@Transactional
	public List<SlimAlleleDomain> getSlimByMCL(Integer key) {
		// return SlimAlleleDoman list of mutant cell line key

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		String cmd = "select _allele_key from ALL_Allele_CellLine_View where _MutantCellLine_key = " + key;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleDomain domain = new SlimAlleleDomain();				
				domain = slimtranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));
				alleleDAO.clear();
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
	public List<SlimAlleleDomain> getAlleleByImagePane(SlimImageDomain searchDomain) {
		// return list of alleles that are associated with image panes of given image
	
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();

		String cmd = "\nselect distinct a._allele_key"
				+ "\nfrom img_image i, img_imagepane ip, img_imagepane_assoc ipa, all_allele a" 
				+ "\nwhere i._image_key = " + searchDomain.getImageKey()
				+ "\nand i._image_key = ip._image_key" 
				+ "\nand ip._imagepane_key = ipa._imagepane_key" 
				+ "\nand ipa._mgitype_key = 11"
				+ "\nand ipa._object_key = a._allele_key";
		
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleDomain domain = new SlimAlleleDomain();
				domain = slimtranslator.translate(alleleDAO.get(rs.getInt("_allele_key")));
				alleleDAO.clear();
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
