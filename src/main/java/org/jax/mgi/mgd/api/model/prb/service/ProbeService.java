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
public class ProbeService extends BaseService<ProbeDomain> implements BaseSearchInterface<ProbeDomain, ReferenceSearchForm> {

	@Inject
	private ProbeDAO probeDAO;

	private ProbeTranslator translator = new ProbeTranslator();
	
	@Transactional
	public ProbeDomain create(ProbeDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ProbeDomain update(ProbeDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ProbeDomain get(Integer key) {
		return translator.translate(probeDAO.get(key));
	}

	@Transactional
	public ProbeDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ProbeDomain> search(ReferenceSearchForm searchForm) {
		SearchResults<Probe> probes;
		if(searchForm.getOrderBy() != null) {
			probes = probeDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			probes = probeDAO.search(searchForm.getSearchFields());
		}
		Iterable<ProbeDomain> newItems = translator.translateEntities(probes.items, searchForm.getSearchDepth());
		return new SearchResults<ProbeDomain>(newItems);
	}



}
