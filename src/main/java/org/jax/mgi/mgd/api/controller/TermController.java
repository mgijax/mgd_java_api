package org.jax.mgi.mgd.api.controller;

import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.TermDomain;
import org.jax.mgi.mgd.api.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

public class TermController extends BaseController<TermDomain> {

	@Inject
	private TermService termService;

	@Override
	public TermDomain create(TermDomain term) {
		return termService.create(term);
	}

	@Override
	public TermDomain update(TermDomain term) {
		return termService.update(term);
	}

	@Override
	public TermDomain get(Integer key) {
		return termService.get(key);
	}

	@Override
	public TermDomain delete(Integer term_key) {
		return termService.delete(term_key);
	}

	@Override
	public SearchResults<TermDomain> search(Map<String, Object> postParams) {
		return termService.search(postParams, "sequenceNum");
	}
	
}
