package org.jax.mgi.mgd.api.model.mld.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.ExperimentDAO;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.mld.translator.ExperimentTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ExperimentService extends BaseService<ExperimentDomain> {

	@Inject
	private ExperimentDAO exptDAO;

	private ExperimentTranslator translator = new ExperimentTranslator();
	
	@Transactional
	public SearchResults<ExperimentDomain> create(ExperimentDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ExperimentDomain> update(ExperimentDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ExperimentDomain get(Integer key) {
		return translator.translate(exptDAO.get(key));
	}

    @Transactional
    public SearchResults<ExperimentDomain> getResults(Integer key) {
        SearchResults<ExperimentDomain> results = new SearchResults<ExperimentDomain>();
        results.setItem(translator.translate(exptDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<ExperimentDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
