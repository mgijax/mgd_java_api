package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.dao.MarkerDAO;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.dao.TermDAO;
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
	private AlleleCellLineService alleleCellLineService;
	
	private AlleleTranslator translator = new AlleleTranslator();
	private SlimAlleleTranslator slimtranslator = new SlimAlleleTranslator();	
	private SlimAlleleRefAssocTranslator slimreftranslator = new SlimAlleleRefAssocTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();

//	String mgiTypeKey = "11";
//	String mgiTypeName = "Allele";
	
	@Transactional
	public SearchResults<AlleleDomain> create(AlleleDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		// database trigger will assign the MGI id/see pgmgddbschema/trigger for details

		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		Allele entity = new Allele();
		
		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		entity.setIsWildType(Integer.valueOf(domain.getIsWildType()));
		entity.setIsExtinct(Integer.valueOf(domain.getIsExtinct()));
		entity.setIsMixed(Integer.valueOf(domain.getIsMixed()));		
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainOfOriginKey())));
		entity.setInheritanceMode(termDAO.get(Integer.valueOf(domain.getInheritanceModeKey())));
		entity.setAlleleType(termDAO.get(Integer.valueOf(domain.getAlleleTypeKey())));
		entity.setAlleleStatus(termDAO.get(Integer.valueOf(domain.getAlleleStatusKey())));
		entity.setTransmission(termDAO.get(Integer.valueOf(domain.getTransmissionKey())));
		entity.setCollection(termDAO.get(Integer.valueOf(domain.getCollectionKey())));
		entity.setAlleleMarkerStatus(termDAO.get(Integer.valueOf(domain.getMarkerStatusKey())));

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

		// process mutant cell lines
		log.info("processAllele/mutant cell lines");
		alleleCellLineService.process(domain.getAlleleKey(), domain.getAlleleTypeKey(), domain.getAlleleType(), domain.getMutantCellLineAssocs(), user);

		// process synonyms
		// process allele attributes/subtypes
		// process molecular mutations
		// process driver genes
		
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
		
		log.info("processAllele/update");

		entity.setSymbol(domain.getSymbol());
		entity.setName(domain.getName());
		entity.setIsWildType(Integer.valueOf(domain.getIsWildType()));
		entity.setIsExtinct(Integer.valueOf(domain.getIsExtinct()));
		entity.setIsMixed(Integer.valueOf(domain.getIsMixed()));		
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrainOfOriginKey())));
		entity.setInheritanceMode(termDAO.get(Integer.valueOf(domain.getInheritanceModeKey())));
		entity.setAlleleType(termDAO.get(Integer.valueOf(domain.getAlleleTypeKey())));
		entity.setAlleleStatus(termDAO.get(Integer.valueOf(domain.getAlleleStatusKey())));
		entity.setTransmission(termDAO.get(Integer.valueOf(domain.getTransmissionKey())));
		entity.setCollection(termDAO.get(Integer.valueOf(domain.getCollectionKey())));
		entity.setAlleleMarkerStatus(termDAO.get(Integer.valueOf(domain.getAlleleMarkerStatusKey())));

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

		// process mutant cell lines
		log.info("processAllele/mutant cell lines");
		if (alleleCellLineService.process(domain.getAlleleKey(), domain.getAlleleTypeKey(), domain.getAlleleType(), domain.getMutantCellLineAssocs(), user)) {
			modified = true;			
		}
		
		// process synonyms
		// process allele attributes/subtypes
		// process molecular mutations
		// process driver genes
		
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			alleleDAO.update(entity);
			log.info("processAllele/changes processed: " + domain.getAlleleKey());
		}
		else {
			log.info("processAllele/no changes processed: " + domain.getAlleleKey());
		}
			
		// return entity translated to domain
		log.info("processAllele/update/returning results");
		results.setItem(translator.translate(entity));
		log.info("processAllele/update/returned results succsssful");
		return results;
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
		
		if (searchDomain.getIsExtinct() != null) {
			where = where + "\nand a.isExtinct = " + searchDomain.getIsExtinct();
		}
		
		if (searchDomain.getIsMixed() != null) {
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
		if (searchDomain.getAlleleMarkerStatusKey() != null && !searchDomain.getAlleleMarkerStatusKey().isEmpty()) {
			where = where + "\nand a._markerallele_status_key = " + searchDomain.getAlleleMarkerStatusKey();
		}
		if (searchDomain.getDetailClip() != null && !searchDomain.getDetailClip().isEmpty()) {
			where = where + "\nand notec.note ilike '" + searchDomain.getDetailClip() + "'" ;
			from_displayclip = true;
		}
				
		// mgi accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
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
				where = where + "\nand ref._refassoctype_key =" + searchDomain.getRefAssocs().get(0).getRefAssocTypeKey();
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
		
		// mutant & parent cell line
		if (searchDomain.getMutantCellLineAssocs() != null) {
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine() != null) {
				where = where + "\nand c.cellLine ilike '" + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLine() + "'";
				from_cellLine = true;
			}
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getCreator() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getCreator().isEmpty()) {
				where = where + "\nand c.creator ilike '" + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getCreator() + "'";
				from_cellLine = true;
			}			
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineTypeKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineTypeKey().isEmpty()) {
				where = where + "\nand c.parentCellLineType_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getCellLineTypeKey();
				from_cellLine = true;
			}			
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getCellLineKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getCellLineKey().isEmpty()) {
				where = where + "\nand c.parentCellLine_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getDerivation().getCellLineKey();
				from_cellLine = true;
			}
			if (searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getStrainKey() != null && !searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getStrainKey().isEmpty()) {
				where = where + "\nand c.cellLineStrain_key = " + searchDomain.getMutantCellLineAssocs().get(0).getMutantCellLine().getStrainKey();
				from_cellLine = true;
			}			
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
		
		// make this easy to copy/paste for troubleshooting
		//cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
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
		
}	
