package org.jax.mgi.mgd.api.model.img.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.img.entities.Image;

public class ImageDAO extends PostgresSQLDAO<Image> {
	public ImageDAO() {
		super(Image.class);
	}
}
