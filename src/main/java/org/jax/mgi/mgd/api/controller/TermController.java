package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.rest.interfaces.TermRESTInterface;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

public class TermController extends BaseController implements TermRESTInterface {

	@Inject
	private TermService termService;
	
	private Logger log = Logger.getLogger(getClass());

	@Override
	public Term create(String api_access_token, Term term) {
		if(authenticate(api_access_token)) {
			return termService.create(term);
		}
		return null;
	}

	@Override
	public Term update(String api_access_token, Term term) {
		if(authenticate(api_access_token)) {
			return termService.update(term);
		}
		return null;
	}

	@Override
	public Term get(Integer key) {
		return termService.get(key);
	}

	@Override
	public Term delete(String api_access_token, Integer term_key) {
		if(authenticate(api_access_token)) {
			return termService.delete(term_key);
		}
		return null;
	}

	@Override
	public SearchResults<Term> search(Map<String, Object> postParams) {
		return termService.search(postParams, "sequenceNum");
	}
	
}
