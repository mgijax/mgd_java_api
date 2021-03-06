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
import org.jax.mgi.mgd.api.model.mgi.domain.SlimUserDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.translator.UserTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class UserService extends BaseService<UserDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private UserDAO userDAO;
	
	private UserTranslator translator = new UserTranslator();
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<UserDomain> create(UserDomain object, User user) {
		SearchResults<UserDomain> results = new SearchResults<UserDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<UserDomain> update(UserDomain object, User user) {
		SearchResults<UserDomain> results = new SearchResults<UserDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<UserDomain> delete(Integer key, User user) {
		SearchResults<UserDomain> results = new SearchResults<UserDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
		
	@Transactional
	public UserDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		UserDomain domain = new UserDomain();
		if (userDAO.get(key) != null) {
			domain = translator.translate(userDAO.get(key));
		}
		userDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<UserDomain> getResults(Integer key) {
		SearchResults<UserDomain> results = new SearchResults<UserDomain>();
		results.setItem(translator.translate(userDAO.get(key)));
		userDAO.clear();
		return results;
    }

	@Transactional
	public List<UserDomain> search() {

		List<UserDomain> results = new ArrayList<UserDomain>();

		String cmd = "select * from mgi_user_active_view order by login";
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {
				UserDomain domain = new UserDomain();
				domain = translator.translate(userDAO.get(rs.getInt("_user_key")));
				userDAO.clear();
				results.add(domain);
			}
			sqlExecutor.cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	/* get the User object corresponding to the given username (Linux login) */
	public User getUserByUsername(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login", username);
		if(userDAO.search(map).total_count > 0) {
			log.info(Constants.LOG_SUCCESS_USERLOGIN + username);
			return userDAO.search(map).items.get(0);
		} else {
			log.info(Constants.LOG_FAIL_USERLOGIN + username);
			return null;
		}
	}

	/* check EI module permissions
	 * if permissions valid, then user key returned from SP = user key
	 * else, user key returned from SP = 0
	 * 
	 * select login, userrole from mgi_userrole_view order by login
	 * 
	 * PWI and TeleUSE/EI use this stored procedure
	 */
	@Transactional
	public List<SlimUserDomain> validEIPermissions(String eiModule, User user) {

		List<SlimUserDomain> results = new ArrayList<SlimUserDomain>();
		
		String cmd = "select * from MGI_checkUserRole("
				+ "'" + eiModule + "'"
				+ ",'" + user.getLogin() + "'"
				+ ")";		log.info(cmd);
		log.info(cmd);

		try {
			ResultSet rs = sqlExecutor.executeProto(cmd);
			while (rs.next()) {	
				SlimUserDomain domain = new SlimUserDomain();
				domain.setUserKey(rs.getString(1));
				domain.setUserLogin(user.getLogin());
				results.add(domain);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
		return results;		
	}
	
}
