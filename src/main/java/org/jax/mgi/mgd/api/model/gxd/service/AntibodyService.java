package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.gxd.dao.AntibodyDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class AntibodyService extends BaseService<AntibodyDomain> {

	@Inject
	private AntibodyDAO antibodyDAO;

	private AntibodyTranslator translator = new AntibodyTranslator();
	
	@Transactional
	public SearchResults<AntibodyDomain> create(AntibodyDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AntibodyDomain> update(AntibodyDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AntibodyDomain get(Integer key) {
		return translator.translate(antibodyDAO.get(key));
	}

    @Transactional
    public SearchResults<AntibodyDomain> getResults(Integer key) {
        SearchResults<AntibodyDomain> results = new SearchResults<AntibodyDomain>();
        results.setItem(translator.translate(antibodyDAO.get(key)));
        return results;
    }

	@Transactional
	public SearchResults<AntibodyDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
