package org.jax.mgi.mgd.api.model.mgi.service;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.NoteSearchForm;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class NoteService extends BaseService<NoteDomain> implements BaseSearchInterface<NoteDomain, NoteSearchForm> {

//	@Inject
//	private NoteDAO organismDAO;

	@Override
	public NoteDomain create(NoteDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteDomain update(NoteDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults<NoteDomain> search(NoteSearchForm searchForm) {
		// TODO Auto-generated method stub
		return null;
	}



}
