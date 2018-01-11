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
@ApiModel(value = "DAG Edge Model Object")
@Table(name="dag_edge")
public class Edge extends EntityBase {

	@Id
	private Integer _edge_key;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
}
