package org.jax.mgi.mgd.api.model.all.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.all.dao.AlleleCellLineDAO;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleCellLineTranslator;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SQLExecutor;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class AlleleCellLineService extends BaseService<AlleleCellLineDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Inject
	private AlleleCellLineDAO alleleCellLineDAO;
	
	private AlleleCellLineTranslator translator = new AlleleCellLineTranslator();				
	private SQLExecutor sqlExecutor = new SQLExecutor();

	@Transactional
	public SearchResults<AlleleCellLineDomain> create(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDomain> update(AlleleCellLineDomain domain, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@Transactional
	public SearchResults<AlleleCellLineDomain> delete(Integer key, User user) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}
	
	@Transactional
	public AlleleCellLineDomain get(Integer key) {
		// get the DAO/entity and translate -> domain
		AlleleCellLineDomain domain = new AlleleCellLineDomain();
		if (alleleCellLineDAO.get(key) != null) {
			domain = translator.translate(alleleCellLineDAO.get(key));
		}
		alleleCellLineDAO.clear();
		return domain;
	}

    @Transactional
    public SearchResults<AlleleCellLineDomain> getResults(Integer key) {
		SearchResults<AlleleCellLineDomain> results = new SearchResults<AlleleCellLineDomain>();
		results.setItem(translator.translate(alleleCellLineDAO.get(key)));
		alleleCellLineDAO.clear();
		return results;
    }
	
}
