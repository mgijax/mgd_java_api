package org.jax.mgi.mgd.api.model.voc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.VocabularyDomain;
import org.jax.mgi.mgd.api.model.voc.search.VocabularySearchForm;
import org.jax.mgi.mgd.api.model.voc.service.VocabService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/vocab")
@Api(value = "Vocab Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VocabController extends BaseController<VocabularyDomain> implements BaseSearchInterface<VocabularyDomain, VocabularySearchForm> {

	@Inject
	private VocabService vocabService;

	public VocabularyDomain create(VocabularyDomain object, User user) {
		try {
			return vocabService.create(object, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public VocabularyDomain get(Integer key) {
		return vocabService.get(key);
	}

	public VocabularyDomain update(VocabularyDomain object, User user) {
		return vocabService.update(object, user);
	}

	public SearchResults<VocabularyDomain> delete(Integer key, User user) {
		return vocabService.delete(key, user);
	}

	public SearchResults<VocabularyDomain> search(VocabularySearchForm form) {
		return vocabService.search(form);
	}

}
