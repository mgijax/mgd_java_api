package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "DAG Label Model Object")
@Table(name="dag_label")
public class DagLabel extends BaseEntity {

	@Id
	private Integer _label_key;
	private String label;
	private Date creation_date;
	private Date modification_date;
}
