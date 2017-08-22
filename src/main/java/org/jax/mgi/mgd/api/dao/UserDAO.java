package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

import org.jax.mgi.mgd.api.entities.User;

@RequestScoped
public class UserDAO extends PostgresSQLDAO<User> {

	public UserDAO() {
		myClass = User.class;
	}

	/* get the User object for the given login, or null if unknown user
	 */
	public User get(String username) {
		if ((username == null) || (username.length() == 0)) { return null; }

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(myClass);
		Root<User> root = query.from(myClass);
		Path<String> column = root.get("login");
		Expression<String> lowerColumn = builder.lower(column);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(lowerColumn, username.toLowerCase()));
		query.where(builder.and(restrictions.toArray(new Predicate[0])));

		List<User> matches = entityManager.createQuery(query).setMaxResults(1).getResultList();
		if ((matches == null) || (matches.size() == 0)) {
			return null;
		}
		return matches.get(0);
	}
}
