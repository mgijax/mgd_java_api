package org.jax.mgi.mgd.api.model.acc.service;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.acc.domain.LogicalDBDomain;
import org.jax.mgi.mgd.api.model.acc.search.LogicalDBSearchForm;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

@RequestScoped
public class LogicalDBService extends BaseService<LogicalDBDomain> {

        //@Inject
        //private LogicallDBDAO logicalDAO;
        //private LogicallDBTranslator translator = new LogicallDBTranslator();

	@Transactional
	public SearchResults<LogicalDBDomain> create(LogicalDBDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<LogicalDBDomain> update(LogicalDBDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public LogicalDBDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

        @Transactional
        public SearchResults<LogicalDBDomain> getResults(Integer key) {
		// TODO Auto-generated method stub
		return null;
        }
    
	@Transactional
	public SearchResults<LogicalDBDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
