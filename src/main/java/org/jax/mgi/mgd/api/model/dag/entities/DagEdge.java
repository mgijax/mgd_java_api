package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "DAG Edge Model Object")
@Table(name="dag_edge")
public class DagEdge extends BaseEntity {

	@Id
	private int _edge_key;
	private Integer _dag_key;
//	private Integer _parent_key;
//	private Integer _child_key;
	private Integer sequenceNum;	
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_parent_key")
	private DagNode parentNode;	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_child_key")
	private DagNode childNode;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_label_key")
	private DagLabel label;

}
