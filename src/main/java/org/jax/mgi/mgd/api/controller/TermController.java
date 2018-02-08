package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.rest.interfaces.TermRESTInterface;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class TermController extends BaseController implements TermRESTInterface {

	@Inject
	private TermService termService;

	@Override
	public TermDomain create(String api_access_token, TermDomain term) {
		if(authenticate(api_access_token)) {
			return termService.create(term);
		}
		return null;
	}

	@Override
	public TermDomain update(String api_access_token, TermDomain term) {
		if(authenticate(api_access_token)) {
			return termService.update(term);
		}
		return null;
	}

	@Override
	public TermDomain get(Integer key) {
		return termService.get(key);
	}

	@Override
	public TermDomain delete(String api_access_token, Integer term_key) {
		if(authenticate(api_access_token)) {
			return termService.delete(term_key);
		}
		return null;
	}

	@Override
	public SearchResults<TermDomain> search(Map<String, Object> postParams) {
		return termService.search(postParams, "sequenceNum");
	}
	
}
