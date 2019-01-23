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
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleService extends BaseService<AlleleDomain> {

	protected Logger log = Logger.getLogger(AlleleService.class);
	
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
	public List<SlimAlleleDomain> search(AlleleDomain searchDomain) {

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._allele_key, a.symbol";
		String from = "from all_allele a, voc_term v1";
		String where = "where a._allele_type_key = v1._term_key";
		String orderBy = "order by a.symbol";
		String limit = "LIMIT 1000";	

		// if parameter exists, then add to where-clause
		
//		String cmResults[] = DateSQLQuery.queryByCreationModification("m", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
//		if (cmResults.length > 0) {
//			from = from + cmResults[0];
//			where = where + cmResults[1];
//		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			where = where + "\nand a.symbol ilike '" + searchDomain.getSymbol() + "'" ;
		}

		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleDomain domain = new SlimAlleleDomain();
				domain.setAlleleKey(rs.getString("_allele_key"));
				domain.setSymbol(rs.getString("symbol"));
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
	public List<SlimAlleleDomain> variant(Integer key) {
		// return all alleles for give allele key that contain allele variants

		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();
		
		String cmd = "select a._allele_key, a.symbol"
				+ "\nfrom all_allele a"
				+ "\nwhere a._allele_key = " + key
				+ "\nand exists (select 1 from all_variant v where a._allele_key = v._allele_key)"
				+ "\norder by a.symbol";
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				SlimAlleleDomain domain = new SlimAlleleDomain();
				domain = slimtranslator.translate(alleleDAO.get(key),1);
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
