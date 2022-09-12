package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "DAG Node Model Object")
@Table(name="dag_node")
public class DagNode extends BaseEntity {

	@Id
	private int _node_key;
	private int _object_key;	
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_dag_key")
	private Dag dag;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_label_key")
	private DagLabel label;	

	@OneToMany()
	@JoinColumn(name="_parent_key", referencedColumnName="_node_key", insertable=false, updatable=false)
	private List<DagEdge> parentEdges;	
	
	@OneToMany()
	@JoinColumn(name="_child_key", referencedColumnName="_node_key", insertable=false, updatable=false)
	private List<DagEdge> childEdges;		
}

