package org.jax.mgi.mgd.api.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.TermDomain;
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
	public Term createTerm(String api_access_token, Term term) {
		if(authenticate(api_access_token)) {
			return termService.createTerm(term);
		}
		return null;
	}

	@Override
	public Term updateTerm(String api_access_token, Term term) {
		if(authenticate(api_access_token)) {
			return termService.updateTerm(term);
		}
		return null;
	}

	@Override
	public TermDomain getTerm(String term_key) {
		return termService.getTerm(Integer.parseInt(term_key));
	}

	@Override
	public SearchResults<Term> deleteTerm(String api_access_token, String term_key) {
		if(authenticate(api_access_token)) {
			return termService.deleteTerm(term_key);
		}
		return null;
	}

	@Override
	public SearchResults search(Map<String, String> postParams) {
		
		List<String> list = Arrays.asList(
				"_term_key", 
				"vocab._vocab_key",
				"vocab.name", 
				"createdBy.login"
		);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		log.info("Map Params: " + postParams);
		
		for(String s: list) {
			if(postParams.containsKey(s)) {
				map.put(s, postParams.get(s));
			}
		}

		return termService.searchTerm(map);

	}

}
