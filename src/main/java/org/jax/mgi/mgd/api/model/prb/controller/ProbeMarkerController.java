package org.jax.mgi.mgd.api.model.prb.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.search.ProbeMarkerSearchForm;
import org.jax.mgi.mgd.api.model.prb.service.ProbeMarkerService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/probeMarker")
@Api(value = "Probe Marker Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeMarkerController extends BaseController<ProbeMarkerDomain> implements BaseSearchInterface<ProbeMarkerDomain, ProbeMarkerSearchForm> {

	@Inject
	private ProbeMarkerService probeMarkerService;

	public ProbeMarkerDomain create(ProbeMarkerDomain probeMarker, User user) {
		try {
			return probeMarkerService.create(probeMarker, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ProbeMarkerDomain update(ProbeMarkerDomain probeMarker, User user) {
		return probeMarkerService.update(probeMarker, user);
	}

	public ProbeMarkerDomain get(Integer probeMarkerKey) {
		return probeMarkerService.get(probeMarkerKey);
	}

	public ProbeMarkerDomain delete(Integer key, User user) {
		return probeMarkerService.delete(key, user);
	}
	
	@Override
	public SearchResults<ProbeMarkerDomain> search(ProbeMarkerSearchForm searchForm) {
		return probeMarkerService.search(searchForm);
	}

}
