package org.jax.mgi.mgd.api.model.img.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssoc;

public class ImagePaneAssocDAO extends PostgresSQLDAO<ImagePaneAssoc> {
	public ImagePaneAssocDAO() {
		super(ImagePaneAssoc.class);
	}
}
