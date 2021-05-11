package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeTissueDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeTissueDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeTissue;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTissueTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeTissueService extends BaseService<ProbeTissueDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	ProbeTissueDAO probeTissueDAO;
	
	private ProbeTissueTranslator translator = new ProbeTissueTranslator();	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeTissueDomain> create(ProbeTissueDomain domain, User user) {
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();		
		ProbeTissue entity = new ProbeTissue();

		log.info("processTissue/create");
		
		if (domain.getStandard() == null || domain.getStandard().isEmpty()) {
			domain.setStandard("1");
		}
		
		entity.setTissue(domain.getTissue());
		entity.setStandard(Integer.valueOf(domain.getStandard()));
		entity.setCreation_date(new Date());
		entity.setModification_date(new Date());	
		
		// execute persist/insert/send to database		
		probeTissueDAO.persist(entity);
		
		log.info("processTissue/create/returning results");
		results.setItem(translator.translate(entity));		
		return results;
	}
	
	@Transactional
	public SearchResults<ProbeTissueDomain> update(ProbeTissueDomain domain, User user) {	
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeTissueDomain> delete(Integer key, User user) {
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}
	
	@Transactional
	public ProbeTissueDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeTissueDomain domain = new ProbeTissueDomain();
		if (probeTissueDAO.get(key) != null) {
			domain = translator.translate(probeTissueDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeTissueDomain> getResults(Integer key) {
        SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
        results.setItem(translator.translate(probeTissueDAO.get(key)));
        return results;
    }

	@Transactional	
    public SearchResults<ProbeTissueDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbeTissueDomain> results = new SearchResults<ProbeTissueDomain>();
		String cmd = "select count(*) as objectCount from prb_tissue";
		
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
	public List<ProbeTissueDomain> validateTissue(ProbeTissueDomain searchDomain) {

		List<ProbeTissueDomain> results = new ArrayList<ProbeTissueDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from prb_Tissue a";
		String where = "where a._Tissue_key is not null";
		String orderBy = "order by a.tissue";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
			
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", null, null, searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		if (searchDomain.getStandard() != null && ! searchDomain.getStandard().isEmpty()) {
			where = where + " and a.standard = " + searchDomain.getStandard();
		}
		
		if (searchDomain.getTissue() != null && ! searchDomain.getTissue().isEmpty()) {
			where = where + " and a.tissue ilike '" + searchDomain.getTissue() + "'";
		} 
		
		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ProbeTissueDomain domain = new ProbeTissueDomain();
				domain = translator.translate(probeTissueDAO.get(rs.getInt("_Tissue_key")));				
				probeTissueDAO.clear();
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
	public SearchResults<String> getTissueList() {
		// generate SQL command to return a list of distinct strains
		
		List<String> results = new ArrayList<String>();

		// building SQL command : select + from + where + orderBy
		String cmd = "select distinct tissue from PRB_Tissue";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				results.add(rs.getString("tissue"));
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(results);
		return new SearchResults<String>(results);
	}	
	   
}
