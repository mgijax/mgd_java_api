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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeStrainDAO;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleVariantService extends BaseService<AlleleVariantDomain> {

	protected Logger log = Logger.getLogger(AlleleVariantService.class);

	@Inject
	private AlleleVariantDAO variantDAO;
	@Inject 
	AlleleDAO alleleDAO;
	@Inject
	private ProbeStrainDAO strainDAO;
	@Inject
	private AnnotationService annotationService;
	
	// translate an entity to a domain to return in the results
	private AlleleVariantTranslator translator = new AlleleVariantTranslator();

	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AlleleVariantDomain> create(AlleleVariantDomain domain, User user) {
		
		// create new entity object from in-coming domain
		// the Entities class handles the generation of the primary key
		
		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		AlleleVariant entity = new AlleleVariant();
		
		// AlleleVariant key will be auto-sequence
		
		// set the Allele
		entity.setAllele(alleleDAO.get(Integer.valueOf(domain.getAllele().getAlleleKey())));
		
		// this is the source variant, so _sourceVariant_key will be null
		
		// set the Strain
		entity.setStrain(strainDAO.get(Integer.valueOf(domain.getStrain().getStrainKey())));
		
		// here I assume that either the default (false) or true is being
		// sent in. Or should the create method always set this to false?
		entity.setIsReviewed(Integer.valueOf(domain.getIsReviewed()).intValue());
		
		// description can be null - do we need to check that or can we just 
		// do the following?
		entity.setDescription(domain.getDescription());
		
		// creation/modification date/by
		entity.setCreatedBy(user);
		entity.setCreation_date(new Date());
		entity.setModifiedBy(user);
		entity.setModification_date(new Date());
		
		// insert into the database
		variantDAO.persist(entity);

		// return entity translated to domain, set in results
		// results has other info too
		log.info("AlleleVariant/create/returning results");
		results.setItem(translator.translate(entity,0));
		log.info("Translator returned");
		return results;
	}

	@Transactional
	public SearchResults<AlleleVariantDomain> update(AlleleVariantDomain domain, User user) {
		// the set of fields in "update" is similar to set of fields in "create"
		// creation user/date are only set in "create"

		SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
		AlleleVariant entity = variantDAO.get(Integer.valueOf(domain.getVariantKey()));
		//Boolean modified = false;
		//String mgiTypeKey = "45";
		//String mgiTypeName = "Allele Variant";
		
		log.info("processAlleleVariant/update");

		// process variant type annotations
		//if (domain.getVariantTypes() != null) {
		//	annotationService.process(domain.getVariantKey(), domain.getVariantTypes(), "1026", user);
		//}
		
		// return entity translated to domain
		log.info("processAlleleVariant/update/returning results");
		results.setItem(translator.translate(entity, 0));
		return results;		
	}

	@Transactional
	public AlleleVariantDomain get(Integer key) {
		return translator.translate(variantDAO.get(key),1);
	}

    @Transactional
    public SearchResults<AlleleVariantDomain> getResults(Integer key) {
        SearchResults<AlleleVariantDomain> results = new SearchResults<AlleleVariantDomain>();
        results.setItem(translator.translate(variantDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<AlleleVariantDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SlimAlleleVariantDomain> search(AlleleVariantDomain searchDomain) {

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
		Boolean from_note = false;		
		Boolean from_reference = false;
		Boolean from_referenceID = false;
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
		if (searchDomain.getSourceVariantKey() != null && !searchDomain.getSourceVariantKey().isEmpty()) {
			where = where + "\nand v._sourcevariant_key = " + searchDomain.getSourceVariantKey();
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
		// notes
		if (searchDomain.getGeneralNote() != null) {
			value = searchDomain.getGeneralNote().getNoteChunk().replaceAll("'",  "''");
			where = where + "\nand note1._notetype_key = 1050 and note1.note ilike '" + value + "'" ;
			from_note = true;
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
		if ((searchDomain.getAllele().getRefAssocs() != null) && (searchDomain.getAllele().getRefAssocs().size() > 0)) {
			from_referenceID = true;
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
		if (from_note == true) {
			from = from + ", mgi_note_allelevariant_view note";
			where = where + "\nand v._variant_key = note._object_key";
		}
		if (from_reference || from_referenceID) {
			from = from + ", mgi_reference_allelevariant_view vr";
			where = where + "\nand v._variant_key = vr._object_key"
					+ "\nand vr._refassoctype_key = 1030";
		}
		if (from_referenceID == true) {
			from = from + ", acc_accession rid";
			where = where + "\nand vr._Refs_key = rid._object_key"
					+ "\nand rid._mgitype_key = 1";
		}		
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleVariantDomain domain = new SlimAlleleVariantDomain();
				domain.setVariantKey(rs.getString("_variant_key"));
				domain.setAlleleKey(rs.getString("_allele_key"));
				domain.setSymbol(rs.getString("symbol"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
