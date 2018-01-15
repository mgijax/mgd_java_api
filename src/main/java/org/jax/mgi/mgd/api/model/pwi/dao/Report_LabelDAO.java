package org.jax.mgi.mgd.api.model.pwi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.pwi.entities.Report_Label;

@RequestScoped
public class Report_LabelDAO extends PostgresSQLDAO<Report_Label> {

	protected Report_LabelDAO() {
		super(Report_Label.class);
	}


}
