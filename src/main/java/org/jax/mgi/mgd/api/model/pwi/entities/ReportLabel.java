package org.jax.mgi.mgd.api.model.pwi.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Report_Label Model Object")
@Table(name="pwi_report_label")
public class ReportLabel extends BaseEntity {

	@Id
	private Integer id;
	private Integer report_id;
	private String label;
}
