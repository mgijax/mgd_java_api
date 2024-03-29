package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "DAG Node Model Object")
@Table(name="dag_node")
public class DagNode extends BaseEntity {

	@Id
	private int _node_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key")
	private Term term;
	
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

