package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "DAG Edge Model Object")
@Table(name="dag_edge")
public class DagEdge extends EntityBase {

	@Id
	private Integer _edge_key;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_dag_key")
	private Dag dag;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_parent_key", referencedColumnName="_node_key")
	private DagNode parentNode;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_child_key", referencedColumnName="_node_key")
	private DagNode childNode;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_label_key")
	private DagLabel label;

}
