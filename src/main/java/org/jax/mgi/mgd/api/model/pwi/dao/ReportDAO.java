package org.jax.mgi.mgd.api.model.pwi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.pwi.entities.Report;

@RequestScoped
public class ReportDAO extends PostgresSQLDAO<Report> {

	protected ReportDAO() {
		super(Report.class);
	}


}
