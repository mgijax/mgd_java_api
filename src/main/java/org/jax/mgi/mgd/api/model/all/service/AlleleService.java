package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
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

	private AlleleTranslator translator = new AlleleTranslator();
	private SlimAlleleTranslator slimtranslator = new SlimAlleleTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<AlleleDomain> create(AlleleDomain object, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleDomain> update(AlleleDomain object, User user) {
		SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
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
	public AlleleDomain get(Integer key) {
		// get the DAO/entity and translate -> domain	
		AlleleDomain domain = new AlleleDomain();
		if (alleleDAO.get(key) != null) {
			domain = translator.translate(alleleDAO.get(key));
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
	public List<SlimAlleleDomain> search(AlleleDomain searchDomain, Boolean hasVariant) {

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._allele_key, a.symbol";
		String from = "from all_allele a, voc_term v1";
		String where = "where a._allele_type_key = v1._term_key";
		String orderBy = "order by a.symbol";
		String limit = Constants.SEARCH_RETURN_LIMIT;
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

		// In testing this can return a null List, a List with a null domain, and probably? an empty list?
		if (searchDomain.getMgiAccessionIds() != null && !searchDomain.getMgiAccessionIds().isEmpty() && searchDomain.getMgiAccessionIds().get(0).getAccID() != null) { 
			cmd = cmd + "\nand a.accid = '" + searchDomain.getMgiAccessionIds().get(0).getAccID() + "'";				 
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
				SlimAccessionDomain accessionDomain = new SlimAccessionDomain();
				List<SlimAccessionDomain> accessions = new ArrayList<SlimAccessionDomain>();
				alleleDomain.setAlleleKey(rs.getString("_allele_key"));
				accessionDomain.setAccID(rs.getString("accID"));
				accessions.add(accessionDomain);
				alleleDomain.setMgiAccessionIds(accessions);
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
