package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "DAG Edge Model Object")
@Table(name="dag_edge")
public class DagEdge extends BaseEntity {

	@Id
	private int _edge_key;
	private Integer _dag_key;
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
