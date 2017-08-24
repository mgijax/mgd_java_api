package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

import org.jax.mgi.mgd.api.entities.Marker;
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
	public SearchResults<Term> getTerm(String term_key) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(term_key != null) { map.put("_term_key", term_key); }
		log.info("Search Params: " + map);
		return termService.searchTerm(map);
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
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		log.info("Map Params: " + postParams);

		if(postParams.containsKey("vocab_name") || postParams.containsKey("_vocab_key")) {
			if(postParams.containsKey("_vocab_key")) map.put("_vocab_key", postParams.get("_vocab_key"));
			if(postParams.containsKey("vocab_name")) map.put("name", postParams.get("vocab_name"));
			return termService.searchVocab(map);
		} else {
			if(postParams.containsKey("_term_key")) map.put("_term_key", postParams.get("_term_key"));
			return termService.searchTerm(map);
		}


//		if(postParams.containsKey("id")) map.put("id", postParams.get("id"));
//		if(postParams.containsKey("creation_date")) map.put("creation_date", postParams.get("creation_date"));
//		if(postParams.containsKey("modification_date")) map.put("modification_date", postParams.get("modification_date"));

		

	}

}
