package org.jax.mgi.mgd.api.model.mgi.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.UserDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class UserService extends BaseService<UserDomain> {

	@Inject
	private UserDAO userDAO;
	
	private Logger log = Logger.getLogger(getClass());

	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<UserDomain> create(UserDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<UserDomain> update(UserDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public UserDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

    @Transactional
    public SearchResults<UserDomain> getResults(Integer key) {
		// TODO Auto-generated method stub
		return null;
    }
    
	@Transactional
	public SearchResults<UserDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UserDomain> search() {

		// list of results to be returned
		List<UserDomain> results = new ArrayList<UserDomain>();

		String cmd = "select * from mgi_user_active_view order by login";
		log.info(cmd);

		// request data, and parse results
		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				UserDomain domain = new UserDomain();

				domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
				domain.setUserKey(rs.getString("_user_key"));
				domain.setUserTypeKey(rs.getString("_usertype_key"));
				domain.setUserStatusKey(rs.getString("_userstatus_key"));
				domain.setUserLogin(rs.getString("login"));
				domain.setUserName(rs.getString("name"));
				domain.setOrcid(rs.getString("orcid"));
				domain.setGroupKey(rs.getString("_group_key"));
				domain.setCreatedByKey(rs.getString("_createdby_key"));
				domain.setCreatedBy(rs.getString("createdby"));
				domain.setModifiedByKey(rs.getString("_modifiedby_key"));
				domain.setModifiedBy(rs.getString("modifiedby"));
				domain.setCreation_date(rs.getString("creation_date"));
				domain.setModification_date(rs.getString("modification_date"));
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {e.printStackTrace();}
		
		// ...off to be turned into JSON
		return results;
	}
	
	/* get the User object corresponding to the given username (Linux login) */
	public User getUserByUsername(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login", username);
		if(userDAO.search(map).total_count > 0) {
			log.info("User found: " + username);
			return userDAO.search(map).items.get(0);
		} else {
			log.info("User NOT found: " + username);
			//map.put("login", "mgd_dbo");
			//return userDAO.search(map).items.get(0);
			return null;
		}
	}

}
