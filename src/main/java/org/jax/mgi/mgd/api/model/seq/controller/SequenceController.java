package org.jax.mgi.mgd.api.model.seq.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.domain.SummarySeqDomain;
import org.jax.mgi.mgd.api.model.seq.service.SequenceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/sequence")
@Api(value = "Sequence Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<SequenceDomain> search() {
		return sequenceService.search();
	}
	
	@GET
	@ApiOperation(value = "Get list of sequence domains by marker acc id")
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
	
    protected String formatTsv (String endpoint, Object obj) {   
    	if (endpoint.startsWith("getSequenceBy")) {
            String[][] cols = {
                {"ID",     			"accID"},
                {"Type",   			"sequenceType"},
                {"Length",     		"length"},
                {"Strain/Species",  "strain"},
                {"Description",		"description"},
                {"Marker Symbols", 	"markerSymbol"}
                };
            return formatTsvHelper(obj, cols);
        } else {
            return null;
        } 	
    }
}
