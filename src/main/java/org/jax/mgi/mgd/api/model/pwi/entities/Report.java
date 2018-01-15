package org.jax.mgi.mgd.api.model.pwi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Report Model Object")
@Table(name="pwi_report")
public class Report extends EntityBase {

	@Id
	private Integer id;
	private String name;
	private String description;
	private String sql_text;
	private String report_author;
	private String requested_by;
	private Date created;
	private Date last_run;
	private String last_run_duration;
	private String report_views;
	private String type;
}
