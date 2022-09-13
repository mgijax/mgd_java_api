package org.jax.mgi.mgd.api.model.dag.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.dag.dao.DagEdgeDAO;
import org.jax.mgi.mgd.api.model.dag.domain.DagEdgeDomain;
import org.jax.mgi.mgd.api.model.dag.domain.DagNodeDomain;
import org.jax.mgi.mgd.api.model.dag.translator.DagEdgeTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import io.swagger.annotations.ApiOperation;

@RequestScoped
public class DagEdgeService extends BaseService<DagEdgeDomain> {

	@Inject
	private DagEdgeDAO dagEdgeDAO;

	private DagEdgeTranslator translator = new DagEdgeTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	protected Logger log = Logger.getLogger(getClass());

	@Transactional
	public SearchResults<DagEdgeDomain> create(DagEdgeDomain object, User user) {
		SearchResults<DagEdgeDomain> results = new SearchResults<DagEdgeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<DagEdgeDomain> update(DagEdgeDomain object, User user) {
		SearchResults<DagEdgeDomain> results = new SearchResults<DagEdgeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<DagEdgeDomain> delete(Integer key, User user) {
		SearchResults<DagEdgeDomain> results = new SearchResults<DagEdgeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public DagEdgeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		DagEdgeDomain domain = new DagEdgeDomain();
		if (dagEdgeDAO.get(key) != null) {
			domain = translator.translate(dagEdgeDAO.get(key));
		}
		dagEdgeDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<DagEdgeDomain> getResults(Integer key) {
		SearchResults<DagEdgeDomain> results = new SearchResults<DagEdgeDomain>();
		results.setItem(translator.translate(dagEdgeDAO.get(key)));
		dagEdgeDAO.clear();
		return results;
    }

	@Transactional	
	public List<DagEdgeDomain> search(DagEdgeDomain searchDomain) {
		// using searchDomain fields, generate SQL command
		
		// not implemented
		List<DagEdgeDomain> results = new ArrayList<DagEdgeDomain>();
		
		return results;
	}	

	@POST
	@ApiOperation(value = "Get Edge Siblings by Parent key")
	@Path("/getSiblingsByParent")
	public List<DagEdgeDomain> getSiblingsByParent(DagNodeDomain parentdomain) {
		// return list of sibling dag edges based on parent key
			
		List<DagEdgeDomain> results = new ArrayList<DagEdgeDomain>();
		
		String cmd = "select * from dag_edge where _parent_key = " + parentdomain.getChildEdges().get(0).getParentKey()
				+ "\nand _child_key != " + parentdomain.getChildEdges().get(0).getChildKey();
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);			
			while (rs.next()) {
				DagEdgeDomain domain = new DagEdgeDomain();
				log.info("getSiblingsByParent():translate sql");
				domain = translator.translate(dagEdgeDAO.get(rs.getInt("_edge_key")));	
				log.info("getSiblingsByParent():clear()");
				dagEdgeDAO.clear();	
				log.info("getSiblingsByParent():results.add()");				
				results.add(domain);
				log.info("getSiblingsByParent():domain added to results");
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("getSiblingsByParent():returning results");
		return results;
	}	
	
}
