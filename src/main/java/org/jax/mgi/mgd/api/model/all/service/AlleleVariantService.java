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
import org.jax.mgi.mgd.api.model.all.dao.AlleleVariantDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleVariantDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleVariant;
import org.jax.mgi.mgd.api.model.all.translator.AlleleVariantTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleVariantTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleVariantService extends BaseService<AlleleVariantDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AlleleVariantDAO variantDAO;
	@Inject
	private AlleleVariantDAO sourceVariantDAO;
	@Inject
	private AlleleVariantDAO curatedVariantDAO;
	   
	@Inject 
	private AlleleDAO alleleDAO;
	@Inject
	private ProbeStrainDAO strainDAO;
	@Inject
	private NoteService noteService;
	@Inject
	private MGIReferenceAssocService referenceAssocService;	
	@Inject
	private AnnotationService annotationService;
	@Inject
	private VariantSequenceService sequenceService;


	// translate an entity to a domain to return in the results
	private AlleleVariantTranslator translator = new AlleleVariantTranslator();
	private SlimAlleleVariantTranslator slimtranslator = new SlimAlleleVariantTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();

	private String mgiTypeKey = "45";
	
	@Transactional
	public SearchResults<AlleleVariantDomain> create(AlleleVariantDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		//
		// create Source variant and Curated variant
		// the Source variant _SourceVariant_key = null
		// the Curated variant _SourceVariant_key = new Source variant key
		// all fields are should be sent in by UI/domain
		//
		// AlleleVariant key will be auto-sequence
		// VariantSequence key will be auto-sequenced
		
		// curated results
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		
		AlleleVariant sourceEntity = new AlleleVariant();
		AlleleVariant curatedEntity = new AlleleVariant();
	
		// create Source entity
		sourceEntity.setAllele(alleleDAO.get(Integer.valueOf(domain.getSourceVariant().getAllele().getAlleleKey())));
		sourceEntity.setStrain(strainDAO.get(Integer.valueOf(domain.getSourceVariant().getStrain().getStrainKey())));
		sourceEntity.setIsReviewed(Integer.valueOf(domain.getSourceVariant().getIsReviewed()).intValue());
		sourceEntity.setDescription(domain.getSourceVariant().getDescription());
		sourceEntity.setCreatedBy(user);
		sourceEntity.setCreation_date(new Date());
		sourceEntity.setModifiedBy(user);
		sourceEntity.setModification_date(new Date());
		sourceVariantDAO.persist(sourceEntity);
		
		// create Curated entity
		log.info("source key: " + sourceEntity.get_variant_key());
		curatedEntity.setSourceVariant(sourceVariantDAO.get(sourceEntity.get_variant_key()));
		curatedEntity.setAllele(alleleDAO.get(Integer.valueOf(domain.getAllele().getAlleleKey())));
		curatedEntity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrain().getStrainKey())));
		curatedEntity.setIsReviewed(Integer.valueOf(domain.getIsReviewed()).intValue());
		curatedEntity.setDescription(domain.getDescription());
		curatedEntity.setCreatedBy(user);
		curatedEntity.setCreation_date(new Date());
		curatedEntity.setModifiedBy(user);
		curatedEntity.setModification_date(new Date());
		curatedVariantDAO.persist(curatedEntity);
		
		// create Source Variant Sequences
		log.info("AlleleVariantDomain checking for sequences");
		if (domain.getSourceVariant().getVariantSequences() != null) {
			log.info("AlleleVariantDomain processing source variant sequences");
			sequenceService.process(String.valueOf(sourceEntity.get_variant_key()), domain.getSourceVariant().getVariantSequences() , user);
			log.info("AlleleVariantDomain done source variantprocessing sequences");

		}
		
		// create Curated Variant Sequences
		if (domain.getVariantSequences() != null) {
			log.info("AlleleVariantDomain processing curated variant sequences");
            sequenceService.process(String.valueOf(curatedEntity.get_variant_key()), domain.getVariantSequences() , user);
            log.info("AlleleVariantDomain done processing curated variant sequences");

		}
	
		// process all notes : curated only
		noteService.process(String.valueOf(curatedEntity.get_variant_key()), domain.getCuratorNote(), mgiTypeKey, "1050", user);
		noteService.process(String.valueOf(curatedEntity.get_variant_key()), domain.getPublicNote(), mgiTypeKey, "1051", user);
		
		// process reference : curated only
		if (domain.getRefAssocs() != null) {
			referenceAssocService.process(String.valueOf(curatedEntity.get_variant_key()), domain.getRefAssocs(), mgiTypeKey, user);
		}
		
		// process variant type : curated only
		// use qualifier 'Generic Annotation Qualifier', value = null
		if (domain.getVariantTypes() != null) {
			annotationService.processAlleleVariant(String.valueOf(curatedEntity.get_variant_key()), 
					domain.getVariantTypes(), 
					domain.getVariantTypes().get(0).getAnnotTypeKey(), 
					Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user);
		}

		// process variant effects : curated only
		// use qualifier 'Generic Annotation Qualifier', value = null
		if (domain.getVariantEffects() != null) {
			annotationService.processAlleleVariant(String.valueOf(curatedEntity.get_variant_key()), 
					domain.getVariantEffects(), 
					domain.getVariantEffects().get(0).getAnnotTypeKey(), 
					Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user);
		}
				
		// return curated entity translated to domain, set in results
		// results has domain info and other info too
		log.info("processAlleleVariant/create/returning results");
		log.info(curatedEntity);
		results.setItem(translator.translate(curatedEntity,0));
		log.info("processAlleleVariant/translator curated entity returned");
		return results;
	}

	@Transactional
	public SearchResults<AlleleVariantDomain> update(AlleleVariantDomain domain, User user) {
		// * the set of fields in "update" is similar to set of fields in "create"
		// * creation user/date are only set in "create"
		// * do not update source, allele, strain (strain is always B6; set by the UI)
		// * The Allele Variant chromosome and strand come from the
		//       Variant Allele's Marker - we will not update this 
		// the domain is assumed to be a Curated Variant object

		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		AlleleVariant entity = variantDAO.get(Integer.valueOf(domain.getVariantKey()));
		Boolean modified = false;
		
		log.info("processAlleleVariant/update");


		// isReviewed
		log.info("process isReviewed");
		if(!String.valueOf(entity.getIsReviewed()).equals(domain.getIsReviewed())) {
			entity.setIsReviewed(Integer.valueOf(domain.getIsReviewed()).intValue());
		    modified = true;
			
		}
		// description
		log.info("process description");
		if (!entity.getDescription().equals(domain.getDescription())) {
			entity.setDescription(domain.getDescription());
			modified = true;
		}

		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			variantDAO.update(entity);
			log.info("processAlleleVariant/changes processed: " + domain.getVariantKey());
		}
		else {
			log.info("processAlleleVariant/no changes processed: " + domain.getVariantKey());
		}
		
		// process all notes DADT-180
		if(noteService.process(domain.getVariantKey(), domain.getCuratorNote(), mgiTypeKey, "1050", user)) {
			modified = true;
		}
		if(noteService.process(domain.getVariantKey(), domain.getPublicNote(), mgiTypeKey, "1051", user)) {
			modified = true;
		}

		// process reference DADT-180
		if (domain.getRefAssocs() != null) {
			log.info("referenceAssocService " + referenceAssocService);
			log.info("domain " + domain);
			if(referenceAssocService.process(domain.getVariantKey(), domain.getRefAssocs(), mgiTypeKey, user)) {
				modified = true;
			}
		}
		
		// process variant type - 
		if (domain.getVariantTypes() != null) {
			// parentKey, List ofAlleleVariantAnnotationDomain, annotTypeKey, qualifierKey, user
			if (annotationService.processAlleleVariant(domain.getVariantKey(), 
					domain.getVariantTypes(), 
					domain.getVariantTypes().get(0).getAnnotTypeKey(), 
					Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user)) {
				modified = true;
			}
		}

		// process variant effects
		if (domain.getVariantEffects() != null) {
			if (annotationService.processAlleleVariant(domain.getVariantKey(), 
					domain.getVariantEffects(), 
					domain.getVariantEffects().get(0).getAnnotTypeKey(), 
					Constants.VOC_GENERIC_ANNOTATION_QUALIFIER, user)) {
				modified = true;
			}
		}
		
		// process curated variant sequences DADT-178
		log.info("calling sequence service to process curated sequence");
		if (domain.getVariantSequences() != null) {
			if (sequenceService.process(String.valueOf(entity.get_variant_key()), domain.getVariantSequences(), user)) {
				modified = true;
			}
		}
		
		// process source variant sequences DADT-178
		log.info("calling sequence service to process source sequence");
		if (domain.getSourceVariant().getVariantSequences() != null) {
			log.info("source variant key");
			log.info("source variant key: " + entity.getSourceVariant().get_variant_key());
			log.info("domain variant sequences");
			log.info("domain variant sequences:" + domain.getSourceVariant().getVariantSequences());
			if(sequenceService.process(String.valueOf(entity.getSourceVariant().get_variant_key()), domain.getSourceVariant().getVariantSequences(), user)) {
				modified = true;
			}
			log.info("done processing source variant sequences");
		}
		// only if modifications were actually made
		if (modified == true) {
			entity.setModification_date(new Date());
			entity.setModifiedBy(user);
			variantDAO.update(entity);
			log.info("processVariant/changes processed: " + domain.getVariantKey());
		}
		else {
			log.info("processVariant/no changes processed: " + domain.getVariantKey());
		}
				
		// return entity translated to domain
		log.info("processAlleleVariant/update/returning results");
		results.setItem(translator.translate(entity, 0));
		return results;		
	}

	@Transactional
	public AlleleVariantDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		AlleleVariantDomain domain = new AlleleVariantDomain();
		if (variantDAO.get(key) != null) {
			domain = translator.translate(variantDAO.get(key),1);
		}
		return domain;
	}

    @Transactional
    public SearchResults<AlleleVariantDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results
    	SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
        results.setItem(translator.translate(variantDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<AlleleVariantDomain> delete(Integer key, User user) {
		// get the entity object and delete
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		AlleleVariant entity = variantDAO.get(key);
		results.setItem(translator.translate(variantDAO.get(key),0));
		variantDAO.remove(entity);
		return results;
	}
	
	@Transactional
	public List<SlimAlleleVariantDomain> search(AlleleVariantDomain searchDomain) {
		// return list of curated variants for specified query parameters

		List<SlimAlleleVariantDomain> results = new ArrayList<SlimAlleleVariantDomain>();

		// building SQL command : select + from + where + orderBy
		String cmd = "";		
		String select = "select distinct v._variant_key, a._allele_key, a.symbol";
		String from = "from all_variant v, all_allele a";
		String where = "where v._sourcevariant_key is not null"
				+ "\nand v._allele_key = a._allele_key";
		String orderBy = "order by a.symbol";
		String limit = "LIMIT 1000";
		String value;	
		Boolean from_strain = false;
		Boolean from_marker = false;
		Boolean from_sequence = false;
		Boolean from_variantType = false;
		Boolean from_variantTypeAcc = false;
		Boolean from_variantEffect = false;
		Boolean from_variantEffectAcc = false;
		Boolean from_note1 = false;	
		Boolean from_note2 = false;
		Boolean from_reference = false;
		Boolean from_alleleReferenceID = false;
		Boolean from_alleleID = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("v", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		if (searchDomain.getVariantKey() != null && !searchDomain.getVariantKey().isEmpty()) {
			where = where + "\nand v._variant_key = " + searchDomain.getVariantKey();
		}			
		if (searchDomain.getIsReviewed() != null && !searchDomain.getIsReviewed().isEmpty()) {
			where = where + "\nand v.isReviewed = " + searchDomain.getIsReviewed();
		}
		if (searchDomain.getDescription() != null && !searchDomain.getDescription().isEmpty()) {
			value = searchDomain.getDescription().replaceAll("'",  "''");
			where = where + "\nand v.description ilike '" + value + "'";
		}		
		
		// allele
		if (searchDomain.getAllele() != null) {
			if (searchDomain.getAllele().getAlleleKey() != null && !searchDomain.getAllele().getAlleleKey().isEmpty()) {
				where = where + "\nand v._allele_key = " + searchDomain.getAllele().getAlleleKey();
			}	
			if (searchDomain.getAllele().getSymbol() != null && !searchDomain.getAllele().getSymbol().isEmpty()) {
				where = where + "\nand a.symbol ilike '" + searchDomain.getAllele().getSymbol() + "'" ;
			}	
			if ((searchDomain.getAllele().getMgiAccessionIds() != null) && (searchDomain.getAllele().getMgiAccessionIds().size() > 0)) {
				if (searchDomain.getAllele().getMgiAccessionIds().get(0).getAccID().trim().length() > 0) {
					from_alleleID = true;
					StringBuffer alleleClauses = new StringBuffer("");
					for (String alleleID : searchDomain.getAllele().getMgiAccessionIds().get(0).getAccID().split(" ")) {
						if (alleleClauses.length() > 0) {
							alleleClauses.append(" or ");
						}
						alleleClauses.append(" aid.accID ilike '" + alleleID + "' ");
					}
					where = where + "\nand (" + alleleClauses.toString() + ")";
				}
			}
		}
	
		// strain
		if (searchDomain.getStrain() != null) {
			if (searchDomain.getStrain().getStrainKey() != null && !searchDomain.getStrain().getStrainKey().isEmpty()) {
				where = where + "\nand v._strain_key = " + searchDomain.getStrain().getStrainKey();
				from_strain = true;
			}
			if (searchDomain.getStrain().getStrain() != null && !searchDomain.getStrain().getStrain().isEmpty()) {
				where = where + "\nand p.strain ilike '" + searchDomain.getStrain().getStrain() + "'" ;
				from_strain = true;
			}
		}
		
		// marker
		if (searchDomain.getChromosome() != null && !searchDomain.getChromosome().isEmpty()) {
			where = where + "\nand m.chromosome ilike '" + searchDomain.getChromosome() + "'";
			from_marker = true;
		}
		if (searchDomain.getStrand() != null && !searchDomain.getStrand().isEmpty()) {
			where = where + "\nand m.strand ilike '" + searchDomain.getStrand() + "'";
			from_marker = true;
		}
		
		// sequence
		if (searchDomain.getVariantSequences() != null) {
			if (searchDomain.getVariantSequences().get(0).getStartCoordinate() != null 
					&& !searchDomain.getVariantSequences().get(0).getStartCoordinate().isEmpty()) {
				where = where + "\nand vs.startCoordinate = " + searchDomain.getVariantSequences().get(0).getStartCoordinate();
				from_sequence = true;
			}
			if (searchDomain.getVariantSequences().get(0).getEndCoordinate() != null 
					&& !searchDomain.getVariantSequences().get(0).getEndCoordinate().isEmpty()) {
				where = where + "\nand vs.endCoordinate = " + searchDomain.getVariantSequences().get(0).getEndCoordinate();
				from_sequence = true;
			}			
			if (searchDomain.getVariantSequences().get(0).getReferenceSequence() != null 
					&& !searchDomain.getVariantSequences().get(0).getReferenceSequence().isEmpty()) {
				value = searchDomain.getVariantSequences().get(0).getReferenceSequence().replaceAll(",",  "''");
				where = where + "\nand vs.referenceSequence ilike '" + value + "'";
				from_sequence = true;
			}		
			if (searchDomain.getVariantSequences().get(0).getVariantSequence() != null 
					&& !searchDomain.getVariantSequences().get(0).getVariantSequence().isEmpty()) {
				value = searchDomain.getVariantSequences().get(0).getVariantSequence().replaceAll(",",  "''");
				where = where + "\nand vs.referenceSequence ilike '" + value + "'";
				from_sequence = true;
			}		
		}

		// variant type
		if (searchDomain.getVariantTypes() != null) {
			if (searchDomain.getVariantTypes().get(0).getTerm() != null 
					&& !searchDomain.getVariantTypes().get(0).getTerm().isEmpty()) {
				value = searchDomain.getVariantTypes().get(0).getTerm().replaceAll(",",  "''");
				where = where + "\nand t1.term ilike '" + value + "'";
				from_variantType = true;
			}		
		}

		// variant type accession
		if (searchDomain.getVariantTypes() != null) {
			if (searchDomain.getVariantTypes().get(0).getAlleleVariantSOIds() != null) {
				if (searchDomain.getVariantTypes().get(0).getAlleleVariantSOIds().get(0).getAccID() != null 
						&& !searchDomain.getVariantTypes().get(0).getAlleleVariantSOIds().get(0).getAccID().isEmpty()) {
					where = where + "\nand va1.accID ilike '" + searchDomain.getVariantTypes().get(0).getAlleleVariantSOIds().get(0).getAccID() + "'";
					from_variantType = true;
					from_variantTypeAcc = true;
				}		
			}
		}

		// variant effect
		if (searchDomain.getVariantEffects() != null) {
			if (searchDomain.getVariantEffects().get(0).getTerm() != null 
					&& !searchDomain.getVariantEffects().get(0).getTerm().isEmpty()) {
				value = searchDomain.getVariantEffects().get(0).getTerm().replaceAll(",",  "''");
				where = where + "\nand t2.term ilike '" + value + "'";
				from_variantEffect = true;
			}		
		}

		// variant effect accession
		if (searchDomain.getVariantEffects() != null) {
			if (searchDomain.getVariantEffects().get(0).getAlleleVariantSOIds() != null) {
				if (searchDomain.getVariantEffects().get(0).getAlleleVariantSOIds().get(0).getAccID() != null 
						&& !searchDomain.getVariantEffects().get(0).getAlleleVariantSOIds().get(0).getAccID().isEmpty()) {
					where = where + "\nand va2.accID ilike '" + searchDomain.getVariantEffects().get(0).getAlleleVariantSOIds().get(0).getAccID() + "'";
					from_variantEffect = true;
					from_variantEffectAcc = true;
				}		
			}
		}		
		// curator notes
		if (searchDomain.getCuratorNote() != null) {
			value = searchDomain.getCuratorNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note1._notetype_key = 1050 and note1.note ilike '" + value + "'" ;
			from_note1 = true;
		}
		// public notes
		if (searchDomain.getPublicNote() != null) {
			value = searchDomain.getPublicNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note2._notetype_key = 1051 and note2.note ilike '" + value + "'" ;
			from_note2 = true;
		}
		
		// reference
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand vr._Ref_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replaceAll("'",  "''");
				where = where + "\nand vr.short_citation ilike '" + value + "'";
				from_reference = true;
			}
		}
		
		// references for variant's allele
		if ((searchDomain.getAllele() != null) && (searchDomain.getAllele().getRefAssocs() != null) && (searchDomain.getAllele().getRefAssocs().size() > 0)) {
			from_alleleReferenceID = true;
			StringBuffer jnumClauses = new StringBuffer("");
			for (String jnumID : searchDomain.getAllele().getRefAssocs().get(0).getJnumid().split(" ")) {
				if (jnumClauses.length() > 0) {
					jnumClauses.append(" or ");
				}
				jnumClauses.append(" rid.accID ilike '" + jnumID + "' ");
			}
			where = where + "\nand (" + jnumClauses.toString() + ")";
		}

		if (from_strain == true) {
			from = from + ", prb_strain p";
			where = where + "\nand v._strain_key = p._strain_key";
		}
		if (from_marker == true) {
			from = from + ", mrk_location_cache m";
			where = where + "\nand a._marker_key = m._marker_key";
		}
		if (from_sequence == true) {
			from = from + ", all_variant_sequence vs";
			where = where + "\nand v.variant_key = vs._variant_key";
		}
		if (from_variantType == true) {
			from = from + ", voc_annot v1, voc_term t1";
			where = where + "\nand v._variant_key = v1._object_key"
					+ "\nand v1._term_key = t1._term_key"
					+ "\nand v1._annottype_key = 1026";
		}
		if (from_variantTypeAcc == true) {
			from = from + ", acc_accession va1";
			where = where + "\nand v1._term_key = va1._object_key"
					+ "\nand va1._mgitype_key = 13"
					+ "\nand va1._logicaldb_key = 145";
		}
		if (from_variantEffect == true) {
			from = from + ", voc_annot v2, voc_term t2";
			where = where + "\nand v._variant_key = v2._object_key"
					+ "\nand v2._term_key = t2._term_key"
					+ "\nand v2._annottype_key = 1027";
		}
		if (from_alleleID == true) {
			from = from + ", acc_accession aid";
			where = where + "\nand a._allele_key = aid._object_key"
					+ "\nand aid._mgitype_key = 11";
		}		
		if (from_variantEffectAcc == true) {
			from = from + ", acc_accession va2";
			where = where + "\nand v2._term_key = va2._object_key"
					+ "\nand va2._mgitype_key = 13"
					+ "\nand va2._logicaldb_key = 145";
		}		
		if (from_note1 == true) {
			from = from + ", mgi_note_allelevariant_view note1";
			where = where + "\nand v._variant_key = note1._object_key";
		}
		if (from_note2 == true) {
			from = from + ", mgi_note_allelevariant_view note2";
			where = where + "\nand v._variant_key = note2._object_key";
		}		
		if (from_reference) {
			from = from + ", mgi_reference_allelevariant_view vr";
			where = where + "\nand v._variant_key = vr._object_key"
					+ "\nand vr._refassoctype_key = 1030";
		}
		if (from_alleleReferenceID == true) {
			from = from + ", mgi_reference_assoc mra, mgi_refassoctype rat, acc_accession rid";
			where = where + "\nand v._Allele_key = mra._Object_key"
					+ "\nand mra._RefAssocType_key = rat._RefAssocType_key"
					+ "\nand rat._MGIType_key = 11"
					+ "\nand mra._Refs_key = rid._object_key"
					+ "\nand rid._mgitype_key = 1";
		}		
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleVariantDomain domain = new SlimAlleleVariantDomain();
				domain = slimtranslator.translate(variantDAO.get(rs.getInt("_variant_key")),1);
				variantDAO.clear();
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
	public List<SlimAlleleVariantDomain> getSlimByAllele(Integer key) {
		// return SlimAlleleVariantDoman list of curated variants for specified allele key

		List<SlimAlleleVariantDomain> results = new ArrayList<SlimAlleleVariantDomain>();
		
		String cmd = "select _variant_key from all_variant"
				+ "\nwhere _sourcevariant_key is not null"
				+ "\nand _allele_key = " + key;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleVariantDomain domain = new SlimAlleleVariantDomain();				
				domain = slimtranslator.translate(variantDAO.get(rs.getInt("_variant_key")),1);
				variantDAO.clear();
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
	public List<AlleleVariantDomain> getByAllele(Integer key) {
		// return AlleleVariantDomain list of curated variants for specified allele key

		List<AlleleVariantDomain> results = new ArrayList<AlleleVariantDomain>();
		
		String cmd = "select _variant_key from all_variant"
				+ "\nwhere _sourcevariant_key is not null"
				+ "\nand _allele_key = " + key;
		log.info(cmd);
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AlleleVariantDomain domain = new AlleleVariantDomain();				
				domain = translator.translate(variantDAO.get(rs.getInt("_variant_key")),1);
				variantDAO.clear();
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
