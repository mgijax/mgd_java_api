package org.jax.mgi.mgd.api.model.bib.service;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

import org.jax.mgi.mgd.api.model.BaseService;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

@RequestScoped
public class ReferenceService extends BaseService<ReferenceDomain> {

	protected static Logger log = Logger.getLogger(MGISynonymService.class);
	
	@Transactional
	public SearchResults<ReferenceDomain> create(ReferenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public SearchResults<ReferenceDomain> update(ReferenceDomain object, User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public ReferenceDomain get(Integer key) {
		// TODO Auto-generated method stub
		return null;	
	}

    @Transactional
    public SearchResults<ReferenceDomain> getResults(Integer key) {
		// TODO Auto-generated method stub
		return null;
    }

	@Transactional
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
