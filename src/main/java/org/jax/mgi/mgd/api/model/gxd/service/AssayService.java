package org.jax.mgi.mgd.api.model.gxd.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.dao.AssayDAO;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.gxd.search.AssaySearchForm;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class AssayService extends BaseService<AssayDomain> {

	@Inject
	private AssayDAO assayDAO;

	private AssayTranslator translator = new AssayTranslator();
	
	@Transactional
	public SearchResults<AssayDomain> create(AssayDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<AssayDomain> update(AssayDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public AssayDomain get(Integer key) {
		return translator.translate(assayDAO.get(key),2);
	}

        @Transactional
        public SearchResults<AssayDomain> getResults(Integer key) {
                SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
                results.setItem(translator.translate(assayDAO.get(key),2));
                return results;
        }

	@Transactional
	public SearchResults<AssayDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
