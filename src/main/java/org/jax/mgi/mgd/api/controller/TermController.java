package org.jax.mgi.mgd.api.controller;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.entities.Marker;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.rest.interfaces.TermRESTInterface;
import org.jax.mgi.mgd.api.service.TermService;
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
	public List<Term> getTerm(String term_key, String vocab_key, String vocab_name) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(term_key != null) { map.put("_term_key", term_key); }
		if(vocab_key != null) { map.put("_vocab_key", vocab_key); }
		if(vocab_name != null) { map.put("vocab_name", vocab_name); }
		log.info("Search Params: " + map);
		return termService.getTerm(map);
	}

	@Override
	public Term deleteTerm(String api_access_token, String term_key) {
		if(authenticate(api_access_token)) {
			return termService.deleteTerm(term_key);
		}
		return null;
	}
}
