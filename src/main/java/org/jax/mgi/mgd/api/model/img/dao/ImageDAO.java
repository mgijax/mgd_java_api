package org.jax.mgi.mgd.api.model.img.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.img.entities.Image;

@RequestScoped
public class ImageDAO extends PostgresSQLDAO<Image> {

	public ImageDAO() {
		super(Image.class);
	}

}
