package org.jax.mgi.mgd.api.model.img.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImagePaneDAO extends PostgresSQLDAO<ImagePane> {
	public ImagePaneDAO() {
		super(ImagePane.class);
	}
}
