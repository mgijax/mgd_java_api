package org.jax.mgi.mgd.api.model.img.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;

@RequestScoped
public class ImagePaneDAO extends PostgresSQLDAO<ImagePane> {

	public ImagePaneDAO() {
		super(ImagePane.class);
	}

}
