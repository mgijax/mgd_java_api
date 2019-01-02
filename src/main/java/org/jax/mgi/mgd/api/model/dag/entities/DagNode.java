package org.jax.mgi.mgd.api.model.dag.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
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


}
