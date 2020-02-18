package org.jax.mgi.mgd.api.model.prb.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ProbeService extends BaseService<ProbeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private ProbeDAO probeDAO;

	private ProbeTranslator translator = new ProbeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<ProbeDomain> create(ProbeDomain object, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeDomain> update(ProbeDomain object, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeDomain domain = new ProbeDomain();
		if (probeDAO.get(key) != null) {
			domain = translator.translate(probeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeDomain> getResults(Integer key) {
        SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
        results.setItem(translator.translate(probeDAO.get(key)));
        return results;
    }

	@Transactional	
	public SearchResults<ProbeDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		String cmd = "select count(*) as objectCount from gxd_antigen";
		
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
	public List<ProbeDomain> search(ProbeDomain searchDomain) {

		List<ProbeDomain> results = new ArrayList<ProbeDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from prb_probe a";
		String where = "where a._probe_key is not null";
		String orderBy = "order by a.probe";
		//String limit = Constants.SEARCH_RETURN_LIMIT;
		//String value;
		Boolean from_accession = false;
		
		//
		// IN PROGRESSS
		//
		
		// if parameter exists, then add to where-clause
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", searchDomain.getCreatedBy(), searchDomain.getModifiedBy(), searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}
						
		// accession id 
		if (searchDomain.getAccID() != null && !searchDomain.getAccID().isEmpty()) {	
			where = where + "\nand acc.accID ilike '" + searchDomain.getAccID() + "'";
			from_accession = true;
		}						
	
		if (from_accession == true) {
			from = from + ", prb_probe_acc_view acc";
			where = where + "\nand a._assay_key = acc._object_key"; 
		}

		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				ProbeDomain domain = new ProbeDomain();
				domain = translator.translate(probeDAO.get(rs.getInt("_antigen_key")));				
				probeDAO.clear();
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
