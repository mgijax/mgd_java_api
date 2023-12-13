package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultImage;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InSituResultImageDAO extends PostgresSQLDAO<InSituResultImage> {
	protected InSituResultImageDAO() {
		super(InSituResultImage.class);
	}
}
