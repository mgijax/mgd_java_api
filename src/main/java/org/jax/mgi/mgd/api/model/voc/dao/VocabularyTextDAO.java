package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Text;

@RequestScoped
public class TextDAO extends PostgresSQLDAO<Text> {

	public TextDAO() {
		super(Text.class);
	}

}
