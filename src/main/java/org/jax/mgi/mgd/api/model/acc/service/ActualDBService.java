package org.jax.mgi.mgd.api.model.acc.service;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.ActualDBDomain;
import org.jax.mgi.mgd.api.model.acc.search.ActualDBSearchForm;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class ActualDBService extends BaseService<ActualDBDomain> {

        //@Inject
	//private ActualDBDAO actualdbDAO;
	//private AcutalDBTranslator translator = new ActualDBTranslator();

	@Transactional
	public SearchResults<ActualDBDomain> create(ActualDBDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ActualDBDomain> update(ActualDBDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ActualDBDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

        @Transactional
        public SearchResults<ActualDBDomain> getResults(Integer key) {
		// TODO Auto-generated method stub
		return null;
        }
    
	@Transactional
	public SearchResults<ActualDBDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
