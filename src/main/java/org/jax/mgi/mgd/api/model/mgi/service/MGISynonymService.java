package org.jax.mgi.mgd.api.model.mgi.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.dao.MGISynonymDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.MGISynonymSearchForm;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class MGISynonymService extends BaseService<MGISynonymDomain> {

	@Inject
	private MGISynonymDAO synonymDAO;

	private MGISynonymTranslator translator = new MGISynonymTranslator();
	
	@Transactional
	public SearchResults<MGISynonymDomain> create(MGISynonymDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<MGISynonymDomain> update(MGISynonymDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public MGISynonymDomain get(Integer key) {
		//return translator.translate(synonymDAO.get(key));
		return translator.translate(synonymDAO.get(key));
	}

	@Transactional
	public SearchResults<MGISynonymDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
