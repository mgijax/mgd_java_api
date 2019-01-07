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
	public SearchResults<AlleleVariantDomain> update(AlleleVariantDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
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
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";		
		String select = "select distinct v._variant_key, a._allele_key, a.symbol, a._strain_key, p.strain";
		String from = "from all_variant v, all_allele a, prb_strain p";
		String where = "where v._allele_key = a._allele_key"
				+ "\nand v._strain_key = p._strain_key";
		String orderBy = "order by a.symbol";
		String limit = "LIMIT 1000";
		Boolean from_note = false;
		Boolean from_sequence = false;
		Boolean from_reference = false;

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("v", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getAllele().getSymbol() != null && !searchDomain.getAllele().getSymbol().isEmpty()) {
			where = where + "\nand v.symbol ilike '" + searchDomain.getAllele().getSymbol() + "'" ;
		}
		if (searchDomain.getStrain().getStrain() != null && !searchDomain.getStrain().getStrain().isEmpty()) {
			where = where + "\nand v.strain ilike '" + searchDomain.getStrain().getStrain() + "'" ;
		}
				
		// notes
//		if (searchDomain.getEditorNote() != null) {
//			value = searchDomain.getEditorNote().getNoteChunk().replaceAll("'",  "''");
//			where = where + "\nand note1._notetype_key = 1004 and note1.note ilike '" + value + "'" ;
//			from_note = true;
//		}
		
		
		// sequence
//		if (searchDomain.getHistory() != null) {
//			if (searchDomain.getHistory().get(0).getMarkerHistorySymbol() != null && !searchDomain.getHistory().get(0).getMarkerHistorySymbol().isEmpty()) {
//				where = where + "\nand mh.history ilike '" + searchDomain.getHistory().get(0).getMarkerHistorySymbol() + "'";
//				from_sequence = true;
//			}
//		}

		// reference
//		if (searchDomain.getRefAssocs() != null) {
//			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
//				where = where + "\nand mr._Ref_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
//				from_reference = true;
//			}
//			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
//				value = searchDomain.getRefAssocs().get(0).getShort_citation().replaceAll("'",  "''");
//				where = where + "\nand mr.short_citation ilike '" + value + "'";
//				from_reference = true;
//			}
//		}

		// use views to match the teleuse implementation

		if (from_note == true) {
			from = from + ", mgi_note_marker_view note";
			where = where + "\nand v._variant_key = note._object_key";
		}
		if (from_sequence == true) {
			from = from + ", all_variant_sequence s";
			where = where + "\nand v.variant_key = s._variant_key";
		}
		if (from_reference == true) {
			from = from + ", mgi_reference_assoc mr";
			where = where + "\nand v._variant_key = mr._object_key"
					+ "\nand mr._refassoctype_key = 1030";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);


		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleVariantDomain domain = new SlimAlleleVariantDomain();
//				domain.setVariantKey(rs.getString("_variant_key"));
				domain.setAlleleKey(rs.getString("_allele_key"));
				domain.setSymbol(rs.getString("symbol"));
//				domain.setStrainKey(rs.getString("_strain_key"));
//				domain.setStrain(rs.getString("strain");
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		return results;
	}	
	
}
