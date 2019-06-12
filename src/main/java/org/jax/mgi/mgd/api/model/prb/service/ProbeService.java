package org.jax.mgi.mgd.api.model.prb.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.dao.ProbeDAO;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ProbeService extends BaseService<ProbeDomain> {

	@Inject
	private ProbeDAO probeDAO;

	private ProbeTranslator translator = new ProbeTranslator();
	
	@Transactional
	public SearchResults<ProbeDomain> create(ProbeDomain object, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<ProbeDomain> update(ProbeDomain object, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
    
	@Transactional
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public ProbeDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		ProbeDomain domain = new ProbeDomain();
		if (probeDAO.get(key) != null) {
			domain = translator.translate(probeDAO.get(key));
		}
		return domain;
	}

    @Transactional
    public SearchResults<ProbeDomain> getResults(Integer key) {
        SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
        results.setItem(translator.translate(probeDAO.get(key)));
        return results;
    }


}
