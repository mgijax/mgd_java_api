package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.SpecimenDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Specimen;
import org.jax.mgi.mgd.api.model.gxd.search.SpecimenSearchForm;
import org.jax.mgi.mgd.api.model.gxd.translator.SpecimenTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class SpecimenService extends BaseService<SpecimenDomain> implements BaseSearchInterface<SpecimenDomain, SpecimenSearchForm> {

	@Inject
	private SpecimenDAO specimenDAO;

	private SpecimenTranslator translator = new SpecimenTranslator();
	
	@Transactional
	public SpecimenDomain create(SpecimenDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	public SpecimenDomain update(SpecimenDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SpecimenDomain get(Integer key) {
		return translator.translate(specimenDAO.get(key));
	}

	@Transactional
	public SpecimenDomain delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<SpecimenDomain> search(SpecimenSearchForm searchForm) {
		SearchResults<Specimen> specimens;
		if(searchForm.getOrderBy() != null) {
			specimens = specimenDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			specimens = specimenDAO.search(searchForm.getSearchFields());
		}
		Iterable<SpecimenDomain> newItems = translator.translateEntities(specimens.items, searchForm.getSearchDepth());
		return new SearchResults<SpecimenDomain>(newItems);
	}




}
