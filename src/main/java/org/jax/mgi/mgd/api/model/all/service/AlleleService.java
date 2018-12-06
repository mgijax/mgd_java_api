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
import org.jax.mgi.mgd.api.model.all.domain.AlleleEIResultDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleService extends BaseService<AlleleDomain> {

	protected Logger log = Logger.getLogger(AlleleService.class);
	
	@Inject
	private AlleleDAO alleleDAO;

	private AlleleTranslator translator = new AlleleTranslator();
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
		return translator.translate(alleleDAO.get(key));
	}

    @Transactional
    public SearchResults<AlleleDomain> getResults(Integer key) {
        SearchResults<AlleleDomain> results = new SearchResults<AlleleDomain>();
        results.setItem(translator.translate(alleleDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<AlleleDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AlleleEIResultDomain> eiSearch(AlleleDomain searchDomain) {

		// list of results to be returned
		List<AlleleEIResultDomain> results = new ArrayList<AlleleEIResultDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select distinct a._allele_key, a.symbol";
		String from = "from all_allele a, voc_term v1";
		String where = "where a._allele_type_key = v1._term_key";
		String orderBy = "order by a.symbol";
		String limit = "LIMIT 1000";
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
		log.info(cmd);

		// if parameter exists, then add to where-clause
		
		String cmResults[] = DateSQLQuery.queryByCreationModification("m", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
		
		if (searchDomain.getSymbol() != null && !searchDomain.getSymbol().isEmpty()) {
			where = where + "\nand a.symbol ilike '" + searchDomain.getSymbol() + "'" ;
		}
		
		// execute sql, returns results to MarkerEIResultDomain
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AlleleEIResultDomain domain = new AlleleEIResultDomain();
				domain.setAlleleKey(rs.getInt("_allele_key"));
				domain.setSymbol(rs.getString("symbol"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// ...off to be turned into JSON
		return results;
	}
}

