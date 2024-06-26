package org.jax.mgi.mgd.api.model.dag.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "DAG Label Model Object")
@Table(name="dag_label")
public class DagLabel extends BaseEntity {

	@Id
	private int _label_key;
	private String label;
	private Date creation_date;
	private Date modification_date;
}
