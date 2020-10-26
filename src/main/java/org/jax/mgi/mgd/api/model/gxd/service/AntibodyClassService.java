package org.jax.mgi.mgd.api.model.gxd.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyClassDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyClassDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyClassTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateSQLQuery;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AntibodyClassService extends BaseService<AntibodyClassDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Inject
	private AntibodyClassDAO antibodyClassDAO;
	
	private AntibodyClassTranslator translator = new AntibodyClassTranslator();
	
	private SQLExecutor sqlExecutor = new SQLExecutor();
	
	
	@Transactional
	public SearchResults<AntibodyClassDomain> create(AntibodyClassDomain domain, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}

	@Transactional
	public SearchResults<AntibodyClassDomain> update(AntibodyClassDomain domain, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AntibodyClassDomain> delete(Integer key, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
		
	}
	
	@Transactional
	public AntibodyClassDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AntibodyClassDomain domain = new AntibodyClassDomain();
		if (antibodyClassDAO.get(key) != null) {
			domain = translator.translate(antibodyClassDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<AntibodyClassDomain> getResults(Integer key) {
        SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
        results.setItem(translator.translate(antibodyClassDAO.get(key)));
        return results;
    } 
	
	@Transactional	
	public SearchResults<AntibodyClassDomain> getObjectCount() {
		// return the object count from the database
		
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		String cmd = "select count(*) as objectCount from gxd_AntibodyClass";
		
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
	public List<AntibodyClassDomain> search(AntibodyClassDomain searchDomain) {

		List<AntibodyClassDomain> results = new ArrayList<AntibodyClassDomain>();
		
		// building SQL command : select + from + where + orderBy
		// use teleuse sql logic (ei/csrc/mgdsql.c/mgisql.c) 
		String cmd = "";
		String select = "select a.*";
		String from = "from gxd_AntibodyClass a";
		String where = "where a._AntibodyClass_key is not null";
		String orderBy = "order by a.class";
		
		// antibodyName
		if(searchDomain.getAntibodyClass() != null && ! searchDomain.getAntibodyClass().isEmpty()) {
			where = where + "\n and a.class ilike '" + searchDomain.getAntibodyClass() + "'";
		}
		
		// create/mod date (no 'by')
		String cmResults[] = DateSQLQuery.queryByCreationModification("a", null, null, searchDomain.getCreation_date(), searchDomain.getModification_date());
		if (cmResults.length > 0) {
			from = from + cmResults[0];
			where = where + cmResults[1];
		}

		// make this easy to copy/paste for troubleshooting
		cmd = "\n" + select + "\n" + from + "\n" + where + "\n" + orderBy;
		log.info(cmd);
		
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				AntibodyClassDomain domain = new AntibodyClassDomain();
				domain = translator.translate(antibodyClassDAO.get(rs.getInt("_antibodyclass_key")));				
				antibodyClassDAO.clear();
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
