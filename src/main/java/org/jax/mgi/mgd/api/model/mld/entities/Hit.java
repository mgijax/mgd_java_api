package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MLD Hit Model Object")
@Table(name="mld_hit")
public class Hit extends EntityBase {

	@EmbeddedId
	private HitKey key;
	private Date creation_date;
	private Date modification_date;
	
	//Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.mld.entities.Hit column: _expt_key 
	//(should be mapped with insert=\"false\" update=\"false\")
	//@OneToOne
	//@JoinColumn(name="_expt_key")
	//private Expts expt;
	
	//Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.mld.entities.Hit column: _probe_key
	//(should be mapped with insert=\"false\" update=\"false\")
	//@OneToOne
	//@JoinColumn(name="_probe_key")
	//private Probe probe;
	
	//@OneToOne
	//@JoinColumn(name="_target_key", referencedColumnName="_probe_key")
	//private Probe target;

}
