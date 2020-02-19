package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultImage;

public class InSituResultImageDAO extends PostgresSQLDAO<InSituResultImage> {
	protected InSituResultImageDAO() {
		super(InSituResultImage.class);
	}
}
