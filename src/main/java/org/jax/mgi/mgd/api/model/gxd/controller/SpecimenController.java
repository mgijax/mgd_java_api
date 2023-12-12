package org.jax.mgi.mgd.api.model.gxd.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.SpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummarySpecimenDomain;
import org.jax.mgi.mgd.api.model.gxd.service.SpecimenService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/specimen")
@Tag(name = "Specimen Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecimenController extends BaseController<SpecimenDomain> {

	@Inject
	private SpecimenService specimenService;

	@Override
	public SearchResults<SpecimenDomain> create(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results = specimenService.create(domain, user);
		results = specimenService.getResults(Integer.valueOf(results.items.get(0).getSpecimenKey()));
		return results;
	}

	@Override
	public SearchResults<SpecimenDomain> update(SpecimenDomain domain, User user) {
		SearchResults<SpecimenDomain> results = new SearchResults<SpecimenDomain>();
		results = specimenService.update(domain, user);
		results = specimenService.getResults(Integer.valueOf(results.items.get(0).getSpecimenKey()));
		return results;
	}

	@Override
	public SearchResults<SpecimenDomain> delete(Integer key, User user) {
		return specimenService.delete(key, user);
	}
	
	@Override
	public SpecimenDomain get(Integer key) {
		return specimenService.get(key);
	}
	
	@GET
	@Operation(description = "Get list of specimen domains by reference jnum id")
	@Path("/getSpecimenByRef")
	public SearchResults<SummarySpecimenDomain> getSpecimenByRef(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummarySpecimenDomain> results = new SearchResults<SummarySpecimenDomain>();

		try {
			results = specimenService.getSpecimenByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadSpecimenByRef")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadSpecimenByRef(@QueryParam("accid") String accid) {
		return specimenService.downloadSpecimenByJnum(accid);
	}
	
}
