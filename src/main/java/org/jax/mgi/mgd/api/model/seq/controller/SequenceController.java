package org.jax.mgi.mgd.api.model.seq.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SummarySeqDomain;
import org.jax.mgi.mgd.api.model.seq.service.SequenceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sequence")
@Tag(name = "Sequence Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SequenceController extends BaseController<SequenceDomain> {

	@Inject
	private SequenceService sequenceService;

	@Override
	public SearchResults<SequenceDomain> create(SequenceDomain sequence, User user) {
		return sequenceService.create(sequence, user);
	}

	@Override
	public SearchResults<SequenceDomain> update(SequenceDomain sequence, User user) {
		return sequenceService.update(sequence, user);
	}

	@Override
	public SequenceDomain get(Integer key) {
		return sequenceService.get(key);
	}

	@Override
	public SearchResults<SequenceDomain> delete(Integer key, User user) {
		return sequenceService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<SequenceDomain> search() {
		return sequenceService.search();
	}
	
	@GET
	@Operation(description = "Get list of sequence domains by marker acc id")
	@Path("/getSequenceByMarker")
	public SearchResults<SummarySeqDomain> getSequenceByMarker(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
		) {

		SearchResults<SummarySeqDomain> results = new SearchResults<SummarySeqDomain>();

		try {
			results = sequenceService.getSequenceByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadSequenceByMarker")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadSequenceByMarker(@QueryParam("accid") String accid) {
             return sequenceService.downloadSequenceByMarker(accid);
	}
	
}
