package org.jax.mgi.mgd.api.model.prb.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;
import org.jax.mgi.mgd.api.model.prb.search.ProbeSearchForm;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ProbeService extends BaseService<ProbeDomain> {

	@Inject
	private ProbeDAO probeDAO;

	private ProbeTranslator translator = new ProbeTranslator();
	
	@Transactional
	public SearchResults<ProbeDomain> create(ProbeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ProbeDomain> update(ProbeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ProbeDomain get(Integer key) {
		return translator.translate(probeDAO.get(key));
	}

        @Transactional
        public SearchResults<ProbeDomain> getResults(Integer key) {
                SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
                results.setItem(translator.translate(probeDAO.get(key)));
                return results;
        }
    
	@Transactional
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
