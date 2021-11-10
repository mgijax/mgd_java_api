package org.jax.mgi.mgd.api.model.dag.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.dag.dao.DagNodeDAO;
import org.jax.mgi.mgd.api.model.dag.domain.DagNodeDomain;
import org.jax.mgi.mgd.api.model.dag.translator.DagNodeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class DagNodeService extends BaseService<DagNodeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private DagNodeDAO dagNodeDAO;

	private DagNodeTranslator translator = new DagNodeTranslator();
	
	//private SQLExecutor sqlExecutor = new SQLExecutor();
	
	@Transactional
	public SearchResults<DagNodeDomain> create(DagNodeDomain object, User user) {
		SearchResults<DagNodeDomain> results = new SearchResults<DagNodeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<DagNodeDomain> update(DagNodeDomain object, User user) {
		SearchResults<DagNodeDomain> results = new SearchResults<DagNodeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<DagNodeDomain> delete(Integer key, User user) {
		SearchResults<DagNodeDomain> results = new SearchResults<DagNodeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public DagNodeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		DagNodeDomain domain = new DagNodeDomain();
		if (dagNodeDAO.get(key) != null) {
			domain = translator.translate(dagNodeDAO.get(key));
		}
		dagNodeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<DagNodeDomain> getResults(Integer key) {
		SearchResults<DagNodeDomain> results = new SearchResults<DagNodeDomain>();
		results.setItem(translator.translate(dagNodeDAO.get(key)));
		dagNodeDAO.clear();
		return results;
    }

	@Transactional	
	public List<DagNodeDomain> search(DagNodeDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		// not implemented
		List<DagNodeDomain> results = new ArrayList<DagNodeDomain>();

		// edit if search is needed
//		// building SQL command : select + from + where + orderBy
//		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
//		String cmd = "";
//		String select = "select * from";
//		String from = "from ";
//		String where = "where ";
//		String 	orderBy = "order by c";			
//		String limit = Constants.SEARCH_RETURN_LIMIT;
//		
//		// make this easy to copy/paste for troubleshooting
//		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy + "\n" + limit;
//		log.info(cmd);
//
//		try {
//			ResultSet rs = sqlExecutor.executeProto(cmd);
//			while (rs.next()) {
//				?Domain domain = new ?Domain();
//				domain.setPubmedid(rs.getString("pubmedid"));
//				results.add(domain);
//			}
//			sqlExecutor.cleanup();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return results;
	}	
	
}
