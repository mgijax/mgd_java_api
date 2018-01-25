package org.jax.mgi.mgd.api.model.pwi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.pwi.entities.ReportLabel;

@RequestScoped
public class ReportLabelDAO extends PostgresSQLDAO<ReportLabel> {

	protected ReportLabelDAO() {
		super(ReportLabel.class);
	}


}
