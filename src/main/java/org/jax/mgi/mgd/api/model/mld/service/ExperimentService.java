package org.jax.mgi.mgd.api.model.mld.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.dao.ExperimentDAO;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Experiment;
import org.jax.mgi.mgd.api.model.mld.search.ExperimentSearchForm;
import org.jax.mgi.mgd.api.model.mld.translator.ExperimentTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ExperimentService extends BaseService<ExperimentDomain> implements BaseSearchInterface<ExperimentDomain, ExperimentSearchForm> {

	@Inject
	private ExperimentDAO exptDAO;

	private ExperimentTranslator translator = new ExperimentTranslator();
	
	@Transactional
	public ExperimentDomain create(ExperimentDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ExperimentDomain update(ExperimentDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ExperimentDomain get(Integer key) {
		return translator.translate(exptDAO.get(key));
	}

	@Transactional
	public SearchResults<ExperimentDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ExperimentDomain> search(ExperimentSearchForm searchForm) {
		SearchResults<Experiment> expts;
		if(searchForm.getOrderBy() != null) {
			expts = exptDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			expts = exptDAO.search(searchForm.getSearchFields());
		}
		Iterable<ExperimentDomain> newItems = translator.translateEntities(expts.items, searchForm.getSearchDepth());
		return new SearchResults<ExperimentDomain>(newItems);
	}



}
