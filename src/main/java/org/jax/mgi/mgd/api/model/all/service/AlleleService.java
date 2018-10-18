package org.jax.mgi.mgd.api.model.all.service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleEIResultDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.search.AlleleSearchForm;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleService extends BaseService<AlleleDomain> implements BaseSearchInterface<AlleleDomain, AlleleSearchForm> {

	protected Logger log = Logger.getLogger(AlleleService.class);
	
	@Inject
	private AlleleDAO alleleDAO;

	private AlleleTranslator translator = new AlleleTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public AlleleDomain create(AlleleDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AlleleDomain update(AlleleDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AlleleDomain get(Integer key) {
		return translator.translate(alleleDAO.get(key));
	}

	@Transactional
	public AlleleDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AlleleDomain> search(AlleleSearchForm searchForm) {
		SearchResults<Allele> alleles;
		if(searchForm.getOrderBy() != null) {
			alleles = alleleDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			alleles = alleleDAO.search(searchForm.getSearchFields());
		}
		Iterable<AlleleDomain> newItems = translator.translateEntities(alleles.items, searchForm.getSearchDepth());
		return new SearchResults<AlleleDomain>(newItems);
	}

	public AlleleEIResultDomain eiSearch(AlleleSearchForm searchForm) {
		// domain object to be JSON-ed
		AlleleEIResultDomain alleleEIResultDomain = new AlleleEIResultDomain();
		
		// markerKey-map of attributes ordered (linked) mapping for summary
		Map<String, HashMap> results = new LinkedHashMap<String, HashMap>();

		Map<String, Object> params = searchForm.getSearchFields();
		log.info(params);
		
		// formulate sql query
		String cmd = "";
		String select = "";
		String from = "";
		String where = "";
		
		// SELECT
		select = select + "select a._allele_key, a.symbol, t1.term as alleletype, t2.term as allelestatus ";
				
		// FROM
		from = from + "from all_allele a, voc_term t1, voc_term t2 ";
		
		// WHERE
				where = where + "where a._allele_type_key = t1._term_key and a._allele_status_key = t2._term_key ";
				if (params.containsKey("symbol")) {
					where = where + "and a.symbol ilike '" + params.get("symbol")  +"' " ;
				}
				
				if (params.containsKey("alleletype")) {
					where = where + "and t1.term  ilike '" + params.get("alleletype") +"' " ;
				}
				if (params.containsKey("allelestatus")) {
					where = where + "and t2.term ilike '" + params.get("allelestatus") +"' " ;
				}
				
				// CATENATE COMMAND
				cmd = select + from + where + "order by a.symbol";
				log.info(cmd);

				// request data, and parse results
				try {
					ResultSet rs = sqlExecutor.executeProto(cmd);
					while (rs.next()) {
						HashMap<String, String> attr = new HashMap<String, String>();
						String alleleKey = rs.getString("_allele_key");
						attr.put("symbol", rs.getString("symbol"));
						attr.put("alleletype", rs.getString("alleletype"));
						attr.put("allelestatus", rs.getString("allelestatus"));
						results.put(alleleKey , attr);
					}
					sqlExecutor.cleanup();
				}
				catch (Exception e) {e.printStackTrace();}
				
				// ...off to be turned into JSON
				alleleEIResultDomain.setResults(results);
				return alleleEIResultDomain;
	}


}
