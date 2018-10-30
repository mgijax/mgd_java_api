package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;
import org.jax.mgi.mgd.api.model.gxd.search.AntibodySearchForm;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class AntibodyService extends BaseService<AntibodyDomain> implements BaseSearchInterface<AntibodyDomain, AntibodySearchForm> {

	@Inject
	private AntibodyDAO antibodyDAO;

	private AntibodyTranslator translator = new AntibodyTranslator();
	
	@Transactional
	public AntibodyDomain create(AntibodyDomain object, User user) throws APIException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AntibodyDomain update(AntibodyDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AntibodyDomain get(Integer key) {
		return translator.translate(antibodyDAO.get(key));
	}

	@Transactional
	public SearchResults<AntibodyDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AntibodyDomain> search(AntibodySearchForm searchForm) {
		SearchResults<Antibody> antibodies;
		if(searchForm.getOrderBy() != null) {
			antibodies = antibodyDAO.search(searchForm.getSearchFields(), searchForm.getOrderBy());
		} else {
			antibodies = antibodyDAO.search(searchForm.getSearchFields());
		}
		Iterable<AntibodyDomain> newItems = translator.translateEntities(antibodies.items, searchForm.getSearchDepth());
		return new SearchResults<AntibodyDomain>(newItems);
	}

	
	
}
