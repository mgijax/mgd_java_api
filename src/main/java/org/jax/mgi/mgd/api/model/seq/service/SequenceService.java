package org.jax.mgi.mgd.api.model.seq.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.dao.SequenceDAO;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.entities.Sequence;
import org.jax.mgi.mgd.api.model.seq.search.SequenceSearchForm;
import org.jax.mgi.mgd.api.model.seq.translator.SequenceTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class SequenceService extends BaseService<SequenceDomain> implements BaseSearchInterface<SequenceDomain, SequenceSearchForm> {

	@Inject
	private SequenceDAO sequenceDAO;

	private SequenceTranslator translator = new SequenceTranslator();
	
	@Transactional
	public SequenceDomain create(SequenceDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SequenceDomain update(SequenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SequenceDomain get(Integer key) {
		return translator.translate(sequenceDAO.get(key),3);
	}

	@Transactional
	public SearchResults<SequenceDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<SequenceDomain> search(SequenceSearchForm searchForm) {
		SearchResults<Sequence> sequences;
		if(searchForm.getOrderBy() != null) {
			sequences = sequenceDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			sequences = sequenceDAO.search(searchForm.getSearchFields());
		}
		Iterable<SequenceDomain> newItems = translator.translateEntities(sequences.items, searchForm.getSearchDepth());
		return new SearchResults<SequenceDomain>(newItems);
	}



}
