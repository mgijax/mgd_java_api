package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.entities.MGIType;

@RequestScoped
public class MGITypeService extends ServiceInterface<MGIType> {
	
	@Inject
	private MGITypeDAO mgitypeDAO;
	
	@Override
	public PostgresSQLDAO<MGIType> getDAO() {
		return mgitypeDAO;
	}
}
