package org.jax.mgi.mgd.api.model.img.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;

public class ImagePaneDAO extends PostgresSQLDAO<ImagePane> {
	public ImagePaneDAO() {
		super(ImagePane.class);
	}
}
