package org.jax.mgi.mgd.api.controller;

import java.util.List;

import org.jax.mgi.mgd.api.entities.Marker;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.rest.interfaces.TermRESTInterface;
import org.jboss.logging.Logger;

public class TermController extends BaseController implements TermRESTInterface {

	private Logger log = Logger.getLogger(getClass());
	
	@Override
	public Term createTerm(String api_access_token, Term term) {
		log.info("Creating new Term: " + term);
		return null;
	}
	
	@Override
	public Marker updateTerm(String api_access_token, Term term) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Term> getTerm(Integer term_key, Integer vocab_key) {
		log.info("Getting term: " + term_key);
		return null;
	}

	@Override
	public Marker deleteTerm(String api_access_token, Integer term_key) {
		// This method WILL NOT be implemented
		log.info("We will never delete this term: " + term_key);
		return null;
	}
	
}
