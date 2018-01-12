package org.jax.mgi.mgd.api.model.mrk.entities;

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
@ApiModel(value = "Cluster Model Object")
@Table(name="mrk_cluster")
public class Cluster extends EntityBase {

	@Id
	private Integer _cluster_key;
	private String clusterID;
	private String version;
	private Date cluster_date;
	private Date creation_date;
	private Date modification_date;
}
