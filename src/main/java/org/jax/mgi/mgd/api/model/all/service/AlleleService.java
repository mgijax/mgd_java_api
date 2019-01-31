package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleService extends BaseService<AlleleDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleDAO alleleDAO;

	private AlleleTranslator translator = new AlleleTranslator();
	private SlimAlleleTranslator slimtranslator = new SlimAlleleTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AlleleDomain> create(AlleleDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AlleleDomain> update(AlleleDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AlleleDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		AlleleDomain domain = new AlleleDomain();
		if (alleleDAO.get(key) != null) {
			domain = translator.translate(alleleDAO.get(key),1);
		}
		return domain;	
	}

    @Transactional
    public SearchResults<AlleleDomain> getResults(Integer key) {
		// get the DAO/entity and translate -> domain -> results 
    	SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
        results.setItem(translator.translate(alleleDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<AlleleDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public List<SlimAlleleDomain> search(AlleleDomain searchDomain, Boolean hasVariant) {

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._allele_key, a.symbol";
		String from = "from all_allele a, voc_term v1";
		String where = "where a._allele_type_key = v1._term_key";
		String orderBy = "order by a.symbol";
		String limit = "LIMIT 1000";	
		String value;
		Boolean from_marker = false;
		Boolean from_accession = false;
		Boolean from_reference = false;
		Boolean from_variant = false;
		
		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("aa", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
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

		// marker location cache
		if (searchDomain.getChromosome() != null && !searchDomain.getChromosome().isEmpty()) {
			where = where + "\nand m.chromosome ilike '" + searchDomain.getChromosome() + "'" ;
			from_marker = true;
		}
		if (searchDomain.getStrand() != null && !searchDomain.getStrand().isEmpty()) {
			where = where + "\nand m.strand ilike '" + searchDomain.getStrand() + "'" ;
			from_marker = true;
		}
				
		// allele accession id 
		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().get(0).getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getMgiAccessionIds().get(0).getAccID() + "'";
			from_accession = true;
		}
						
		// reference; allow > 1 jnumid
		if (searchDomain.getRefAssocs() != null) {
			if (searchDomain.getRefAssocs().get(0).getRefsKey() != null && !searchDomain.getRefAssocs().get(0).getRefsKey().isEmpty()) {
				where = where + "\nand ref._Ref_key = " + searchDomain.getRefAssocs().get(0).getRefsKey();
				from_reference = true;
			}
			if (searchDomain.getRefAssocs().get(0).getShort_citation() != null && !searchDomain.getRefAssocs().get(0).getShort_citation().isEmpty()) {
				value = searchDomain.getRefAssocs().get(0).getShort_citation().replaceAll("'",  "''");
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
	
		// if searching for allele variants
		if (hasVariant == true) {
			from_variant = true;
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
		
		if (from_variant == true) {
			from = from + ", all_variant av";
			where = where + "\nand a._allele_key = av._allele_key";
		}
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleDomain domain = new SlimAlleleDomain();
				domain = slimtranslator.translate(alleleDAO.get(rs.getInt("_allele_key")),1);				
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
