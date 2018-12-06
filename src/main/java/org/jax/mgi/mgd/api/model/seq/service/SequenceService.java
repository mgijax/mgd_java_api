package org.jax.mgi.mgd.api.model.seq.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.dao.SequenceDAO;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.translator.SequenceTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class SequenceService extends BaseService<SequenceDomain> {

	@Inject
	private SequenceDAO sequenceDAO;
	
	private SequenceTranslator translator = new SequenceTranslator();
	
	@Transactional
	public SearchResults<SequenceDomain> create(SequenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<SequenceDomain> update(SequenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SequenceDomain get(Integer key) {
		return translator.translate(sequenceDAO.get(key));
	}

    @Transactional
    public SearchResults<SequenceDomain> getResults(Integer key) {
        SearchResults<SequenceDomain> results = new SearchResults<SequenceDomain>();
        results.setItem(translator.translate(sequenceDAO.get(key)));
        return results;
    }
    
	@Transactional
	public SearchResults<SequenceDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
