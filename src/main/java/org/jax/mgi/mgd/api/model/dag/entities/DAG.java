package org.jax.mgi.mgd.api.model.dag.entities;

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
@ApiModel(value = "DAG Model Object")
@Table(name="dag_dag")
public class DAG extends EntityBase {

	@Id
	private Integer _dag_key;
	private String name;
	private String abbreviation;
	private Date creation_date;
	private Date modification_date;
}
