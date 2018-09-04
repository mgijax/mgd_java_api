package org.jax.mgi.mgd.api.model.seq.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.search.SequenceSearchForm;
import org.jax.mgi.mgd.api.model.seq.service.SequenceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/sequence")
@Api(value = "Sequence Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SequenceController extends BaseController<SequenceDomain> implements BaseSearchInterface<SequenceDomain, SequenceSearchForm> {

	@Inject
	private SequenceService sequenceService;

	@Override
	public SequenceDomain create(SequenceDomain sequence, User user) {
		try {
			return sequenceService.create(sequence, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SequenceDomain update(SequenceDomain sequence, User user) {
		return sequenceService.update(sequence, user);
	}

	@Override
	public SequenceDomain get(Integer sequenceKey) {
		return sequenceService.get(sequenceKey);
	}

	@Override
	public SequenceDomain delete(Integer key, User user) {
		return sequenceService.delete(key, user);
	}
	
	@Override
	public SearchResults<SequenceDomain> search(SequenceSearchForm searchForm) {
		return sequenceService.search(searchForm);
	}

}
