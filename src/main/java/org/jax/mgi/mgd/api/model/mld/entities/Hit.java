package org.jax.mgi.mgd.api.model.mld.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

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
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_expt_key")
	//private Expts expt;
	
	//Repeated column in mapping for entity: org.jax.mgi.mgd.api.model.mld.entities.Hit column: _probe_key
	//(should be mapped with insert=\"false\" update=\"false\")
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_probe_key")
	//private Probe probe;
	
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_target_key", referencedColumnName="_probe_key")
	//private Probe target;
	
	@Getter @Setter
	@Embeddable
	public class HitKey implements Serializable {
		private Integer _expt_key;
		private Integer _probe_key;
		private Integer _target_key;
	}
}
